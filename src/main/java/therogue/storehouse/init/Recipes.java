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

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.crafting.MachineRecipe;
import therogue.storehouse.crafting.wrapper.FluidStackComponent;
import therogue.storehouse.crafting.wrapper.ItemStackComponent;
import therogue.storehouse.tile.machine.TileCrystaliser;
import therogue.storehouse.tile.machine.TileThermalPress;
import therogue.storehouse.util.LOG;
import therogue.storehouse.util.RecipeHelper;

public class Recipes {
	
	/**
	 * Sets all the recipes
	 */
	public static void init () {
		LOG.log("debug", "Registering default Recipes");
		setDefaultRecipes();
		LOG.log("debug", "Registering Multi Recipes");
		setMultipleRecipes();
		LOG.log("debug", "Registering Shaped Recipes");
		setShapedRecipes();
		LOG.log("debug", "Registering Shapeless Recipes");
		setShapelessRecipes();
		LOG.log("debug", "Registering Machine Recipes");
		setMachineRecipes();
	}
	
	/**
	 * Adds all the default/ generic recipes for a particular block/item e.g. the stair recipe
	 */
	private static void setDefaultRecipes () {
		for (IStorehouseBaseBlock block : ModBlocks.blocklist)
		{
			block.setDefaultRecipes();
		}
	}
	
	/**
	 * Adds all Recipes Involving the Ore Dictionary
	 */
	private static void setMultipleRecipes () {
		RecipeHelper.registerTwoWayBlockRecipe(ModBlocks.azurite_crystal_block, ModItems.azurite_crystal, RegOreDictionary.oreDictEntries.get(0), null);
		RecipeHelper.registerTwoWayBlockRecipe(ModBlocks.azurite_dust_block, ModItems.azurite_dust, null, null);
	}
	
	/**
	 * Adds all other shaped recipes
	 */
	private static void setShapedRecipes () {
	}
	
	/**
	 * Adds all other shapeless recipes
	 */
	private static void setShapelessRecipes () {
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.azurite_crystal_block_chiseled), ModBlocks.azurite_crystal_block_half_slab, ModBlocks.azurite_crystal_block_half_slab);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.azurite_crystal_block_pillar), ModBlocks.azurite_crystal_block, ModBlocks.azurite_crystal_block);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.azurite_dust_block), ModBlocks.azurite_dust_block_half_slab, ModBlocks.azurite_dust_block_half_slab);
	}
	
	private static void setMachineRecipes () {
		MachineCraftingHandler.register(TileThermalPress.class, new MachineRecipe(TileThermalPress.Mode.HIGH_PRESSURE.modeTest, 40, new ItemStackComponent(ModItems.azurite_crystal), new ItemStackComponent(ModBlocks.azurite_dust_block), new ItemStackComponent(ModItems.azurite_dust),
				new ItemStackComponent(ModBlocks.azurite_crystal_block_pillar), new ItemStackComponent(ModItems.azurite_dust), new ItemStackComponent(ModBlocks.azurite_crystal_block_pillar)));
		MachineCraftingHandler.register(TileThermalPress.class, new MachineRecipe(TileThermalPress.Mode.HIGH_PRESSURE.modeTest, 40, new ItemStackComponent(Items.DIAMOND), new ItemStackComponent(ModItems.azurite_dust), new ItemStackComponent(ModItems.azurite_dust),
				new ItemStackComponent(Items.IRON_INGOT), new ItemStackComponent(Items.REDSTONE), new ItemStackComponent(Items.REDSTONE)));
		MachineCraftingHandler.register(TileCrystaliser.class, new MachineRecipe(MachineRecipe.ALWAYSMODE, 80, new ItemStackComponent(ModItems.azurite_crystal), new ItemStackComponent(ModItems.azurite_dust), new FluidStackComponent(new FluidStack(FluidRegistry.WATER, 1000))));
	}
}