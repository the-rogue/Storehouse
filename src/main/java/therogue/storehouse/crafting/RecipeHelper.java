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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import therogue.storehouse.crafting.wrapper.FluidStackComponent;
import therogue.storehouse.crafting.wrapper.ItemOreNameComponent;
import therogue.storehouse.crafting.wrapper.ItemStackComponent;
import therogue.storehouse.tile.machine.TileAlloyFurnace;
import therogue.storehouse.tile.machine.TileBurner;
import therogue.storehouse.tile.machine.TileCrystaliser;
import therogue.storehouse.tile.machine.TileForge;
import therogue.storehouse.tile.machine.TileThermalPress;

/**
 * Contains lots of methods for setting common recipes
 */
public class RecipeHelper {
	
	// -----------------Registering Recipes---------------------
	public static List<String> getOreDictEntries (Block block) {
		return getOreDictEntries(new ItemStack(block));
	}
	
	public static List<String> getOreDictEntries (Item item) {
		return getOreDictEntries(new ItemStack(item));
	}
	
	public static List<String> getOreDictEntries (ItemStack stack) {
		List<String> entries = new ArrayList<String>();
		for (int i : OreDictionary.getOreIDs(stack))
		{
			entries.add(OreDictionary.getOreName(i));
		}
		return entries;
	}
	
	// ----------------Storehouse Recipes----------------------
	public static void registerCrystaliserRecipe (int timetaken, ItemStack output, ItemStack input) {
		MachineRecipe.create(TileCrystaliser.class, timetaken, new ItemStackComponent(output), new ItemStackComponent(input), new FluidStackComponent(FluidRegistry.WATER));
	}
	
	public static void registerAlloyFurnaceRecipe (int timetaken, ItemStack output, ItemStack... inputs) {
		MachineRecipe.create(TileAlloyFurnace.class, timetaken, new ItemStackComponent(output), ItemStackComponent.convert(inputs));
	}
	
	public static void registerForgeRecipe (ItemStack output, ItemStack input) {
		MachineRecipe.create(TileForge.class, 0, new ItemStackComponent(output), new ItemStackComponent(input));
	}
	
	public static void registerThermalPressStampRecipe (int timetaken, ItemStack output, ItemStack... inputs) {
		MachineRecipe.create(TileThermalPress.class, TileThermalPress.Mode.STAMP.modeTest, timetaken, new ItemStackComponent(output), ItemStackComponent.convert(inputs));
	}
	
	public static void registerThermalPressStampRecipe (int timetaken, ItemStack output, ItemStackComponent... inputs) {
		MachineRecipe.create(TileThermalPress.class, TileThermalPress.Mode.STAMP.modeTest, timetaken, new ItemStackComponent(output), inputs);
	}
	
	public static void registerThermalPressPressRecipe (int timetaken, ItemStack output, ItemStack... inputs) {
		MachineRecipe.create(TileThermalPress.class, TileThermalPress.Mode.PRESS.modeTest, timetaken, new ItemStackComponent(output), ItemStackComponent.convert(inputs));
	}
	
	public static void registerThermalPressJoinRecipe (int timetaken, ItemStack output, ItemStack... inputs) {
		MachineRecipe.create(TileThermalPress.class, TileThermalPress.Mode.JOIN.modeTest, timetaken, new ItemStackComponent(output), ItemStackComponent.convert(inputs));
	}
	
	public static void registerThermalPressHighPressureRecipe (int timetaken, ItemStack output, ItemStack... inputs) {
		MachineRecipe.create(TileThermalPress.class, TileThermalPress.Mode.HIGH_PRESSURE.modeTest, timetaken, new ItemStackComponent(output), ItemStackComponent.convert(inputs));
	}
	
	public static void registerBurnerRecipe (int timetaken, ItemStack output, String oreName, int amount) {
		MachineRecipe.create(TileBurner.class, timetaken, new ItemStackComponent(output), new ItemOreNameComponent(oreName, amount));
	}
}
