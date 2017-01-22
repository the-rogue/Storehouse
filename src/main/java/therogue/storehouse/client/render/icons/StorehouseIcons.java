/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.render.icons;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.reference.IDs;
import therogue.storehouse.reference.Texture;
import therogue.storehouse.util.loghelper;

public enum StorehouseIcons implements Icon
{
	EnergyBar("gui/icons/EnergyBar"),
	SolarGenOn("gui/icons/SolarGenOn");
	
	private String location;
	
	private StorehouseIcons (String location) {
		this.location = IDs.RESOURCENAMEPREFIX + location;
	}
	
	@Override
	public TextureAtlasSprite getIcon(){
		return Texture.getTexture(name());
	}
	
	@Override
	public ResourceLocation getLocation() {
		return new ResourceLocation(getIcon().getIconName());
	}
	
	public static void registerIcons(TextureMap map){
		loghelper.log("info", "Registering Icons...");
		for (StorehouseIcons icon : values()) {
			Texture.register(icon.name(), icon.location, map);
		}
	}
}
