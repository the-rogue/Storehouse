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
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.container.GuiItemCapability;
import therogue.storehouse.container.InventorySlot;
import therogue.storehouse.inventory.IInventoryItemHandler;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.machine.TilePotionBrewer;

public class ContainerPotionBrewer extends ContainerBase {
	
	public ContainerPotionBrewer (IInventory playerInv, TilePotionBrewer teInv) {
		super(198, 186, playerInv, teInv);
		IInventoryItemHandler inventory = teInv.getCapability(GuiItemCapability.CAP, null, ModuleContext.GUI);
		this.addTESlot(new InventorySlot(inventory, 6, 28, 19));
		this.addTESlot(new InventorySlot(inventory, 7, 28, 37));
		this.addTESlot(new InventorySlot(inventory, 8, 28, 55));
		this.addTESlot(new InventorySlot(inventory, 9, 46, 19));
		this.addTESlot(new InventorySlot(inventory, 10, 46, 37));
		this.addTESlot(new InventorySlot(inventory, 11, 46, 55));
		this.addTESlot(new InventorySlot(inventory, 12, 64, 19));
		this.addTESlot(new InventorySlot(inventory, 13, 64, 37));
		this.addTESlot(new InventorySlot(inventory, 14, 64, 55));
		this.addTESlot(new InventorySlot(inventory, 15, 82, 19));
		this.addTESlot(new InventorySlot(inventory, 16, 82, 37));
		this.addTESlot(new InventorySlot(inventory, 17, 82, 55));
		this.addTESlot(new InventorySlot(inventory, 3, 118, 19));
		this.addTESlot(new InventorySlot(inventory, 4, 118, 37));
		this.addTESlot(new InventorySlot(inventory, 5, 118, 55));
		this.addTESlot(new InventorySlot(inventory, 0, 154, 19));
		this.addTESlot(new InventorySlot(inventory, 1, 154, 37));
		this.addTESlot(new InventorySlot(inventory, 2, 154, 55));
	}
}
