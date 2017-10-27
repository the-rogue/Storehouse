/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
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

public enum TierIcons {
	EnergyIndicator (new String[] { "textures/gui/icons/basic/energyindicator.png", "textures/gui/icons/advanced/energyindicator.png", "textures/gui/icons/infused/energyindicator.png", "textures/gui/icons/ender/energyindicator.png", "textures/gui/icons/ultimate/energyindicator.png" }),
	SolarGenOn (new String[] { "textures/gui/icons/basic/solargenon.png", "textures/gui/icons/advanced/solargenon.png", "textures/gui/icons/infused/solargenon.png", "textures/gui/icons/ender/solargenon.png", "textures/gui/icons/ultimate/solargenon.png" }),
	EnergyBar (new String[] { "textures/gui/icons/basic/energybar.png", "textures/gui/icons/advanced/energybar.png", "textures/gui/icons/infused/energybar.png", "textures/gui/icons/ender/energybar.png", "textures/gui/icons/ultimate/energybar.png" }),
	CombustionIndicator (
			new String[] { "textures/gui/icons/basic/combustionindicator.png", "textures/gui/icons/advanced/combustionindicator.png", "textures/gui/icons/infused/combustionindicator.png", "textures/gui/icons/ender/combustionindicator.png", "textures/gui/icons/ultimate/combustionindicator.png" }),
	FluidTank (new String[] { "textures/gui/icons/basic/fluidtank.png", "textures/gui/icons/advanced/fluidtank.png", "textures/gui/icons/infused/fluidtank.png", "textures/gui/icons/ender/fluidtank.png", "textures/gui/icons/ultimate/fluidtank.png" });
	
	private String[] location;
	
	private TierIcons (String[] location) {
		for (int i = 0; i < location.length; i++)
		{
			location[i] = Storehouse.RESOURCENAMEPREFIX + location[i];
		}
		this.location = location;
	}
	
	public ResourceLocation getLocation (int tier) {
		if (tier < 0 || tier >= location.length) return new ResourceLocation(location[0]);
		return new ResourceLocation(location[tier]);
	}
}
