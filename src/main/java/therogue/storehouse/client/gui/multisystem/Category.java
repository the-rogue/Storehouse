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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.util.TextureHelper;

public class Category implements ICategory
{
	private final List<IGuiItem> entries = new ArrayList<IGuiItem>();
	private final String name;
	private int icon_type;
	private ItemStack itemstack;
	private ResourceLocation icon;
	private boolean changeOccured = false;
	private IPage[] page = null;
	private ICategory superCategory = null;
	
	public Category(String name, ItemStack stack)
	{
		this.name = name;
		this.itemstack = stack;
		this.icon_type = 1;
	}
	
	public Category(String name, TextureAtlasSprite sprite)
	{
		this.name = name;
		this.icon = TextureHelper.convertSpritetoLocation(sprite);
		this.icon_type = 0;
	}
	
	public Category(String name, ResourceLocation icon)
	{
		this.name = name;
		this.icon = icon;
		this.icon_type = 0;
	}
	
	@Override
	public Runnable addTitle(GuiBase gui, int x, int y, int width, int height)
	{
		int length = width < height ? width : height;
		switch (icon_type)
		{
			case 0:
				return new Runnable() {
					@Override
					public void run()
					{
						TextureHelper.bindTexture(icon);
						gui.drawTexturedModalRect(x, y, length, length);
					}
				};
			case 1:
				return new Runnable() {
					@Override
					public void run()
					{
						GlStateManager.pushMatrix();
						GlStateManager.scale(length / 16.0F, length / 16.0F, 1.0F);
						Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(itemstack, x, y);
						GlStateManager.popMatrix();
					}
				};
			default:
				return new Runnable() {
					@Override
					public void run()
					{
					}
				};
		}
	}
	
	@Override
	public IPage[] buildPage(GuiBase gui, int pageWidth, int pageHeight)
	{
		if (this.page == null || changeOccured == true)
		{
			IPage page = new Page();
			if (this.entries.size() != 0) {			
				int widthHeight = (int) Math.ceil(Math.sqrt(entries.size())), width = pageWidth / widthHeight, height = pageHeight / widthHeight, x = 0, y = 0;
				for (IGuiItem e : entries)
				{
					page.addToDrawQueue(e.addTitle(gui, x, y, width, height));
					if (x == width * widthHeight)
					{
						x = 0;
						y += height;
					}
					else
					{
						x += width;
					}
				}
			}
			this.page = new IPage[] { page };
		}
		return this.page;
	}
	
	@Override
	public void addEntry(IGuiItem entry)
	{
		entries.add(entry);
		entry.setSuperCategory(this);
		if (entry instanceof ICategory) {
			SystemManager.categories.put(entry.getName(), (ICategory)entry);
		} else {
			SystemManager.entries.put(entry.getName(), entry);
		}
		changeOccured = true;
	}
	
	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void setSuperCategory(ICategory category)
	{
		this.superCategory = category;
	}
	
	@Override
	public ICategory getCategory()
	{
		return superCategory;
	}
}
