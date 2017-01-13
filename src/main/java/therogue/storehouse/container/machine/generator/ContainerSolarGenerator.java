/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.container.machine.generator;

import net.minecraft.inventory.IInventory;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.inventory.SlotAdv;
import therogue.storehouse.tile.generator.TileSolarGenerator;

public class ContainerSolarGenerator extends ContainerBase
{
/*
 * SLOTS:
 * 
 * Player's Inventory (HotBar) 0 - 8..0 - 8
 * Player's Inventory (Main) 9 - 35...9 - 35
 * TileSolarGenerator 36, 37..........0 , 1
 */
	public ContainerSolarGenerator(IInventory playerInv, TileSolarGenerator teInv)
	{
		super(playerInv, teInv);
		
		// Add Solar Generator's Inventory Slot IDs 36,37
		this.addSlotToContainer(new SlotAdv(teInv, 0, 80, 17));
		this.addSlotToContainer(new SlotAdv(teInv, 1, 80, 53));
	}

}
