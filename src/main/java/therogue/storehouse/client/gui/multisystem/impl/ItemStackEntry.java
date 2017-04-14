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

import net.minecraft.item.ItemStack;
import therogue.storehouse.client.gui.element.ElementEntryBar;
import therogue.storehouse.client.gui.multisystem.IBoundedPage;
import therogue.storehouse.client.gui.multisystem.IEntry;

public abstract class ItemStackEntry implements IEntry {
	
	protected final String superCategory;
	protected final String name;
	protected final ItemStack icon;
	
	public ItemStackEntry (String superCategory, String name, ItemStack icon) {
		this.superCategory = superCategory;
		this.name = name;
		this.icon = icon;
	}
	
	@Override
	public ElementEntryBar getTitleBar (IBoundedPage bounds, int x, int y, int width, int height) {
		return new ElementEntryBar(name, this, bounds, icon, x, y, width, height);
	}
	
	@Override
	public String getCategory () {
		return this.superCategory;
	}
}
