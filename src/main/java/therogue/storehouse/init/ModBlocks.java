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

import java.util.ArrayList;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.block.StorehouseBaseBlock;
import therogue.storehouse.block.StorehouseBaseOre;
import therogue.storehouse.block.Decorative.StorehouseBaseRotatedBlock;
import therogue.storehouse.block.Decorative.StorehouseBaseSlab;
import therogue.storehouse.block.Decorative.StorehouseBaseStair;
import therogue.storehouse.block.machine.BlockCombustionGenerator;
import therogue.storehouse.block.machine.BlockCrystaliser;
import therogue.storehouse.block.machine.BlockLiquidGenerator;
import therogue.storehouse.block.machine.BlockSolarGenerator;
import therogue.storehouse.block.machine.BlockThermalPress;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.crafting.MachineRecipe;
import therogue.storehouse.crafting.wrapper.FluidStackComponent;
import therogue.storehouse.crafting.wrapper.ItemStackComponent;
import therogue.storehouse.reference.General;
import therogue.storehouse.reference.OreDictEntries;
import therogue.storehouse.tile.machine.TileCrystaliser;
import therogue.storehouse.tile.machine.TileThermalPress;
import therogue.storehouse.util.LOG;
import therogue.storehouse.util.RecipeHelper;

@GameRegistry.ObjectHolder (General.MOD_ID)
public class ModBlocks {
	
	/**
	 * Initialises a new array to hold all the blocks
	 */
	public static ArrayList<IStorehouseBaseBlock> blocklist = new ArrayList<IStorehouseBaseBlock>();
	/**
	 * Initialises all the blocks
	 */
	public static final StorehouseBaseBlock azurite_dust_block = new StorehouseBaseBlock("azurite_dust_block");
	public static final StorehouseBaseBlock azurite_crystal_block = new StorehouseBaseBlock("azurite_crystal_block").setOredictEntry(OreDictEntries.AZURITE_CRYSTAL_BLOCK);
	public static final StorehouseBaseBlock azurite_crystal_block_chiseled = new StorehouseBaseBlock("azurite_crystal_block_chiseled").setOredictEntry(OreDictEntries.AZURITE_CRYSTAL_BLOCK);
	public static final StorehouseBaseRotatedBlock azurite_crystal_block_pillar = new StorehouseBaseRotatedBlock(azurite_crystal_block, "pillar").setOredictEntry(OreDictEntries.AZURITE_CRYSTAL_BLOCK);
	public static final StorehouseBaseStair azurite_dust_block_stair = new StorehouseBaseStair(azurite_dust_block);
	public static final StorehouseBaseSlab.Half azurite_dust_block_half_slab = new StorehouseBaseSlab.Half(azurite_dust_block);
	public static final StorehouseBaseSlab.Double azurite_dust_block_double_slab = new StorehouseBaseSlab.Double(azurite_dust_block, azurite_dust_block_half_slab);
	public static final StorehouseBaseStair azurite_crystal_block_stair = new StorehouseBaseStair(azurite_crystal_block);
	public static final StorehouseBaseSlab.Half azurite_crystal_block_half_slab = new StorehouseBaseSlab.Half(azurite_crystal_block);
	public static final StorehouseBaseSlab.Double azurite_crystal_block_double_slab = new StorehouseBaseSlab.Double(azurite_crystal_block, azurite_crystal_block_half_slab);
	public static final StorehouseBaseBlock azurite_ore_block = new StorehouseBaseOre("azurite_ore_block", ModItems.azurite_dust_itemstack, 3, 6);
	public static final StorehouseBaseBlock solar_generator = new BlockSolarGenerator("solar_generator");
	public static final StorehouseBaseBlock combustion_generator = new BlockCombustionGenerator("combustion_generator");
	public static final StorehouseBaseBlock liquid_generator = new BlockLiquidGenerator("liquid_generator");
	public static final StorehouseBaseBlock thermal_press = new BlockThermalPress("thermal_press");
	public static final StorehouseBaseBlock crystaliser = new BlockCrystaliser("crystaliser");
	/**
	 * Adds ItemStack versions that I can reference
	 */
	public static ItemStack azurite_dust_block_itemstack = new ItemStack(azurite_dust_block);
	public static ItemStack azurite_crystal_block_itemstack = new ItemStack(azurite_crystal_block);
	public static ItemStack azurite_crystal_block_chiseled_itemstack = new ItemStack(azurite_crystal_block_chiseled);
	public static ItemStack azurite_crystal_block_pillar_itemstack = new ItemStack(azurite_crystal_block_pillar);
	public static ItemStack azurite_dust_block_stair_itemstack = new ItemStack(azurite_dust_block_stair);
	public static ItemStack azurite_dust_block_half_slab_itemstack = new ItemStack(azurite_dust_block_half_slab);
	public static ItemStack azurite_crystal_block_stair_itemstack = new ItemStack(azurite_crystal_block_stair);
	public static ItemStack azurite_crystal_block_half_slab_itemstack = new ItemStack(azurite_crystal_block_half_slab);
	public static ItemStack azurite_ore_block_itemstack = new ItemStack(azurite_ore_block);
	public static ItemStack solar_generator_itemstack = new ItemStack(solar_generator);
	public static ItemStack combustion_generator_itemstack = new ItemStack(combustion_generator);
	public static ItemStack liquid_generator_itemstack = new ItemStack(liquid_generator);
	public static ItemStack thermal_press_itemstack = new ItemStack(thermal_press);
	public static ItemStack crystaliser_itemstack = new ItemStack(crystaliser);
	/**
	 * Adds all the blocks to the array
	 */
	static
	{
		LOG.log("debug", "Adding Blocks");
		// Adds Decorative blocks
		blocklist.add(azurite_dust_block);
		blocklist.add(azurite_crystal_block);
		blocklist.add(azurite_crystal_block_chiseled);
		blocklist.add(azurite_crystal_block_pillar);
		// Adds Decorative block varients
		blocklist.add(azurite_dust_block_stair);
		blocklist.add(azurite_dust_block_half_slab);
		blocklist.add(azurite_dust_block_double_slab);
		blocklist.add(azurite_crystal_block_stair);
		blocklist.add(azurite_crystal_block_half_slab);
		blocklist.add(azurite_crystal_block_double_slab);
		blocklist.add(azurite_ore_block);
		blocklist.add(solar_generator);
		blocklist.add(combustion_generator);
		blocklist.add(liquid_generator);
		blocklist.add(thermal_press);
		blocklist.add(crystaliser);
	}
	
