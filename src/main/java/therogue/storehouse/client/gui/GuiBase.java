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

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import therogue.storehouse.client.gui.element.ElementBase;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.reference.IDs;
import therogue.storehouse.util.RGBAColor;
import therogue.storehouse.util.TextureHelper;

public class GuiBase extends GuiContainer {
	
	public static final ResourceLocation NORMAL_TEXTURE = new ResourceLocation(IDs.RESOURCENAMEPREFIX + "textures/gui/normal.png");
	protected ResourceLocation texture;
	protected List<ElementBase> elements = Lists.<ElementBase> newArrayList();
	public final ContainerBase inventory;
	
	public GuiBase (ResourceLocation texture, ContainerBase inventory) {
		super(inventory);
		this.texture = texture;
		this.xSize = 176;
		this.ySize = 166;
		this.inventory = inventory;
	}
	
	/**
	 * @return the texture
	 */
	public ResourceLocation getTexture () {
		return texture;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer (float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		TextureHelper.bindTexture(this, texture);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		inventory.update();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) this.guiLeft, (float) this.guiTop, 0.0F);
		for (Slot s : this.inventorySlots.inventorySlots)
		{
			this.drawTexturedModalRect(s.xPos - 1, s.yPos - 1, this.xSize, 0, 18, 18);
		}
		GlStateManager.enableAlpha();
		for (ElementBase e : elements)
		{
			e.drawBottomLayer(mouseX - this.guiLeft, mouseY - this.guiTop);
		}
		GlStateManager.disableAlpha();
		GlStateManager.popMatrix();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer (int mouseX, int mouseY) {
		GlStateManager.enableAlpha();
		int x = mouseX - this.guiLeft, y = mouseY - this.guiTop;
		for (ElementBase e : elements)
		{
			e.drawElement(x, y);
		}
		for (ElementBase e : elements)
		{
			e.drawTopLayer(x, y);
		}
		GlStateManager.disableAlpha();
	}
	
	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	@Override
	protected void mouseClicked (int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		int x = mouseX - this.guiLeft, y = mouseY - this.guiTop;
		for (ElementBase e : elements)
		{
			e.onClick(x, y, mouseButton);
		}
	}
	
	@Override
	protected void mouseReleased (int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		int x = mouseX - this.guiLeft, y = mouseY - this.guiTop;
		for (ElementBase e : elements)
		{
			e.onRelease(x, y, state);
		}
	}
	
	/**
	 * Draws a texture rectangle using the texture currently bound to the TextureManager
	 */
	public void drawTexturedModalRect (int x, int y, float minU, float minV, float maxU, float maxV, int width, int height) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos((double) (x + 0), (double) (y + height), (double) this.zLevel).tex((double) minU, (double) maxV).endVertex();
		vertexbuffer.pos((double) (x + width), (double) (y + height), (double) this.zLevel).tex((double) maxU, (double) maxV).endVertex();
		vertexbuffer.pos((double) (x + width), (double) (y + 0), (double) this.zLevel).tex((double) maxU, (double) minV).endVertex();
		vertexbuffer.pos((double) (x + 0), (double) (y + 0), (double) this.zLevel).tex((double) minU, (double) minV).endVertex();
		tessellator.draw();
	}
	
	public void drawTintedTexturedModalRect (int x, int y, float minU, float minV, float maxU, float maxV, int width, int height, Color color) {
		GlStateManager.enableBlend();
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		vertexbuffer.pos((double) (x + 0), (double) (y + height), (double) this.zLevel).tex((double) minU, (double) maxV).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
		vertexbuffer.pos((double) (x + width), (double) (y + height), (double) this.zLevel).tex((double) maxU, (double) maxV).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
		vertexbuffer.pos((double) (x + width), (double) (y + 0), (double) this.zLevel).tex((double) maxU, (double) minV).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
		vertexbuffer.pos((double) (x + 0), (double) (y + 0), (double) this.zLevel).tex((double) minU, (double) minV).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
		tessellator.draw();
		GlStateManager.disableBlend();
	}
	
	/**
	 * Draws a texture rectangle using the texture currently bound to the TextureManager
	 */
	public void drawTexturedModalRect (int x, int y, int width, int height) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos((double) (x + 0), (double) (y + height), 0.0D).tex(0.0D, 1.0D).endVertex();
		vertexbuffer.pos((double) (x + width), (double) (y + height), 0.0D).tex(1.0D, 1.0D).endVertex();
		vertexbuffer.pos((double) (x + width), (double) (y + 0), 0.0D).tex(1.0D, 0.0D).endVertex();
		vertexbuffer.pos((double) (x + 0), (double) (y + 0), 0.0D).tex(0.0D, 0.0D).endVertex();
		tessellator.draw();
	}
	
	/**
	 * Draws a solid color rectangle with the specified coordinates and color.
	 */
	public static void drawRect (int left, int top, int right, int bottom, RGBAColor color) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.color(color.red, color.green, color.blue, color.alpha);
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION);
		vertexbuffer.pos((double) left, (double) bottom, 0.0D).endVertex();
		vertexbuffer.pos((double) right, (double) bottom, 0.0D).endVertex();
		vertexbuffer.pos((double) right, (double) top, 0.0D).endVertex();
		vertexbuffer.pos((double) left, (double) top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
	
	public void drawHoveringText (List<String> textLines, int x, int y) {
		super.drawHoveringText(textLines, x, y);
	}
	
	public boolean isPointInGuiRegion (int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY) {
		return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
	}
	
	public int getXSize () {
		return this.xSize;
	}
	
	public int getYSize () {
		return this.ySize;
	}
	
	public FontRenderer getFontRenderer () {
		return fontRendererObj;
	}
}
