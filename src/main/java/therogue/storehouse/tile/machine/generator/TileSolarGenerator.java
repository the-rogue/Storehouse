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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.CapabilityEnergy;
import therogue.storehouse.container.machine.generator.ContainerSolarGenerator;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.reference.MachineStats;
import therogue.storehouse.tile.MachineTier;
import therogue.storehouse.util.BlockUtils;
import therogue.storehouse.util.EnergyUtils;
import cofh.api.energy.IEnergyContainerItem;


public class TileSolarGenerator extends TileBaseGenerator implements ITickable
{
	public TileSolarGenerator(MachineTier type)
	{
		super(ModBlocks.solar_generator, type, MachineStats.SOLARGENPERTICK, false);
		inventory = new InventoryManager(this, 2, new int[] { 0 }, new int[] { 1 }) {
			@Override
			public boolean isItemValidForSlotChecks(int index, ItemStack stack)
			{
				return stack.getItem() instanceof IEnergyContainerItem || stack.hasCapability(CapabilityEnergy.ENERGY, null);
			}

			@Override
			public int getInventoryStackLimit(int slot)
			{
				return 1;
			}
		};
	}

	@Override
	public boolean isRunning()
	{
		return BlockUtils.canBlockSeeSky(this.pos, this.world) && (this.world.getWorldInfo().getWorldTime() % 24000) < 12000;
	}

	@Override
	protected void tick()
	{
		this.sendEnergyToItems(0);
		if (EnergyUtils.isItemFull(inventory.getStackInSlot(0)))
		{
			inventory.setInventorySlotContents(0, inventory.pushItems(1, inventory.getStackInSlot(0)));
		}
	}

	@Override
	protected void doRunTick()
	{

	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerSolarGenerator(playerInventory, this);
	}

	@Override
	public String getGuiID()
	{
		return "storehouse:" + ModBlocks.solar_generator.getName() + "." + type.getName();
	}

}
