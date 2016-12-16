/*
 * This file is part of DBforBix.
 *
 * DBforBix is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * DBforBix is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * DBforBix. If not, see <http://www.gnu.org/licenses/>.
 */

package com.smartmarmot.dbforbix.zabbix;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.smartmarmot.common.StackSingletonPersistent;
import com.smartmarmot.dbforbix.config.Config;
import com.smartmarmot.dbforbix.zabbix.protocol.Sender18;
import com.smartmarmot.dbforbix.zabbix.protocol.SenderProtocol;

/**
 * Sender query handler
 * 
 * @author Andrea Dalle Vacche
 */
public class PersistentStackSender extends Thread {

	public enum PROTOCOL {
		V14, V18
	}

	private static final Logger	LOG					= Logger.getLogger(PersistentStackSender.class);
	private boolean				terminate			= false;
	private Config.ZServer[]	configuredServers	= new Config.ZServer[0];
	private SenderProtocol		protocol;

	
	public PersistentStackSender(PROTOCOL protVer) {
		super("PersistentStackSender");
		switch (protVer) {
		default:
			protocol = new Sender18();
			break;
		}
		setDaemon(true);
	}

	@Override
	public void run() {
		LOG.debug("PersistentStackSender - starting sender thread");
		while (!terminate) {
			try {
				if (StackSingletonPersistent.getInstance().peek() == null) {
					Thread.sleep(10000);
				}
				else {
					Config.ZServer[] servers;
					synchronized (configuredServers) {
						servers = configuredServers;
					}

					LOG.info("PersitenceStackSender - retrieving the first element to send");
					while (StackSingletonPersistent.getInstance().size() != 0 ){
						LOG.debug("PersistentStackSender - found "+StackSingletonPersistent.getInstance().size()+" persistent items to send");
						ZabbixItem zx = (ZabbixItem) StackSingletonPersistent.getInstance().pop();
						for (Config.ZServer serverConfig : servers) {
							try {
								Socket zabbix = null;
								OutputStreamWriter out = null;
								InputStream in = null;
								byte[] response = new byte[1024];

								zabbix = new Socket();
								zabbix.setSoTimeout(5000);
								zabbix.connect(new InetSocketAddress(serverConfig.getHost(), serverConfig.getPort()));
								OutputStream os = zabbix.getOutputStream();
								LOG.debug("PersistentStackSender - Sending to " +zx.getHost() + " Item=" + zx.getKey() + " Value=" + zx.getValue());
								String data = protocol.encodeItem(zx);
								out = new OutputStreamWriter(os);
								out.write(data);
								out.flush();

								in = zabbix.getInputStream();
								final int read = in.read(response);
								if (!protocol.isResponeOK(read, response))
									LOG.warn("PersistentStackSender - Received unexpected response '" + new String(response).trim() + "' for key '" + zx.getKey()
									+ "'");
								in.close();
								out.close();
								zabbix.close();
							}			 
							catch (Exception ex) {
								LOG.error("PersistentStackSender - Error contacting Zabbix server " + configuredServers[0].getHost() +" port "+ configuredServers[0].getPort()+ " - " + ex.getMessage());
								LOG.debug("PersistentStackSender - Current PeristentStack size ="+StackSingletonPersistent.getInstance().size());
								LOG.info("PersistentStackSender - Adding the item Adding the item="+zx.getHost()+" key="+zx.getKey()+" value="+zx.getValue()+" clock="+zx.getClock()+ " back to the persisent stack");
								StackSingletonPersistent.getInstance().push(zx);
								LOG.info("PersistentStackSender - going to sleep for 1 minute");
								Thread.sleep(60000);	
							}

						}
					}
				}
			}
			catch (Exception e) {
				LOG.debug("PeristentStack issue "+e);
			}
		}
	}

	synchronized public void updateServerList(Config.ZServer[] newServers) {
		synchronized (configuredServers) {
			configuredServers = newServers;
		}
	}

	public void terminate() {
		terminate = true;
	}
}
