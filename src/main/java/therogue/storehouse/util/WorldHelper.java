/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class WorldHelper {
	
	public static void addTileWithoutBlockToWorld (World world, BlockPos pos, TileEntity tileEntityIn) {
		pos = pos.toImmutable(); // Forge - prevent mutable BlockPos leaks
		if (tileEntityIn != null && !tileEntityIn.isInvalid())
		{
			Chunk chunk = world.getChunkFromBlockCoords(pos);
			if (chunk != null) chunk.addTileEntity(pos, tileEntityIn);
			if (tileEntityIn.getWorld() != chunk.getWorld()) // Forge don't call unless it's changed, could screw up bad mods.
				tileEntityIn.setWorld(chunk.getWorld());
			tileEntityIn.setPos(pos);
			if (chunk.getTileEntityMap().containsKey(pos))
			{
				((TileEntity) chunk.getTileEntityMap().get(pos)).invalidate();
			}
			tileEntityIn.validate();
			chunk.getTileEntityMap().put(pos, tileEntityIn);
			tileEntityIn.onLoad();
			world.addTileEntity(tileEntityIn);
		}
	}
}
