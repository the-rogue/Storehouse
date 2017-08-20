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

public class ElementVerticalProgressBar extends ElementProgressBar {
	
	private final boolean upwards;
	
	public ElementVerticalProgressBar (int x, int y, int width, int height, int foregroundColour, int backgroundColour) {
		this(x, y, width, height, foregroundColour, backgroundColour, true);
	}
	
	public ElementVerticalProgressBar (int x, int y, int width, int height, int foregroundColour, int backgroundColour, boolean upwards) {
		super(x, y, width, height, foregroundColour, backgroundColour);
		this.upwards = upwards;
	}
	
	public ElementVerticalProgressBar (int x, int y, ResourceLocation iconLocation) {
		this(x, y, iconLocation, true);
	}
	
	public ElementVerticalProgressBar (int x, int y, ResourceLocation iconLocation, boolean upwards) {
		super(x, y, iconLocation);
		this.upwards = upwards;
	}
	
	@Override
	public int getNumberOfPixels () {
		return height;
	}
	
	@Override
	public float getMinV (float progress) {
		if (upwards) return 1.0F - TextureHelper.scalePercentageToLength(height, progress);
		return 0.0F;
	}
	
	@Override
	public float getMaxV (float progress) {
		if (upwards) return 1.0F;
		return TextureHelper.scalePercentageToLength(height, progress);
	}
	
	@Override
	public int getY (float progress) {
		if (upwards) return y + height - getHeight(progress);
		return y;
	}
	
	@Override
	public int getHeight (float progress) {
		return TextureHelper.calculateLength(height, progress);
	}
}
