/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.init;

import java.util.ArrayList;

import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.block.Decorative.StorehouseBaseDecorativeBlock;
import therogue.storehouse.block.Decorative.StorehouseBaseSlab;
import therogue.storehouse.block.Decorative.StorehouseBaseStair;


public class ModBlocks
{
	public static ArrayList<IStorehouseBaseBlock> blocklist = new ArrayList<IStorehouseBaseBlock>();

	public static final IStorehouseBaseBlock azurite_dust_block = new StorehouseBaseDecorativeBlock("azurite_dust_block");
	public static final IStorehouseBaseBlock azurite_dust_block_stair = new StorehouseBaseStair(azurite_dust_block);
	public static final IStorehouseBaseBlock azurite_dust_block_half_slab = new StorehouseBaseSlab.Half(azurite_dust_block);
	public static final IStorehouseBaseBlock azurite_dust_block_double_slab = new StorehouseBaseSlab.Double(azurite_dust_block, azurite_dust_block_half_slab);

	static
	{
		blocklist.add(azurite_dust_block);
		blocklist.add(azurite_dust_block_stair);
		blocklist.add(azurite_dust_block_half_slab);
		blocklist.add(azurite_dust_block_double_slab);
	}

	public static void preinit()
	{
		for (IStorehouseBaseBlock block : ModBlocks.blocklist)
		{
			ModInitHelper.registerblock(block);
		}
	}
}
