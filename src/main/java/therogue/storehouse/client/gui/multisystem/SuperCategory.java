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

public class SuperCategory implements ISuperCategory
{
	private final List<ICategory> categories = new ArrayList<ICategory>();
	private final String name;
	private int icon_type;
	private ItemStack itemstack;
	private ResourceLocation icon;
	private boolean changeOccured = false;
	private IPage[] page = null;
	
	public SuperCategory(String name, ItemStack stack)
	{
		this.name = name;
		this.itemstack = stack;
		this.icon_type = 1;
	}
	
	public SuperCategory(String name, TextureAtlasSprite sprite)
	{
		this.name = name;
		this.icon = TextureHelper.convertSpritetoLocation(sprite);
		this.icon_type = 0;
	}
	
	public SuperCategory(String name, ResourceLocation icon)
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
			int widthHeight = (int) Math.ceil(Math.sqrt(categories.size())), width = pageWidth / widthHeight, height = pageHeight / widthHeight, x = 0, y = 0;
			for (ICategory c : categories)
			{
				page.addToDrawQueue(c.addTitle(gui, x, y, width, height));
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
			this.page = new IPage[] { page };
		}
		return this.page;
	}
	
	@Override
	public void addCategory(ICategory category)
	{
		categories.add(category);
		changeOccured = true;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
}
