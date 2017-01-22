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

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class Texture
{
	private static HashMap<String, String> textures = new HashMap<String, String>();
	private static HashMap<String, TextureMap> maps = new HashMap<String, TextureMap>();
	
	public static TextureAtlasSprite register(String name, String location, TextureMap map) {
		textures.put(name, location);
		maps.put(name, map);
		return map.registerSprite(new ResourceLocation(location));
	}
	
	public static TextureAtlasSprite register(String name, String location){
		TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
		textures.put(name, location);
		maps.put(name, map);
		return map.registerSprite(new ResourceLocation(location));
	}
	
	public static TextureAtlasSprite register(String name, TextureAtlasSprite sprite, TextureMap map) {
		textures.put(name, sprite.getIconName());
		maps.put(name, map);
		return map.registerSprite(new ResourceLocation(sprite.getIconName()));
	}
	
	public static TextureAtlasSprite register(String name, TextureAtlasSprite sprite){
		TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
		textures.put(name, sprite.getIconName());
		maps.put(name, map);
		return map.registerSprite(new ResourceLocation(sprite.getIconName()));
	}
	
	public static TextureAtlasSprite getTexture(String name) {
		return maps.get(name).getAtlasSprite(textures.get(name));
	}
}
