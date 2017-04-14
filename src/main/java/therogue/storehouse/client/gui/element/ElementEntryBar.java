/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.gui.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.client.gui.multisystem.IBoundedPage;
import therogue.storehouse.client.gui.multisystem.IPageProvider;
import therogue.storehouse.util.TextureHelper;

public class ElementEntryBar extends ElementIcon {
	
	public ElementEntryBar (String name, IPageProvider linked, IBoundedPage pageBounds, ItemStack stack, int x, int y, int width, int height) {
		super(name, linked, pageBounds, stack, x, y, width, height);
	}
	
	public ElementEntryBar (String name, IPageProvider linked, IBoundedPage pageBounds, TextureAtlasSprite sprite, int x, int y, int width, int height) {
		super(name, linked, pageBounds, sprite, x, y, width, height);
	}
	
	public ElementEntryBar (String name, IPageProvider linked, IBoundedPage pageBounds, ResourceLocation icon, int x, int y, int width, int height) {
		super(name, linked, pageBounds, icon, x, y, width, height);
	}
	
	@Override
	public void drawElement (int mouseX, int mouseY) {
		switch (icon_type) {
			case 0:
				TextureHelper.bindTexture(icon);
				gui.drawTexturedModalRect(x, y, height, height);
			case 1:
				// float widthScale = ((float) width) / 16.0F;
				float heightScale = ((float) height) / 16.0F;
				GlStateManager.pushMatrix();
				GlStateManager.scale(heightScale, heightScale, 1.0F);
				RenderHelper.enableGUIStandardItemLighting();
				Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(itemstack, (int) Math.floor(x / heightScale), (int) Math.floor(y / heightScale));
				RenderHelper.disableStandardItemLighting();
				GlStateManager.popMatrix();
				gui.getFontRenderer().drawString(" " + name, x + height, y, 0);
			default:
		}
	}
}
