/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.type;

import net.minecraft.item.ItemStack;
import therogue.storehouse.client.gui.multisystem.Category;
import therogue.storehouse.client.gui.multisystem.SystemManager;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.init.ModItems;
import therogue.storehouse.tile.MachineTier;
import therogue.storehouse.tile.generator.GeneratorUtils;

public enum CategoryEnum
{
	GENERATORS ("generators", new Category(new ItemStack(ModBlocks.solar_generator, 1, GeneratorUtils.getMeta(MachineTier.basic)))),
	MACHINES ("machines", new Category(new ItemStack(ModBlocks.thermal_press, 1, 0))),
	DECORATIVE ("decorative_blocks", new Category(new ItemStack(ModBlocks.azurite_dust_block, 1, 0))),
	WORLDGEN ("world_gen", new Category(new ItemStack(ModBlocks.azurite_ore_block, 1, 0))),
	TOOLS ("tools", new Category(new ItemStack(ModItems.network_Inspector, 1, 0))),
	OTHER ("other", new Category(new ItemStack(ModItems.azurite_dust, 1, 0)));
	
	public final String name;
	public final Category category;
	
	private CategoryEnum (String name, Category category) {
		this.name = name;
		this.category = category;
	}
	
	public static void initCategories () {
		for (CategoryEnum c : values())
		{
			SystemManager.categories.put(c.name, c.category);
		}
	}
}
