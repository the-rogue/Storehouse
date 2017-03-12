/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.gui.multisystem.impl;

import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.multisystem.ICategory;
import therogue.storehouse.client.gui.multisystem.IGuiItem;

public abstract class Entry implements IGuiItem
{
	protected String name;
	protected ICategory superCategory;
	
	public Entry(String name) {
		this.name = name;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public Runnable addTitle(GuiBase gui, int x, int y, int width, int height)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setSuperCategory(ICategory category)
	{
		this.superCategory = category;
	}
	
	@Override
	public ICategory getCategory()
	{
		return this.superCategory;
	}
}
