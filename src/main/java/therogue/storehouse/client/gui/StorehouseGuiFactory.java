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

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import therogue.storehouse.util.loghelper;


public class StorehouseGuiFactory implements IModGuiFactory
{
	/**
	 * Unused Implemented Method
	 */
	@Override
	public void initialize(Minecraft minecraftInstance)
	{
	}

	/**
	 * Implemented Method, which returns the Configuration GUI class
	 */
	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass()
	{
		loghelper.log("trace", "Storehouse Config Gui Class Given");
		return StorehouseConfigGui.class;
	}

	/**
	 * Unused Implemented Method
	 */
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}

	/**
	 * Unused Implemented Method
	 */
	@SuppressWarnings("deprecation")
	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
	{
		return null;
	}
}
