/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.tile.machine;

import net.minecraft.util.ResourceLocation;
import therogue.storehouse.client.gui.GuiHelper;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.crafting.wrapper.ItemStackWrapper;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.tile.StorehouseBaseMachine;

public class TileAlloyFurnace extends StorehouseBaseMachine {
	
	protected final MachineCraftingHandler<TileAlloyFurnace>.CraftingManager theCrafter;
	public static final ResourceLocation COMBUSTION_INDICATOR_LOCATION = GuiHelper.makeStorehouseLocation("textures/gui/icons/combustionindicator.png");
	
	public TileAlloyFurnace () {
		super(ModBlocks.alloy_furnace);
		this.setInventory(new InventoryManager(this, 4, new Integer[] { 1, 2, 3 }, new Integer[] { 0 }));
		theCrafter = MachineCraftingHandler.getHandler(TileAlloyFurnace.class).newCrafter(this, "ITM 1 4", "ITM 0 1", energyStorage);
		modules.add(theCrafter);
		energyStorage.setRFPerTick(80);
		inventory.setItemValidForSlotChecks( (index, stack) -> theCrafter.checkItemValidForSlot(index - 1, new ItemStackWrapper(stack)));
		containerFactory = (player) -> new ContainerBase(player.inventory, this).setTESlotList(inventory.guiAccess, new int[] { 2, 60, 37, 3, 60, 57, 0, 120, 37, 1, 60, 17 });
		this.setElementString("ENERGYBAR 8 8,  PROGRESS_BAR CRFT_TL CRFT_TT D( UP_PB 90 58 %s )D( J( RBAR 88 44 12 2 )J( RARROW 100 39 ) )", COMBUSTION_INDICATOR_LOCATION);
	}
}
