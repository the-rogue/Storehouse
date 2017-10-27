/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.block.machine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import therogue.storehouse.Storehouse;
import therogue.storehouse.container.GuiHandler;
import therogue.storehouse.tile.machine.TileThermalPress;

public class BlockThermalPress extends StorehouseBaseFacingMachine {
	
	public BlockThermalPress (String name) {
		super(name);
	}
	
	@Override
	public TileEntity createNewTileEntity (World worldIn, int meta) {
		TileThermalPress tile = new TileThermalPress();
		tile.setWorld(worldIn);
		return tile;
	}
	
	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote)
		{
			player.openGui(Storehouse.instance, GuiHandler.THERMALPRESS, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
}
