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
import therogue.storehouse.LOG;
import therogue.storehouse.Storehouse;

public class StorehouseBaseBlock extends Block implements IStorehouseBaseBlock {
	
	/**
	 * Declares defaults
	 */
	protected static Material default_material = Material.ROCK;
	protected static float default_hardness = 3.0F;
	protected static float default_resistance = 10.0F;
	
	/**
	 * Initiates the block with various defaults
	 */
	public StorehouseBaseBlock (String name) {
		this(name, default_material);
	}
	
	public StorehouseBaseBlock (String name, Material material) {
		this(name, material, default_hardness);
	}
	
	public StorehouseBaseBlock (String name, float hardness) {
		this(name, hardness, default_resistance);
	}
	
	public StorehouseBaseBlock (String name, Material material, float hardness) {
		this(name, material, hardness, default_resistance);
	}
	
	public StorehouseBaseBlock (String name, float hardness, float resistance) {
		this(name, default_material, hardness, resistance);
	}
	
	/**
	 * Initiates the block using the specified properties
	 */
	public StorehouseBaseBlock (String name, Material material, float hardness, float resistance) {
		super(material);
		LOG.log("trace", "Creating new StorehouseBaseBlock: " + name);
		this.setUnlocalizedName(name);
		this.setRegistryName(Storehouse.MOD_ID, name);
		this.setHardness(hardness);
		this.setResistance(resistance);
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
	@Override
	public String getName () {
		return getUnwrappedUnlocalizedName(super.getUnlocalizedName());
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
