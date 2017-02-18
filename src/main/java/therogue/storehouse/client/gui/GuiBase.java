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
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.client.gui.element.ElementBase;
import therogue.storehouse.util.GuiUtils;


public class GuiBase extends GuiContainer
{
	protected ResourceLocation texture;
	private ArrayList<ElementBase> elements = new ArrayList<ElementBase>();

	public GuiBase(ResourceLocation texture, Container inventorySlotsIn)
	{
		super(inventorySlotsIn);

		this.texture = texture;

		this.xSize = 176;
		this.ySize = 166;
	}

	public ResourceLocation getTexture()
	{
		return texture;
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
		int x = mouseX - this.guiLeft;
		int y = mouseY - this.guiTop;
		for (ElementBase element : elements)
		{
			if (element.isVisible()) element.drawElementForegroundLayer(x, y);
		}
		for (ElementBase element : elements)
		{
			if (element.isVisible()) element.drawTopLayer(x, y);
		}
	}
    /**
     * Draws a texture rectangle using the texture currently bound to the TextureManager
     */
    public void drawTexturedModalRect(int x, int y, float minU, float minV, float maxU, float maxV, int width, int height)
    {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos((double)(x + 0), (double)(y + height), (double)this.zLevel).tex((double)minU, (double)maxV).endVertex();
        vertexbuffer.pos((double)(x + width), (double)(y + height), (double)this.zLevel).tex((double)maxU, (double)maxV).endVertex();
        vertexbuffer.pos((double)(x + width), (double)(y + 0), (double)this.zLevel).tex((double)maxU, (double)minV).endVertex();
        vertexbuffer.pos((double)(x + 0), (double)(y + 0), (double)this.zLevel).tex((double)minU, (double)minV).endVertex();
        tessellator.draw();
    }
    
    public void drawTintedTexturedModalRect(int x, int y, float minU, float minV, float maxU, float maxV, int width, int height, int red, int green, int blue, int alpha)
    {
    	GlStateManager.enableBlend();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        vertexbuffer.pos((double)(x + 0), (double)(y + height), (double)this.zLevel).tex((double)minU, (double)maxV).color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos((double)(x + width), (double)(y + height), (double)this.zLevel).tex((double)maxU, (double)maxV).color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos((double)(x + width), (double)(y + 0), (double)this.zLevel).tex((double)maxU, (double)minV).color(red, green, blue, alpha).endVertex();
        vertexbuffer.pos((double)(x + 0), (double)(y + 0), (double)this.zLevel).tex((double)minU, (double)minV).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
    }
    
    /**
     * Draws a texture rectangle using the texture currently bound to the TextureManager
     */
    public void drawTexturedModalRect(int x, int y, int width, int height)
    {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos((double)(x + 0), (double)(y + height), 0.0D).tex(0.0D, 1.0D).endVertex();
        vertexbuffer.pos((double)(x + width), (double)(y + height), 0.0D).tex(1.0D, 1.0D).endVertex();
        vertexbuffer.pos((double)(x + width), (double)(y + 0), 0.0D).tex(1.0D, 0.0D).endVertex();
        vertexbuffer.pos((double)(x + 0), (double)(y + 0), 0.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
    }
    
    public void drawHoveringText(List<String> textLines, int x, int y)
    {
    	super.drawHoveringText(textLines, x, y);
    }
    
    public boolean isPointInGuiRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY)
    {
    	return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
    }
    
    public int getXSize(){
    	return this.xSize;
    }
    
    public int getYSize(){
    	return this.ySize;
    }
    public FontRenderer getFontRenderer(){
		return fontRendererObj;
    	
    }
}
