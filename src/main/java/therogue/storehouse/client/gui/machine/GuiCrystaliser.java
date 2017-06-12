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

import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.element.ElementEnergyBar;
import therogue.storehouse.client.gui.element.ElementFluidTank;
import therogue.storehouse.client.gui.element.ProgressHandler;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.reference.TierIcons;
import therogue.storehouse.tile.machine.TileCrystaliser;

public class GuiCrystaliser extends GuiBase {
	
	public GuiCrystaliser (ContainerBase inventory, TileCrystaliser linked) {
		super(linked, inventory);
		elements.add(new ProgressHandler(this, linked, 2, 3, new ElementEnergyBar(8, 8)));
		elements.add(new ElementFluidTank(this, TierIcons.FluidTank.getLocation(linked.getField(1)), 105, 12, linked.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null), linked));
	}
}
