/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.gui.multisystem;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.element.ElementIcon;
import therogue.storehouse.reference.General;
import therogue.storehouse.util.TextureHelper;

public class Category {
	
	private final List<IEntry> entries = new ArrayList<IEntry>();
	private int icon_type;
	private ItemStack itemstack;
	private ResourceLocation icon;
	
	public Category (ItemStack stack) {
		this.itemstack = stack;
		this.icon_type = 1;
	}
	
	public Category (TextureAtlasSprite sprite) {
		this.icon = TextureHelper.convertSpritetoLocation(sprite);
		this.icon_type = 0;
	}
	
	public Category (ResourceLocation icon) {
		this.icon = icon;
		this.icon_type = 0;
	}
	
	public ElementIcon addTitle (GuiBase gui, int x, int y, int width, int height) {
		switch (icon_type) {
			case 0:
				return new ElementIcon(gui, icon, x, y, width, height);
			case 1:
				return new ElementIcon(gui, itemstack, x, y, width, height);
			default:
				return new ElementIcon(gui, new ResourceLocation(General.MOD_ID, "textures/gui/icons/CategoryGeneric.png"), x, y, width, height);
		}
	}
	
	public Runnable addHovering (int mouseX, int mouseY) {
		return null;
	}
	
	public Page buildPage (GuiBase gui, int xStart, int yStart, int pageWidth, int pageHeight) {
		Page page = new Page(1);
		if (this.entries.size() != 0)
		{
			int entriesPerPage = (int) Math.floor(((float) pageHeight) / 8.0), x = xStart, y = yStart;
			for (IEntry e : entries)
			{
				page.addElement(1, e.getTitleBar(x, y, pageWidth, pageHeight));
				if (y - yStart + 8 <= 8 * entriesPerPage)
				{
					y += 8;
				}
			}
		}
		return page;
	}
	
	public void addEntry (IEntry entry) {
		entries.add(entry);
	}
}
