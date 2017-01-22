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

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.IInventory;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.render.icons.Icon;
import therogue.storehouse.util.GuiUtils;

public abstract class ElementProgressBar extends ElementBase
{
	public final TextureAtlasSprite icon;
	public final int x;
	public final int y;
	public final IInventory stateChanger;
	public final int progressField;
	public final int maxProgressField;

	public ElementProgressBar(GuiBase gui, int x, int y, Icon icon, IInventory stateChanger, int progressField, int maxProgressField)
	{
		super(gui);
		this.icon = icon.getIcon();
		this.x = x;
		this.y = y;
		this.stateChanger = stateChanger;
		this.progressField = progressField;
		this.maxProgressField = maxProgressField;
	}
	
	@Override
	public void drawElementForegroundLayer(int mouseX, int mouseY)
	{
		float progress = (float)stateChanger.getField(progressField) / (float)stateChanger.getField(maxProgressField);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GuiUtils.bindTexture(this, TextureMap.LOCATION_BLOCKS_TEXTURE);
		gui.drawTexturedModalRect(x + icon.getIconWidth() - getWidth(progress), y + icon.getIconHeight() - getHeight(progress), getMinU(progress), getMinV(progress), getMaxU(progress), getMaxV(progress), getWidth(progress), getHeight(progress));
	}
	
	public abstract float getMinU(float progress);
	public abstract float getMinV(float progress);
	public abstract float getMaxU(float progress);
	public abstract float getMaxV(float progress);
	public abstract int getWidth(float progress);
	public abstract int getHeight(float progress);
	
	@Override
	public void drawElementBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		
	}
}
