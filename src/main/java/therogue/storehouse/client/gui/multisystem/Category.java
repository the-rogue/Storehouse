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
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.util.TextureHelper;

public class Category implements ICategory
{
	private final List<IEntry> entries = new ArrayList<IEntry>();
	private final String name;
	private int icon_type;
	private ItemStack itemstack;
	private ResourceLocation icon;
	
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
	public void renderIcon(GuiBase gui, int x, int y, int width, int height)
	{
		switch (icon_type)
		{
			case 0:
				TextureHelper.bindTexture(icon);
				gui.drawTexturedModalRect(x, y, width, height);
			case 1:
				Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(itemstack, x, y);
		}
	}
	
	@Override
	public void renderText(GuiBase gui, int x, int y, int maxWidth)
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void addEntry(IEntry entry)
	{
		entries.add(entry);
	}
	
	@Override
	public String getName()
	{
		return name;
	}
}
