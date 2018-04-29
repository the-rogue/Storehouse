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
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.crafting.ICrafter;
import therogue.storehouse.crafting.IMachineRecipe;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.crafting.inventory.EnergyInventory;
import therogue.storehouse.crafting.inventory.FluidTankInventory;
import therogue.storehouse.crafting.inventory.IRecipeInventory;
import therogue.storehouse.crafting.wrapper.FluidStackWrapper;
import therogue.storehouse.crafting.wrapper.IRecipeWrapper;
import therogue.storehouse.energy.TileEnergyStorage;
import therogue.storehouse.fluid.TileFluidTank;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.multiblock.structure.MultiBlockStructure;
import therogue.storehouse.tile.MachineTier;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.TileBaseGenerator;
import therogue.storehouse.util.GeneralUtils;
import therogue.storehouse.util.ItemStackUtils;

public class TileLiquidGenerator extends TileBaseGenerator implements ICrafter {
	
	public static final int[] RFPerTicks = { 20, 160, 4800 };
	public static final int[] TimeModifiers = { 1, 4, 12 };
	private static final IStorehouseBaseBlock[] BLOCKS = { ModBlocks.liquid_generator_basic, ModBlocks.liquid_generator_advanced, ModBlocks.liquid_generator_ender };
	protected final MachineCraftingHandler<TileLiquidGenerator>.CraftingManager theCrafter = MachineCraftingHandler.getHandler(TileLiquidGenerator.class).newCrafter(this);
	protected TileFluidTank tank = new TileFluidTank(10000) {
		
		@Override
		public boolean canFillFluidType (FluidStack fluid) {
			if (fluid.getFluid().getTemperature(fluid) > 500) return canFill();
			return false;
		}
	};
	
	public TileLiquidGenerator (MachineTier tier) {
		super(BLOCKS[tier.ordinal()], tier, RFPerTicks[tier.ordinal()], TimeModifiers[tier.ordinal()]);
		modules.add(theCrafter);
		modules.add(tank);
		this.setInventory(new InventoryManager(this, 4, new Integer[] { 0, 2 }, new Integer[] { 1, 3 }) {
			
			@Override
			public boolean isItemValidForSlotChecks (int index, ItemStack stack) {
				if ((index == 0 || index == 1) && stack.hasCapability(CapabilityEnergy.ENERGY, null)) return true;
				if ((index == 2 || index == 3) && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) return true;
				return false;
			}
		});
		tank.setTileEntity(this);
		tank.setCanDrain(false);
	}
	
	// -------------------------Customisable Generator Methods-------------------------------------------
	@Override
	public Set<Integer> getOrderMattersSlots () {
		return Sets.newHashSet();
	}
	
	@Override
	public IRecipeInventory getCraftingInventory () {
		return new FluidTankInventory(tank);
	}
	
	@Override
	public IRecipeInventory getOutputInventory () {
		return new EnergyInventory(energyStorage);
	}
	
	@Override
	public void update () {
		super.update();
		if (GeneralUtils.isServerSide(world))
		{
			IItemHandler internalView = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL);
			if (!internalView.getStackInSlot(2).isEmpty())
			{
				ItemStack tankItem = internalView.extractItem(2, -1, true);
				ItemStack outputSlot = internalView.getStackInSlot(3);
				ItemStack result = FluidUtil.tryEmptyContainer(tankItem, tank, tank.getCapacity() - tank.getFluidAmount(), null, false).result;
				if (ItemStackUtils.areStacksMergableWithLimit(internalView.getSlotLimit(3), result, outputSlot))
				{
					internalView.extractItem(2, -1, false);
					internalView.insertItem(3, ItemStackUtils.mergeStacks(internalView.getSlotLimit(3), true, internalView.extractItem(3, -1, false), FluidUtil.tryEmptyContainer(tankItem, tank, tank.getCapacity()
							- tank.getFluidAmount(), null, true).result), false);
				}
			}
		}
	}
	
	// ----------------------IMultiBlockController-----------------------------------------------------------------
	@Override
	public MultiBlockStructure getStructure () {
		return null;
	}
	
	// ------------------------PlaceHolder Classes------------------------------------------------------------------
	public static class TileLiquidGeneratorBasic extends TileLiquidGenerator {
		
		public TileLiquidGeneratorBasic () {
			super(MachineTier.basic);
		}
	}
	
	public static class TileLiquidGeneratorAdvanced extends TileLiquidGenerator {
		
		public TileLiquidGeneratorAdvanced () {
			super(MachineTier.advanced);
		}
	}
	
	public static class TileLiquidGeneratorEnder extends TileLiquidGenerator {
		
		public TileLiquidGeneratorEnder () {
			super(MachineTier.ender);
		}
	}
	
	public static enum LiquidRecipe implements IMachineRecipe<TileLiquidGenerator> {
		INSTANCE;
		
		public static void registerRecipes () {
			MachineCraftingHandler.register(TileLiquidGenerator.class, INSTANCE);
		}
		
		@Override
		public int timeTaken (TileLiquidGenerator machine) {
			return machine.tank.getFluid().getFluid().getTemperature(machine.tank.getFluid().copy()) / machine.timeModifier;
		}
		
		@Override
		public boolean itemValidForRecipe (TileLiquidGenerator tile, int index, IRecipeWrapper stack) {
			if (stack instanceof FluidStackWrapper)
			{
				FluidStack fluidStack = ((FluidStackWrapper) stack).getStack();
				if (fluidStack.getFluid().getTemperature(fluidStack) >= 600) return true;
			}
			return false;
		}
		
		@Override
		public boolean matches (TileLiquidGenerator machine) {
			FluidStack fluidStack = machine.tank.getFluid();
			if (fluidStack.getFluid().getTemperature(fluidStack) >= 600 && fluidStack.amount > 100) return true;
			return false;
		}
		
		@Override
		public Result begin (TileLiquidGenerator machine) {
			if (!matches(machine)) return Result.RESET;
			machine.tank.drainInternal(-100, true);
			return Result.CONTINUE;
		}
		
		@Override
		public Result doTick (TileLiquidGenerator machine) {
			TileEnergyStorage energy = machine.energyStorage;
			if (energy.getEnergyStored() > machine.getOutputInventory().getComponentSlotLimit(0) + machine.RFPerTick) return Result.PAUSE;
			energy.modifyEnergyStored(machine.RFPerTick);
			return Result.CONTINUE;
		}
	}
}
