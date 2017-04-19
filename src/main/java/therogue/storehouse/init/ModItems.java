/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.init;

import java.util.ArrayList;

import net.minecraftforge.fml.common.registry.GameRegistry;
import therogue.storehouse.item.IStorehouseBaseItem;
import therogue.storehouse.item.ItemStorehouseBaseMaterial;
import therogue.storehouse.item.StorehouseBaseItem;
import therogue.storehouse.item.tool.NetworkInspector;
import therogue.storehouse.reference.General;
import therogue.storehouse.util.loghelper;

@GameRegistry.ObjectHolder (General.MOD_ID)
public class ModItems {
	
	/**
	 * Initialises a new array to hold all the items
	 */
	public static ArrayList<IStorehouseBaseItem> itemlist = new ArrayList<IStorehouseBaseItem>();
	/**
	 * Initialises all the items
	 */
	public static final StorehouseBaseItem azurite_dust = new ItemStorehouseBaseMaterial("azurite_dust");
	public static final StorehouseBaseItem azurite_crystal = new ItemStorehouseBaseMaterial("azurite_crystal");
	public static final StorehouseBaseItem network_Inspector = new NetworkInspector("network_inspector");
	/**
	 * Adds all the items to the array
	 */
	static
	{
		loghelper.log("debug", "Adding Items");
		itemlist.add(azurite_dust);
		itemlist.add(azurite_crystal);
		itemlist.add(network_Inspector);
	}
	
	/**
	 * Registers all the items
	 */
	public static void preInit () {
		loghelper.log("debug", "Registering Items");
		for (IStorehouseBaseItem item : itemlist)
		{
			item.registeritem();
		}
	}
}
