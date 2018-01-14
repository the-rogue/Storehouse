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
import therogue.storehouse.block.BlockUtils;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.energy.EnergyUtils;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.init.ModMultiBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.multiblock.structure.MultiBlockStructure;
import therogue.storehouse.tile.MachineTier;
import therogue.storehouse.tile.TileBaseGenerator;

public class TileSolarGenerator extends TileBaseGenerator {
	
	public static final int[] RFPerTick = { 5, 40, 1200 };
	private static final IStorehouseBaseBlock[] BLOCKS = { ModBlocks.solar_generator_basic, ModBlocks.solar_generator_advanced, ModBlocks.solar_generator_ender };
	
	public TileSolarGenerator (MachineTier tier) {
		super(BLOCKS[tier.ordinal()], tier, RFPerTick[tier.ordinal()]);
		inventory = new InventoryManager(this, 2, new Integer[] { 0 }, new Integer[] { 1 }) {
			
			@Override
			public boolean isItemValidForSlotChecks (int index, ItemStack stack) {
				if ((index == 0 || index == 1) && stack.hasCapability(CapabilityEnergy.ENERGY, null)) return true;
				return false;
			}
		};
	}
	
	// -------------------------Customisable Generator Methods-------------------------------------------
	@Override
	public boolean isRunning () {
		return BlockUtils.canBlockSeeSky(this.pos, this.world) && (this.world.getWorldInfo().getWorldTime() % 24000) < 12000;
	}
	
	@Override
	protected void tick () {
		this.sendEnergyToItems(0);
		if (EnergyUtils.isItemFull(inventory.getStackInSlot(0)))
		{
			inventory.pushItems(0, 1);
		}
	}
	
	@Override
	protected void doRunTick () {
	}
	
	// ----------------------IMultiBlockController-----------------------------------------------------------------
	@Override
	public MultiBlockStructure getStructure () {
		switch (tier)
		{
			case basic:
				return ModMultiBlocks.basicSolarGeneratorStructure;
			case advanced:
				return ModMultiBlocks.advancedSolarGeneratorStructure;
			case ender:
				return ModMultiBlocks.enderSolarGeneratorStructure;
			default:
				return null;
		}
	}
	
	// ------------------------PlaceHolder Classes------------------------------------------------------------------
	public static class TileSolarGeneratorBasic extends TileSolarGenerator {
		
		public TileSolarGeneratorBasic () {
			super(MachineTier.basic);
		}
	}
	
	public static class TileSolarGeneratorAdvanced extends TileSolarGenerator {
		
		public TileSolarGeneratorAdvanced () {
			super(MachineTier.advanced);
		}
	}
	
	public static class TileSolarGeneratorEnder extends TileSolarGenerator {
		
		public TileSolarGeneratorEnder () {
			super(MachineTier.ender);
		}
	}
}
