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

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.multisystem.BlockEntry;
import therogue.storehouse.client.gui.multisystem.IEntry;
import therogue.storehouse.client.gui.multisystem.IPage;
import therogue.storehouse.client.render.blocks.BlockRender;
import therogue.storehouse.core.StorehouseCreativeTab;
import therogue.storehouse.reference.General;
import therogue.storehouse.reference.IDs;
import therogue.storehouse.util.loghelper;


public class StorehouseBaseStair extends BlockStairs implements IStorehouseBaseBlock
{
	public final IStorehouseBaseBlock blocktype;
	private final ArrayList<String> OredictEntrys = new ArrayList<String>();

	/**
	 * Does all the normal registering of stuff that the base block does
	 */
	public StorehouseBaseStair(IStorehouseBaseBlock block)
	{
		super(block.getBlock().getDefaultState());
		loghelper.log("trace", "Creating new StorehouseBaseStair: " + block.getName() + "_stair");
		this.blocktype = block;
		this.setUnlocalizedName(block.getName() + "_stair");
		this.setRegistryName(General.MOD_ID, block.getName() + "_stair");
		this.setCreativeTab(StorehouseCreativeTab.CREATIVE_TAB);
	}

	/**
	 * Returns the Properly Formatted Unlocalised Name
	 */
	@Override
	public String getUnlocalizedName()
	{
		return String.format("tile.%s%s", IDs.RESOURCENAMEPREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
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

	/**
	 * Getter for blockHardness
	 */
	@Override
	public float getblockHardness()
	{
		return blockHardness;
	}

	/**
	 * Getter for blockResistance
	 */
	@Override
	public float getblockResistance()
	{
		return blockResistance;
	}

	/**
	 * Getter for blockMaterial
	 */
	@Override
	public Material getblockMaterial()
	{
		return blockMaterial;
	}

	/**
	 * Sets Generic Recipes for the Block Type
	 */
	public void setDefaultRecipes()
	{
		if (blocktype.getOredictEntrys().equals(new ArrayList<String>()))
		{
			setrecipes(blocktype);
		}
		else
		{
			for (String s : blocktype.getOredictEntrys())
			{
				setrecipes(s);
			}
		}
	}

	/**
	 * Helper method to make code look cleaner
	 */
	private void setrecipes(Object s)
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 4), "  d", " dd", "ddd", 'd', s));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 4), "d  ", "dd ", "ddd", 'd', s));
	}

	/**
	 * Gets the Ore Dictionary names this block is registered as
	 */
	public ArrayList<String> getOredictEntrys()
	{
		return OredictEntrys;
	}

	/**
	 * Registers a name in the Ore Dictionary for this block and adds it to the list of entries
	 */
	public void setOredictEntry(String oredictEntry)
	{
		OreDictionary.registerOre(oredictEntry, this);
		OredictEntrys.add(oredictEntry);
	}

	@Override
	public Block getBlock()
	{
		return this;
	}
	
	@Override
	public IEntry getEntry()
	{
		return new BlockEntry(){

			@Override
			public IPage[] buildPage(GuiBase gui, int width, int height)
			{
				// TODO Auto-generated method stub
				return null;
			}
			
		};
	}
}
