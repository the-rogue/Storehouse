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
import net.minecraftforge.oredict.OreDictionary;
import therogue.storehouse.item.IStorehouseBaseItem;
import therogue.storehouse.item.ItemStorehouseBaseMaterial;
import therogue.storehouse.item.StorehouseBaseItem;
import therogue.storehouse.item.tool.NetworkInspector;

public class ModItems {
	
	/**
	 * Initialises a new array to hold all the items
	 */
	public static ArrayList<IStorehouseBaseItem> itemlist = new ArrayList<IStorehouseBaseItem>();
	
	public static void preInit () {
		/**
		 * Tools
		 */
		network_Inspector = new NetworkInspector("network_inspector");
		itemlist.add(network_Inspector);
		/**
		 * Materials
		 */
		materials = new ItemStorehouseBaseMaterial("material");
		itemlist.add(materials);
		materials.addMaterial(0, "azurite_dust");
		materials.addMaterial(1, "azurite_crystal");
		materials.addMaterial(2, "copper_ingot");
		materials.addMaterial(3, "tin_ingot");
		materials.addMaterial(4, "circuit_chip");
		/**
		 * PreInit Items
		 */
		for (IStorehouseBaseItem item : itemlist)
		{
			item.preInit();
		}
		/**
		 * Tools
		 */
		network_Inspector_itemstack = new ItemStack(network_Inspector);
		/**
		 * Materials
		 */
		azurite_dust_itemstack = new ItemStack(materials, 1, 0);
		azurite_crystal_itemstack = new ItemStack(materials, 1, 1);
		copper_ingot_itemstack = new ItemStack(materials, 1, 2);
		tin_ingot_itemstack = new ItemStack(materials, 1, 3);
		circuit_chip_itemstack = new ItemStack(materials, 1, 4);
	}
	
	public static void Init () {
		/**
		 * Init Items
		 */
		for (IStorehouseBaseItem item : itemlist)
		{
			item.Init();
		}
		/**
		 * Register Ore Dictionary Names
		 */
		OreDictionary.registerOre("ingotCopper", copper_ingot_itemstack);
		OreDictionary.registerOre("ingotTin", tin_ingot_itemstack);
	}
	
	public static void postInit () {
		/**
		 * PostInit Items
		 */
		for (IStorehouseBaseItem item : itemlist)
		{
			item.postInit();
		}
	}
	
	/**
	 * Items
	 */
	public static StorehouseBaseItem network_Inspector;
	public static ItemStorehouseBaseMaterial materials;
	/**
	 * ItemStack versions
	 */
	public static ItemStack network_Inspector_itemstack;
	public static ItemStack azurite_dust_itemstack;
	public static ItemStack azurite_crystal_itemstack;
	public static ItemStack copper_ingot_itemstack;
	public static ItemStack tin_ingot_itemstack;
	public static ItemStack circuit_chip_itemstack;
}
