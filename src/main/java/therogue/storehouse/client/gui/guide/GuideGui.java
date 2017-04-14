/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.gui.guide;

import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.multisystem.Category;
import therogue.storehouse.client.gui.multisystem.IBoundedPage;
import therogue.storehouse.client.gui.multisystem.SystemManager;
import therogue.storehouse.reference.General;

public class GuideGui extends GuiBase implements IBoundedPage {
	
	public static final ResourceLocation TEXTURE = new ResourceLocation(General.MOD_ID, "textures/gui/StorehouseGuide.png");
	
	public GuideGui (Container inventorySlotsIn) {
		super(TEXTURE, inventorySlotsIn);
		int entriesPerPage = (int) Math.floor(112.0 / 32.0), x = 16, y = 32;
		for (Category c : SystemManager.categories.values())
		{
			homePage.addElement(c.addTitle(this, x, y, 24, 24));
			if (x - 16 + 24 >= 32 * entriesPerPage)
			{
				x = 16;
				y += 32;
			}
			else
			{
				x += 32;
			}
		}
		this.xSize = 146;
		this.ySize = 180;
	}
	
	@Override
	public GuiBase getGui () {
		return this;
	}
	
	@Override
	public int getUsableXStart () {
		return 16;
	}
	
	@Override
	public int getUsableYStart () {
		return 32;
	}
	
	@Override
	public int getUsableWidth () {
		return 120;
	}
	
	@Override
	public int getUsableHeight () {
		return 180;
	}
}
