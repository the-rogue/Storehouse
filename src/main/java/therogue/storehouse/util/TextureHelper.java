/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.util;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;

import javax.annotation.Nonnull;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import therogue.storehouse.client.gui.element.ElementBase;

public class TextureHelper {
	
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
		element.gui.mc.getTextureManager().bindTexture(new ResourceLocation(location.getResourceDomain(), "textures/" + location.getResourcePath() + ".png"));
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
	
	public static void drawFluid (FluidStack fluid, int x, int y, int width, int height) {
		if (fluid == null || fluid.getFluid() == null) { return; }
		TextureAtlasSprite texture = convertLocationToSprite(fluid.getFluid().getStill(fluid));
		double minU = texture.getMinU(), minV = texture.getMinV(), maxU = texture.getMaxU(), maxV = texture.getMaxV();
		int colour = fluid.getFluid().getColor(fluid);
		GlStateManager.color((colour >> 16 & 0xFF) / 255, (colour >> 8 & 0xFF) / 255, (colour & 0xFF) / 255);
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.enableBlend();
		for (int i = 0; i < width; i += 16)
		{
			int drawWidth = Math.min(width - i, 16), drawX = x + i;
			for (int j = 0; j < height; j += 16)
			{
				int drawHeight = Math.min(height - j, 16), drawY = y + j;
				Tessellator tessellator = Tessellator.getInstance();
				VertexBuffer vb = tessellator.getBuffer();
				vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				vb.pos(drawX, drawY + drawHeight, 0.0D).tex(minU, minV + (maxV - minV) * drawHeight / 16.0F).endVertex();
				vb.pos(drawX + drawWidth, drawY + drawHeight, 0.0D).tex(minU + (maxU - minU) * drawWidth / 16.0F, minV + (maxV - minV) * drawHeight / 16.0F).endVertex();
				vb.pos(drawX + drawWidth, drawY, 0.0D).tex(minU + (maxU - minU) * drawWidth / 16.0F, minV).endVertex();
				vb.pos(drawX, drawY, 0.0D).tex(minU, minV).endVertex();
				tessellator.draw();
			}
		}
		GlStateManager.disableBlend();
	}
}
