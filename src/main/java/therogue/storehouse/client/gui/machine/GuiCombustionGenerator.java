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

import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.element.ElementEnergyBar;
import therogue.storehouse.client.gui.element.ElementVerticalProgressBar;
import therogue.storehouse.client.gui.element.ProgressHandler;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.reference.Icons;
import therogue.storehouse.tile.machine.generator.TileCombustionGenerator;

public class GuiCombustionGenerator extends GuiBase {
	
	public GuiCombustionGenerator (ContainerBase inventory, TileCombustionGenerator linked) {
		super(linked, inventory);
		elements.add(new ProgressHandler(this, linked, 7, 8, new ElementVerticalProgressBar(48, 35, Icons.CombustionIndicator.getLocation())));
		elements.add(new ProgressHandler(this, linked, 5, 6, new ElementVerticalProgressBar(51, 17, Icons.EnergyIndicator.getLocation())));
		elements.add(new ProgressHandler(this, linked, 2, 3, new ElementEnergyBar(8, 8)));
	}
}
