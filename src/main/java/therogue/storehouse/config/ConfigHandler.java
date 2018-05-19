/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import therogue.storehouse.LOG;
import therogue.storehouse.Storehouse;

public class ConfigHandler {
	
	/**
	 * Creates required Variables
	 */
	private static Configuration configuration;
	private static File configFile;
	
	/**
	 * Gets configuration
	 */
	public static Configuration getConfiguration () {
		return configuration;
	}
	
	/**
	 * Gets configFile
	 */
	public static File getConfigFile () {
		return configFile;
	}
	
	/**
	 * Initialises a new configuration if it has not already been done and then loads the variables
	 */
	public static void preInit (File suggestedconfigFile) {
		LOG.log("debug", "Initialization of configuration");
		// Makes sure we have not already created a config object and initialises a new config object
		if (configuration == null)
		{
			configuration = new Configuration(suggestedconfigFile);
		}
		// Load Config and get Config parameters
		loadConfig();
	}
	
	/**
	 * Required for the Configuration GUI system, reloads the configuration if anything has changed in it
	 */
	@SubscribeEvent
	public void ConfigChangedEvent (ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equalsIgnoreCase(Storehouse.MOD_ID))
		{
			LOG.log("debug", "Config Params Changed");
			// Load Config and get Config parameters
			loadConfig();
		}
	}
	
	/**
	 * Tries to load the config and read the values, logs any exceptions and then saves the config file if it has changed
	 */
	public static void loadConfig () {
		LOG.log("trace", "Loading Config");
		try
		{
			// Load the configuration file
			configuration.load();
			// Read Values from the configuration file
			ConfigValues.readConfigValues();
		}
		catch (Exception e)
		{
			// Log the exception
			e.printStackTrace();
			LOG.log("warn", "Configuration Failed to load or read values");
		}
		finally
		{
			LOG.log("trace", "Saving Config");
			// Save the configuration file
			if (configuration.hasChanged())
			{
				configuration.save();
			}
		}
	}
}
