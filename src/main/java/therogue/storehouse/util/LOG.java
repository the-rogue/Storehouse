/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.util;

import java.util.HashMap;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import therogue.storehouse.config.ConfigValues;

public class LOG {
	
	private static final HashMap<String, Integer> SETTIMES = new HashMap<String, Integer>();
	public static Logger LOG;
	
	/**
	 * Log Method to make logging easier
	 */
	public static void log (String logLevel, Object object) {
		if (logLevel.equals("all"))
		{
			LOG.log(Level.ALL, String.valueOf(object));
		}
		else if (logLevel.equals("off"))
		{
			LOG.log(Level.OFF, String.valueOf(object));
		}
		else if (logLevel.equals("fatal"))
		{
			LOG.log(Level.FATAL, String.valueOf(object));
		}
		else if (logLevel.equals("error"))
		{
			LOG.log(Level.ERROR, String.valueOf(object));
		}
		else if (logLevel.equals("warn"))
		{
			LOG.log(Level.WARN, String.valueOf(object));
		}
		else if (logLevel.equals("info"))
		{
			LOG.log(Level.INFO, String.valueOf(object));
		}
		// Since FML doesn't print these to the console by default this is a good way to print debug/trace information when a config option is selected
		else if (logLevel.equals("debug"))
		{
			if (ConfigValues.debuglogging)
			{
				LOG.log(Level.INFO, "DEBUG: " + String.valueOf(object));
			}
			LOG.log(Level.DEBUG, String.valueOf(object));
		}
		else if (logLevel.equals("trace"))
		{
			if (ConfigValues.debuglogging)
			{
				LOG.log(Level.INFO, "TRACE: " + String.valueOf(object));
			}
			LOG.log(Level.TRACE, String.valueOf(object));
		}
	}
	
	public static void all (Object message) {
		LOG.log(Level.ALL, String.valueOf(message));
	}
	
	public static void off (Object message) {
		LOG.log(Level.OFF, String.valueOf(message));
	}
	
	public static void fatal (Object message) {
		LOG.log(Level.FATAL, String.valueOf(message));
	}
	
	public static void error (Object message) {
		LOG.log(Level.ERROR, String.valueOf(message));
	}
	
	public static void warn (Object message) {
		LOG.log(Level.WARN, String.valueOf(message));
	}
	
	public static void info (Object message) {
		LOG.log(Level.INFO, String.valueOf(message));
	}
	
	// Since FML doesn't print these to the console by default this is a good way to print debug/trace information when a config option is selected
	public static void debug (Object message) {
		if (ConfigValues.debuglogging)
		{
			LOG.log(Level.INFO, "DEBUG: " + String.valueOf(message));
		}
		LOG.log(Level.DEBUG, String.valueOf(message));
	}
	
	public static void trace (Object message) {
		if (ConfigValues.debuglogging)
		{
			LOG.log(Level.INFO, "TRACE: " + String.valueOf(message));
		}
		LOG.log(Level.TRACE, String.valueOf(message));
	}
	
	public static void logSetTimes (String key, int startvalue, Object object) {
		if (SETTIMES.get(key) == null)
		{
			SETTIMES.put(key, startvalue);
		}
		if (SETTIMES.get(key) != 0)
		{
			log("info", object);
			if (SETTIMES.get(key) != null && SETTIMES.get(key) != -1 && SETTIMES.get(key) != 0)
			{
				SETTIMES.put(key, SETTIMES.get(key) - 1);
			}
		}
	}
	
	public static void logSetTimes (String key, Object object) {
		logSetTimes(key, -1, object);
	}
	
	public static void updateSetTimes (String key, int times) {
		if (times >= -1)
		{
			SETTIMES.put(key, times);
		}
	}
	
	public static Set<String> getkeys () {
		return SETTIMES.keySet();
	}
}
