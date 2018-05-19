/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import therogue.storehouse.LOG;
import therogue.storehouse.Storehouse;
import therogue.storehouse.config.ConfigHandler;

public class StorehouseConfigGui extends GuiConfig {
	
	/**
	 * Creates a new Configuration GUI
	 */
	public StorehouseConfigGui (GuiScreen parent) {
		super(parent, getConfigElements(), Storehouse.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(ConfigHandler.getConfiguration().toString()));
		LOG.log("trace", "Config GUI Finished (Successfully!)");
	}
	
	/**
	 * Compiles a list of Configuration elements to add to the screen if i want to separate different categories out into different screens
	 */
	private static List<IConfigElement> getConfigElements () {
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		LOG.log("trace", "Adding Config Elements to Config GUI: ");
		// Add categories to config GUI
		list.add(new ConfigElement(ConfigHandler.getConfiguration().getCategory(Configuration.CATEGORY_GENERAL)));
		return list;
	}
}
