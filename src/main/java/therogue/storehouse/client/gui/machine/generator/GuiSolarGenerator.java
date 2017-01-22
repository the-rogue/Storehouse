/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.gui.machine.generator;

import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.element.ElementActiveIcon;
import therogue.storehouse.client.gui.element.ElementChargingBar;
import therogue.storehouse.client.render.icons.StorehouseIcons;
import therogue.storehouse.tile.generator.TileSolarGenerator;

public class GuiSolarGenerator extends GuiBase
{
	public static final ResourceLocation TEXTURE = new ResourceLocation("storehouse", "textures/gui/solar_generator_basic.png");

	public GuiSolarGenerator(Container inventorySlotsIn, TileSolarGenerator inventory)
	{
		super(TEXTURE, inventorySlotsIn, inventory);
		addElement(new ElementActiveIcon(this, 90, 23, StorehouseIcons.SolarGenOn, inventory, 1));
		addElement(new ElementChargingBar(this, 33, 35, StorehouseIcons.EnergyBar, inventory, 2, 3));
	}

}
