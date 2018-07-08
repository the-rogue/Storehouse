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

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import therogue.storehouse.GeneralUtils;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.client.gui.ElementFactory;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.TierIcons;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.crafting.IMachineRecipe;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.crafting.MachineCraftingHandler.ICraftingManager;
import therogue.storehouse.crafting.wrapper.FluidStackWrapper;
import therogue.storehouse.crafting.wrapper.IRecipeWrapper;
import therogue.storehouse.fluid.TileFluidTank;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.inventory.ItemStackUtils;
import therogue.storehouse.tile.MachineTier;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.TileBaseGenerator;

public class TileLiquidGenerator extends TileBaseGenerator {
	
	public static final int[] RFPerTicks = { 20, 160, 4800 };
	public static final int[] TimeModifiers = { 1, 4, 12 };
	private static final IStorehouseBaseBlock[] BLOCKS = { ModBlocks.liquid_generator_basic, ModBlocks.liquid_generator_advanced, ModBlocks.liquid_generator_ender };
	protected final MachineCraftingHandler<TileLiquidGenerator>.CraftingManager theCrafter;
	protected TileFluidTank tank = new TileFluidTank(this, 10000) {
		
		@Override
		public boolean canFillFluidType (FluidStack fluid) {
			if (fluid.getFluid().getTemperature(fluid) > 500) return canFill();
			return false;
		}
	};
	
	public TileLiquidGenerator (MachineTier tier) {
		super(BLOCKS[tier.ordinal()], tier, RFPerTicks[tier.ordinal()], TimeModifiers[tier.ordinal()]);
		modules.add(tank);
		this.setInventory(new InventoryManager(this, 4, new Integer[] { 0, 2 }, new Integer[] { 1, 3 }));
		theCrafter = MachineCraftingHandler.getHandler(TileLiquidGenerator.class).newCrafter(this, "FLD", "ENG", energyStorage);
		modules.add(theCrafter);
		tank.setTileEntity(this);
		tank.setCanDrain(false);
		inventory.setItemValidForSlotChecks( (index, stack) -> {
			if ((index == 0 || index == 1) && stack.hasCapability(CapabilityEnergy.ENERGY, null)) return true;
			if ((index == 2 || index == 3) && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) return true;
			return false;
		});
		this.containerFactory = (player) -> new ContainerBase(player.inventory, this).setTESlotList(inventory.guiAccess, new int[] { 0, 30, 17, 1, 66, 17, 2, 141, 17, 3, 141, 53 });
		this.guiFactory = (player) -> {
			GuiBase gui = new GuiBase(this.tier.guiLocation, containerFactory.apply(player), this);
			String s = "ENERGYBAR 8 8 %s,  PROGRESS_BAR ITEM_CHARGE0 ITEM_MAXCHARGE0 RIGHT_PB 51 17 %s,  PROGRESS_BAR CRFT_TL CRFT_TT UP_PB 48 35 %s,  FLUID_TANK 105 12 %s";
			int iTier = tier.ordinal();
			Object[] sP = new Object[] { TierIcons.EnergyBar.getLocation(iTier), TierIcons.EnergyIndicator.getLocation(iTier), TierIcons.CombustionIndicator.getLocation(iTier), TierIcons.FluidTank.getLocation(iTier) };
			ElementFactory.makeElements(gui, gui.elements, this, s, sP);
			return gui;
		};
	}
	
	// -------------------------Customisable Generator Methods-------------------------------------------
	@Override
	public void update () {
		super.update();
		if (GeneralUtils.isServerSide(world))
		{
			IItemHandler internalView = inventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL);
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
		public int timeTaken (ICraftingManager<TileLiquidGenerator> machine) {
			return machine.getAttachedTile().tank.getFluid().getFluid().getTemperature(machine.getAttachedTile().tank.getFluid().copy()) / machine.getAttachedTile().timeModifier;
		}
		
		@Override
		public boolean itemValidForRecipe (ICraftingManager<TileLiquidGenerator> tile, int index, IRecipeWrapper stack) {
			if (stack instanceof FluidStackWrapper)
			{
				FluidStack fluidStack = ((FluidStackWrapper) stack).getStack();
				if (fluidStack.getFluid().getTemperature(fluidStack) >= 600) return true;
			}
			return false;
		}
		
		@Override
		public boolean matches (ICraftingManager<TileLiquidGenerator> machine) {
			FluidStack fluidStack = machine.getAttachedTile().tank.getFluid();
			if (fluidStack.getFluid().getTemperature(fluidStack) >= 600 && fluidStack.amount > 100) return true;
			return false;
		}
		
		@Override
		public Result begin (ICraftingManager<TileLiquidGenerator> machine) {
			if (!matches(machine)) return Result.RESET;
			machine.getCraftingInventory().extractComponent(0, 100, false);
			return Result.CONTINUE;
		}
		
		@Override
		public Result doTick (ICraftingManager<TileLiquidGenerator> machine) {
			Result res = machine.canRun();
			if (res == Result.CONTINUE) machine.doSpecifiedRunTick();
			return res;
		}
	}
}
