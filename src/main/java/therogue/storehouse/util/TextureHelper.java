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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.io.IOUtils;

public class TextureHelper
{
	public static int calculateLength(int length, float percentage)
	{
		return Math.round(length * percentage);
	}
	
	public static float scalePercentageToLength(int length, float percentage)
	{
		return ((float) Math.round(length * percentage)) / ((float) length);
	}
	
	public static float convertUVToUsable(float minUV, float maxUV, float scaledPercentage)
	{
		return (maxUV - minUV) * scaledPercentage + minUV;
	}
	
	public static BufferedImage getImageAt(ResourceLocation location)
	{
		IResource iresource = null;
		BufferedImage bufferedimage = null;
		try
		{
			iresource = Minecraft.getMinecraft().getResourceManager().getResource(location);
			bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());
		}
		catch (IOException e)
		{
			loghelper.log("error", "Could not load the Texture at: " + location.toString());
		}
		finally
		{
			IOUtils.closeQuietly((Closeable) iresource);
		}
		return bufferedimage;
	}
}
