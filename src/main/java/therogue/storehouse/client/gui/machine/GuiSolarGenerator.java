/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
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
import therogue.storehouse.client.gui.TierIcons;
import therogue.storehouse.client.gui.element.ElementActiveIcon;
import therogue.storehouse.client.gui.element.ElementEnergyBar;
import therogue.storehouse.client.gui.element.ElementVerticalProgressBar;
import therogue.storehouse.client.gui.element.ProgressHandler;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.tile.IDataHandler;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.TileData.CapabilityDataHandler;
import therogue.storehouse.tile.machine.TileSolarGenerator;

public class GuiSolarGenerator extends GuiBase {
	
	public GuiSolarGenerator (ContainerBase inventory, TileSolarGenerator linked) {
		super(linked.tier.guiLocation, inventory, linked);
		IEnergyStorage energy = linked.getCapability(CapabilityEnergy.ENERGY, null, ModuleContext.GUI);
		IDataHandler data = linked.getCapability(CapabilityDataHandler.DATAHANDLER, null, ModuleContext.GUI);
		int tier = data.getField(0);
		elements.add(new ElementActiveIcon(this, 90, 23, TierIcons.SolarGenOn.getLocation(tier), () -> data.getField(3)));
		elements.add(new ProgressHandler(this, () -> data.getField(1), () -> data.getField(2), new ElementVerticalProgressBar(33, 35, TierIcons.EnergyIndicator.getLocation(tier))));
		elements.add(new ProgressHandler(this, () -> energy.getEnergyStored(), () -> energy.getMaxEnergyStored(), new ElementEnergyBar(8, 8, TierIcons.EnergyBar.getLocation(tier))));
	}
}
