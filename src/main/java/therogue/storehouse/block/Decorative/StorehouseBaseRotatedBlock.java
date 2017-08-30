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
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.client.init.BlockRender;
import therogue.storehouse.core.StorehouseCreativeTab;
import therogue.storehouse.reference.General;
import therogue.storehouse.reference.IDs;
import therogue.storehouse.util.LOG;

public class StorehouseBaseRotatedBlock extends BlockRotatedPillar implements IStorehouseBaseBlock {
	
	private final ArrayList<String> OredictEntrys = new ArrayList<String>();
	
	/**
	 * Does all the normal registering of stuff that the base block does
	 */
	public StorehouseBaseRotatedBlock (IStorehouseBaseBlock blocktype, String namesuffix) {
		super(blocktype.getblockMaterial());
		LOG.log("trace", "Creating new StorehouseBaseRotatedBlock: " + blocktype.getName() + "_" + namesuffix);
		this.setUnlocalizedName(blocktype.getName() + "_" + namesuffix);
		this.setRegistryName(General.MOD_ID, blocktype.getName() + "_" + namesuffix);
		this.setCreativeTab(StorehouseCreativeTab.CREATIVE_TAB);
		this.setHardness(blocktype.getblockHardness());
		this.setResistance(blocktype.getblockResistance());
	}
	
	/**
	 * Returns the Properly Formatted Unlocalised Name
	 */
	@Override
	public String getUnlocalizedName () {
		return String.format("tile.%s%s", IDs.RESOURCENAMEPREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
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
	 * Registers this block easily
	 */
	@Override
	public void preInit () {
		LOG.log("trace", "Registering StorehouseBaseRotatedBlock: " + getName());
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this).setRegistryName(getRegistryName()));
	}
	
	/**
	 * Registers the texture for this block easily
	 */
	@SideOnly (Side.CLIENT)
	@Override
	public void InitClient () {
		LOG.log("trace", "Registering StorehouseBaseRotatedBlock Texture: " + getName());
		BlockRender.blockTexture(this);
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
	 * Gets the Ore Dictionary names this block is registered as
	 */
	public ArrayList<String> getOredictEntrys () {
		return OredictEntrys;
	}
	
	/**
	 * Registers a name in the Ore Dictionary for this block and adds it to the list of entries
	 */
	public StorehouseBaseRotatedBlock setOredictEntry (String oredictEntry) {
		OreDictionary.registerOre(oredictEntry, this);
		OredictEntrys.add(oredictEntry);
		return this;
	}
	
	@Override
	public Block getBlock () {
		return this;
	}
}
