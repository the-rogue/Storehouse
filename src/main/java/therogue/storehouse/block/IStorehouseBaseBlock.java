/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IStorehouseBaseBlock extends IForgeRegistryEntry<Block> {
	
	/**
	 * Defines Methods that I need when referencing my block classes elsewhere
	 */
	public Block getBlock ();
	
	public String getName ();
	
	public float getblockHardness ();
	
	public float getblockResistance ();
	
	public Material getblockMaterial ();
	
	public default void registerModels () {
		ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(getBlock()), 0, new ModelResourceLocation(
				getBlock().getUnlocalizedName().substring(5)));
	}
	
	public default String getUnlocalizedName (ItemStack stack) {
		return getBlock().getUnlocalizedName();
	}
	
	public default Item getItemBlock () {
		return new ItemBlock(getBlock()).setRegistryName(getBlock().getRegistryName());
	}
}
