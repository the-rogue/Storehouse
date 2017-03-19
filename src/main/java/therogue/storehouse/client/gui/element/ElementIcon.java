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
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.util.TextureHelper;

public class ElementIcon extends ElementBase {
	
	private int icon_type;
	private ItemStack itemstack;
	private ResourceLocation icon;
	private int x;
	private int y;
	private int width;
	private int height;
	
	public ElementIcon (GuiBase gui, ItemStack stack, int x, int y, int width, int height) {
		super(gui);
		this.itemstack = stack;
		this.icon_type = 1;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public ElementIcon (GuiBase gui, TextureAtlasSprite sprite, int x, int y, int width, int height) {
		super(gui);
		this.icon = TextureHelper.convertSpritetoLocation(sprite);
		this.icon_type = 0;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public ElementIcon (GuiBase gui, ResourceLocation icon, int x, int y, int width, int height) {
		super(gui);
		this.icon = icon;
		this.icon_type = 0;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void drawElement (int mouseX, int mouseY) {
		switch (icon_type) {
			case 0:
				TextureHelper.bindTexture(icon);
				gui.drawTexturedModalRect(x, y, width, height);
			case 1:
				float widthScale = ((float) width) / 16.0F;
				float heightScale = ((float) height) / 16.0F;
				GlStateManager.pushMatrix();
				GlStateManager.scale(widthScale, heightScale, 1.0F);
				RenderHelper.enableGUIStandardItemLighting();
				Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(itemstack, (int) Math.floor(x / widthScale), (int) Math.floor(y / heightScale));
				RenderHelper.disableStandardItemLighting();
				GlStateManager.popMatrix();
			default:
		}
	}
}
