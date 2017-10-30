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

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import therogue.storehouse.tile.multiblock.IMultiBlockPart;

public class BlockUtils {
	
	/**
	 * Helper method to test whether a block has a line of sight to the sky
	 * 
	 * @param pos the position of the block
	 * @param world the world the block is in
	 * @return whether the block has a line of sight to the sky
	 */
	public static boolean canBlockSeeSky (BlockPos pos, World world) {
		for (BlockPos blockpos = pos.up(); blockpos.getY() < world.getActualHeight(); blockpos = blockpos.up())
		{
			IBlockState iblockstate = world.getBlockState(blockpos);
			if (iblockstate.getBlock().getLightOpacity(iblockstate, world, blockpos) > 1 && iblockstate.getBlock() != Blocks.GLASS && !iblockstate.getMaterial().isLiquid()) { return false; }
		}
		return true;
	}
	
	/**
	 * Helper method to work out if a player is within 8 blocks of a TileEntity (and can therefore open it)
	 * 
	 * @param te the TileEntity in question
	 * @param player the player trying to open the TileEntity
	 * @return Whether the TileEntity could be opened
	 */
	public static boolean isUsableByPlayer (TileEntity te, EntityPlayer player) {
		return te.getWorld().getTileEntity(te.getPos()) != te ? false : player.getDistanceSq((double) te.getPos().getX() + 0.5D, (double) te.getPos().getY() + 0.5D, (double) te.getPos().getZ() + 0.5D) <= 64.0D;
	}
	
	/**
	 * Helper method to drop the inventory of a tileEntity using IItemHandler into the world
	 * 
	 * @param world the world to drop the contents into
	 * @param pos the position to drop the contents at
	 * @param tile the TileEntity to drop the contents of
	 */
	public static void dropInventory (World world, BlockPos pos, TileEntity tile) {
		if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
		{
			IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			for (int i = 0; i < inventory.getSlots(); ++i)
			{
				ItemStack itemstack = inventory.getStackInSlot(i);
				if (!itemstack.isEmpty())
				{
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemstack);
				}
			}
		}
	}
	
	public static boolean onMultiBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof IMultiBlockPart)
		{
			IMultiBlockPart mbpte = (IMultiBlockPart) te;
			return mbpte.getController().onMultiBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
		}
		return false;
	}
}
