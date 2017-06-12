/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.tile.machine.generator;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.tile.MachineTier;
import therogue.storehouse.tile.StorehouseBaseMachine;
import therogue.storehouse.util.EnergyUtils;
import therogue.storehouse.util.GeneralUtils;

public abstract class TileBaseGenerator extends StorehouseBaseMachine {
	
	protected int RFPerTick;
	protected MachineTier tier;
	
	public TileBaseGenerator (IStorehouseBaseBlock block, MachineTier tier, int baseGeneration, boolean allowInsert) {
		super(block);
		this.tier = tier;
		this.RFPerTick = GeneratorUtils.getRecieve(tier, baseGeneration);
		energyStorage = GeneratorUtils.getAppropriateEnergyStored(tier, baseGeneration, allowInsert);
	}
	
	// -------------------------ITickable-----------------------------------------------------------------
	@Override
	public void update () {
		if (GeneralUtils.isServerSide(world))
		{
			if (isRunning())
			{
				doRunTick();
				energyStorage.modifyEnergyStored(RFPerTick);
			}
			tick();
			this.sendEnergyToNeighbours();
			if (world != null)
			{
				this.world.notifyBlockUpdate(this.getPos(), ModBlocks.azurite_dust_block.getDefaultState(), ModBlocks.azurite_dust_block.getDefaultState(), 0);
			}
		}
	}
	
	// -------------------------Utility Methods to keep update() short-----------------------------------
	/**
	 * Sends energy to neighbouring energy blocks
	 */
	protected void sendEnergyToNeighbours () {
		for (EnumFacing facing : EnumFacing.values())
		{
			if (energyStorage.getEnergyStored() <= 0) return;
			int sentRF = EnergyUtils.sendEnergy(world, getPos(), facing, energyStorage.extractEnergy(energyStorage.getMaxExtract(), true));
			energyStorage.extractEnergy(sentRF, false);
		}
	}
	
	protected void sendEnergyToItems (int slot) {
		if (energyStorage.getEnergyStored() <= 0) return;
		int sentRF = EnergyUtils.sendItemEnergy(inventory.getStackInSlot(slot), energyStorage.extractEnergy(energyStorage.getMaxExtract() / 4, true));
		energyStorage.extractEnergy(sentRF, false);
	}
	
	// -------------------------Customisable Generator Methods-------------------------------------------
	public abstract boolean isRunning ();
	
	protected abstract void doRunTick ();
	
	protected abstract void tick ();
	
	public int runtimeLeft () {
		return 0;
	}
	
	public int maxruntime () {
		return 0;
	}
	
	// -------------------------Container/Gui Methods----------------------------------------------------
	/**
	 * Fields Used: #4 - Running? #5 - Current item energy level #6 - Max item energy level #7 - Current burn time left #8 - Max burn time for this fuel
	 */
	@Override
	public int getField (int id) {
		ItemStack stack;
		switch (id) {
			case 1:
				return tier.ordinal();
			case 4:
				return isRunning() ? 1 : 0;
			case 5:
				stack = inventory.getStackInSlot(0);
				if (stack != null && !stack.isEmpty() && stack.hasCapability(CapabilityEnergy.ENERGY, null)) return stack.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored();
				return 0;
			case 6:
				stack = inventory.getStackInSlot(0);
				if (stack != null && !stack.isEmpty() && stack.hasCapability(CapabilityEnergy.ENERGY, null)) return stack.getCapability(CapabilityEnergy.ENERGY, null).getMaxEnergyStored();
				return 0;
			case 7:
				return runtimeLeft();
			case 8:
				return maxruntime();
			default:
				return super.getField(id);
		}
	}
	
	@Override
	public void setField (int id, int value) {
		super.setField(id, value);
		switch (id) {
		}
	}
	
	@Override
	public int getFieldCount () {
		return super.getFieldCount() + 5;
	}
	
	// -------------------------IWorldNamable Methods-----------------------------------------------------
	@Override
	public String getName () {
		return super.getName() + "_" + tier.getName();
	}
}
