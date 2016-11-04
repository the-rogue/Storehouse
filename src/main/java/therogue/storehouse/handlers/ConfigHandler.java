/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.handlers;

import java.io.File;

import therogue.storehouse.reference.ConfigValues;
import therogue.storehouse.reference.General;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;


public class ConfigHandler
{
	private static Configuration configuration;
	private static File configFile;

	public static Configuration getConfiguration()
	{
		return configuration;
	}

	public static File getConfigFile()
	{
		return configFile;
	}

	public static void init(File suggestedconfigFile)
	{
		// Makes sure we have not already created a config object and initialises a new config object
		if (configuration == null)
		{
			configuration = new Configuration(suggestedconfigFile);
		}

		// Load Config and get Config parameters
		loadConfig();
	}

	public static void ConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equalsIgnoreCase(General.MODID))
		{
			// Load Config and get Config parameters
			loadConfig();
		}
	}

	public static void loadConfig()
	{
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
		}
		finally
		{
			// Save the configuration file
			if (configuration.hasChanged())
			{
				configuration.save();
			}
		}
	}
}
