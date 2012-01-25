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

package com.smartmarmot.dbforbix.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.dbcp.datasources.SharedPoolDataSource;
import org.apache.log4j.Level;

import com.smartmarmot.dbforbix.Configurator;
import com.smartmarmot.dbforbix.Constants;
import com.smartmarmot.dbforbix.Query;
import com.smartmarmot.dbforbix.Sender;
import com.smartmarmot.zabbix.ZabbixItem;

public class DBJob implements Runnable {
	private final SharedPoolDataSource spds;
	private Query[] queries;
	private final BlockingQueue<ZabbixItem> queue = new LinkedBlockingQueue<ZabbixItem>();
	private final String dbname;
	private final String queriesGroup;
	private final int dgNum;
	private final DBType dbtype;
	private final Hashtable<String, Integer> _zabbixServers;

	public DBJob(SharedPoolDataSource _spds, Query[] _queries, String _queriesGroup,
			Hashtable<String, Integer> _zabbixServers, String _dbname,DBType _dbtype) {
		this.spds = _spds;
		this.queries = _queries;
		this.queriesGroup = _queriesGroup;
		this._zabbixServers = _zabbixServers;
		this.dbname = _dbname;
		this.dbtype = _dbtype;
		this.dgNum = 0;
	}

	public DBJob(SharedPoolDataSource _spds, Query[] _queries, String _queriesGroup,
			Hashtable<String, Integer> _zabbixServers, String _dbname,DBType _dbtype, int _dgNum) {
		this.spds = _spds;
		this.queries = _queries;
		this._zabbixServers = _zabbixServers;
		this.queriesGroup = _queriesGroup;
		this.dbname = _dbname;
		this.dbtype = _dbtype;
		if (_dgNum > 0) {
			this.dgNum = _dgNum;
		} else {
			this.dgNum = 0;
		}
	}

	private boolean Alive(Connection _conn){
		try {
			PreparedStatement p_stmt = null;
			p_stmt = _conn
			.prepareStatement(dbtype.getValidationQuery());
			ResultSet rs = null;
			rs = p_stmt.executeQuery();
			rs.next();
			//_conn.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Configurator.logThis(Level.DEBUG, "Database "
					+ this.dbname + " is not alive");
			return false;
		}
	}

	public void run() {

		Configurator.logThis(Level.DEBUG, "Starting dbJob on database "
				+ dbname + " " + queriesGroup);
		final long start = System.currentTimeMillis();

		try {
			Connection dbConn=this.spds.getConnection();
/*			if (dbConn.isClosed()){
				dbConn = this._spds.getConnection();
			}*/
			if (Alive(dbConn)){
				queue.offer(new ZabbixItem(Constants.PROJECT_NAME+"."+dbtype.getDBTypeString()+"."+"alive", "1",this.dbname));
				queue.offer(new ZabbixItem(Constants.PROJECT_NAME+"."+"Version", Constants.BANNER,this.dbname));

				ZabbixItem[] zitems = DBEnquiry.execute(this.queries,
						dbConn, this.dbname);
				if (zitems != null && zitems.length > 0) {
					Configurator.logThis(Level.DEBUG, "Item retrieved "
							+ zitems.length + " on database " + this.dbname);
					for (int cnt = 0; cnt < zitems.length; cnt++) {
						String zItemName = Constants.PROJECT_NAME+"."+dbtype.getDBTypeString()+"."+zitems[cnt].getKey();
						if (this.dgNum > 0) {
							zItemName = zItemName + "_" + dgNum;
						}
						Configurator.logThis(Level.DEBUG, "dbname " + this.dbname
								+ " sending item  " + zitems[cnt].getKey()
								+ " value " + zitems[cnt].getValue());
						queue.offer(new ZabbixItem(zItemName, zitems[cnt]
						                                              .getValue(),
						                                              dbname));
					}
				}
				dbConn.close();
			} else{
				for (int cnt = 0; cnt < this.queries.length; cnt++) {
					queue.offer(new ZabbixItem(Constants.PROJECT_NAME+"."+dbtype.getDBTypeString()+"."+"alive", "0",this.dbname));
				//	queue.offer(new ZabbixItem(Constants.PROJECT_NAME+"."+dbtype.getDBTypeString()+"."+"Version", Constants.BANNER,this.dbname));
					queue.offer(new ZabbixItem(Constants.PROJECT_NAME+"."+"Version", Constants.BANNER,this.dbname));
					queue.offer(new ZabbixItem(Constants.PROJECT_NAME+"."+dbtype.getDBTypeString()+"."+queries[cnt].getName(),
							queries[cnt].getNoData(),dbname));
				}
			}
			Sender sender = new Sender(queue, _zabbixServers, this.dbname);
			sender.run();
			} catch (Exception e) {
				Configurator.logThis(Level.ERROR, "Error on dbJob for database "
						+ dbname + " " + queriesGroup + " error: " + e);
			} finally {
				if (queries != null)
					queries = null;
			}
			Configurator.logThis(Level.INFO, "Done with dbJob on database "
					+ dbname + " " + queriesGroup + " elapsed time "
					+ (System.currentTimeMillis() - start) + " ms");
		}
	}
