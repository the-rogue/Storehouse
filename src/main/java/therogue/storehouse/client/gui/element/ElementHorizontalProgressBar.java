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

import net.minecraft.util.ResourceLocation;
import therogue.storehouse.util.TextureHelper;

public class ElementHorizontalProgressBar extends ElementProgressBar {
	
	private final boolean toRight;
	
	public ElementHorizontalProgressBar (int x, int y, int width, int height, int foregroundColour, int backgroundColour) {
		this(x, y, width, height, foregroundColour, backgroundColour, true);
	}
	
	public ElementHorizontalProgressBar (int x, int y, int width, int height, int foregroundColour, int backgroundColour, boolean toRight) {
		super(x, y, width, height, foregroundColour, backgroundColour);
		this.toRight = toRight;
	}
	
	public ElementHorizontalProgressBar (int x, int y, ResourceLocation iconLocation) {
		this(x, y, iconLocation, true);
	}
	
	public ElementHorizontalProgressBar (int x, int y, ResourceLocation iconLocation, boolean toRight) {
		super(x, y, iconLocation);
		this.toRight = toRight;
	}
	
	@Override
	public int getNumberOfPixels () {
		return width;
	}
	
	@Override
	public float getMinU (float progress) {
		if (toRight) return 0.0F;
		return 0.5F - TextureHelper.scalePercentageToLength(height, progress) / 2;
	}
	
	@Override
	public float getMaxU (float progress) {
		if (toRight) return TextureHelper.scalePercentageToLength(width, progress) / 2;
		return 0.5F;
	}
	
	@Override
	public int getX (float progress) {
		if (toRight) return x;
		return x + width - getWidth(progress);
	}
	
	@Override
	public int getWidth (float progress) {
		return TextureHelper.calculateLength(width, progress);
	}
}
