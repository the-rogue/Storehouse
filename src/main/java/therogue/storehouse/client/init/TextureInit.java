/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.init;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import therogue.storehouse.reference.General;

public class TextureInit {
	
	public static Map<String, TextureAtlasSprite> sprites = new HashMap<String, TextureAtlasSprite>();
	
	public static void init (TextureStitchEvent.Pre event) {
		TextureMap theMap = event.getMap();
		registerSprite(theMap, "storehouse_iron", new ResourceLocation(General.MOD_ID, "blocks/machine/storehouse_iron"));
	}
	
	private static void registerSprite (TextureMap map, String name, ResourceLocation location) {
		sprites.put(name, map.registerSprite(location));
	}
}