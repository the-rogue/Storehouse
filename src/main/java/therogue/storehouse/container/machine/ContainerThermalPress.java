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
import net.minecraft.inventory.Slot;
import therogue.storehouse.client.gui.GuiHelper.XYCoords;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.container.GuiItemCapability;
import therogue.storehouse.container.InventorySlot;
import therogue.storehouse.inventory.IInventoryItemHandler;
import therogue.storehouse.tile.ClientButton.CapabilityButton;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.machine.TileThermalPress;
import therogue.storehouse.tile.machine.TileThermalPress.Mode;

public class ContainerThermalPress extends ContainerBase {
	
	private static final XYCoords[][] slotPositions = new XYCoords[4][];
	private final TileThermalPress teInv;
	
	public ContainerThermalPress (IInventory playerInv, TileThermalPress teInv) {
		super(playerInv, teInv);
		IInventoryItemHandler inventory = teInv.getCapability(GuiItemCapability.CAP, null, ModuleContext.GUI);
		this.teInv = teInv;
		// Add Thermal Press's Inventory Slot IDs 36-41
		this.addTESlot(new InventorySlot(inventory, 0, 120, 37));
		this.addTESlot(new InventorySlot(inventory, 1, 65, 37));
		this.addTESlot(new InventorySlot(inventory, 2, 65, 10));
		this.addTESlot(new InventorySlot(inventory, 3, 65, 64));
		this.addTESlot(new InventorySlot(inventory, 4, Integer.MIN_VALUE, Integer.MIN_VALUE));
		this.addTESlot(new InventorySlot(inventory, 5, Integer.MIN_VALUE, Integer.MIN_VALUE));
		this.detectAndSendChanges();
		update();
	}
	
	// -------------------Methods to move slots around--------------------
	public static void addPositions (Mode mode, XYCoords... coords) {
		slotPositions[mode.ordinal()] = coords;
	}
	
	@Override
	public void update () {
		XYCoords[] positions = slotPositions[teInv.getCapability(CapabilityButton.BUTTON, null, ModuleContext.GUI).getMode().ordinal()];
		for (int i = 0; i < tileEntitySlots.size() - 2; i++)
		{
			Slot s = tileEntitySlots.get(i + 2);
			XYCoords coords = positions[i];
			s.xPos = coords.x;
			s.yPos = coords.y;
			if (s.xPos == Integer.MIN_VALUE && s.yPos == Integer.MIN_VALUE)
			{
				if (this.inventorySlots.contains(s))
				{
					this.inventorySlots.remove(s);
				}
			}
			else
			{
				if (!this.inventorySlots.contains(s))
				{
					this.inventorySlots.add(s);
				}
			}
		}
	}
}
