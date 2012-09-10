package com.smartmarmot.common.db;

import java.util.Date;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Level;

import com.smartmarmot.common.SmartLogger;

public class DBConnManager {

	private DataSource ds = null;
	private GenericObjectPool _pool = null;
	private DBType dbType;

	/**
	 * @param config
	 *            configuration from an XML file.
	 */
	public DBConnManager(DBcfg config) {
		try {
			this.setDBType(config.geDBType());
			connectToDB(config);
		} catch (Exception e) {
			SmartLogger.logThis(Level.ERROR,
					"Failed to construct ConnectionManager" + e);
		}
	}

	/**
	 * destructor
	 */
	protected void finalize() {
		SmartLogger.logThis(Level.DEBUG,"Finalizing ConnectionManager");
		try {
			super.finalize();
		} catch (Throwable ex) {
			SmartLogger.logThis(Level.ERROR,
					"ConnectionManager finalize failed to disconnect from "+getDbType().getDBTypeString()
					+ ex);
		}
	}

	/**
	 * connectToDB - Connect to the MySql DB!
	 */
	private void connectToDB(DBcfg dbconfig) {

		try {
			java.lang.Class.forName(dbconfig.getDbtype().getJDBCDriverClass()).newInstance();
		} catch (Exception e) {
			SmartLogger.logThis(Level.ERROR,
					"Error when attempting to obtain DB Driver: "
					+ dbconfig.getDbtype().getJDBCDriverClass() + " on "
					+ new Date().toString()+ e);
		}

		SmartLogger.logThis(Level.DEBUG,"Trying to connect to database...");
		try {
			setDs(setupDataSource(dbconfig.getDbURI(),
					dbconfig.getDbUser(), dbconfig.getDbPassword(),
					dbconfig.getDbPoolMaxIdle(), dbconfig.getDbPoolMaxActive()));

			SmartLogger.logThis(Level.DEBUG,"Connection attempt to database succeeded.");
		} catch (Exception e) {
			SmartLogger.logThis(Level.ERROR,"Error when attempting to connect to DB "+ e);
		}
	}

	/**
	 * 
	 * @param connectURI
	 *            - JDBC Connection URI
	 * @param username
	 *            - JDBC Connection username
	 * @param password
	 *            - JDBC Connection password
	 * @param minIdle
	 *            - Minimum number of idle connection in the connection pool
	 * @param maxActive
	 *            - Connection Pool Maximum Capacity (Size)
	 * @throws Exception
	 */
	public  DataSource setupDataSource(String connectURI,
			String username, String password, int minIdle, int maxActive)
	throws Exception {
		//
		// First, we'll need a ObjectPool that serves as the
		// actual pool of connections.
		//
		// We'll use a GenericObjectPool instance, although
		// any ObjectPool implementation will suffice.
		//
		GenericObjectPool connectionPool = new GenericObjectPool(null);

		connectionPool.setMinIdle(minIdle);
		connectionPool.setMaxActive(maxActive);
		

		_pool = connectionPool;
		// we keep it for two reasons
		// #1 We need it for statistics/debugging
		// #2 PoolingDataSource does not have getPool()
		// method, for some obscure, weird reason.

		//
		// Next, we'll create a ConnectionFactory that the
		// pool will use to create Connections.
		// We'll use the DriverManagerConnectionFactory,
		// using the connect string from configuration
		//
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
				connectURI, username, password);
		
		
		//
		// Now we'll create the PoolableConnectionFactory, which wraps
		// the "real" Connections created by the ConnectionFactory with
		// the classes that implement the pooling functionality.
		//
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
				connectionFactory, connectionPool, null, null, false, true);
		poolableConnectionFactory.setValidationQuery(getDbType().getValidationQuery());
		
		PoolingDataSource dataSource = new PoolingDataSource(connectionPool);
		

		return dataSource;
	}

	public  void printDriverStats() throws Exception {
		ObjectPool connectionPool = _pool;
		SmartLogger.logThis(Level.DEBUG,
				"NumActive: " + connectionPool.getNumActive());
		SmartLogger.logThis(Level.DEBUG,
				"NumIdle: " + connectionPool.getNumIdle());
	}


	public DBType getDBType() {
		return getDbType();
	}
	private void setDBType(DBType _dbt) {
		this.setDbType(_dbt);
	}

	public DataSource getDs() {
		return ds;
	}

	public  void setDs(DataSource ds) {
		this.ds = ds;
	}

	public  DBType getDbType() {
		return dbType;
	}

	public void setDbType(DBType dbType) {
		this.dbType = dbType;
	}


}
