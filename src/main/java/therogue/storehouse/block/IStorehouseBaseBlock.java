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
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import therogue.storehouse.util.IInit;

public interface IStorehouseBaseBlock extends IForgeRegistryEntry<Block>, IInit {
	
	/**
	 * Defines Methods that I need when referencing my block classes elsewhere
	 */
	public Block getBlock ();
	
	public String getName ();
	
	public float getblockHardness ();
	
	public float getblockResistance ();
	
	public Material getblockMaterial ();
	
	public default String getUnlocalizedName (ItemStack stack) {
		return getBlock().getUnlocalizedName();
	}
	
	@SideOnly (Side.CLIENT)
	public default void preInitClient () {
	};
	
	public default void Init () {
	};
	
	public default void postInit () {
	}
	
	@SideOnly (Side.CLIENT)
	public default void postInitClient () {
	};
}
