/*
 * Copyright (C) 2010 Andrea Dalle Vacche.
 * @author Andrea Dalle Vacche
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

package com.smartmarmot.dbforbix;




import com.smartmarmot.dbforbix.Constants;
import com.smartmarmot.dbforbix.DBforBIX;



public class bootstrap {

	private static DBforBIX runner;
	public static void printUsage() {
		System.out.println(Constants.BANNER);
		System.out.println("USAGE");
		System.out
		.println("[run.sh|run.bat] -Dlog4j.properties=log4jpropertiesfile [start|stop] configfile");
		System.out
		.println("log4jpropertiesfile     = E.g. ./conf/log4j.properties");
		System.out
		.println("configfile              = E.g. ./conf/config.props	");
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) {
		try {
			if (args.length > 2) {
				printUsage();
				System.exit(0);
			}

			String cmd = new String(args[0].toString());
			String configFile = new String(args[1].toString());


			if (cmd.equalsIgnoreCase("start")) {
				runner = new DBforBIX(configFile);
				new Thread(runner);
				runner.run();
			}
			else if (args[0].equalsIgnoreCase("stop")) {
				if (runner != null) {
					runner.terminate();
					System.err.println("Runner terminated");
				}
				else
					System.err.println("No daemon running");
			} else{
			printUsage();
		}
	} catch (Exception ex) {
		ex.printStackTrace();
	}

}



}