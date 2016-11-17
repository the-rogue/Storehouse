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

import net.minecraftforge.fml.common.registry.GameRegistry;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.block.StorehouseBaseBlock;
import therogue.storehouse.block.Decorative.StorehouseBaseDecorativeBlock;
import therogue.storehouse.block.Decorative.StorehouseBaseRotatedBlock;
import therogue.storehouse.block.Decorative.StorehouseBaseSlab;
import therogue.storehouse.block.Decorative.StorehouseBaseStair;
import therogue.storehouse.reference.General;
import therogue.storehouse.util.loghelper;


@GameRegistry.ObjectHolder(General.MOD_ID)
public class ModBlocks
{
	/**
	 * Initialises a new array to hold all the blocks
	 */
	public static ArrayList<IStorehouseBaseBlock> blocklist = new ArrayList<IStorehouseBaseBlock>();

	/**
	 * Initialises all the blocks
	 */
	public static final StorehouseBaseBlock azurite_dust_block = new StorehouseBaseDecorativeBlock("azurite_dust_block");
	public static final StorehouseBaseBlock azurite_crystal_block = new StorehouseBaseDecorativeBlock("azurite_crystal_block");
	public static final StorehouseBaseBlock azurite_crystal_block_chiseled = new StorehouseBaseDecorativeBlock("azurite_crystal_block_chiseled");
	public static final StorehouseBaseRotatedBlock azurite_crystal_block_pillar = new StorehouseBaseRotatedBlock(azurite_crystal_block, "pillar");
	public static final StorehouseBaseStair azurite_dust_block_stair = new StorehouseBaseStair(azurite_dust_block);
	public static final StorehouseBaseSlab.Half azurite_dust_block_half_slab = new StorehouseBaseSlab.Half(azurite_dust_block);
	public static final StorehouseBaseSlab.Double azurite_dust_block_double_slab = new StorehouseBaseSlab.Double(azurite_dust_block, azurite_dust_block_half_slab);
	public static final StorehouseBaseStair azurite_crystal_block_stair = new StorehouseBaseStair(azurite_crystal_block);
	public static final StorehouseBaseSlab.Half azurite_crystal_block_half_slab = new StorehouseBaseSlab.Half(azurite_crystal_block);
	public static final StorehouseBaseSlab.Double azurite_crystal_block_double_slab = new StorehouseBaseSlab.Double(azurite_crystal_block, azurite_crystal_block_half_slab);

	/**
	 * Adds all the blocks to the array
	 */
	static
	{
		loghelper.log("debug", "Adding Blocks");
		// Adds Decorative blocks
		blocklist.add(azurite_dust_block);
		blocklist.add(azurite_crystal_block);
		blocklist.add(azurite_crystal_block_chiseled);
		blocklist.add(azurite_crystal_block_pillar);
		// Adds Decorative block varients
		blocklist.add(azurite_dust_block_stair);
		blocklist.add(azurite_dust_block_half_slab);
		blocklist.add(azurite_dust_block_double_slab);
		blocklist.add(azurite_crystal_block_stair);
		blocklist.add(azurite_crystal_block_half_slab);
		blocklist.add(azurite_crystal_block_double_slab);
	}

	/**
	 * Registers all the blocks
	 */
	public static void preinit()
	{
		loghelper.log("debug", "Registering Blocks");
		for (IStorehouseBaseBlock block : blocklist)
		{
			block.registerblock();
		}
	}
}
