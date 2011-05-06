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

import org.apache.commons.dbcp.datasources.SharedPoolDataSource;

public class DBConn {
	private SharedPoolDataSource spds;
	private String dbname;
	private DBType dbtype;
	/*private dbType dbtype;*/

	public DBConn(SharedPoolDataSource _spds, String _name,DBType _dbtype) {
		if (_spds == null)
			throw new RuntimeException("empty datasource");
		this.spds = _spds;
		this.dbtype=_dbtype;
		if (_name != null && _name.length() > 0)
			this.dbname = _name;
		else
			this.dbname = "undefined";
	}


	public String getName() {
		return this.dbname;
	}
	
	public DBType getDBType() {
		return this.dbtype;
	}

	public SharedPoolDataSource getSPDS() {
		return this.spds;
	}

}