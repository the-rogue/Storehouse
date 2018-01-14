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
import therogue.storehouse.container.SlotItemHandlerFix;
import therogue.storehouse.tile.machine.TileAlloyFurnace;

public class ContainerAlloyFurnace extends ContainerBase {
	
	public ContainerAlloyFurnace (IInventory playerInv, TileAlloyFurnace teInv) {
		super(playerInv, teInv);
		this.addTESlot(new SlotItemHandlerFix(teInv, 2, 60, 37));
		this.addTESlot(new SlotItemHandlerFix(teInv, 3, 60, 57));
		this.addTESlot(new SlotItemHandlerFix(teInv, 0, 120, 37));
		this.addTESlot(new SlotItemHandlerFix(teInv, 1, 60, 17));
	}
}
