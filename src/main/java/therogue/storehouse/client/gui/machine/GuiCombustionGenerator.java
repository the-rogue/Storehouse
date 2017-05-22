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

import net.minecraft.inventory.Container;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.element.ElementChargingBar;
import therogue.storehouse.client.gui.element.ElementEnergyBar;
import therogue.storehouse.reference.Icons;
import therogue.storehouse.tile.machine.generator.TileCombustionGenerator;

public class GuiCombustionGenerator extends GuiBase {
	
	public GuiCombustionGenerator (Container inventorySlotsIn, TileCombustionGenerator inventory) {
		super(inventory, inventorySlotsIn);
		elements.add(new ElementChargingBar(this, 48, 35, Icons.CombustionIndicator.getLocation(), inventory, 7, 8));
		elements.add(new ElementChargingBar(this, 51, 17, Icons.EnergyIndicator.getLocation(), inventory, 5, 6));
		elements.add(new ElementEnergyBar(this, 8, 8, inventory, 2, 3));
	}
}
