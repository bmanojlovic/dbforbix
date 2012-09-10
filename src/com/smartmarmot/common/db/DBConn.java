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
	private  DBConnManager dbcm =null;
	private  String dbname = null;

	public DBConn(DBConnManager _dbcm, String _name) {
		if (_dbcm == null)
			throw new RuntimeException("empty datasource");
		this.dbcm=_dbcm;
		if (_name!=null)
			if (_name.length() > 0)
				this.dbname=_name;
		else
			this.dbname="undefined";
	}

	public String getName() {
		return this.getDbname();
	}

	
	public  DBConnManager getDBcm() {
		return dbcm;
	}
	

	public  void setDBcm(DBConnManager dbcm) {
		this.dbcm = dbcm;
	}

	public  String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

}