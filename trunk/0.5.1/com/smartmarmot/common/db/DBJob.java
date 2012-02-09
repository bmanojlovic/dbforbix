/*
 * Copyright (C) 2010 Andrea Dalle Vacche.
 * 
 * This file is part of DBforBIX.
 *
 * DBforBIX is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * DBforBIX is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * DBforBIX. If not, see <http://www.gnu.org/licenses/>.
 */

package com.smartmarmot.common.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Level;

import com.smartmarmot.common.Constants;
import com.smartmarmot.common.SmartLogger;
import com.smartmarmot.common.enquiry.Query;
import com.smartmarmot.zabbix.Sender;
import com.smartmarmot.zabbix.ZabbixItem;


public class DBJob implements Runnable {
	private final DBConnManager dbcm;
	private Query[] queries;
	private final BlockingQueue<ZabbixItem> queue = new LinkedBlockingQueue<ZabbixItem>();
	private final String dbname;
	private final Hashtable<String, Integer> _zabbixServers;

	public DBJob(DBConnManager _dbcm, Query[] _queries,
			Hashtable<String, Integer> _zabbixServers, String _dbname) {
		this.dbcm = _dbcm;
		this.queries = _queries;
		this._zabbixServers = _zabbixServers;
		this.dbname = _dbname;
		


	}

	private boolean Alive(){
		try {
			Connection dbConn= null;
			dbConn=this.dbcm.getDs().getConnection();
			PreparedStatement p_stmt = null;
			p_stmt = dbConn.prepareStatement(this.dbcm.getDBType().getValidationQuery());
			ResultSet rs = null;
			rs = p_stmt.executeQuery();
			rs.next();
			dbConn.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			SmartLogger.logThis(Level.DEBUG, "Database "
					+ this.dbname + " is not alive");
			return false;
		}
	}

	public void run() {

		SmartLogger.logThis(Level.DEBUG, "Starting dbJob on database "
				+ dbname);
		final long start = System.currentTimeMillis();

		try {
			//Connection dbConn=this.dbcm.ds.getConnection();
/*			if (dbConn.isClosed()){
				dbConn = this._spds.getConnection();
			}*/
			if (Alive()){
				
				queue.offer(new ZabbixItem(Constants.ITEM_PREFIX+this.dbcm.getDBType().getDBTypeString()+"."+"alive", "1",this.dbname));
				queue.offer(new ZabbixItem(Constants.ITEM_PREFIX+"Version", Constants.BANNER,this.dbname));

				ZabbixItem[] zitems = DBEnquiry.execute(this.queries,
						this.dbcm, this.dbname);
				if (zitems != null && zitems.length > 0) {
					SmartLogger.logThis(Level.DEBUG, "Item retrieved "
							+ zitems.length + " on database " + this.dbname);
					for (int cnt = 0; cnt < zitems.length; cnt++) {
						SmartLogger.logThis(Level.DEBUG, "dbname " + this.dbname
								+ " sending item  " + zitems[cnt].getKey()
								+ " value " + zitems[cnt].getValue());
						queue.offer(new ZabbixItem(zitems[cnt].getKey(), zitems[cnt]
						                                              .getValue(),
						                                              dbname));
					}
				}
				Sender sender = new Sender(queue, _zabbixServers, this.dbname);
				sender.run();
			} else{
				BlockingQueue<ZabbixItem> _queue = new LinkedBlockingQueue<ZabbixItem>();
				_queue.offer(new ZabbixItem(Constants.ITEM_PREFIX+this.dbcm.getDBType().getDBTypeString()+"."+"alive", "0",this.dbname));
				_queue.offer(new ZabbixItem(Constants.ITEM_PREFIX+"Version", Constants.BANNER,this.dbname));
				for (int cnt = 0; cnt < this.queries.length; cnt++) {
					if (queries[cnt].ifNotAlive()){
					_queue.offer(new ZabbixItem(Constants.ITEM_PREFIX+this.dbcm.getDBType().getDBTypeString()+"."+queries[cnt].getName(),
							queries[cnt].getWhenNotAlive(),dbname));
						}
					}
				Sender sender = new Sender(queue, _zabbixServers, this.dbname);
				sender.run();
				}
			} catch (Exception e) {
				SmartLogger.logThis(Level.ERROR, "Error on dbJob for database "
						+ dbname + " error: " + e);
			} finally {
				if (queries != null)
					queries = null;
			}
			SmartLogger.logThis(Level.INFO, "Done with dbJob on database "
					+ dbname + " elapsed time "
					+ (System.currentTimeMillis() - start) + " ms");
		}
	}
