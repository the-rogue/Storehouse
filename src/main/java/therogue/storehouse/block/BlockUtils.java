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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

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
			if (iblockstate.getBlock().getLightOpacity(iblockstate, world, blockpos) > 1 && iblockstate.getBlock() != Blocks.GLASS
					&& !iblockstate.getMaterial().isLiquid()) { return false; }
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
		return te.getWorld().getTileEntity(te.getPos()) != te ? false : player.getDistanceSq((double) te.getPos().getX()
				+ 0.5D, (double) te.getPos().getY() + 0.5D, (double) te.getPos().getZ() + 0.5D) <= 65.0D;
	}
	
	public static Rotation rotationInverse (Rotation rotation) {
		if (rotation == Rotation.CLOCKWISE_90) return Rotation.COUNTERCLOCKWISE_90;
		if (rotation == Rotation.COUNTERCLOCKWISE_90) return Rotation.CLOCKWISE_90;
		return rotation;
	}
	
	public static IBlockState getUnwrappedState (IBlockState state) {
		while (state.getBlock() instanceof IWrappedBlock)
			state = ((IWrappedBlock) state.getBlock()).unwrap(state);
		return state;
	}
	
	public static IBlockState getUnwrappedWorldState (IBlockAccess world, BlockPos pos) {
		return getUnwrappedState(world.getBlockState(pos));
	}
}
