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

import therogue.storehouse.item.IStorehouseBaseItem;
import therogue.storehouse.item.StorehouseBaseItem;
import therogue.storehouse.item.CraftingUsed.ItemStorehouseBaseMaterial;


public class ModItems
{
	public static ArrayList<IStorehouseBaseItem> itemlist = new ArrayList<IStorehouseBaseItem>();

	public static final StorehouseBaseItem azurite_dust = new ItemStorehouseBaseMaterial("azurite_dust");

	static
	{
		itemlist.add(azurite_dust);
	}

	public static void init()
	{
		for (IStorehouseBaseItem item : ModItems.itemlist)
		{
			item.registeritem();
		}
	}
}
