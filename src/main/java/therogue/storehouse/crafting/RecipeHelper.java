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

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import therogue.storehouse.crafting.wrapper.FluidStackComponent;
import therogue.storehouse.crafting.wrapper.ItemStackComponent;
import therogue.storehouse.tile.machine.TileAlloyFurnace;
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
		MachineCraftingHandler.register(TileCrystaliser.class, new MachineRecipe(timetaken, new ItemStackComponent(output), new ItemStackComponent(input), new FluidStackComponent(FluidRegistry.WATER)));
	}
	
	public static void registerAlloyFurnaceRecipe (int timetaken, ItemStack output, ItemStack... inputs) {
		MachineCraftingHandler.register(TileAlloyFurnace.class, new MachineRecipe(timetaken, new ItemStackComponent(output), ItemStackComponent.convert(inputs)));
	}
	
	public static void registerForgeRecipe (ItemStack output, ItemStack input) {
		MachineCraftingHandler.register(TileForge.class, new MachineRecipe(0, new ItemStackComponent(output), new ItemStackComponent(input)));
	}
	
	public static void registerThermalPressStampRecipe (int timetaken, ItemStack output, ItemStack... inputs) {
		MachineCraftingHandler.register(TileThermalPress.class, new MachineRecipe(TileThermalPress.Mode.STAMP.modeTest, timetaken, new ItemStackComponent(output), ItemStackComponent.convert(inputs)));
	}
	
	public static void registerThermalPressStampRecipe (int timetaken, ItemStack output, ItemStackComponent... inputs) {
		MachineCraftingHandler.register(TileThermalPress.class, new MachineRecipe(TileThermalPress.Mode.STAMP.modeTest, timetaken, new ItemStackComponent(output), inputs));
	}
	
	public static void registerThermalPressPressRecipe (int timetaken, ItemStack output, ItemStack... inputs) {
		MachineCraftingHandler.register(TileThermalPress.class, new MachineRecipe(TileThermalPress.Mode.PRESS.modeTest, timetaken, new ItemStackComponent(output), ItemStackComponent.convert(inputs)));
	}
	
	public static void registerThermalPressJoinRecipe (int timetaken, ItemStack output, ItemStack... inputs) {
		MachineCraftingHandler.register(TileThermalPress.class, new MachineRecipe(TileThermalPress.Mode.JOIN.modeTest, timetaken, new ItemStackComponent(output), ItemStackComponent.convert(inputs)));
	}
	
	public static void registerThermalPressHighPressureRecipe (int timetaken, ItemStack output, ItemStack... inputs) {
		MachineCraftingHandler.register(TileThermalPress.class, new MachineRecipe(TileThermalPress.Mode.HIGH_PRESSURE.modeTest, timetaken, new ItemStackComponent(output), ItemStackComponent.convert(inputs)));
	}
	
	// -------------------Vanilla--------------------------
	public static void registerTwoWayBlockRecipe (ItemStack block, ItemStack item, @Nullable String blockoreentry, @Nullable String itemoreentry) {
		if (block == null || item == null) { throw new RuntimeException("Attempted to register twoWayBlockRecipe with null item or block. Block: " + block + " Item: " + item + " BlockOreEntry: " + blockoreentry + " ItemOreEntry: " + itemoreentry); }
		ItemStack item9 = item.copy();
		item9.setCount(9);
		if (blockoreentry != null && itemoreentry != null)
		{
			GameRegistry.addRecipe(new ShapelessOreRecipe(item9, blockoreentry));
			GameRegistry.addRecipe(new ShapedOreRecipe(block, "ddd", "ddd", "ddd", 'd', itemoreentry));
		}
		else if (blockoreentry != null)
		{
			GameRegistry.addRecipe(new ShapelessOreRecipe(item9, blockoreentry));
			GameRegistry.addRecipe(block, "ddd", "ddd", "ddd", 'd', item);
		}
		else if (itemoreentry != null)
		{
			GameRegistry.addShapelessRecipe(item9, block);
			GameRegistry.addRecipe(new ShapedOreRecipe(block, "ddd", "ddd", "ddd", 'd', itemoreentry));
		}
		else
		{
			GameRegistry.addShapelessRecipe(item9, block);
			GameRegistry.addRecipe(block, "ddd", "ddd", "ddd", 'd', item);
		}
	}
}
