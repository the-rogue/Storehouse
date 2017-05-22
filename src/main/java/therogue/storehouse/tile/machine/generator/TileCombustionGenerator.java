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
	
	public TileCombustionGenerator (MachineTier tier) {
		super(ModBlocks.combustion_generator, tier, MachineStats.COMBUSTIONGENPERTICK, false);
		inventory = new InventoryManager(this, 3, new Integer[] { 0, 2 }, new Integer[] { 1 }) {
			
			@Override
			public boolean isItemValidForSlotChecks (int index, ItemStack stack) {
				if (index == 2 && TileEntityFurnace.isItemFuel(stack)) return true;
				if ((index == 0 || index == 1) && stack.hasCapability(CapabilityEnergy.ENERGY, null)) return true;
				return false;
			}
		};
	}
	
	// -------------------------Customisable Generator Methods-------------------------------------------
	@Override
	public boolean isRunning () {
		return generatorburntime > 0;
	}
	
	@Override
	protected void doRunTick () {
		--generatorburntime;
	}
	
	@Override
	protected void tick () {
		this.sendEnergyToItems(0);
		if (EnergyUtils.isItemFull(inventory.getStackInSlot(0)))
		{
			inventory.pushItems(0, 1);
		}
		if (!isRunning())
		{
			ItemStack burnable = inventory.getStackInSlot(2);
			if (burnable != null && !burnable.isEmpty() && TileEntityFurnace.isItemFuel(burnable))
			{
				int burntime = TileEntityFurnace.getItemBurnTime(burnable) / (18 * GeneratorUtils.getBaseModifier(tier) + 1);
				if (burntime * this.RFPerTick <= energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored())
				{
					generatorburntime += burntime;
					maxruntime = burntime;
					Item burnitem = burnable.getItem();
					burnable.shrink(1);
					if (burnable.isEmpty())
					{
						inventory.setStackInSlot(2, burnitem.getContainerItem(burnable));
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
	public int runtimeLeft () {
		return generatorburntime;
	}
	
	@Override
	public int maxruntime () {
		return maxruntime;
	}
	
	// -------------------------IInteractionObject-----------------------------------------------------------------
	@Override
	public Container createContainer (InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerCombustionGenerator(playerInventory, this);
	}
	
	@Override
	public String getGuiID () {
		return "storehouse:" + ModBlocks.combustion_generator.getName() + "." + tier.getName();
	}
	
	// -------------------------Standard TE methods-----------------------------------
	@Override
	public GuiUpdateTEPacket getGUIPacket () {
		GuiUpdateTEPacket packet = super.getGUIPacket();
		packet.getNbt().setInteger("BurnTime", generatorburntime);
		packet.getNbt().setInteger("MaxRuntime", maxruntime);
		return packet;
	}
	
	@Override
	public void processGUIPacket (GuiUpdateTEPacket packet) {
		super.processGUIPacket(packet);
		generatorburntime = packet.getNbt().getInteger("BurnTime");
		maxruntime = packet.getNbt().getInteger("MaxRuntime");
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
	
	public static class TileCombustionGeneratorInfused extends TileCombustionGenerator {
		
		public TileCombustionGeneratorInfused () {
			super(MachineTier.infused);
		}
	}
	
	public static class TileCombustionGeneratorEnder extends TileCombustionGenerator {
		
		public TileCombustionGeneratorEnder () {
			super(MachineTier.ender);
		}
	}
	
	public static class TileCombustionGeneratorUltimate extends TileCombustionGenerator {
		
		public TileCombustionGeneratorUltimate () {
			super(MachineTier.ultimate);
		}
	}
}
