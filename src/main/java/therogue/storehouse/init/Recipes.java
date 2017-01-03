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

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.util.RecipeHelper;
import therogue.storehouse.util.loghelper;


public class Recipes
{
	/**
	 * Sets all the recipes
	 */
	public static void init()
	{
		loghelper.log("debug", "Registering default Recipes");
		setDefaultRecipes();
		loghelper.log("debug", "Registering Multi Recipes");
		setMultipleRecipes();
		loghelper.log("debug", "Registering Shaped Recipes");
		setShapedRecipes();
		loghelper.log("debug", "Registering Shapeless Recipes");
		setShapelessRecipes();
	}

	/**
	 * Adds all the default/ generic recipes for a particular block/item e.g. the stair recipe
	 */
	private static void setDefaultRecipes()
	{
		for (IStorehouseBaseBlock block : ModBlocks.blocklist)
		{
			block.setDefaultRecipes();
		}
	}

	/**
	 * Adds all Recipes Involving the Ore Dictionary
	 */
	private static void setMultipleRecipes()
	{
		RecipeHelper.registerTwoWayBlockRecipe(ModBlocks.azurite_crystal_block, ModItems.azurite_crystal, RegOreDictionary.oreDictEntries.get(0), null);
		RecipeHelper.registerTwoWayBlockRecipe(ModBlocks.azurite_dust_block, ModItems.azurite_dust, null, null);
	}

	/**
	 * Adds all other shaped recipes
	 */
	private static void setShapedRecipes()
	{
	}

	/**
	 * Adds all other shapeless recipes
	 */
	private static void setShapelessRecipes()
	{
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.azurite_crystal_block_chiseled), ModBlocks.azurite_crystal_block_half_slab, ModBlocks.azurite_crystal_block_half_slab);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.azurite_crystal_block_pillar), ModBlocks.azurite_crystal_block, ModBlocks.azurite_crystal_block);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.azurite_dust_block), ModBlocks.azurite_dust_block_half_slab, ModBlocks.azurite_dust_block_half_slab);
	}
}