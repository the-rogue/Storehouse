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

import net.minecraftforge.energy.CapabilityEnergy;
import therogue.storehouse.block.BlockUtils;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.client.gui.ElementFactory;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.TierIcons;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.tile.MachineTier;
import therogue.storehouse.tile.TileBaseGenerator;

public class TileSolarGenerator extends TileBaseGenerator {
	
	public static final int[] RFPerTicks = { 5, 40, 1200 };
	public static final int[] TimeModifiers = { 1, 4, 12 };
	private static final IStorehouseBaseBlock[] BLOCKS = { ModBlocks.solar_generator_basic, ModBlocks.solar_generator_advanced, ModBlocks.solar_generator_ender };
	
	public TileSolarGenerator (MachineTier tier) {
		super(BLOCKS[tier.ordinal()], tier, RFPerTicks[tier.ordinal()], TimeModifiers[tier.ordinal()]);
		this.setInventory(new InventoryManager(this, 2, new Integer[] { 0 }, new Integer[] { 1 }));
		inventory.setItemValidForSlotChecks( (index, stack) -> (index == 0 || index == 1) && stack.hasCapability(CapabilityEnergy.ENERGY, null) ? true : false);
		FIELDDATA.addField( () -> BlockUtils.canBlockSeeSky(this.pos, this.world)
				&& (this.world.getWorldInfo().getWorldTime() % 24000) < 12000 ? 1 : 0);// Is Running?
		this.containerFactory = (player) -> new ContainerBase(player.inventory, this).setTESlotList(inventory.guiAccess, new int[] { 0, 30, 53, 1, 30, 17 });
		this.guiFactory = (player) -> {
			GuiBase gui = new GuiBase(this.tier.guiLocation, containerFactory.apply(player), this);
			String s = "ENERGYBAR 8 8 %s,  ACTIVE_ICON 90 23 %s SOLARGEN_ON,  PROGRESS_BAR ITEM_CHARGE0 ITEM_MAXCHARGE0 RIGHT_PB 33 35 %s";
			int iTier = tier.ordinal();
			Object[] sP = new Object[] { TierIcons.EnergyBar.getLocation(iTier), TierIcons.SolarGenOn.getLocation(iTier), TierIcons.EnergyIndicator.getLocation(iTier) };
			ElementFactory.makeElements(gui, gui.elements, this, s, sP);
			return gui;
		};
	}
	
	// -------------------------ITickable-----------------------------------------------------------------
	@Override
	public void update () {
		if (isFormed() && BlockUtils.canBlockSeeSky(this.pos, this.world) && (this.world.getWorldInfo().getWorldTime() % 24000) < 12000)
		{
			energyStorage.modifyEnergyStored(super.RFPerTick);
		}
		super.update();
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
