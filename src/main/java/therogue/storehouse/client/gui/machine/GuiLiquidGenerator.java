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
import therogue.storehouse.client.gui.element.ElementActiveIcon;
import therogue.storehouse.client.gui.element.ElementChargingBar;
import therogue.storehouse.client.gui.element.ElementEnergyBar;
import therogue.storehouse.client.gui.element.ElementFluidTank;
import therogue.storehouse.reference.Icons;
import therogue.storehouse.tile.machine.generator.TileLiquidGenerator;

public class GuiLiquidGenerator extends GuiBase {
	
	public GuiLiquidGenerator (Container inventorySlotsIn, TileLiquidGenerator inventory) {
		super(inventory, inventorySlotsIn);
		elements.add(new ElementActiveIcon(this, 48, 35, Icons.CombustionIndicator.getLocation(), inventory, 2));
		elements.add(new ElementChargingBar(this, 51, 17, Icons.EnergyIndicator.getLocation(), inventory, 3, 4));
		elements.add(new ElementEnergyBar(this, 8, 8, inventory, 5, 6));
		elements.add(new ElementFluidTank(this, 105, 12, inventory, inventory));
	}
}
