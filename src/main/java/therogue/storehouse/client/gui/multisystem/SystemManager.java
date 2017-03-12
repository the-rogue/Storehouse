/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.gui.multisystem;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.init.ModItems;
import therogue.storehouse.item.IStorehouseBaseItem;

public class SystemManager
{
	private static final Category BOOK = new Category("book", new ItemStack(ModItems.storehouse_guide, 1, 0));
	public static final HashMap<String, ICategory> categories = new HashMap<String, ICategory>();
	public static final HashMap<String, IGuiItem> entries = new HashMap<String, IGuiItem>();
	
	public static void build()
	{
		for (IStorehouseBaseBlock block : ModBlocks.blocklist)
		{
			if (block instanceof IInfoSupplier) {
				SystemManager.categories.get(((IInfoSupplier)block).getEntry().getCategory()).addEntry(((IInfoSupplier)block).getEntry());
			}
		}
		for (IStorehouseBaseItem item : ModItems.itemlist)
		{
			if (item instanceof IInfoSupplier) {
				SystemManager.categories.get(((IInfoSupplier)item).getEntry().getCategory()).addEntry(((IInfoSupplier)item).getEntry());
			}
		}
	}

	/**
	 * @return the book
	 */
	public static ICategory getBook()
	{
		return BOOK;
	}
}
