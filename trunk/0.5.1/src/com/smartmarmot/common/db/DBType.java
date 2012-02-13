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

import com.smartmarmot.common.Constants;

public class DBType{
	public enum dbtype {pgsql, oracle, mssql, mysql,db2,undefined}
	private dbtype type;

   
	public  DBType(String data) {
        if (data.equalsIgnoreCase("pgsql"))
            this.type=dbtype.pgsql;
        if (data.equalsIgnoreCase("oracle"))
        	this.type=dbtype.oracle;
        if (data.equalsIgnoreCase("mssql"))
        	this.type=dbtype.mssql;
        if (data.equalsIgnoreCase("mysql"))
        	this.type=dbtype.mysql;
        if (data.equalsIgnoreCase("db2"))
        	this.type=dbtype.db2;
    }

    public String getDBTypeString() {
        switch (this.type) {
            case pgsql:
                return Constants.POSTGRESQL;
            case oracle:
                return  Constants.ORACLE;
            case mssql:
                return Constants.MSSQL;
            case mysql:
                return Constants.MYSQL;
            case db2:
            	return Constants.DB2;
        }
        return null;
    }
    public String getJDBCDriverClass() {
        switch (this.type) {
            case pgsql:
                return Constants.POSTGRESQL_DRIVER;
            case oracle:
                return  Constants.ORACLE_DRIVER;
            case mssql:
                return Constants.MSSQL_DRIVER;
            case mysql:
                return Constants.MYSQL_DRIVER;
            case db2:
            	return Constants.DB2_DRIVER;
        }
        return null;
    }
    public  String getValidationQuery() {
        switch (this.type) {
            case pgsql:
                return Constants.POSTGRESQL_VALIDATION_QUERY;
            case oracle:
                return Constants.ORACLE_VALIDATION_QUERY;
            case mssql:
                return Constants.MSSQL_VALIDATION_QUERY;
            case mysql:
                return Constants.MYSQL_VALIDATION_QUERY;
            case db2:
                return Constants.DB2_VALIDATION_QUERY;
       }
        return null;
    }
    
    public String getWhoAmIQuery() {
        switch (this.type) {
        case pgsql:
            return Constants.POSTGRESQL_WHOAMI_QUERY;
        case oracle:
            return Constants.ORACLE_WHOAMI_QUERY;
        case mssql:
            return Constants.MSSQL_WHOAMI_QUERY;
        case mysql:
            return Constants.MYSQL_WHOAMI_QUERY;
        case db2:
            return Constants.DB2_WHOAMI_QUERY;
        }
        return null;
    }

    public String getDbNameQuery() {
        switch (this.type) {
            case pgsql:
                return Constants.POSTGRESQL_DBNAME_QUERY;
            case oracle:
                return Constants.ORACLE_DBNAME_QUERY;
            case mssql:
                return Constants.MSSQL_DBNAME_QUERY;
            case mysql:
                return Constants.MYSQL_DBNAME_QUERY;
            case db2:
                return Constants.DB2_DBNAME_QUERY;
       }
        return null;
    }

}
