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
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.energy.CapabilityEnergy;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.client.gui.ElementFactory;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.TierIcons;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.crafting.IMachineRecipe;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.crafting.MachineCraftingHandler.ICraftingManager;
import therogue.storehouse.crafting.wrapper.IRecipeWrapper;
import therogue.storehouse.crafting.wrapper.ItemStackWrapper;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.tile.MachineTier;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.TileBaseGenerator;

public class TileCombustionGenerator extends TileBaseGenerator {
	
	public static final int[] RFPerTicks = { 20, 160, 4800 };
	public static final int[] TimeModifiers = { 1, 4, 12 };
	private static final IStorehouseBaseBlock[] BLOCKS = { ModBlocks.combustion_generator_basic, ModBlocks.combustion_generator_advanced, ModBlocks.combustion_generator_ender };
	protected final MachineCraftingHandler<TileCombustionGenerator>.CraftingManager theCrafter;
	
	public TileCombustionGenerator (MachineTier tier) {
		super(BLOCKS[tier.ordinal()], tier, RFPerTicks[tier.ordinal()], TimeModifiers[tier.ordinal()]);
		this.setInventory(new InventoryManager(this, 3, new Integer[] { 0, 2 }, new Integer[] { 1 }));
		theCrafter = MachineCraftingHandler.getHandler(TileCombustionGenerator.class).newCrafter(this, "ITM 2 3", "ENG", energyStorage);
		modules.add(theCrafter);
		inventory.setItemValidForSlotChecks( (index, stack) -> {
			if (index == 2 && TileEntityFurnace.isItemFuel(stack)) return true;
			if ((index == 0 || index == 1) && stack.hasCapability(CapabilityEnergy.ENERGY, null)) return true;
			return false;
		});
		this.containerFactory = (player) -> new ContainerBase(player.inventory, this).setTESlotList(inventory.guiAccess, new int[] { 0, 30, 17, 1, 66, 17, 2, 48, 53 });
		this.guiFactory = (player) -> {
			GuiBase gui = new GuiBase(this.tier.guiLocation, containerFactory.apply(player), this);
			String s = "ENERGYBAR 8 8 %s,  PROGRESS_BAR ITEM_CHARGE0 ITEM_MAXCHARGE0 RIGHT_PB 33 35 %s,  PROGRESS_BAR CRFT_TL CRFT_TT UP_PB 48 35 %s";
			int iTier = tier.ordinal();
			Object[] sP = new Object[] { TierIcons.EnergyBar.getLocation(iTier), TierIcons.EnergyIndicator.getLocation(iTier), TierIcons.CombustionIndicator.getLocation(iTier) };
			ElementFactory.makeElements(gui, gui.elements, this, s, sP);
			return gui;
		};
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
		public int timeTaken (ICraftingManager<TileCombustionGenerator> machine) {
			return TileEntityFurnace.getItemBurnTime(machine.getAttachedTile().inventory.extractItem(2, -1, true, ModuleContext.INTERNAL)) / machine.getAttachedTile().timeModifier;
		}
		
		@Override
		public boolean itemValidForRecipe (ICraftingManager<TileCombustionGenerator> tile, int index, IRecipeWrapper stack) {
			if (stack instanceof ItemStackWrapper)
			{
				ItemStackWrapper itemStack = ((ItemStackWrapper) stack);
				if (TileEntityFurnace.isItemFuel(itemStack.getStack())) return true;
			}
			return false;
		}
		
		@Override
		public boolean matches (ICraftingManager<TileCombustionGenerator> machine) {
			IRecipeWrapper wrapper = machine.getCraftingInventory().getComponent(0);
			return itemValidForRecipe(machine, 0, wrapper);
		}
		
		@Override
		public Result begin (ICraftingManager<TileCombustionGenerator> machine) {
			if (!matches(machine)) return Result.RESET;
			ItemStack itemStack = machine.getAttachedTile().inventory.extractItem(2, -1, true, ModuleContext.INTERNAL);
			if (itemStack.getItem().getContainerItem(itemStack).isEmpty())
			{
				machine.getCraftingInventory().extractComponent(0, 1, false);
				return Result.CONTINUE;
			}
			else if (itemStack.getCount() == 1)
			{
				machine.getCraftingInventory().extractComponent(0, 1, false);
				machine.getCraftingInventory().insertComponent(0, new ItemStackWrapper(new ItemStack(itemStack.getItem().getContainerItem())), false);
				return Result.CONTINUE;
			}
			return Result.RESET;
		}
		
		@Override
		public Result doTick (ICraftingManager<TileCombustionGenerator> machine) {
			Result res = machine.canRun();
			if (res == Result.CONTINUE) machine.doSpecifiedRunTick();
			return res;
		}
	}
}
