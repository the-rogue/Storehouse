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
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import therogue.storehouse.LOG;
import therogue.storehouse.Storehouse;

public class StorehouseBaseRotatedBlock extends BlockRotatedPillar implements IStorehouseBaseBlock {
	
	/**
	 * Does all the normal registering of stuff that the base block does
	 */
	public StorehouseBaseRotatedBlock (IStorehouseBaseBlock blocktype, String namesuffix) {
		super(blocktype.getblockMaterial());
		LOG.log("trace", "Creating new StorehouseBaseRotatedBlock: " + blocktype.getName() + "_" + namesuffix);
		this.setUnlocalizedName(blocktype.getName() + "_" + namesuffix);
		this.setRegistryName(Storehouse.MOD_ID, blocktype.getName() + "_" + namesuffix);
		this.setCreativeTab(Storehouse.CREATIVE_TAB);
		this.setHardness(blocktype.getblockHardness());
		this.setResistance(blocktype.getblockResistance());
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
	@Override
	public String getName () {
		return getUnwrappedUnlocalizedName(super.getUnlocalizedName());
	}
	
	/**
	 * Registers the texture for this block easily
	 */
	@Override
	public void registerModels () {
		ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(this), 0, new ModelResourceLocation(getUnlocalizedName().substring(5), "axis=y"));
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
	
	@Override
	public Block getBlock () {
		return this;
	}
}
