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

import java.util.ArrayList;
import java.util.HashMap;

import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.init.ModItems;
import therogue.storehouse.item.IStorehouseBaseItem;
import therogue.storehouse.type.CategoryEnum;

public class SystemManager {
	
	public static final HashMap<String, Category> categories = new HashMap<String, Category>();
	public static final ArrayList<IEntry> entries = new ArrayList<IEntry>();
	
	public static void getEntries () {
		for (IStorehouseBaseBlock block : ModBlocks.blocklist)
		{
			if (block instanceof IInfoSupplier)
			{
				entries.add(((IInfoSupplier) block).getEntry());
			}
		}
		for (IStorehouseBaseItem item : ModItems.itemlist)
		{
			if (item instanceof IInfoSupplier)
			{
				entries.add(((IInfoSupplier) item).getEntry());
			}
		}
	}
	
	public static void build () {
		for (IEntry e : entries)
		{
			if (categories.get(e.getCategory()) == null)
			{
				categories.get(CategoryEnum.OTHER.category.name).addEntry(e);
			}
			else
			{
				categories.get(e.getCategory()).addEntry(e);
			}
		}
	}
}
