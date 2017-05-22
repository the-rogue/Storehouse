/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.container.machine;

import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.SlotItemHandler;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.tile.machine.TileThermalPress;

public class ContainerThermalPress extends ContainerBase {
	
	public ContainerThermalPress (IInventory playerInv, TileThermalPress teInv) {
		super(playerInv, teInv);
		// Add Thermal Press's Inventory Slot IDs 36-41
		this.addSlotToContainer(new SlotItemHandler(teInv.getContainerCapability(), 0, 110, 39));
		this.addSlotToContainer(new SlotItemHandler(teInv.getContainerCapability(), 1, 74, 39));
		this.addSlotToContainer(new SlotItemHandler(teInv.getContainerCapability(), 2, 74, 12));
		this.addSlotToContainer(new SlotItemHandler(teInv.getContainerCapability(), 3, 74, 66));
		this.addSlotToContainer(new SlotItemHandler(teInv.getContainerCapability(), 4, 47, 12));
		this.addSlotToContainer(new SlotItemHandler(teInv.getContainerCapability(), 5, 47, 66));
	}
}
