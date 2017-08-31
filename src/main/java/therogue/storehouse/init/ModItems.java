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
	public static ItemStorehouseBaseMaterial upgrades;
	/**
	 * ItemStack versions
	 */
	public static ItemStack network_Inspector_itemstack;
	public static ItemStack extrusion_tool_itemstack;
	public static ItemStack plate_tool_itemStack;
	public static ItemStack cutter_tool_itemStack;
	public static ItemStack azurite_dust_itemstack;
	public static ItemStack azurite_crystal_itemstack;
	public static ItemStack copper_ingot_itemstack;
	public static ItemStack tin_ingot_itemstack;
	public static ItemStack aluminum_ingot_itemstack;
	public static ItemStack lead_ingot_itemstack;
	public static ItemStack steel_ingot_itemstack;
	public static ItemStack brass_ingot_itemstack;
	public static ItemStack bronze_ingot_itemstack;
	public static ItemStack Duralium_ingot_itemstack;
	public static ItemStack circuit_chip_itemstack;
	public static ItemStack iron_plate_itemstack;
	public static ItemStack copper_plate_itemstack;
	public static ItemStack tin_plate_itemstack;
	public static ItemStack gold_plate_itemstack;
	public static ItemStack steel_plate_itemstack;
	public static ItemStack diamond_edging_itemstack;
	public static ItemStack silicon_itemstack;
	public static ItemStack integrated_chip_itemstack;
	public static ItemStack copper_wire_itemstack;
	public static ItemStack gold_wire_itemstack;
	public static ItemStack solder_itemstack;
	public static ItemStack diamond_edged_steel_plate_itemstack;
	public static ItemStack timer_upgrade;
	public static ItemStack variable_chance_upgrade;
	public static ItemStack speed_upgrade;
	public static ItemStack crafting_loop_upgrade;
	public static ItemStack wireless_redstone_upgrade;
	public static ItemStack ejection_upgrade;
	public static ItemStack importer_upgrade;
	public static ItemStack interdimentional_upgrade;
	public static ItemStack singularity_core;
	public static ItemStack nitrogel;
	public static ItemStack black_hole_kickstarter;
	public static ItemStack transfer_unit;
	public static ItemStack fan_blade;
	public static ItemStack refrigerant_parts;
	public static ItemStack network_configurer;
	public static ItemStack network_requester;
	public static ItemStack network_tinkerer;
	public static ItemStack personal_stocker;
}
