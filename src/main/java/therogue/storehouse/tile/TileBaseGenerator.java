/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.energy.EnergyUtils;
import therogue.storehouse.energy.TileEnergyStorage;
import therogue.storehouse.util.GeneralUtils;

public abstract class TileBaseGenerator extends StorehouseBaseTileMultiBlock implements ITickable {
	
	protected int RFPerTick;
	protected int timeModifier;
	public final MachineTier tier;
	protected final TileData FIELDDATA = new TileData();
	
	public TileBaseGenerator (IStorehouseBaseBlock block, MachineTier tier, int RFPerTick, int timeModifier) {
		super(block);
		modules.add(FIELDDATA);
		this.tier = tier;
		this.RFPerTick = RFPerTick;
		this.timeModifier = timeModifier;
		this.setEnergyStorage(new TileEnergyStorage(this, RFPerTick * 3600, 0, RFPerTick * 9));
		FIELDDATA.addField( () -> tier.ordinal());
		FIELDDATA.addField( () -> {// Current item energy level
			ItemStack stack = inventory.extractItem(0, -1, true, ModuleContext.INTERNAL);
			if (stack != null && !stack.isEmpty() && stack.hasCapability(CapabilityEnergy.ENERGY, null))
				return stack.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored();
			return 0;
		});
		FIELDDATA.addField( () -> {// Max item energy level
			ItemStack stack = inventory.extractItem(0, -1, true, ModuleContext.INTERNAL);
			if (stack != null && !stack.isEmpty() && stack.hasCapability(CapabilityEnergy.ENERGY, null))
				return stack.getCapability(CapabilityEnergy.ENERGY, null).getMaxEnergyStored();
			return 0;
		});
	}
	
	// -------------------------ITickable-----------------------------------------------------------------
	@Override
	public void update () {
		if (isFormed() && GeneralUtils.isServerSide(world))
		{
			this.sendEnergyToItems(0);
			if (EnergyUtils.isItemFull(inventory.extractItem(0, -1, true, ModuleContext.INTERNAL)))
			{
				inventory.insertItem(0, inventory.insertItem(1, inventory.extractItem(0, -1, false, ModuleContext.INTERNAL), false, ModuleContext.INTERNAL), false, ModuleContext.INTERNAL);
			}
			this.sendEnergyToNeighbours();
			if (world != null)
			{
				this.world.notifyBlockUpdate(this.getPos(), block.getBlock().getDefaultState(), block.getBlock().getDefaultState(), 0);
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
		EnergyUtils.sendItemEnergy(energyStorage, inventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL), slot, energyStorage.getMaxExtract()
				/ 4);
	}
	
	// -------------------------IWorldNamable Methods-----------------------------------------------------
	@Override
	public String getName () {
		return super.getName() + "_" + tier.getName();
	}
}
