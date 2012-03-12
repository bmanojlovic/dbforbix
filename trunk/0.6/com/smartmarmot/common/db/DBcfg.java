package com.smartmarmot.common.db;

import java.util.Properties;

import org.apache.log4j.Level;

import com.smartmarmot.common.Constants;
import com.smartmarmot.common.SmartLogger;


public class DBcfg {
	
	private String dbUser = null;
	private String dbPassword = null;
	private String dbURI = null;
	private int dbPoolMaxActive = 5;
	private int dbPoolMaxIdle = 1;
	private int dbPoolMaxWait = 100;
	private DBType dbtype;


	public DBType getDbtype() {
		return dbtype;
	}
	public int getDbPoolMaxWait() {
		return dbPoolMaxWait;
	}
	public int getDbPoolMaxIdle() {
		return dbPoolMaxIdle;
	}
	public int getDbPoolMaxActive() {
		return dbPoolMaxActive;
	}
	public String getDbURI() {
		return dbURI;
	}
	public String getDbUser() {
		return dbUser;
	}
	public String getDbPassword() {
		return dbPassword;
	}

	public  DBcfg (Properties _props,String _dbName) {
		
		
		try {
			dbURI = new String(_props.getProperty(_dbName + "."
					+ Constants.CONN_URL));
		} catch (Exception ex) {
			SmartLogger.logThis(Level.ERROR,
					"Error on DBcfg while getting "
							+ _dbName + "." + Constants.CONN_URL + " "
							+ ex.getMessage());
		}

		
		try {
			dbUser = new String(_props.getProperty(_dbName + "."
					+ Constants.CONN_USERNAME));
		} catch (Exception ex) {
			SmartLogger.logThis(Level.INFO,
					"Error on DBcfg while getting "
							+ _dbName + "." + Constants.CONN_USERNAME + " "
							+ ex.getMessage());
			try {
				dbUser = new String(_props.getProperty(_dbName + "."
					+ Constants.CONN_DEFAULT_USERNAME));
			} catch (Exception ex1){
				SmartLogger.logThis(Level.ERROR,
						"Error on DBcfg while getting "
								+ Constants.CONN_DEFAULT_USERNAME + " "
								+ ex1.getMessage());
					}
		}
		
		
		try {
			this.dbPassword = new String(_props.getProperty(_dbName + "."
					+ Constants.CONN_PASSWORD));
		} catch (Exception ex) {
			SmartLogger.logThis(Level.INFO,
					"Error on DBcfg while getting "
							+ _dbName + "." + Constants.CONN_PASSWORD + " "
							+ ex.getMessage());
			try {
					dbPassword = new String(_props.getProperty(
					Constants.CONN_DEFAULT_PASSWORD));
				} catch (Exception ex2){
						SmartLogger.logThis(Level.ERROR,
							"Error on DBcfg getConnection while getting "
									+ _dbName + "." + Constants.CONN_PASSWORD + " "
									+ ex.getMessage());
		}

			
		}
		
		try {
			this.dbtype = new DBType(new String(_props.getProperty(_dbName  + "." + Constants.DATABASES_TYPE)));
		} catch (Exception ex) {
			SmartLogger.logThis(Level.ERROR,
					"Error on DBcfg while getting "
							+ _dbName + "." + Constants.DATABASES_TYPE + " "
							+ ex.getMessage());
		}
		
		try {
			this.dbPoolMaxActive = new Integer(_props.getProperty(_dbName + "."
					+ Constants.CONN_MAX_ACTIVE));
		} catch (Exception ex) {
			SmartLogger.logThis(Level.DEBUG, "Note: " + _dbName + "."
					+ Constants.CONN_MAX_ACTIVE + " " + ex.getMessage());
			try {
				this.dbPoolMaxActive = new Integer(_props
						.getProperty(Constants.DATABASES_LIST + "."
								+ Constants.CONN_MAX_ACTIVE));
			} catch (Exception e) {
				SmartLogger.logThis(Level.WARN, "Note: "
						+ Constants.DATABASES_LIST + "."
						+ Constants.CONN_MAX_ACTIVE + " " + e.getMessage());
				SmartLogger.logThis(Level.WARN, "Warning I will use default value "
						+ this.dbPoolMaxActive);
			}
		}
		
		
		

	}
	public DBType geDBType() {
		// TODO Auto-generated method stub
		return this.dbtype;
	}
}









