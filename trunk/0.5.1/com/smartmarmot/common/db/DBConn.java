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


public class DBConn {
	private static DBConnManager dbcm =null;
	private static String dbname = null;

	public DBConn(DBConnManager _dbcm, String _name) {
		if (_dbcm == null)
			throw new RuntimeException("empty datasource");
		DBConn.setDBcm(_dbcm);
		if (_name!=null)
			if (_name.length() > 0)
				DBConn.setDbname(_name);
		else
			DBConn.setDbname("undefined");
	}

	public String getName() {
		return DBConn.getDbname();
	}

	
	public static DBConnManager getDBcm() {
		return dbcm;
	}
	

	public static void setDBcm(DBConnManager dbcm) {
		DBConn.dbcm = dbcm;
	}

	public static String getDbname() {
		return dbname;
	}

	public static void setDbname(String dbname) {
		DBConn.dbname = dbname;
	}

}