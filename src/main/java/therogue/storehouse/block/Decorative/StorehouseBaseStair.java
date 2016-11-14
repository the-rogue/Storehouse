/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.block.Decorative;

import net.minecraft.block.BlockStairs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.client.render.blocks.BlockRender;
import therogue.storehouse.reference.General;
import therogue.storehouse.reference.Resources;
import therogue.storehouse.util.loghelper;


public class StorehouseBaseStair extends BlockStairs implements IStorehouseBaseBlock
{
	/**
	 * Does all the normal registering of stuff that the base block does
	 */
	public StorehouseBaseStair(IStorehouseBaseBlock block)
	{
		super(block.getDefaultState());
		loghelper.log("trace", "Creating new StorehouseBaseStair: " + block.getName() + "_stair");
		this.setUnlocalizedName(block.getName() + "_stair");
		this.setRegistryName(General.MOD_ID, block.getName() + "_stair");
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}

	/**
	 * Returns the Properly Formatted Unlocalised Name
	 */
	@Override
	public String getUnlocalizedName()
	{
		return String.format("tile.%s%s", Resources.RESOURCENAMEPREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}

	/**
	 * Useful method to make the code easier to read
	 */
	private String getUnwrappedUnlocalizedName(String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}

	/**
	 * Gets the raw name as passed to the constructor of this class, useful in various places and also specified in IStorehouseBaseBlock.
	 */
	public String getName()
	{
		return getUnwrappedUnlocalizedName(super.getUnlocalizedName());
	}

	/**
	 * Registers this block easily
	 */
	@Override
	public void registerblock()
	{
		loghelper.log("trace", "Registering StorehouseBaseStair: " + getName());
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this).setRegistryName(getRegistryName()));
	}

	/**
	 * Registers the texture for this block easily
	 */
	@SideOnly(Side.CLIENT)
	public void registertexture()
	{
		loghelper.log("trace", "Registering StorehouseBaseStair Texture: " + getName());
		BlockRender.blockTexture(this);
	}
}
