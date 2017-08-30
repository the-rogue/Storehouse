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
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.block.StorehouseBaseBlock;
import therogue.storehouse.block.StorehouseBaseVariantBlock;
import therogue.storehouse.block.Decorative.StorehouseBaseRotatedBlock;
import therogue.storehouse.block.Decorative.StorehouseBaseSlab;
import therogue.storehouse.block.Decorative.StorehouseBaseStair;
import therogue.storehouse.block.machine.BlockCombustionGenerator;
import therogue.storehouse.block.machine.BlockCrystaliser;
import therogue.storehouse.block.machine.BlockLiquidGenerator;
import therogue.storehouse.block.machine.BlockSolarGenerator;
import therogue.storehouse.block.machine.BlockThermalPress;

public class ModBlocks {
	
	/**
	 * Initialises a new array to hold all the blocks
	 */
	public static ArrayList<IStorehouseBaseBlock> blocklist = new ArrayList<IStorehouseBaseBlock>();
	
	/**
	 * Adds all the blocks to the array
	 */
	public static void preInit () {
		/**
		 * Adds Decorative blocks
		 */
		azurite_dust_block = new StorehouseBaseBlock("azurite_dust_block");
		blocklist.add(azurite_dust_block);
		azurite_crystal_block = new StorehouseBaseBlock("azurite_crystal_block");
		blocklist.add(azurite_crystal_block);
		azurite_crystal_block_chiseled = new StorehouseBaseBlock("azurite_crystal_block_chiseled");
		blocklist.add(azurite_crystal_block_chiseled);
		azurite_crystal_block_pillar = new StorehouseBaseRotatedBlock(azurite_crystal_block, "pillar");
		blocklist.add(azurite_crystal_block_pillar);
		/**
		 * Adds Decorative block varients
		 */
		azurite_dust_block_stair = new StorehouseBaseStair(azurite_dust_block);
		blocklist.add(azurite_dust_block_stair);
		azurite_dust_block_half_slab = new StorehouseBaseSlab.Half(azurite_dust_block);
		blocklist.add(azurite_dust_block_half_slab);
		azurite_dust_block_double_slab = new StorehouseBaseSlab.Double(azurite_dust_block, azurite_dust_block_half_slab);
		blocklist.add(azurite_dust_block_double_slab);
		azurite_crystal_block_stair = new StorehouseBaseStair(azurite_crystal_block);
		blocklist.add(azurite_crystal_block_stair);
		azurite_crystal_block_half_slab = new StorehouseBaseSlab.Half(azurite_crystal_block);
		blocklist.add(azurite_crystal_block_half_slab);
		azurite_crystal_block_double_slab = new StorehouseBaseSlab.Double(azurite_crystal_block, azurite_crystal_block_half_slab);
		blocklist.add(azurite_crystal_block_double_slab);
		/**
		 * Add Materials
		 */
		ore_blocks = new StorehouseBaseVariantBlock("ore_block", 3);
		blocklist.add(ore_blocks);
		ore_blocks.addSubBlock(0, "azurite", ModItems.azurite_dust_itemstack, 3, 6);
		ore_blocks.addSubBlock(1, "copper");
		ore_blocks.addSubBlock(2, "tin");
		/**
		 * Add Machines
		 */
		solar_generator = new BlockSolarGenerator("solar_generator");
		blocklist.add(solar_generator);
		combustion_generator = new BlockCombustionGenerator("combustion_generator");
		blocklist.add(combustion_generator);
		liquid_generator = new BlockLiquidGenerator("liquid_generator");
		blocklist.add(liquid_generator);
		thermal_press = new BlockThermalPress("thermal_press");
		blocklist.add(thermal_press);
		crystaliser = new BlockCrystaliser("crystaliser");
		blocklist.add(crystaliser);
		/**
		 * PreInit Blocks
		 */
		for (IStorehouseBaseBlock block : blocklist)
		{
			block.preInit();
		}
		/**
		 * Adds Decorative blocks
		 */
		azurite_dust_block_itemstack = new ItemStack(azurite_dust_block);
		azurite_crystal_block_itemstack = new ItemStack(azurite_crystal_block);
		azurite_crystal_block_chiseled_itemstack = new ItemStack(azurite_crystal_block_chiseled);
		azurite_crystal_block_pillar_itemstack = new ItemStack(azurite_crystal_block_pillar);
		/**
		 * Adds Decorative block varients
		 */
		azurite_dust_block_stair_itemstack = new ItemStack(azurite_dust_block_stair);
		azurite_dust_block_half_slab_itemstack = new ItemStack(azurite_dust_block_half_slab);
		azurite_crystal_block_stair_itemstack = new ItemStack(azurite_crystal_block_stair);
		azurite_crystal_block_half_slab_itemstack = new ItemStack(azurite_crystal_block_half_slab);
		/**
		 * Add Materials
		 */
		azurite_ore_itemstack = new ItemStack(ore_blocks, 1, 0);
		copper_ore_itemstack = new ItemStack(ore_blocks, 1, 1);
		tin_ore_itemstack = new ItemStack(ore_blocks, 1, 2);
		/**
		 * Add Machines
		 */
		solar_generator_itemstack = new ItemStack(solar_generator);
		combustion_generator_itemstack = new ItemStack(combustion_generator);
		liquid_generator_itemstack = new ItemStack(liquid_generator);
		thermal_press_itemstack = new ItemStack(thermal_press);
		crystaliser_itemstack = new ItemStack(crystaliser);
	}
	
