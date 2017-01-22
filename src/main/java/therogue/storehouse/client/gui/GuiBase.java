/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.client.gui.element.ElementBase;
import therogue.storehouse.util.GuiUtils;


public class GuiBase extends GuiContainer
{
	protected ResourceLocation texture;
	private ArrayList<ElementBase> elements = new ArrayList<ElementBase>();
	private IInventory inventory;

	public GuiBase(ResourceLocation texture, Container inventorySlotsIn, IInventory inventory)
	{
		super(inventorySlotsIn);

		this.texture = texture;
		this.inventory = inventory;

		this.xSize = 176;
		this.ySize = 166;
	}

	public ResourceLocation getTexture()
	{
		return texture;
	}

	public IInventory getInventory()
	{
		return inventory;
	}

	protected ElementBase addElement(ElementBase element)
	{
		elements.add(element);
		return element;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GuiUtils.bindTexture(this, texture);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		for (ElementBase element : elements)
		{
			if (element.isVisible()) element.drawElementBackgroundLayer(partialTicks, mouseX, mouseY);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		for (ElementBase element : elements)
		{
			if (element.isVisible()) element.drawElementForegroundLayer(mouseX, mouseY);
		}
	}
    /**
     * Draws a texture rectangle using the texture currently bound to the TextureManager
     */
    public void drawTexturedModalRect(int xCoord, int yCoord, float minU, float minV, float maxU, float maxV, int widthIn, int heightIn)
    {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos((double)(xCoord + 0), (double)(yCoord + heightIn), (double)this.zLevel).tex((double)minU, (double)maxV).endVertex();
        vertexbuffer.pos((double)(xCoord + widthIn), (double)(yCoord + heightIn), (double)this.zLevel).tex((double)maxU, (double)maxV).endVertex();
        vertexbuffer.pos((double)(xCoord + widthIn), (double)(yCoord + 0), (double)this.zLevel).tex((double)maxU, (double)minV).endVertex();
        vertexbuffer.pos((double)(xCoord + 0), (double)(yCoord + 0), (double)this.zLevel).tex((double)minU, (double)minV).endVertex();
        tessellator.draw();
    }
}
