/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.container.machine;

import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.tile.machine.TileCrystaliser;

public class ContainerCrystaliser extends ContainerBase {
	
	protected final IItemHandlerModifiable tileCrystaliser;
	
	public ContainerCrystaliser (IInventory playerInv, TileCrystaliser teInv) {
		super(playerInv, teInv);
		this.tileCrystaliser = teInv.getContainerCapability();
		this.addTESlot(new SlotItemHandler(tileCrystaliser, 0, 65, 37));
		this.addTESlot(new SlotItemHandler(tileCrystaliser, 1, 65, 37));
		tileEntitySlots.get(1).slotNumber = tileEntitySlots.get(0).slotNumber;
		update();
	}
	
	@Override
	public void update () {
		if (tileCrystaliser.getStackInSlot(0).isEmpty())
		{
			if (inventorySlots.contains(tileEntitySlots.get(0))) inventorySlots.remove(tileEntitySlots.get(0));
			if (!inventorySlots.contains(tileEntitySlots.get(1))) inventorySlots.add(tileEntitySlots.get(1));
		}
		else
		{
			if (inventorySlots.contains(tileEntitySlots.get(1))) inventorySlots.remove(tileEntitySlots.get(1));
			if (!inventorySlots.contains(tileEntitySlots.get(0))) inventorySlots.add(tileEntitySlots.get(0));
		}
	}
}
