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

import java.awt.image.BufferedImage;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.util.TextureHelper;

public abstract class ElementProgressBar implements IProgressBar {
	
	protected ResourceLocation iconLocation;
	public final int x;
	public final int y;
	public final int width;
	public final int height;
	protected int foregroundColour = 0;
	protected int backgroundColour = 0;
	
	public ElementProgressBar (int x, int y, ResourceLocation iconLocation) {
		this.iconLocation = iconLocation;
		BufferedImage icon = TextureHelper.getImageAt(iconLocation);
		this.x = x;
		this.y = y;
		if (icon != null)
		{
			this.width = icon.getWidth() / 2;
			this.height = icon.getHeight();
		}
		else
		{
			this.width = this.height = 0;
		}
	}
	
	public ElementProgressBar (int x, int y, int width, int height, int foregroundColour, int backgroundColour) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.foregroundColour = foregroundColour;
		this.backgroundColour = backgroundColour;
	}
	
	@Override
	public void drawBar (GuiBase gui, int mouseX, int mouseY, float progress) {
		if (iconLocation != null)
		{
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			TextureHelper.bindTexture(gui, iconLocation);
			gui.drawTexturedModalRect(getX(), getY(), getMinU(), getMinV(), getMaxU(), getMaxV(), getWidth(), getHeight());
			TextureHelper.bindTexture(gui, iconLocation);
			gui.drawTexturedModalRect(getX(progress), getY(progress), getMinU(progress), getMinV(progress), getMaxU(progress), getMaxV(progress), getWidth(progress), getHeight(progress));
		}
		else
		{
			Gui.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), backgroundColour);
			Gui.drawRect(getX(progress), getY(progress), getX(progress) + getWidth(progress), getY(progress) + getHeight(progress), foregroundColour);
		}
	}
	
	public float getMinU () {
		return 0.5F;
	}
	
	public float getMinV () {
		return 0.0F;
	}
	
	public float getMaxU () {
		return 1.0F;
	}
	
	public float getMaxV () {
		return 1.0F;
	}
	
	public int getX () {
		return x;
	}
	
	public int getY () {
		return y;
	}
	
	public int getWidth () {
		return width;
	}
	
	public int getHeight () {
		return height;
	}
	
	public float getMinU (float progress) {
		return 0.0F;
	}
	
	public float getMinV (float progress) {
		return 0.0F;
	}
	
	public float getMaxU (float progress) {
		return 0.5F;
	}
	
	public float getMaxV (float progress) {
		return 1.0F;
	}
	
	public int getX (float progress) {
		return x;
	}
	
	public int getY (float progress) {
		return y;
	}
	
	public int getWidth (float progress) {
		return width;
	}
	
	public int getHeight (float progress) {
		return height;
	}
	
	@Override
	public void drawTopLayer (GuiBase gui, int mouseX, int mouseY, int progress, int maxprogress) {
	}
}