	public static void Init () {
		/**
		 * Init Blocks
		 */
		for (IStorehouseBaseBlock block : blocklist)
		{
			block.Init();
		}
		/**
		 * Register Ore Dictionary Names
		 */
		OreDictionary.registerOre("blockazuritecrystal", azurite_crystal_block);
		OreDictionary.registerOre("blockazuritecrystal", azurite_crystal_block_chiseled);
		OreDictionary.registerOre("blockazuritecrystal", azurite_crystal_block_pillar);
		OreDictionary.registerOre("oreCopper", copper_ore_itemstack);
		OreDictionary.registerOre("oreTin", tin_ore_itemstack);
	}
	
	public static void postInit () {
		/**
		 * PostInit Blocks
		 */
		for (IStorehouseBaseBlock block : blocklist)
		{
			block.postInit();
		}
	}
	
	/**
	 * Blocks
	 */
	public static StorehouseBaseBlock azurite_dust_block;
	public static StorehouseBaseBlock azurite_crystal_block;
	public static StorehouseBaseBlock azurite_crystal_block_chiseled;
	public static StorehouseBaseRotatedBlock azurite_crystal_block_pillar;
	public static StorehouseBaseStair azurite_dust_block_stair;
	public static StorehouseBaseSlab.Half azurite_dust_block_half_slab;
	public static StorehouseBaseSlab.Double azurite_dust_block_double_slab;
	public static StorehouseBaseStair azurite_crystal_block_stair;
	public static StorehouseBaseSlab.Half azurite_crystal_block_half_slab;
	public static StorehouseBaseSlab.Double azurite_crystal_block_double_slab;
	public static StorehouseBaseVariantBlock ore_blocks;
	public static StorehouseBaseVariantBlock crafting_blocks;
	public static BlockSolarGenerator solar_generator;
	public static BlockCombustionGenerator combustion_generator;
	public static BlockLiquidGenerator liquid_generator;
	public static BlockThermalPress thermal_press;
	public static BlockCrystaliser crystaliser;
	/**
	 * ItemStack versions
	 */
	public static ItemStack azurite_dust_block_itemstack;
	public static ItemStack azurite_crystal_block_itemstack;
	public static ItemStack azurite_crystal_block_chiseled_itemstack;
	public static ItemStack azurite_crystal_block_pillar_itemstack;
	public static ItemStack azurite_dust_block_stair_itemstack;
	public static ItemStack azurite_dust_block_half_slab_itemstack;
	public static ItemStack azurite_crystal_block_stair_itemstack;
	public static ItemStack azurite_crystal_block_half_slab_itemstack;
	public static ItemStack azurite_ore_itemstack;
	public static ItemStack copper_ore_itemstack;
	public static ItemStack tin_ore_itemstack;
	public static ItemStack machine_shell_itemstack;
	public static ItemStack advanced_machine_shell_itemstack;
	public static ItemStack solar_generator_itemstack;
	public static ItemStack combustion_generator_itemstack;
	public static ItemStack liquid_generator_itemstack;
	public static ItemStack thermal_press_itemstack;
	public static ItemStack crystaliser_itemstack;
}
