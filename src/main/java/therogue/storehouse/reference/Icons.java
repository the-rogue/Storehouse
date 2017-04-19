/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.reference;

import net.minecraft.util.ResourceLocation;

public enum Icons
{
	EnergyIndicator ("textures/gui/icons/EnergyIndicator.png"),
	SolarGenOn ("textures/gui/icons/SolarGenOn.png"),
	EnergyBar ("textures/gui/icons/EnergyBar.png");
	
	private String location;
	
	private Icons (String location) {
		this.location = IDs.RESOURCENAMEPREFIX + location;
	}
	
	public ResourceLocation getLocation () {
		return new ResourceLocation(location);
	}
}
