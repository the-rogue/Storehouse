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

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.config.GuiUtils;
import therogue.storehouse.GeneralUtils;
import therogue.storehouse.LOG;
import therogue.storehouse.Storehouse;
import therogue.storehouse.client.gui.element.ElementBase;

public class GuiHelper {
	
	public static final int NORMAL_COLOUR = 0xFFCFCFCF;
	public static final int WHITE = 0xFFFFFFFF;
	public static final int GUI_TEXT_COLOUR = 0x8B8B8B;
	
	/**
	 * @param mouseX - Position of the mouse on the x-axis
	 * @param mouseY - Position of the mouse on the y-axis
	 * @param x - Starting x for the rectangle
	 * @param y - Starting y for the rectangle
	 * @param width - Width of the rectangle
	 * @param height - Height of the rectangle
	 * @return whether or not the mouse is in the rectangle
	 */
	public static boolean isMouseInRectange (int mouseX, int mouseY, int x, int y, int width, int height) {
		int xSize = x + width;
		int ySize = y + height;
		return (mouseX >= x && mouseX <= xSize && mouseY >= y && mouseY <= ySize);
	}
	
	/**
	 * Draws Text with a box around it, generally used for tooltips
	 * 
	 * @param font the FontRenderer to use to render the text
	 * @param xRight the base x point to be used if the text is to be rendered on the right side of a point
	 * @param xLeft the base x point to be used if the text is to be rendered on the left side of a point
	 * @param y the y-level to render the text on
	 * @param screenWidth the width of the screen the text will be rendered on
	 * @param screenHeight the height of the screen the text will be rendered on
	 * @param maxTextWidth the maximum width the caller of this method would like the text to be
	 * @param screenAdjusterLeft shifts the x point to the right, and then back to the left, useful if the drawMatrix has been translated
	 * @param screenAdjusterTop shifts the y point down, and then back up, useful if the drawMatrix has been translated
	 * @param useLeftSide whether or not to draw on the left side of the point, and use xLeft - note that if both useLeftSide and useRightSide are false then both will be treated as true
	 * @param useRightSide whether or not to draw on the right side of the point, and use xRight - note that if both useLeftSide and useRightSide are false then both will be treated as true
	 */
	public static void drawHoveringText (FontRenderer font, List<String> textLines, int xRight, int xLeft, int y, int screenWidth, int screenHeight,
			int maxTextWidth, int screenAdjusterLeft, int screenAdjusterTop, boolean useLeftSide, boolean useRightSide) {
		textLines = GeneralUtils.copyList(textLines);
		xRight += screenAdjusterLeft;
		xLeft += screenAdjusterLeft;
		y += screenAdjusterTop;
		if (textLines.isEmpty() || xLeft < 24 && !useRightSide
				|| screenWidth - xRight < 24 && !useLeftSide
				|| xLeft < 24 && screenWidth - xRight < 24)
			return;
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		// Gets the width of the longest line of text
		int tooltipTextWidth = getMaxLineLength(font, textLines);
		// Sets the tooltip width to the maximum text width if necessary
		if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth) tooltipTextWidth = maxTextWidth;
		// Separates the title lines, wraps all of the lines, and adjusts the line length accordingly
		tooltipTextWidth = adjustLength(xRight, xLeft, screenWidth, tooltipTextWidth, useLeftSide, useRightSide);
		List<String> titleLines = Arrays.asList(textLines.remove(0));
		titleLines = wrapText(font, titleLines, tooltipTextWidth);
		textLines = wrapText(font, textLines, tooltipTextWidth);
		int temp = getMaxLineLength(font, titleLines);
		if (tooltipTextWidth < temp) tooltipTextWidth = temp;
		temp = getMaxLineLength(font, textLines);
		if (tooltipTextWidth < temp) tooltipTextWidth = temp;
		// Gets the X position of the tooltip
		int tooltipX = scaleX(xRight, xLeft, screenWidth, tooltipTextWidth, useLeftSide, useRightSide);
		// Work out the height and y position
		int tooltipY = y + 4;
		int tooltipHeight = (titleLines.size() + textLines.size()) * 10;
		if (!textLines.isEmpty()) tooltipHeight += 2; // gap between title lines and next lines
		if (tooltipY + tooltipHeight + 6 > screenHeight) tooltipY = screenHeight - tooltipHeight - 6;
		tooltipX -= screenAdjusterLeft;
		tooltipY -= screenAdjusterTop;
		// Draw the background
		drawRoundedBorderedBox(tooltipX, tooltipY, 300, tooltipTextWidth, tooltipHeight, 0xF0100010, 0xF0100010, 0x505000FF, 0x5028007F);
		// Draw the text
		tooltipY = drawText(font, titleLines, tooltipX, tooltipY, 10);
		tooltipY += 2;
		tooltipY = drawText(font, textLines, tooltipX, tooltipY, 10);
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.enableRescaleNormal();
	}
	
	private static int adjustLength (int xRight, int xLeft, int width, int initialLength, boolean useLeft, boolean useRight) {
		if (!useRight && !useLeft) useRight = useLeft = true;
		if (xRight + initialLength + 8 > width && useLeft || !useRight)
		{
			if (xLeft - initialLength - 8 < 0 || !useLeft)
			{
				if (xLeft + (xRight - xLeft) / 2 < width / 2 && useRight && useLeft || useRight && !useLeft)
				{
					if (width - xRight - 8 > 0)
					{
						return width - xRight - 8;
					}
					else
					{
						return initialLength;
					}
				}
				if (xLeft - 8 > 0)
				{
					return xLeft - 8;
				}
				else
				{
					return initialLength;
				}
			}
			return initialLength;
		}
		if (xRight + initialLength + 8 > width) { return width - xRight - 8; }
		return initialLength;
	}
	
	private static int scaleX (int xRight, int xLeft, int width, int length, boolean useLeft, boolean useRight) {
		if (!useRight && !useLeft) useRight = useLeft = true;
		if (xRight + length + 8 > width && useLeft || !useRight)
		{
			if (xLeft - length - 8 < 0 || !useLeft)
			{
				if (xLeft + (xRight - xLeft) / 2 < width / 2 && useLeft && useRight || useRight && !useLeft) { return xRight + 4; }
				return 4;
			}
			return xLeft - length - 4;
		}
		return xRight + 4;
	}
	
	/**
	 * Gets the length of the longest line in a list of lines
	 * 
	 * @param font the FontRenderer to use to determine the length
	 * @param lines the lines of text to determine from
	 * @return the lengeth of the longest line
	 */
	public static int getMaxLineLength (FontRenderer font, List<String> lines) {
		int textWidth = 0;
		for (String textLine : lines)
		{
			int textLineWidth = font.getStringWidth(textLine);
			if (textLineWidth > textWidth) textWidth = textLineWidth;
		}
		return textWidth;
	}
	
	/**
	 * Wrapped a list of text to the specified width
	 * 
	 * @param toWrap the list of string to wrap, also is the list of strings that are wrapped
	 * @param font the FontRenderer to do the wrapping
	 * @param the width to wrap to
	 * @return the length of the longest line if it is longer than the width to wrap to
	 */
	public static List<String> wrapText (FontRenderer font, List<String> toWrap, int wrapWidth) {
		List<String> wrappedLines = new ArrayList<String>();
		for (String toWrapLine : toWrap)
		{
			List<String> wrappedLine = font.listFormattedStringToWidth(toWrapLine, wrapWidth);
			for (String line : wrappedLine)
				wrappedLines.add(line);
		}
		return wrappedLines;
	}
	
	/**
	 * Draws a list of text at specified co-ordinates with a specified gap between them (10 is recommended)
	 * 
	 * @param font the FontRenderer to render with
	 * @param textLines the text to render
	 * @param x the x co-ordinate to write at
	 * @param y the y co-ordinate to start at
	 * @param textHeight the gap between the top of one line of text and the top of another line of text
	 * @return the y co-ordinate that was reached
	 */
	public static int drawText (FontRenderer font, List<String> textLines, int x, int y, int textHeight) {
		for (String line : textLines)
		{
			font.drawStringWithShadow(line, (float) x, (float) y, -1);
			y += textHeight;
		}
		return y;
	}
	
	/**
	 * Draws a slightly rounded box around the specified area (leave 4 pixels around the area)
	 * 
	 * Will fade the colours together
	 * 
	 * @param x - the x co-ordinate of the space inside the box
	 * @param y - the y co-ordinate of the space inside the box
	 * @param z - the z level to draw at
	 * @param width - the width of the space inside the box
	 * @param height - the x height of the space inside the box
	 * @param startBackgroundColor - the background colour for the top of the box
	 * @param endBackgroundColor - the background colour for the bottom of the box
	 * @param startBorderColor - the border colour for the top of the box
	 * @param endBorderColor - the border colour for the bottom of the box
	 */
	public static void drawRoundedBorderedBox (int x, int y, int z, int width, int height, int startBackgroundColor, int endBackgroundColor,
			int startBorderColor, int endBorderColor) {
		GuiUtils.drawGradientRect(z, x - 3, y - 4, x + width + 3, y - 3, startBackgroundColor, endBackgroundColor);
		GuiUtils.drawGradientRect(z, x - 3, y + height + 3, x + width + 3, y + height + 4, endBackgroundColor, endBackgroundColor);
		GuiUtils.drawGradientRect(z, x - 3, y - 3, x + width + 3, y + height + 3, startBackgroundColor, endBackgroundColor);
		GuiUtils.drawGradientRect(z, x - 4, y - 3, x - 3, y + height + 3, startBackgroundColor, endBackgroundColor);
		GuiUtils.drawGradientRect(z, x + width + 3, y - 3, x + width + 4, y + height + 3, startBackgroundColor, startBackgroundColor);
		GuiUtils.drawGradientRect(z, x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, startBorderColor, endBorderColor);
		GuiUtils.drawGradientRect(z, x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, startBorderColor, endBorderColor);
		GuiUtils.drawGradientRect(z, x - 3, y - 3, x + width + 3, y - 3 + 1, startBorderColor, startBorderColor);
		GuiUtils.drawGradientRect(z, x - 3, y + height + 2, x + width + 3, y + height + 3, endBorderColor, endBorderColor);
	}
	
	public static class XYCoords {
		
		public static final XYCoords OFF_SCREEN = new XYCoords(Integer.MIN_VALUE, Integer.MIN_VALUE);
		public final int x;
		public final int y;
		
		public XYCoords (int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	public static int calculateLength (int length, float percentage) {
		return Math.round(length * percentage);
	}
	
	public static float scalePercentageToLength (int length, float percentage) {
		return ((float) Math.round(length * percentage)) / ((float) length);
	}
	
	public static float convertUVToUsable (float minUV, float maxUV, float scaledPercentage) {
		return (maxUV - minUV) * scaledPercentage + minUV;
	}
	
	public static BufferedImage getImageAt (ResourceLocation location) {
		IResource iresource = null;
		BufferedImage bufferedimage = null;
		try
		{
			iresource = Minecraft.getMinecraft().getResourceManager().getResource(location);
			bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());
		}
		catch (IOException e)
		{
			LOG.log("error", "Could not load the Texture at: " + location.toString());
		}
		finally
		{
			IOUtils.closeQuietly((Closeable) iresource);
		}
		return bufferedimage;
	}
	
	public static void bindTexture (ResourceLocation texture) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
	}
	
	public static void bindTexture (GuiScreen gui, ResourceLocation texture) {
		gui.mc.getTextureManager().bindTexture(texture);
	}
	
	public static void bindTexture (ElementBase element, String texture) {
		element.gui.mc.getTextureManager().bindTexture(new ResourceLocation(texture));
	}
	
	public static void bindTexture (ElementBase element, ResourceLocation texture) {
		element.gui.mc.getTextureManager().bindTexture(texture);
	}
	
	public static void bindTexture (ElementBase element, TextureAtlasSprite texture) {
		ResourceLocation location = new ResourceLocation(texture.getIconName());
		element.gui.mc.getTextureManager().bindTexture(new ResourceLocation(location.getResourceDomain(), "textures/" + location.getResourcePath()
				+ ".png"));
	}
	
	public static @Nonnull TextureAtlasSprite getMissingSprite () {
		return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
	}
	
	public static ResourceLocation convertSpritetoLocation (TextureAtlasSprite icon) {
		ResourceLocation original = new ResourceLocation(icon.getIconName());
		return new ResourceLocation(original.getResourceDomain(), "textures/" + original.getResourcePath() + ".png");
	}
	
	public static TextureAtlasSprite convertLocationToSprite (ResourceLocation location) {
		if (location == null) { return getMissingSprite(); }
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
	}
	
	public static ResourceLocation makeStorehouseLocation (String withoutDomain) {
		return new ResourceLocation(Storehouse.RESOURCENAMEPREFIX + withoutDomain);
	}
	
	public static void drawFluid (FluidStack fluid, int x, int y, int width, int height) {
		if (fluid == null || fluid.getFluid() == null) { return; }
		TextureAtlasSprite texture = convertLocationToSprite(fluid.getFluid().getStill(fluid));
		double minU = texture.getMinU(), minV = texture.getMinV(), maxU = texture.getMaxU(), maxV = texture.getMaxV();
		// int colour = fluid.getFluid().getColor(fluid);
		// GlStateManager.color((colour >> 16 & 0xFF) / 255, (colour >> 8 & 0xFF) / 255, (colour & 0xFF) / 255);
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.enableBlend();
		for (int i = 0; i < width; i += 16)
		{
			int drawWidth = Math.min(width - i, 16), drawX = x + i;
			for (int j = 0; j < height; j += 16)
			{
				int drawHeight = Math.min(height - j, 16), drawY = y + j;
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder vb = tessellator.getBuffer();
				vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				vb.pos(drawX, drawY + drawHeight, 0.0D).tex(minU, minV + (maxV - minV) * drawHeight / 16.0F).endVertex();
				vb.pos(drawX + drawWidth, drawY + drawHeight, 0.0D).tex(minU + (maxU - minU) * drawWidth / 16.0F, minV
						+ (maxV - minV) * drawHeight / 16.0F).endVertex();
				vb.pos(drawX + drawWidth, drawY, 0.0D).tex(minU + (maxU - minU) * drawWidth / 16.0F, minV).endVertex();
				vb.pos(drawX, drawY, 0.0D).tex(minU, minV).endVertex();
				tessellator.draw();
			}
		}
		GlStateManager.disableBlend();
	}
}
