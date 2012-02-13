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

package com.smartmarmot.common;

public class Constants {
	public static final String VERSION = "Version 0.5.1";
	public static final String BANNER = Constants.PROJECT_NAME + " " + VERSION;
	public static final String PROJECT_NAME = "DBforBIX";
	public static final String ITEM_PREFIX = PROJECT_NAME+".";	
	public static final String DATABASES_LIST = "DatabaseList";
	public static final String DATABASES_TYPE = "DatabaseType";
	public static final String DELIMITER = ",";
	public static final String QUERY_LIST = "QueryList";
	public static final String QUERY_LIST_FILE = "QueryListFile";
	public static final String EXTRA_QUERY_LIST_FILE = "ExtraQueryListFile";
	public static final String QUERY_POSTFIX = "Query";
	public static final String QUERY_NO_DATA_FOUND = "NoDataFound";
	public static final String QUERY_WHEN_NOT_ALIVE = "WhenNotAlive";
	public static final String CONN_URL = "Url";
	public static final String CONN_USERNAME = "User";
	public static final String CONN_PASSWORD = "Password";
	public static final String CONN_DEFAULT_USERNAME = "DefaultUser";
	public static final String CONN_DEFAULT_PASSWORD = "DefaultPassword";
	public static final String CONN_MAX_ACTIVE = "MaxActive";
	public static final String CONN_MAX_IDLE = "MaxIdle";
	public static final String CONN_MAX_WAIT = "MaxWait";
	
	public static final String ORACLE = "Oracle";
	public static final String ORACLE_VALIDATION_QUERY = "SELECT SYSDATE FROM DUAL";
	public static final String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
	public static final String ORACLE_WHOAMI_QUERY = "SELECT SYS_CONTEXT ('USERENV', 'SESSION_USER') FROM DUAL";
	public static final String ORACLE_DBNAME_QUERY = "SELECT SYS_CONTEXT ('USERENV', 'DB_NAME') FROM DUAL";
	
	public static final String MYSQL = "MySQL";
	public static final String MYSQL_VALIDATION_QUERY = "SELECT 1 FROM DUAL";
	public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	public static final String MYSQL_WHOAMI_QUERY = "SELECT USER()";
	public static final String MYSQL_DBNAME_QUERY = "SELECT DATABASE()";
	
	public static final String POSTGRESQL     			 = "PostgreSQL";
	public static final String POSTGRESQL_VALIDATION_QUERY = "SELECT 1";
	public static final String POSTGRESQL_WHOAMI_QUERY	 = "SELECT CURRENT_USER";
	public static final String POSTGRESQL_DBNAME_QUERY	 = "SELECT CURRENT_DATABASE()";
	public static final String POSTGRESQL_DRIVER     	     = "org.postgresql.Driver";
	
	public static final String DB2     			= "DB2";
	public static final String DB2_VALIDATION_QUERY = "SELECT 1";
	public static final String DB2_WHOAMI_QUERY	= "SELECT SYSTEM_USER";
	public static final String DB2_DBNAME_QUERY	= "SELECT @@SERVERNAME";
	public static final String DB2_DRIVER     	= "com.ibm.db2.jcc.DB2Driver";
	
	
	public static final String MSSQL     			= "MSSQL";
	public static final String MSSQL_VALIDATION_QUERY = "SELECT 1";
	public static final String MSSQL_WHOAMI_QUERY	= "SELECT CURRENT_USER";
	public static final String MSSQL_DBNAME_QUERY	= "SELECT db_name()";
	public static final String MSSQL_DRIVER     	= "net.sourceforge.jtds.jdbc.Driver";
	
	public static final String RACE_CONDITION_QUERY = "RaceConditionQuery";
	public static final String RACE_CONDITION_EXCLUDE_COLUMNS = "RaceConditionQueryExcludeColumnsList";
	public static final String RACE_CONDITION_VALUE = "RaceConditionValue";
	public static final String QUERY_PERIOD = "Period";
	public static final String QUERY_DEFAULT_PERIOD = "DefaultQueryPeriod";
	public static final String QUERY_ACTIVE = "Active";
	public static final String ZABBIX_SERVER_LIST = "ZabbixServerList";
	public static final String ZABBIX_SERVER_PORT = "Port";
	public static final String ZABBIX_SERVER_HOST = "Address";
	public static final String DBFORBIX_PIDFILE = "DBforBIX.PidFile";
	public static final String DBFORBIX_DAEMON_SLEEP = "DBforBIX.Sleep";
	public static final String DBFORBIX_DAEMON_THREAD = "DBforBIX.MaxThreadNumber";
	public static final int ZABBIX_SERVER_DEFAULT_PORT = 10051;
	public static final String QUERY_TRIM = "Trim";
	public static final String QUERY_SPACE = "AddSpaces";
	public static final String QUERY_EXCLUDE_COLUMNS = "ExcludeColumnsList";
}