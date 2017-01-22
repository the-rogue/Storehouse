/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.client.gui.element.ElementBase;

public class GuiUtils
{
	public static void bindTexture(ResourceLocation texture){
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
	}
	public static void bindTexture(GuiScreen gui, ResourceLocation texture){
		gui.mc.getTextureManager().bindTexture(texture);
	}
	public static void bindTexture(ElementBase element, String texture){
		element.gui.mc.getTextureManager().bindTexture(new ResourceLocation(texture));
	}
	public static void bindTexture(ElementBase element, ResourceLocation texture){
		element.gui.mc.getTextureManager().bindTexture(texture);
	}
	public static void bindTexture(ElementBase element, TextureAtlasSprite texture){
		ResourceLocation location = new ResourceLocation(texture.getIconName());
		element.gui.mc.getTextureManager().bindTexture(new ResourceLocation(location.getResourceDomain(), "textures/" + location.getResourcePath() + ".png"));
	}
}
