/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.util;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * Contains lots of methods for setting common recipes
 */
public class RecipeHelper
{
	public static void registerTwoWayBlockRecipe(Block block, Item item, @Nullable String blockoreentry, @Nullable String itemoreentry){
		if (block == null || item == null) {
			throw new RuntimeException("Attempted to register twoWayBlockRecipe with null item or block. Block: " + block + " Item: " + item + " BlockOreEntry: " + blockoreentry + " ItemOreEntry: " + itemoreentry);
		}
		if (blockoreentry != null && itemoreentry != null) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(item, 9), blockoreentry));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(block), "ddd", "ddd", "ddd", 'd', itemoreentry));
		} else if (blockoreentry != null) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(item, 9), blockoreentry));
			GameRegistry.addRecipe(new ItemStack(block), "ddd", "ddd", "ddd", 'd', item);
		} else if (itemoreentry != null) {
			GameRegistry.addShapelessRecipe(new ItemStack(item, 9), block);
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(block), "ddd", "ddd", "ddd", 'd', itemoreentry));
		} else {
			GameRegistry.addShapelessRecipe(new ItemStack(item, 9), block);
			GameRegistry.addRecipe(new ItemStack(block), "ddd", "ddd", "ddd", 'd', item);
		}
	}
	
}
