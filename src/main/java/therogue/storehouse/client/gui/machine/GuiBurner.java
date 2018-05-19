/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.gui.machine;

import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.GuiHelper;
import therogue.storehouse.client.gui.Icons;
import therogue.storehouse.client.gui.element.ElementEnergyBar;
import therogue.storehouse.client.gui.element.ElementHorizontalProgressBar;
import therogue.storehouse.client.gui.element.IProgressBar;
import therogue.storehouse.client.gui.element.JoinProgressBar;
import therogue.storehouse.client.gui.element.ProgressHandler;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.crafting.MachineCraftingHandler.CapabilityCrafter;
import therogue.storehouse.crafting.MachineCraftingHandler.ICraftingManager;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.machine.TileBurner;

public class GuiBurner extends GuiBase {
	
	public GuiBurner (ContainerBase inventory, TileBurner linked) {
		super(NORMAL_TEXTURE, inventory, linked);
		ICraftingManager<?> crafter = linked.getCapability(CapabilityCrafter.CraftingManager, null, ModuleContext.GUI);
		IEnergyStorage energy = linked.getCapability(CapabilityEnergy.ENERGY, null, ModuleContext.GUI);
		elements.add(new ProgressHandler( () -> energy.getEnergyStored(), () -> energy.getMaxEnergyStored(), new ElementEnergyBar(8, 8, Icons.EnergyBar.getLocation())));
		elements.add(new ProgressHandler( () -> crafter.getTimeElapsed(), () -> crafter.getTotalCraftingTime(), createProgressBar()));
		onConstructorFinishTEMP();
	}
	
	private static IProgressBar createProgressBar () {
		IProgressBar pointerright = new ElementHorizontalProgressBar(104, 39, Icons.ProgressRight.getLocation());
		IProgressBar barright = new ElementHorizontalProgressBar(92, 44, 12, 2, GuiHelper.WHITE, GuiHelper.NORMAL_COLOUR);
		return new JoinProgressBar(barright, pointerright);
	}
}
