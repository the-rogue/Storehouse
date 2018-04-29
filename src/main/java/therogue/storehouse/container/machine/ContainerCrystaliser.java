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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.container.GuiItemCapability;
import therogue.storehouse.container.InventorySlot;
import therogue.storehouse.inventory.IInventoryItemHandler;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.machine.TileCrystaliser;

public class ContainerCrystaliser extends ContainerBase {
	
	protected final IItemHandler tileCrystaliser;
	
	public ContainerCrystaliser (IInventory playerInv, TileCrystaliser teInv) {
		super(playerInv, teInv);
		this.tileCrystaliser = teInv.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.GUI);
		IInventoryItemHandler inventory = teInv.getCapability(GuiItemCapability.CAP, null, ModuleContext.GUI);
		this.addTESlot(new InventorySlot(inventory, 2, 141, 17));
		this.addTESlot(new InventorySlot(inventory, 3, 141, 53));
		this.addTESlot(new InventorySlot(inventory, 0, 65, 37));
		this.addTESlot(new InventorySlot(inventory, 1, 65, 37));
		tileEntitySlots.get(3).slotNumber = tileEntitySlots.get(2).slotNumber;
		update();
	}
	
	@Override
	public void update () {
		if (tileCrystaliser.getStackInSlot(0).isEmpty())
		{
			if (inventorySlots.contains(tileEntitySlots.get(2))) inventorySlots.remove(tileEntitySlots.get(2));
			if (!inventorySlots.contains(tileEntitySlots.get(3))) inventorySlots.add(tileEntitySlots.get(3));
		}
		else
		{
			if (inventorySlots.contains(tileEntitySlots.get(3))) inventorySlots.remove(tileEntitySlots.get(3));
			if (!inventorySlots.contains(tileEntitySlots.get(2))) inventorySlots.add(tileEntitySlots.get(2));
		}
	}
}
