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

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.tile.MachineTier;
import therogue.storehouse.util.GuiHelper;
import therogue.storehouse.util.TextureHelper;

public abstract class ElementProgressBar extends ElementBase {
	
	public final BufferedImage icon;
	public final ResourceLocation iconLocation;
	public final int x;
	public final int y;
	public final IInventory stateChanger;
	public final int progressField;
	public final int maxProgressField;
	
	public ElementProgressBar (GuiBase gui, int x, int y, ResourceLocation iconLocation, IInventory stateChanger, int progressField, int maxProgressField) {
		super(gui);
		this.iconLocation = iconLocation;
		this.icon = TextureHelper.getImageAt(iconLocation);
		this.x = x;
		this.y = y;
		this.stateChanger = stateChanger;
		this.progressField = progressField;
		this.maxProgressField = maxProgressField;
	}
	
	@Override
	public void drawElement (int mouseX, int mouseY) {
		if (icon == null) return;
		float progress = (float) stateChanger.getField(progressField) / (stateChanger.getField(maxProgressField) != 0 ? (float) stateChanger.getField(maxProgressField) : 1);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		TextureHelper.bindTexture(this, iconLocation);
		gui.drawTintedTexturedModalRect(x, y, 0.5F, 0.0F, 1.0F, 1.0F, icon.getWidth() / 2, icon.getHeight(), GuiHelper.getColor(MachineTier.values()[stateChanger.getField(1)]));
		TextureHelper.bindTexture(this, iconLocation);
		gui.drawTexturedModalRect(x + (icon.getWidth() - getWidth(progress)) / 2, y + icon.getHeight() - getHeight(progress), getMinU(progress) / 2, getMinV(progress), getMaxU(progress) / 2, getMaxV(progress), getWidth(progress) / 2, getHeight(progress));
	}
	
	public abstract float getMinU (float progress);
	
	public abstract float getMinV (float progress);
	
	public abstract float getMaxU (float progress);
	
	public abstract float getMaxV (float progress);
	
	public abstract int getWidth (float progress);
	
	public abstract int getHeight (float progress);
}
