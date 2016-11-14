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
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import therogue.storehouse.client.render.blocks.BlockRender;
import therogue.storehouse.reference.General;
import therogue.storehouse.reference.Resources;
import therogue.storehouse.util.loghelper;


public class StorehouseBaseBlock extends Block implements IStorehouseBaseBlock
{
	/**
	 * Initiates the block with a default material
	 */
	public StorehouseBaseBlock(String name)
	{
		this(name, Material.ROCK);
	}

	/**
	 * Initiates the block using a specified material and the name
	 */
	public StorehouseBaseBlock(String name, Material material)
	{
		super(material);
		loghelper.log("trace", "Creating new StorehouseBaseBlock: " + name);
		this.setUnlocalizedName(name);
		this.setRegistryName(General.MOD_ID, name);
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
	public void registerblock()
	{
		loghelper.log("trace", "Registering StorehouseBaseBlock: " + getName());
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this).setRegistryName(getRegistryName()));
	}

	/**
	 * Registers the texture for this block easily
	 */
	@SideOnly(Side.CLIENT)
	public void registertexture()
	{
		loghelper.log("trace", "Registering StorehouseBaseBlock Texture: " + getName());
		BlockRender.blockTexture(this);
	}
}
