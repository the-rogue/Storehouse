/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.init;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import therogue.storehouse.items.StorehouseBaseItem;
import therogue.storehouse.items.CraftingUsed.*;


public class ModItems
{
	public static final StorehouseBaseItem azuriteDust = new ItemAzuriteDust();

	public static void init()
	{
		GameRegistry.register(azuriteDust, new ResourceLocation("storehouse:azuritedust"));
	}
}
