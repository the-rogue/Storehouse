/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.tile.machine;

import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.Sets;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import therogue.storehouse.client.gui.GuiHelper.XYCoords;
import therogue.storehouse.container.machine.ContainerThermalPress;
import therogue.storehouse.crafting.ICrafter;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.crafting.inventory.IRecipeInventory;
import therogue.storehouse.crafting.inventory.RangedItemInventory;
import therogue.storehouse.crafting.wrapper.ItemStackWrapper;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.tile.ClientButton;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.StorehouseBaseMachine;

public class TileThermalPress extends StorehouseBaseMachine implements ICrafter {
	
	public static final int RFPerTick = 40;
	private ClientButton<Mode> mode = new ClientButton<Mode>("TPMode", Mode.class, Mode.PRESS);
	private MachineCraftingHandler<TileThermalPress>.CraftingManager theCrafter = MachineCraftingHandler.getHandler(TileThermalPress.class).newCrafter(this);
	
	public TileThermalPress () {
		super(ModBlocks.thermal_press);
		mode.setOnUpdate( () -> theCrafter.checkRecipes());
		modules.add(mode);
		modules.add(theCrafter);
		this.setInventory(new InventoryManager(this, 6, new Integer[] { 1, 2, 3, 4, 5 }, new Integer[] { 0 }) {
			
			protected boolean isItemValidForSlotChecks (int index, ItemStack stack) {
				return theCrafter.checkItemValidForSlot(index - 1, new ItemStackWrapper(stack));
			}
		});
		energyStorage.setRFPerTick(40);
	}
	
	// -------------------------ICrafter Methods-----------------------------------
	@Override
	public Set<Integer> getOrderMattersSlots () {
		return mode.getMode().orderMattersSlots;
	}
	
	@Override
	public IRecipeInventory getCraftingInventory () {
		return new RangedItemInventory(this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL), 1, mode.getMode().craftingSlots
				+ 1);
	}
	
	@Override
	public IRecipeInventory getOutputInventory () {
		return new RangedItemInventory(this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL), 0, 1);
	}
	
	@Override
	public boolean isRunning () {
		return energyStorage.hasSufficientRF();
	}
	
	@Override
	public void doRunTick () {
		energyStorage.runTick();
	}
	
	// ------------------Thermal Press Mode Enum-------------------------------------------------------
	public static enum Mode {
		PRESS (3, new XYCoords(65, 10), new XYCoords(65, 64), XYCoords.OFF_SCREEN, XYCoords.OFF_SCREEN),
		JOIN (4, new XYCoords(65, 10), new XYCoords(47, 10), new XYCoords(83, 10), XYCoords.OFF_SCREEN),
		STAMP (2, new XYCoords(65, 10), XYCoords.OFF_SCREEN, XYCoords.OFF_SCREEN, XYCoords.OFF_SCREEN),
		HIGH_PRESSURE (5, new XYCoords(47, 10), new XYCoords(83, 10), new XYCoords(47, 64), new XYCoords(83, 64));
		
		public final int craftingSlots;
		public final Set<Integer> orderMattersSlots = Sets.newHashSet(0);
		public final Predicate<ICrafter> modeTest;
		
		private Mode (int craftingSlots, XYCoords... coords) {
			this.craftingSlots = craftingSlots;
			this.modeTest = machine -> machine instanceof TileThermalPress ? ((TileThermalPress) machine).mode.getMode() == this : false;
			ContainerThermalPress.addPositions(this, coords);
		}
	}
}
