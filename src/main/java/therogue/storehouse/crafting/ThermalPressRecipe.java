/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.crafting;

import net.minecraft.item.ItemStack;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.tile.machine.TileThermalPress.Mode;

public class ThermalPressRecipe {
	
	public final Mode TPMode;
	public final int energyRequired;
	public final ItemStack output;
	public final RecipeInput[] inputs;
	public final RecipeInput mainInput;
	
	public ThermalPressRecipe (Mode TPMode, int energyRequired, ItemStack output, RecipeInput mainInput, RecipeInput... inputs) {
		this.TPMode = TPMode;
		this.energyRequired = energyRequired;
		this.output = output;
		this.mainInput = mainInput;
		this.inputs = inputs;
	}
	
	public boolean matches (InventoryManager inventory) {
		switch (TPMode) {
			case HIGH_PRESSURE:
				mainInput.matches(inventory.getStackInSlot(1));
				
				break;
			case JOIN:
				break;
			case PRESS:
				break;
			case STAMP:
				break;
			default:
				break;
		}
	}
}
