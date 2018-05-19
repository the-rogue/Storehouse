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
import java.util.ArrayList;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import therogue.storehouse.client.gui.GuiHelper;

public class ElementFluidTank extends ElementBase {
	
	public final BufferedImage icon;
	public final ResourceLocation iconLocation;
	public final int x;
	public final int y;
	public final IFluidHandler tank;
	
	public ElementFluidTank (ResourceLocation location, int x, int y, IFluidHandler tank) {
		this.iconLocation = location;
		this.icon = GuiHelper.getImageAt(iconLocation);
		this.x = x;
		this.y = y;
		this.tank = tank;
	}
	
	@Override
	public void drawElement (int mouseX, int mouseY) {
		if (icon == null) return;
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		FluidStack contents = tank.getTankProperties()[0].getContents();
		if (contents != null)
		{
			int capacity = tank.getTankProperties()[0].getCapacity();
			float percentage = (float) contents.amount / (float) capacity;
			int height = GuiHelper.calculateLength(icon.getHeight() - 2, percentage);
			GuiHelper.drawFluid(contents, x + 1, y + icon.getHeight() - 1 - height, icon.getWidth() - 2, height);
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GuiHelper.bindTexture(this, iconLocation);
		gui.drawTexturedModalRect(x, y, 0.0F, 0.0F, 1.0F, 1.0F, icon.getWidth(), icon.getHeight());
	}
	
	@Override
	public void drawTopLayer (int mouseX, int mouseY) {
		if (icon == null) return;
		if (gui.isPointInGuiRegion(this.x, this.y, this.icon.getWidth(), this.icon.getHeight(), mouseX, mouseY))
		{
			ArrayList<String> textLines = new ArrayList<String>();
			// TODO Insert Commas
			tank.getTankProperties()[0].getContents();
			textLines.add(TextFormatting.GREEN + "" + (tank.getTankProperties()[0].getContents() != null ? tank.getTankProperties()[0].getContents().amount + "" : 0) + " mB /");
			textLines.add(TextFormatting.GREEN + "" + tank.getTankProperties()[0].getCapacity() + " mB");
			GuiHelper.drawHoveringText(gui.getFontRenderer(), textLines, 0, 1, 0, gui.width, gui.height, -1, gui.getGuiLeft(), gui.getGuiTop(), true, false);
			// gui.drawHoveringText(textLines, gui.getXSize(), this.y);
		}
	}
}
