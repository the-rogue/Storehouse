/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.container.multiblock;

import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.tile.multiblock.TileCarbonCompressor;

public class ContainerCarbonCompressor extends ContainerBase {
	
	public ContainerCarbonCompressor (IInventory playerInv, TileCarbonCompressor teInv) {
		super(playerInv, teInv);
		IItemHandlerModifiable tileCarbonCompressor = teInv.getContainerCapability();
		this.addTESlot(new SlotItemHandler(tileCarbonCompressor, 0, 120, 37));
		this.addTESlot(new SlotItemHandler(tileCarbonCompressor, 1, 50, 17));
		this.addTESlot(new SlotItemHandler(tileCarbonCompressor, 2, 50, 37));
		this.addTESlot(new SlotItemHandler(tileCarbonCompressor, 3, 50, 57));
		this.addTESlot(new SlotItemHandler(tileCarbonCompressor, 4, 70, 27));
		this.addTESlot(new SlotItemHandler(tileCarbonCompressor, 5, 70, 47));
	}
}
