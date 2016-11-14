/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.reference;

import net.minecraftforge.common.config.Configuration;
import therogue.storehouse.handlers.ConfigHandler;
import therogue.storehouse.util.loghelper;


public class ConfigValues
{
	/**
	 * Defines a new configuration object to make referencing it easier
	 */
	private static Configuration configuration;

	/**
	 * Creates any configuration categories needed
	 */

	/**
	 * Defines all values and their defaults for the GENERAL category
	 */
	public static boolean debuglogging = false;
	public static final boolean debugloggingdefault = false;

	/**
	 * Reads all configuration values in (easiest to do that here since all values are in this file already)
	 */
	public static void readConfigValues()
	{
		loghelper.log("trace", "Getting Configuration from ConfigHandler");
		configuration = ConfigHandler.getConfiguration();
		loghelper.log("trace", "Started Reading Config Values");

		// Reads Values from the config
		debuglogging = configuration.get(Configuration.CATEGORY_GENERAL, "DebugLogging", debugloggingdefault, "Whether or not to print out lots of debug info to the console").getBoolean();
		loghelper.log("trace", "Finished Reading Config Values");
	}
}
