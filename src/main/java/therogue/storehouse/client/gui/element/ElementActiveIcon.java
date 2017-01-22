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

public class ElementActiveIcon extends ElementBase
{
	public final TextureAtlasSprite icon;
	public final int x;
	public final int y;
	public final IInventory stateChanger;
	public final int activeField;

	public ElementActiveIcon(GuiBase gui, int x, int y, Icon icon, IInventory stateChanger, int activeField)
	{
		super(gui);
		this.icon = icon.getIcon();
		this.x = x;
		this.y = y;
		this.stateChanger = stateChanger;
		this.activeField = activeField;
	}

	@Override
	public void drawElementForegroundLayer(int mouseX, int mouseY)
	{
		if (stateChanger.getField(activeField) == 1) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GuiUtils.bindTexture(this, TextureMap.LOCATION_BLOCKS_TEXTURE);
			gui.drawTexturedModalRect(x, y, icon, icon.getIconWidth(), icon.getIconHeight());
		}
	}

	@Override
	public void drawElementBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
	}

}
