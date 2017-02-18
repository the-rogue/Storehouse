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

import net.minecraftforge.fml.common.FMLLog;

import org.apache.logging.log4j.Level;

import therogue.storehouse.reference.ConfigValues;
import therogue.storehouse.reference.General;


public class loghelper
{
	private static final HashMap<String, Integer> SETTIMES = new HashMap<String, Integer>();

	/**
	 * Log Method to make logging easier
	 */
	public static void log(String logLevel, Object object)
	{
		if (logLevel.equals("all"))
		{
			FMLLog.log(General.MOD_NAME, Level.ALL, String.valueOf(object));
		}
		else if (logLevel.equals("off"))
		{
			FMLLog.log(General.MOD_NAME, Level.OFF, String.valueOf(object));
		}
		else if (logLevel.equals("fatal"))
		{
			FMLLog.log(General.MOD_NAME, Level.FATAL, String.valueOf(object));
		}
		else if (logLevel.equals("error"))
		{
			FMLLog.log(General.MOD_NAME, Level.ERROR, String.valueOf(object));
		}
		else if (logLevel.equals("warn"))
		{
			FMLLog.log(General.MOD_NAME, Level.WARN, String.valueOf(object));
		}
		else if (logLevel.equals("info"))
		{
			FMLLog.log(General.MOD_NAME, Level.INFO, String.valueOf(object));
		}
		// Since FML doesn't print these to the console by default this is a good way to print debug/trace information when a config option is selected
		else if (logLevel.equals("debug"))
		{
			if (ConfigValues.debuglogging)
			{
				FMLLog.log(General.MOD_NAME, Level.INFO, "DEBUG: " + String.valueOf(object));
			}
		}
		else if (logLevel.equals("trace"))
		{
			if (ConfigValues.debuglogging)
			{
				FMLLog.log(General.MOD_NAME, Level.INFO, "TRACE: " + String.valueOf(object));
			}
		}
	}

	public static void logSetTimes(String key, int startvalue, Object object)
	{
		if (SETTIMES.get(key) == null) {
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
	public static void logSetTimes(String key, Object object) {
		logSetTimes(key, -1, object);
	}

	public static void updateSetTimes(String key, int times)
	{
		if (times >= -1)
		{
			SETTIMES.put(key, times);
		}
	}
	
	public static Set<String> getkeys(){
		return SETTIMES.keySet();
	}
}
