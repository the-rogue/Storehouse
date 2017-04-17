/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import therogue.storehouse.init.ModItems;
import therogue.storehouse.reference.General;

public class StorehouseCreativeTab {
	
	/**
	 * This mod's creative tab
	 */
	public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(General.MOD_ID) {
		
		@Override
		public ItemStack getTabIconItem () {
			return new ItemStack(ModItems.azurite_dust);
		}
		
		@Override
		public String getTranslatedTabLabel () {
			return General.MOD_NAME;
		}
	};
}
