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

public class DoubleInventory implements IRecipeInventory {
	
	private final IRecipeInventory inventory1;
	private final IRecipeInventory inventory2;
	private final int inventory2Slot1;
	
	public DoubleInventory (IRecipeInventory inventory1, IRecipeInventory inventory2) {
		this.inventory1 = inventory1;
		this.inventory2 = inventory2;
		this.inventory2Slot1 = inventory1.getSize();
	}
	
	@Override
	public IRecipeWrapper getComponent (int slot) {
		if (slot < inventory2Slot1) return inventory1.getComponent(slot);
		else return inventory2.getComponent(slot - inventory2Slot1);
	}
	
	@Override
	public void insertComponent (int slot, IRecipeWrapper component, boolean simulate) {
		if (slot < inventory2Slot1) inventory1.insertComponent(slot, component, simulate);
		else inventory2.insertComponent(slot - inventory2Slot1, component, simulate);
	}
	
	@Override
	public IRecipeWrapper extractComponent (int slot, int amount, boolean simulate) {
		if (slot < inventory2Slot1) return inventory1.extractComponent(slot, amount, simulate);
		return inventory2.extractComponent(slot - inventory2Slot1, amount, simulate);
	}
	
	@Override
	public int getComponentSlotLimit (int slot) {
		if (slot < inventory2Slot1) return inventory1.getComponentSlotLimit(slot);
		else return inventory2.getComponentSlotLimit(slot - inventory2Slot1);
	}
	
	@Override
	public int getSize () {
		return inventory1.getSize() + inventory2.getSize();
	}
}
