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

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import therogue.storehouse.LOG;
import therogue.storehouse.Storehouse;

public abstract class StorehouseBaseSlab extends BlockSlab implements IStorehouseBaseBlock {
	
	public static final PropertyEnum<StorehouseBaseSlab.Variant> VARIANT = PropertyEnum.<StorehouseBaseSlab.Variant> create("variant", StorehouseBaseSlab.Variant.class);
	protected StorehouseBaseSlab.Half halfslab;
	public final IStorehouseBaseBlock blocktype;
	
	/**
	 * Registers the normal stuff and then sets the default properties depending on whether it is a double slab or not
	 */
	public StorehouseBaseSlab (IStorehouseBaseBlock blocktype) {
		super(blocktype.getblockMaterial());
		this.blocktype = blocktype;
		this.setHardness(blocktype.getblockHardness());
		this.setResistance(blocktype.getblockResistance());
		IBlockState iblockstate = this.getDefaultState();
		if (!this.isDouble())
		{
			iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
			this.setCreativeTab(Storehouse.CREATIVE_TAB);
		}
		this.setDefaultState(iblockstate.withProperty(VARIANT, Variant.DEFAULT));
	}
	
	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	@Nullable
	public Item getItemDropped (IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}
	
	public ItemStack getItem (World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(this);
	}
	
	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta (int meta) {
		IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, Variant.DEFAULT);
		if (!this.isDouble())
		{
			iblockstate = iblockstate.withProperty(HALF, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
		}
		return iblockstate;
	}
	
	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState (IBlockState state) {
		int i = 0;
		if (!this.isDouble() && state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP)
		{
			i |= 8;
		}
		return i;
	}
	
	/**
	 * Creates the Block State
	 */
	protected BlockStateContainer createBlockState () {
		return this.isDouble() ? new BlockStateContainer(this, new IProperty[] { VARIANT }) : new BlockStateContainer(
				this, new IProperty[] { HALF, VARIANT });
	}
	
	/**
	 * Returns the Properly Formatted Unlocalised Name
	 */
	public String getUnlocalizedName (int meta) {
		return String.format("tile.%s%s", Storehouse.RESOURCENAMEPREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}
	
	/**
	 * Returns the Properly Formatted Unlocalised Name
	 */
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
	 * Method Required By BlockSlab Returns Variant Property
	 */
	public IProperty<?> getVariantProperty () {
		return VARIANT;
	}
	
	/**
	 * Method Required By BlockSlab Returns The Variant Property type for this slab (Always default)
	 */
	public Comparable<?> getTypeForItem (ItemStack stack) {
		return Variant.DEFAULT;
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
	
	/**
	 * Double Slab Class
	 */
	public static class Double extends StorehouseBaseSlab {
		
		/**
		 * Constructs a new Double Slab and sets the specific properties of the double slab
		 */
		public Double (IStorehouseBaseBlock blocktype, StorehouseBaseSlab.Half halfslab) {
			super(blocktype);
			LOG.log("trace", "Creating new StorehouseBaseSlab.Double: " + blocktype.getName() + "_double_slab");
			this.halfslab = halfslab;
			super.setUnlocalizedName(blocktype.getName() + "_double_slab");
			this.setRegistryName(Storehouse.MOD_ID, blocktype.getName() + "_double_slab");
		}
		
		/**
		 * Method Required By BlockSlab Returns whether it is a double slab or not
		 */
		public boolean isDouble () {
			return true;
		}
		
		/**
		 * Tells the game to drop the halfslab item
		 */
		@Override
		public Item getItemDropped (IBlockState state, Random rand, int fortune) {
			return Item.getItemFromBlock(halfslab);
		}
		
		/**
		 * Tells the game to drop the halfslab item
		 */
		@Override
		public ItemStack getItem (World worldIn, BlockPos pos, IBlockState state) {
			return new ItemStack(halfslab);
		}
		
		/**
		 * Registers the block and the item, it is easiest to register the halfslab item here
		 */
		@Override
		public Item getItemBlock () {
			return new ItemSlab(halfslab, halfslab, this).setRegistryName(Storehouse.MOD_ID, halfslab.getName());
		}
		
		@Override
		public void registerModels () {
		}
	}
	
	/**
	 * Half Slab Class
	 */
	public static class Half extends StorehouseBaseSlab {
		
		/**
		 * Constructs a new Half Slab and sets the specific properties of the half slab
		 */
		public Half (IStorehouseBaseBlock blocktype) {
			super(blocktype);
			LOG.log("trace", "Creating new StorehouseBaseSlab.Half: " + blocktype.getName() + "_slab");
			super.setUnlocalizedName(blocktype.getName() + "_slab");
			this.setRegistryName(Storehouse.MOD_ID, blocktype.getName() + "_slab");
		}
		
		/**
		 * Method Required By BlockSlab Returns whether it is a double slab or not
		 */
		public boolean isDouble () {
			return false;
		}
		
		/**
		 * Only registers the block, because the halfslab item is registered with the doubleslab
		 */
		@Override
		public Item getItemBlock () {
			return null;
		}
		
		/**
		 * Registers the texture for this block easily
		 */
		@Override
		public void registerModels () {
			ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(this), 0, new ModelResourceLocation(
					getUnlocalizedName().substring(5), "half=bottom,variant=default"));
		}
	}
	
	/**
	 * Required Enum Property for some of the required BlockSlab methods to use
	 */
	static enum Variant implements IStringSerializable {
		DEFAULT;
		
		public String getName () {
			return "default";
		}
	}
}
