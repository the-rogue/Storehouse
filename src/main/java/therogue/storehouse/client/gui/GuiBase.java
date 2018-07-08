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
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import therogue.storehouse.Storehouse;
import therogue.storehouse.client.gui.element.ElementBase;
import therogue.storehouse.container.ContainerBase;

public class GuiBase extends GuiContainer {
	
	public static final ResourceLocation NORMAL_TEXTURE = new ResourceLocation(Storehouse.RESOURCENAMEPREFIX + "textures/gui/normal.png");
	protected ResourceLocation texture;
	public List<ElementBase> elements = new ArrayList<>();
	public List<Runnable> drawingInstructions = new ArrayList<>();
	public final ContainerBase inventory;
	public final String name;
	public final IGuiSupplier tile;
	protected int texWidth = 176;
	protected int texHeight = 166;
	
	public GuiBase (ResourceLocation texture, ContainerBase inventory, IGuiSupplier tile) {
		super(inventory);
		this.texture = texture;
		this.xSize = 176;
		this.ySize = 166;
		this.inventory = inventory;
		this.tile = tile;
		this.name = tile.getGuiName();
	}
	
	public void onConstructorFinishTEMP () {
		for (ElementBase element : elements)
			element.setGUI(this);
	}
	
	/**
	 * @return the texture
	 */
	public ResourceLocation getTexture () {
		return texture;
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen (int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer (float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GuiHelper.bindTexture(this, texture);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTextureBackgroundRect(i, j, this.xSize, this.ySize, this.texWidth, this.texHeight);
		inventory.updateProcedure.run();;
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) this.guiLeft, (float) this.guiTop, 0.0F);
		for (Slot s : this.inventorySlots.inventorySlots)
		{
			this.drawTexturedModalRect(s.xPos - 1, s.yPos - 1, this.texWidth, 0, 18, 18);
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
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		int nameWidth = fontRenderer.getStringWidth(new TextComponentTranslation(name).getUnformattedComponentText());
		fontRenderer.drawString(new TextComponentTranslation(name).getFormattedText(), this.xSize / 2
				- nameWidth / 2, 4, GuiHelper.GUI_TEXT_COLOUR);
		GlStateManager.enableAlpha();
		int x = mouseX - this.guiLeft, y = mouseY - this.guiTop;
		for (ElementBase e : elements)
		{
			e.drawElement(x, y);
		}
		for (Runnable r : drawingInstructions)
		{
			r.run();
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
	 * Draws a textured rectangle at the current z-value, with default background texture values.
	 */
	public void drawTextureBackgroundRect (int x, int y, int width, int height, int texWidth, int texHeight) {
		drawTextureBackgroundRect(x, y, width, height, 0, 0, texWidth, texHeight, 256, 256);
	}
	
	/**
	 * Draws a textured rectangle at the current z-value.
	 */
	public void drawTextureBackgroundRect (int x, int y, int width, int height, int textureX, int textureY, int texWidth, int texHeight, int totalWidth, int totalHeight) {
		float f = 1.0f / totalWidth;
		float f1 = 1.0f / totalHeight;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos((double) (x + 0), (double) (y + height), (double) this.zLevel).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + texHeight) * f1)).endVertex();
		bufferbuilder.pos((double) (x + width), (double) (y + height), (double) this.zLevel).tex((double) ((float) (textureX + texWidth) * f), (double) ((float) (textureY + texHeight)
				* f1)).endVertex();
		bufferbuilder.pos((double) (x + width), (double) (y + 0), (double) this.zLevel).tex((double) ((float) (textureX + texWidth) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
		bufferbuilder.pos((double) (x + 0), (double) (y + 0), (double) this.zLevel).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
		tessellator.draw();
	}
	
	/**
	 * Draws a texture rectangle using the texture currently bound to the TextureManager
	 */
	public void drawTexturedModalRect (int x, int y, float minU, float minV, float maxU, float maxV, int width, int height) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
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
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		vertexbuffer.pos((double) (x + 0), (double) (y
				+ height), (double) this.zLevel).tex((double) minU, (double) maxV).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
		vertexbuffer.pos((double) (x + width), (double) (y
				+ height), (double) this.zLevel).tex((double) maxU, (double) maxV).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
		vertexbuffer.pos((double) (x + width), (double) (y
				+ 0), (double) this.zLevel).tex((double) maxU, (double) minV).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
		vertexbuffer.pos((double) (x + 0), (double) (y
				+ 0), (double) this.zLevel).tex((double) minU, (double) minV).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
		tessellator.draw();
		GlStateManager.disableBlend();
	}
	
	/**
	 * Draws a texture rectangle using the texture currently bound to the TextureManager
	 */
	public void drawTexturedModalRect (int x, int y, int width, int height) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
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
	public static void drawRect (int left, int top, int right, int bottom, int color) {
		float alpha = (float) (color >> 24 & 255) / 255.0F;
		float red = (float) (color >> 16 & 255) / 255.0F;
		float green = (float) (color >> 8 & 255) / 255.0F;
		float blue = (float) (color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.color(red, green, blue, alpha);
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
	
	public GuiBase setXSize (int xSize) {
		this.xSize = xSize;
		return this;
	}
	
	public GuiBase setYSize (int ySize) {
		this.ySize = ySize;
		return this;
	}
	
	public int getXSize () {
		return this.xSize;
	}
	
	public int getYSize () {
		return this.ySize;
	}
	
	public FontRenderer getFontRenderer () {
		return this.fontRenderer;
	}
}
