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

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import therogue.storehouse.item.IStorehouseBaseItem;
import therogue.storehouse.item.ItemStorehouseBaseMaterial;
import therogue.storehouse.item.StorehouseBaseItem;
import therogue.storehouse.item.tool.NetworkInspector;
import therogue.storehouse.reference.General;
import therogue.storehouse.reference.OreDictEntries;
import therogue.storehouse.util.LOG;

@GameRegistry.ObjectHolder (General.MOD_ID)
public class ModItems {
	
	/**
	 * Initialises a new array to hold all the items
	 */
	public static ArrayList<IStorehouseBaseItem> itemlist = new ArrayList<IStorehouseBaseItem>();
	/**
	 * Initialises all the items
	 */
	public static final StorehouseBaseItem network_Inspector = new NetworkInspector("network_inspector");
	public static final ItemStorehouseBaseMaterial materials = new ItemStorehouseBaseMaterial("material");
	/**
	 * Adds ItemStack versions that I can reference
	 */
	public static ItemStack network_Inspector_itemstack = new ItemStack(network_Inspector);
	public static ItemStack azurite_dust_itemstack = new ItemStack(materials, 1, 0);
	public static ItemStack azurite_crystal_itemstack = new ItemStack(materials, 1, 1);
	public static ItemStack copper_ingot_itemstack = new ItemStack(materials, 1, 2);
	public static ItemStack tin_ingot_itemstack = new ItemStack(materials, 1, 3);
	public static ItemStack circuit_chip_itemstack = new ItemStack(materials, 1, 4);
	/**
	 * Adds all the items to the array
	 */
	static
	{
		LOG.log("debug", "Adding Items");
		itemlist.add(network_Inspector);
		itemlist.add(materials);
		materials.addMaterial(0, "azurite_dust");
		materials.addMaterial(1, "azurite_crystal");
		materials.addMaterial(2, "copper_ingot", OreDictEntries.COPPER_INGOT);
		materials.addMaterial(3, "tin_ingot", OreDictEntries.TIN_INGOT);
		materials.addMaterial(4, "circuit_chip");
	}
	
	public static void preInit () {
		for (IStorehouseBaseItem item : itemlist)
		{
			item.preInit();
		}
	}
	
	public static void Init () {
		for (IStorehouseBaseItem item : itemlist)
		{
			item.Init();
		}
	}
	
	public static void postInit () {
		for (IStorehouseBaseItem item : itemlist)
		{
			item.postInit();
		}
	}
}
