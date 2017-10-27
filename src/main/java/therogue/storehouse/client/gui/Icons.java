/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.gui;

import net.minecraft.util.ResourceLocation;
import therogue.storehouse.Storehouse;

public enum Icons {
	EnergyIndicator ("textures/gui/icons/energyindicator.png"),
	SolarGenOn ("textures/gui/icons/solargenon.png"),
	EnergyBar ("textures/gui/icons/energybar.png"),
	CombustionIndicator ("textures/gui/icons/combustionindicator.png"),
	FluidTank ("textures/gui/icons/fluidtank.png"),
	CrystaliserProgressIcon ("textures/gui/icons/crystaliserprogress.png"),
	Button ("textures/gui/icons/button.png"),
	ProgressUp ("textures/gui/icons/progressbar/up.png"),
	ProgressDown ("textures/gui/icons/progressbar/down.png"),
	ProgressLeft ("textures/gui/icons/progressbar/left.png"),
	ProgressRight ("textures/gui/icons/progressbar/right.png");
	
	private String location;
	
	private Icons (String location) {
		this.location = Storehouse.RESOURCENAMEPREFIX + location;
	}
	
	public ResourceLocation getLocation () {
		return new ResourceLocation(location);
	}
}
