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

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.client.render.blocks.BlockRender;
import therogue.storehouse.item.ItemStorehouseBaseSlab;
import therogue.storehouse.reference.General;
import therogue.storehouse.reference.Resources;


public abstract class StorehouseBaseSlab extends BlockSlab implements IStorehouseBaseBlock
{
	public static final PropertyEnum<StorehouseBaseSlab.Variant> VARIANT = PropertyEnum.<StorehouseBaseSlab.Variant> create("variant", StorehouseBaseSlab.Variant.class);
	public StorehouseBaseSlab.Half halfslab;

	public StorehouseBaseSlab()
	{
		super(Material.ROCK);

		IBlockState iblockstate = this.blockState.getBaseState();

		if (!this.isDouble())
		{
			iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
			this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		}
		this.setDefaultState(iblockstate.withProperty(VARIANT, Variant.DEFAULT));
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	@Nullable
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(this);
	}

	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(this);
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta)
	{
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
	public int getMetaFromState(IBlockState state)
	{
		int i = 0;

		if (!this.isDouble() && state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP)
		{
			i |= 8;
		}

		return i;
	}

	protected BlockStateContainer createBlockState()
	{
		return this.isDouble() ? new BlockStateContainer(this, new IProperty[] { VARIANT }) : new BlockStateContainer(this, new IProperty[] { HALF, VARIANT });
	}

	/**
	 * Returns the slab block name with the type associated with it
	 */
	public String getUnlocalizedName(int meta)
	{
		return String.format("tile.%s%s", Resources.RESOURCENAMEPREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}

	public String getUnlocalizedName()
	{
		return String.format("tile.%s%s", Resources.RESOURCENAMEPREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}

	private String getUnwrappedUnlocalizedName(String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}

	@SideOnly(Side.CLIENT)
	public void registertexture()
	{
		BlockRender.blockTexture(this);
	}

	@Override
	public String getName()
	{
		return getUnwrappedUnlocalizedName(super.getUnlocalizedName());
	}

	public IProperty<?> getVariantProperty()
	{
		return VARIANT;
	}

	public Comparable<?> getTypeForItem(ItemStack stack)
	{
		return Variant.DEFAULT;
	}

	public StorehouseBaseSlab.Half getHalfslab()
	{
		return halfslab;
	}

	public static class Double extends StorehouseBaseSlab
	{
		public Double(IStorehouseBaseBlock blocktype, StorehouseBaseSlab.Half halfslab)
		{
			super();
			this.halfslab = halfslab;
			super.setUnlocalizedName(blocktype.getName() + "_double_slab");
			this.setRegistryName(General.MOD_ID, blocktype.getName() + "_double_slab");
		}

		public boolean isDouble()
		{
			return true;
		}

		@Override
		public Item getItemDropped(IBlockState state, Random rand, int fortune)
		{
			return Item.getItemFromBlock(halfslab);
		}

		@Override
		public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
		{
			return new ItemStack(halfslab);
		}

		@Override
		public void registerblock()
		{
			GameRegistry.register(this);
			GameRegistry.register(new ItemStorehouseBaseSlab(halfslab, this));
		}
	}

	public static class Half extends StorehouseBaseSlab
	{
		public Half(IStorehouseBaseBlock blocktype)
		{
			super();
			super.setUnlocalizedName(blocktype.getName() + "_slab");
			this.setRegistryName(General.MOD_ID, blocktype.getName() + "_slab");
		}

		public boolean isDouble()
		{
			return false;
		}

		@Override
		public void registerblock()
		{
			GameRegistry.register(this);
		}

	}

	public static enum Variant implements IStringSerializable
	{
		DEFAULT;

		public String getName()
		{
			return "default";
		}
	}
}
