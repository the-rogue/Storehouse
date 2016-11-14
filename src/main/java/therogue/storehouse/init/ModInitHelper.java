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

import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.block.StorehouseBaseBlock;
import therogue.storehouse.item.IStorehouseBaseItem;
import therogue.storehouse.item.StorehouseBaseItem;
import therogue.storehouse.util.loghelper;


public class ModInitHelper
{
	public static void registerblock(IStorehouseBaseBlock block)
	{
		switch (block.getType())
		{
		case StorehouseBaseBlock:
			registerblock((StorehouseBaseBlock) block);
			break;
		default:
			loghelper.log("warn", "Failed to register block, doesnt have a Block Type" + block.toString());
			break;
		}
	}

	private static void registerblock(StorehouseBaseBlock block)
	{
		GameRegistry.register(block);
		GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}

	public static void registeritem(IStorehouseBaseItem item)
	{
		switch (item.getType())
		{
		case StorehouseBaseItem:
			registeritem((StorehouseBaseItem) item);
			break;
		default:
			loghelper.log("warn", "Failed to register Item, doesnt have an Item Type" + item.toString());
			break;

		}
	}

	private static void registeritem(StorehouseBaseItem item)
	{
		GameRegistry.register(item);
	}
}
