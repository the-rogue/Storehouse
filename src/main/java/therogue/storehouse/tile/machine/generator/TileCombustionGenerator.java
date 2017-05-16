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

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.energy.CapabilityEnergy;
import therogue.storehouse.container.machine.ContainerCombustionGenerator;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.network.GuiUpdateTEPacket;
import therogue.storehouse.reference.MachineStats;
import therogue.storehouse.tile.MachineTier;
import therogue.storehouse.util.EnergyUtils;

public class TileCombustionGenerator extends TileBaseGenerator {
	
	private int generatorburntime = 0;
	private int maxruntime = 0;
	
	public TileCombustionGenerator (MachineTier type) {
		super(ModBlocks.combustion_generator, type, MachineStats.COMBUSTIONGENPERTICK, false);
		inventory = new InventoryManager(this, 3, new int[] { 0, 2 }, new int[] { 1 }) {
			
			@Override
			public boolean isItemValidForSlotChecks (int index, ItemStack stack) {
				if (index == 2 && TileEntityFurnace.isItemFuel(stack))
				{
					return true;
				}
				else if (index == 0 && (stack.getItem() instanceof IEnergyContainerItem || stack.hasCapability(CapabilityEnergy.ENERGY, null))) { return true; }
				return false;
			}
		};
	}
	
	@Override
	public boolean isRunning () {
		return generatorburntime > 0;
	}
	
	protected void tick () {
		this.sendEnergyToItems(0);
		if (EnergyUtils.isItemFull(inventory.getStackInSlot(0)))
		{
			inventory.setInventorySlotContents(0, inventory.pushItems(1, inventory.getStackInSlot(0)));
		}
		if (!isRunning())
		{
			ItemStack burnable = inventory.getStackInSlot(2);
			if (burnable != null && !burnable.isEmpty() && TileEntityFurnace.isItemFuel(burnable))
			{
				int burntime = TileEntityFurnace.getItemBurnTime(burnable) / (18 * GeneratorUtils.getBaseModifier(tier) + 1);
				if (burntime * this.RFPerTick <= this.getMaxEnergyStored() - this.getEnergyStored())
				{
					generatorburntime += burntime;
					maxruntime = burntime;
					Item burnitem = burnable.getItem();
					burnable.shrink(1);
					if (burnable.isEmpty())
					{
						inventory.setInventorySlotContents(2, burnitem.getContainerItem(burnable));
					}
					this.markDirty();
				}
			}
		}
		if (generatorburntime == 0)
		{
			maxruntime = 0;
		}
	}
	
	@Override
	protected void doRunTick () {
		--generatorburntime;
	}
	
	@Override
	public int runtimeLeft () {
		return generatorburntime;
	}
	
	@Override
	public int maxruntime () {
		return maxruntime;
	}
	
	public GuiUpdateTEPacket getGUIPacket () {
		GuiUpdateTEPacket packet = super.getGUIPacket();
		packet.getNbt().setInteger("BurnTime", generatorburntime);
		packet.getNbt().setInteger("MaxRuntime", maxruntime);
		return packet;
	}
	
	public void processGUIPacket (GuiUpdateTEPacket packet) {
		super.processGUIPacket(packet);
		generatorburntime = packet.getNbt().getInteger("BurnTime");
		maxruntime = packet.getNbt().getInteger("MaxRuntime");
	}
	
	@Override
	public Container createContainer (InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerCombustionGenerator(playerInventory, this);
	}
	
	@Override
	public String getGuiID () {
		return "storehouse:" + ModBlocks.combustion_generator.getName() + "." + type.getName();
	}
}
