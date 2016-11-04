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


public class ConfigValues
{
	private static Configuration configuration;

	// Categories

	// General Category (Values)
	private static boolean debuglogging = false;

	public static void readConfigValues()
	{
		configuration = ConfigHandler.getConfiguration();

		// Reads The values from the Config
		// Example: Value = configuration.get("Category under", "Property to find", default value, "Comment on the property").getTypethepropertyshouldbe(default value);
		debuglogging = configuration.get(Configuration.CATEGORY_GENERAL, "DebugLogging", debuglogging, "Whether or not to print out lots of debug info to the console").getBoolean();
	}
}
