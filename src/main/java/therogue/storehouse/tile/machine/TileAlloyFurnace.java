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

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.item.ItemStack;
import therogue.storehouse.crafting.ICrafter;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.crafting.MachineCraftingHandler.CraftingManager;
import therogue.storehouse.crafting.inventory.IRecipeInventory;
import therogue.storehouse.crafting.inventory.RangedItemInventory;
import therogue.storehouse.crafting.wrapper.ItemStackWrapper;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.network.GuiUpdateTEPacket;
import therogue.storehouse.tile.StorehouseBaseMachine;

public class TileAlloyFurnace extends StorehouseBaseMachine implements ICrafter {
	
	public static final int RFPerTick = 80;
	private CraftingManager theCrafter = MachineCraftingHandler.getHandler(this.getClass()).newCrafter(this);
	
	public TileAlloyFurnace () {
		super(ModBlocks.alloy_furnace);
		inventory = new InventoryManager(this, 4, new Integer[] { 1, 2, 3 }, new Integer[] { 0 }) {
			
			@Override
			protected boolean isItemValidForSlotChecks (int index, ItemStack stack) {
				return theCrafter.checkItemValidForSlot(index - 1, new ItemStackWrapper(stack));
			}
		};
	}
	
	// -------------------------ICrafter Methods-----------------------------------
	@Override
	public Set<Integer> getOrderMattersSlots () {
		return Sets.newHashSet();
	}
	
	@Override
	public IRecipeInventory getCraftingInventory () {
		return new RangedItemInventory(getInventory(), 1, 4);
	}
	
	@Override
	public IRecipeInventory getOutputInventory () {
		return new RangedItemInventory(getInventory(), 0, 1);
	}
	
	@Override
	public boolean isRunning () {
		return energyStorage.getEnergyStored() >= RFPerTick;
	}
	
	@Override
	public void doRunTick () {
		if (isRunning())
		{
			energyStorage.modifyEnergyStored(-RFPerTick);
		}
	}
	
	// -------------------------Inventory Methods-----------------------------------
	@Override
	public void onInventoryChange () {
		super.onInventoryChange();
		theCrafter.checkRecipes();
	}
	
	// -------------------------Gui Methods----------------------------------------------------
	@Override
	public int getField (int id) {
		switch (id)
		{
			case 4:
				return theCrafter.totalCraftingTime - theCrafter.craftingTime;
			case 5:
				return theCrafter.totalCraftingTime;
			default:
				return super.getField(id);
		}
	}
	
	@Override
	public void setField (int id, int value) {
		switch (id)
		{
			default:
				super.setField(id, value);
		}
	}
	
	@Override
	public int getFieldCount () {
		return super.getFieldCount() + 2;
	}
	
	// -------------------------Standard TE methods-----------------------------------
	@Override
	public GuiUpdateTEPacket getGUIPacket () {
		GuiUpdateTEPacket packet = super.getGUIPacket();
		packet.getNbt().setInteger("maxCraftingTime", theCrafter.totalCraftingTime);
		packet.getNbt().setInteger("craftingTime", theCrafter.craftingTime);
		return packet;
	}
	
	@Override
	public void processGUIPacket (GuiUpdateTEPacket packet) {
		super.processGUIPacket(packet);
		theCrafter.totalCraftingTime = packet.getNbt().getInteger("maxCraftingTime");
		theCrafter.craftingTime = packet.getNbt().getInteger("craftingTime");
	}
}
