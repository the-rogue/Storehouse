/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.crafting;

import therogue.storehouse.crafting.wrapper.IRecipeWrapper;
import therogue.storehouse.tile.ITile;

public interface IRecipeInventory {
	
	public IRecipeWrapper getComponent (int slot);
	
	public IRecipeWrapper extractComponent (int slot, int amount, boolean simulate);
	
	public void insertComponent (int slot, IRecipeWrapper component, boolean simulate);
	
	public int getComponentSlotLimit (int slot);
	
	public int getSize ();
	
	public interface IRecipeInventoryConverter {
		
		public String getString ();
		
		public IRecipeInventory getFromTile (ITile tile, int[] data);
		
		public default int numOfDataItems () {
			return 0;
		}
	}
}
