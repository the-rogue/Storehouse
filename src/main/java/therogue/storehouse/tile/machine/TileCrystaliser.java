/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.tile.machine;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import therogue.storehouse.GeneralUtils;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.crafting.wrapper.ItemStackWrapper;
import therogue.storehouse.fluid.TileFluidTank;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.inventory.ItemStackUtils;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.StorehouseBaseMachine;

public class TileCrystaliser extends StorehouseBaseMachine implements ITickable {
	
	public static final int RFPerTick = 10;
	protected final MachineCraftingHandler<TileCrystaliser>.CraftingManager theCrafter;
	protected TileFluidTank tank = new TileFluidTank(this, 10000) {
		
		@Override
		public boolean canFillFluidType (FluidStack fluid) {
			if (fluid.getFluid() == FluidRegistry.WATER) return canFill();
			return false;
		}
	};
	
	public TileCrystaliser () {
		super(ModBlocks.crystaliser);
		modules.add(tank);
		this.setInventory(new InventoryManager(this, 4, new Integer[] { 1, 2 }, new Integer[] { 0, 3 }) {
			
			@Override
			public int getSlotLimit (int slot) {
				switch (slot)
				{
					case 0:
					case 1:
						return 1;
					default:
						return 64;
				}
			}
		});
		theCrafter = MachineCraftingHandler.getHandler(TileCrystaliser.class).newCrafter(this, "ITM 1 2 FLD", "ITM 0 1", energyStorage);
		modules.add(theCrafter);
		tank.setTileEntity(this);
		tank.setCanDrain(false);
		energyStorage.setRFPerTick(40);
		inventory.setItemValidForSlotChecks( (index, stack) -> {
			if (index == 1)
			{
				if (!this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL).getStackInSlot(0).isEmpty())
					return false;
				return theCrafter.checkItemValidForSlot(index - 1, new ItemStackWrapper(stack));
			}
			if ((index == 2 || index == 3) && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) return true;
			return false;
		});
	}
	
	// -------------------------ITickable-----------------------------------------------------------------
	@Override
	public void update () {
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
}
