/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.item;

import net.minecraft.item.ItemSlab;
import therogue.storehouse.block.Decorative.StorehouseBaseSlab;
import therogue.storehouse.reference.General;
import therogue.storehouse.util.loghelper;


public class ItemStorehouseBaseSlab extends ItemSlab
{
	/**
	 * Creates a new ItemSlab with the appropriate parameters for my Mod
	 */
	public ItemStorehouseBaseSlab(StorehouseBaseSlab.Half singleSlab, StorehouseBaseSlab.Double doubleSlab)
	{
		super(singleSlab, singleSlab, doubleSlab);
		loghelper.log("trace", "Creating new ItemStorehouseBaseSlab: " + singleSlab.getName());
		this.setRegistryName(General.MOD_ID, singleSlab.getName());
	}
}