	public static void preInit () {
		for (IStorehouseBaseBlock block : blocklist)
		{
			block.preInit();
		}
	}
	
	public static void Init () {
		for (IStorehouseBaseBlock block : blocklist)
		{
			block.Init();
		}
		registerRecipes();
	}
	
	public static void postInit () {
		for (IStorehouseBaseBlock block : blocklist)
		{
			block.postInit();
		}
	}
	
	private static void registerRecipes () {
		RecipeHelper.registerTwoWayBlockRecipe(ModBlocks.azurite_crystal_block_itemstack, ModItems.azurite_crystal_itemstack, OreDictEntries.AZURITE_CRYSTAL_BLOCK, null);
		RecipeHelper.registerTwoWayBlockRecipe(ModBlocks.azurite_dust_block_itemstack, ModItems.azurite_dust_itemstack, null, null);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.azurite_crystal_block_chiseled), ModBlocks.azurite_crystal_block_half_slab, ModBlocks.azurite_crystal_block_half_slab);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.azurite_crystal_block_pillar), ModBlocks.azurite_crystal_block, ModBlocks.azurite_crystal_block);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.azurite_dust_block), ModBlocks.azurite_dust_block_half_slab, ModBlocks.azurite_dust_block_half_slab);
		MachineCraftingHandler.register(TileCrystaliser.class, new MachineRecipe(MachineRecipe.ALWAYSMODE, 80, new ItemStackComponent(ModItems.azurite_crystal_itemstack), new ItemStackComponent(ModItems.azurite_dust_itemstack), new FluidStackComponent(new FluidStack(FluidRegistry.WATER, 1000))));
		MachineCraftingHandler.register(TileThermalPress.class, new MachineRecipe(TileThermalPress.Mode.HIGH_PRESSURE.modeTest, 40, new ItemStackComponent(Items.DIAMOND), new ItemStackComponent(ModItems.azurite_dust_itemstack), new ItemStackComponent(ModItems.azurite_dust_itemstack),
				new ItemStackComponent(Items.IRON_INGOT), new ItemStackComponent(Items.REDSTONE), new ItemStackComponent(Items.REDSTONE)));
	}
}
