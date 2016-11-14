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
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public interface IStorehouseBaseBlock extends IForgeRegistryEntry<Block>
{
	/**
	 * Defines Methods that i need when referencing my block classes elsewhere
	 */
	// Convenient because not all blocks extend StorehouseBaseBlock, e.g. Stairs and Slabs
	@SideOnly(Side.CLIENT)
	public void registertexture();

	public IBlockState getDefaultState();

	public String getName();

	public void registerblock();

	public float getblockHardness();

	public float getblockResistance();

	public Material getblockMaterial();
}