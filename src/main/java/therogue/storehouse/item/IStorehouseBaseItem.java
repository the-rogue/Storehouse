/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IStorehouseBaseItem extends IForgeRegistryEntry<Item> {
	
	public Item getItem ();
	
	/**
	 * Defines Methods that i need when referencing my item classes elsewhere
	 */
	// Convenient because not all items may extend StorehouseBaseItem in the future,
	// see IStorehouseBaseBlock for an example of this being used properly
	public String getName ();
	
	public default void registerModels () {
		ModelLoader.setCustomModelResourceLocation(getItem(), 0, new ModelResourceLocation(getItem().getUnlocalizedName().substring(5), "inventory"));
	}
}
