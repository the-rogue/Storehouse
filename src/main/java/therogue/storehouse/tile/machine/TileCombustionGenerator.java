/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.tile.machine;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.crafting.ICrafter;
import therogue.storehouse.crafting.IMachineRecipe;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.crafting.inventory.EnergyInventory;
import therogue.storehouse.crafting.inventory.IRecipeInventory;
import therogue.storehouse.crafting.inventory.RangedItemInventory;
import therogue.storehouse.crafting.wrapper.IRecipeWrapper;
import therogue.storehouse.crafting.wrapper.ItemStackWrapper;
import therogue.storehouse.energy.TileEnergyStorage;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.multiblock.structure.MultiBlockStructure;
import therogue.storehouse.tile.MachineTier;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.TileBaseGenerator;

public class TileCombustionGenerator extends TileBaseGenerator implements ICrafter {
	
	public static final int[] RFPerTicks = { 20, 160, 4800 };
	public static final int[] TimeModifiers = { 1, 4, 12 };
	private static final IStorehouseBaseBlock[] BLOCKS = { ModBlocks.combustion_generator_basic, ModBlocks.combustion_generator_advanced, ModBlocks.combustion_generator_ender };
	protected final MachineCraftingHandler<TileCombustionGenerator>.CraftingManager theCrafter = MachineCraftingHandler.getHandler(TileCombustionGenerator.class).newCrafter(this);
	
	public TileCombustionGenerator (MachineTier tier) {
		super(BLOCKS[tier.ordinal()], tier, RFPerTicks[tier.ordinal()], TimeModifiers[tier.ordinal()]);
		modules.add(theCrafter);
		this.setInventory(new InventoryManager(this, 3, new Integer[] { 0, 2 }, new Integer[] { 1 }) {
			
			@Override
			public boolean isItemValidForSlotChecks (int index, ItemStack stack) {
				if (index == 2 && TileEntityFurnace.isItemFuel(stack)) return true;
				if ((index == 0 || index == 1) && stack.hasCapability(CapabilityEnergy.ENERGY, null)) return true;
				return false;
			}
		});
	}
	
	// -------------------------Customisable Generator Methods-------------------------------------------
	@Override
	public Set<Integer> getOrderMattersSlots () {
		return Sets.newHashSet();
	}
	
	@Override
	public IRecipeInventory getCraftingInventory () {
		return new RangedItemInventory(this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL), 2, 3);
	}
	
	@Override
	public IRecipeInventory getOutputInventory () {
		return new EnergyInventory(energyStorage);
	}
	
	// ----------------------IMultiBlockController-----------------------------------------------------------------
	@Override
	public MultiBlockStructure getStructure () {
		return null;
	}
	
	// ------------------------PlaceHolder Classes------------------------------------------------------------------
	public static class TileCombustionGeneratorBasic extends TileCombustionGenerator {
		
		public TileCombustionGeneratorBasic () {
			super(MachineTier.basic);
		}
	}
	
	public static class TileCombustionGeneratorAdvanced extends TileCombustionGenerator {
		
		public TileCombustionGeneratorAdvanced () {
			super(MachineTier.advanced);
		}
	}
	
	public static class TileCombustionGeneratorEnder extends TileCombustionGenerator {
		
		public TileCombustionGeneratorEnder () {
			super(MachineTier.ender);
		}
	}
	
	public static enum CombustionRecipe implements IMachineRecipe<TileCombustionGenerator> {
		INSTANCE;
		
		public static void registerRecipes () {
			MachineCraftingHandler.register(TileCombustionGenerator.class, INSTANCE);
		}
		
		@Override
		public int timeTaken (TileCombustionGenerator machine) {
			return TileEntityFurnace.getItemBurnTime(machine.inventory.extractItem(2, -1, true, ModuleContext.INTERNAL)) / machine.timeModifier;
		}
		
		@Override
		public boolean itemValidForRecipe (TileCombustionGenerator tile, int index, IRecipeWrapper stack) {
			if (stack instanceof ItemStackWrapper)
			{
				ItemStackWrapper itemStack = ((ItemStackWrapper) stack);
				if (TileEntityFurnace.isItemFuel(itemStack.getStack())) return true;
			}
			return false;
		}
		
		@Override
		public boolean matches (TileCombustionGenerator machine) {
			IRecipeWrapper wrapper = machine.getCraftingInventory().getComponent(0, true);
			return itemValidForRecipe(machine, 0, wrapper);
		}
		
		@Override
		public Result begin (TileCombustionGenerator machine) {
			if (!matches(machine)) return Result.RESET;
			ItemStack itemStack = machine.inventory.extractItem(2, -1, true, ModuleContext.INTERNAL);
			if (itemStack.getItem().getContainerItem(itemStack).isEmpty())
			{
				itemStack.grow(-1);
				machine.inventory.insertItem(0, itemStack, false, ModuleContext.INTERNAL);
				return Result.CONTINUE;
			}
			else if (itemStack.getCount() == 1)
			{
				machine.inventory.insertItem(0, new ItemStack(itemStack.getItem().getContainerItem()), false, ModuleContext.INTERNAL);
				return Result.CONTINUE;
			}
			return Result.RESET;
		}
		
		@Override
		public Result doTick (TileCombustionGenerator machine) {
			TileEnergyStorage energy = machine.energyStorage;
			if (energy.getEnergyStored() > energy.getMaxEnergyStored() + machine.RFPerTick) return Result.PAUSE;
			energy.modifyEnergyStored(machine.RFPerTick);
			return Result.CONTINUE;
		}
	}
}
