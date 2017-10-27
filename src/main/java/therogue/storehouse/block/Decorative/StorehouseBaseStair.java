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
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import therogue.storehouse.Storehouse;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.crafting.RecipeHelper;
import therogue.storehouse.util.LOG;

public class StorehouseBaseStair extends BlockStairs implements IStorehouseBaseBlock {
	
	public final IStorehouseBaseBlock blocktype;
	
	/**
	 * Does all the normal registering of stuff that the base block does
	 */
	public StorehouseBaseStair (IStorehouseBaseBlock block) {
		super(block.getBlock().getDefaultState());
		LOG.log("trace", "Creating new StorehouseBaseStair: " + block.getName() + "_stair");
		this.blocktype = block;
		this.setUnlocalizedName(block.getName() + "_stair");
		this.setRegistryName(Storehouse.MOD_ID, block.getName() + "_stair");
		this.setCreativeTab(Storehouse.CREATIVE_TAB);
	}
	
	/**
	 * Returns the Properly Formatted Unlocalised Name
	 */
	@Override
	public String getUnlocalizedName () {
		return String.format("tile.%s%s", Storehouse.RESOURCENAMEPREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}
	
	/**
	 * Useful method to make the code easier to read
	 */
	private String getUnwrappedUnlocalizedName (String unlocalizedName) {
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
	
	/**
	 * Gets the raw name as passed to the constructor of this class, useful in various places and also specified in IStorehouseBaseBlock.
	 */
	public String getName () {
		return getUnwrappedUnlocalizedName(super.getUnlocalizedName());
	}
	
	/**
	 * Registers this block easily
	 */
	@Override
	public void preInit () {
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this).setRegistryName(getRegistryName()));
	}
	
	/**
	 * Registers the texture for this block easily
	 */
	@Override
	@SideOnly (Side.CLIENT)
	public void preInitClient () {
		ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(this), 0, new ModelResourceLocation(getUnlocalizedName().substring(5), "facing=east,half=bottom,shape=straight"));
	}
	
	/**
	 * Getter for blockHardness
	 */
	@Override
	public float getblockHardness () {
		return blockHardness;
	}
	
	/**
	 * Getter for blockResistance
	 */
	@Override
	public float getblockResistance () {
		return blockResistance;
	}
	
	/**
	 * Getter for blockMaterial
	 */
	@Override
	public Material getblockMaterial () {
		return blockMaterial;
	}
	
	/**
	 * Sets Generic Recipes for the Block Type
	 */
	@Override
	public void Init () {
		if (RecipeHelper.getOreDictEntries(blocktype.getBlock()).equals(new ArrayList<String>()))
		{
			setrecipes(blocktype);
		}
		else
		{
			for (String s : RecipeHelper.getOreDictEntries(blocktype.getBlock()))
			{
				setrecipes(s);
			}
		}
	}
	
	/**
	 * Helper method to make code look cleaner
	 */
	private void setrecipes (Object s) {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 4), "  d", " dd", "ddd", 'd', s));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 4), "d  ", "dd ", "ddd", 'd', s));
	}
	
	@Override
	public Block getBlock () {
		return this;
	}
}
