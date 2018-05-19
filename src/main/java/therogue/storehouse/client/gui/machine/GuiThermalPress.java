/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.client.gui.machine;

import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import therogue.storehouse.Storehouse;
import therogue.storehouse.client.gui.GuiBase;
import therogue.storehouse.client.gui.GuiHelper;
import therogue.storehouse.client.gui.Icons;
import therogue.storehouse.client.gui.element.DoubleProgressBar;
import therogue.storehouse.client.gui.element.ElementBase;
import therogue.storehouse.client.gui.element.ElementButton;
import therogue.storehouse.client.gui.element.ElementEnergyBar;
import therogue.storehouse.client.gui.element.ElementHorizontalProgressBar;
import therogue.storehouse.client.gui.element.ElementMode;
import therogue.storehouse.client.gui.element.ElementVerticalProgressBar;
import therogue.storehouse.client.gui.element.IProgressBar;
import therogue.storehouse.client.gui.element.IconDefinition;
import therogue.storehouse.client.gui.element.JoinProgressBar;
import therogue.storehouse.client.gui.element.ProgressHandler;
import therogue.storehouse.container.ContainerBase;
import therogue.storehouse.crafting.MachineCraftingHandler.CapabilityCrafter;
import therogue.storehouse.crafting.MachineCraftingHandler.ICraftingManager;
import therogue.storehouse.tile.ClientButton.CapabilityButton;
import therogue.storehouse.tile.IButton;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.machine.TileThermalPress;

public class GuiThermalPress extends GuiBase {
	
	public GuiThermalPress (ContainerBase inventory, TileThermalPress linked) {
		super(NORMAL_TEXTURE, inventory, linked);
		ICraftingManager<?> crafter = linked.getCapability(CapabilityCrafter.CraftingManager, null, ModuleContext.GUI);
		Supplier<Integer> timeElapsed = () -> crafter.getTimeElapsed();
		Supplier<Integer> totalTime = () -> crafter.getTotalCraftingTime();
		IEnergyStorage energy = linked.getCapability(CapabilityEnergy.ENERGY, null, ModuleContext.GUI);
		elements.add(new ProgressHandler( () -> energy.getEnergyStored(), () -> energy.getMaxEnergyStored(), new ElementEnergyBar(8, 8, Icons.EnergyBar.getLocation())));
		IButton<?> button = linked.getCapability(CapabilityButton.BUTTON, null, ModuleContext.GUI);
		IconDefinition[] innerIcons = new IconDefinition[] { new IconDefinition(new ResourceLocation(Storehouse.RESOURCENAMEPREFIX + "textures/gui/icons/thermalpress/press_mode.png"), this.xSize
				- 17, 7, 10, 10), new IconDefinition(new ResourceLocation(Storehouse.RESOURCENAMEPREFIX + "textures/gui/icons/thermalpress/join_mode.png"), this.xSize
						- 17, 7, 10, 10), new IconDefinition(new ResourceLocation(Storehouse.RESOURCENAMEPREFIX + "textures/gui/icons/thermalpress/stamp_mode.png"), this.xSize
								- 17, 7, 10, 10), new IconDefinition(new ResourceLocation(Storehouse.RESOURCENAMEPREFIX + "textures/gui/icons/thermalpress/high_pressure_mode.png"), this.xSize
										- 17, 7, 10, 10), };
		elements.add(new ElementButton(new IconDefinition(Icons.Button.getLocation(), this.xSize
				- 20, 4, 16, 16), "Click to change the mode of the Thermal Press", innerIcons, new String[] { "Current Setting: Press", "Current Setting: Join", "Current Setting: Stamp", "Current Setting: High Pressure" }, () -> button.getOrdinal(), () -> button.pressed()));
		elements.add(new ElementMode( () -> button.getOrdinal(), createPressProgressBar(timeElapsed, totalTime), createJoinProgressBar(timeElapsed, totalTime), createStampProgressBar(timeElapsed, totalTime), createHighPressureProgressBar(timeElapsed, totalTime)));
		onConstructorFinishTEMP();
	}
	
	private ElementBase createPressProgressBar (Supplier<Integer> progressSupplier, Supplier<Integer> maxProgressSupplier) {
		IProgressBar pointerbottom = new ElementVerticalProgressBar(67, 55, Icons.ProgressUp.getLocation());
		IProgressBar pointertop = new ElementVerticalProgressBar(67, 27, Icons.ProgressDown.getLocation(), false);
		IProgressBar topbottom = new DoubleProgressBar(pointerbottom, pointertop);
		IProgressBar bar = new JoinProgressBar(topbottom, createFinal());
		return new ProgressHandler(progressSupplier, maxProgressSupplier, bar);
	}
	
	private ElementBase createJoinProgressBar (Supplier<Integer> progressSupplier, Supplier<Integer> maxProgressSupplier) {
		IProgressBar pointertop = new ElementVerticalProgressBar(67, 27, Icons.ProgressDown.getLocation(), false);
		IProgressBar bar = new JoinProgressBar(pointertop, createFinal());
		return new ProgressHandler(progressSupplier, maxProgressSupplier, bar);
	}
	
	private ElementBase createStampProgressBar (Supplier<Integer> progressSupplier, Supplier<Integer> maxProgressSupplier) {
		IProgressBar pointertop = new ElementVerticalProgressBar(67, 27, Icons.ProgressDown.getLocation(), false);
		IProgressBar bar = new JoinProgressBar(pointertop, createFinal());
		return new ProgressHandler(progressSupplier, maxProgressSupplier, bar);
	}
	
	private ElementBase createHighPressureProgressBar (Supplier<Integer> progressSupplier, Supplier<Integer> maxProgressSupplier) {
		IProgressBar rectupbottom = new ElementVerticalProgressBar(72, 63, 2, 2, GuiHelper.WHITE, GuiHelper.NORMAL_COLOUR);
		IProgressBar leftbarbottom = new ElementHorizontalProgressBar(65, 65, 8, 2, GuiHelper.WHITE, GuiHelper.NORMAL_COLOUR, true);
		IProgressBar rightbarbottom = new ElementHorizontalProgressBar(73, 65, 8, 2, GuiHelper.WHITE, GuiHelper.NORMAL_COLOUR, false);
		IProgressBar pointerbottom = new ElementVerticalProgressBar(67, 55, Icons.ProgressUp.getLocation());
		IProgressBar barbottom = new DoubleProgressBar(leftbarbottom, rightbarbottom);
		IProgressBar combo1bottom = new JoinProgressBar(barbottom, rectupbottom);
		IProgressBar combo2bottom = new JoinProgressBar(combo1bottom, pointerbottom);
		IProgressBar rectdowntop = new ElementVerticalProgressBar(72, 25, 2, 2, GuiHelper.WHITE, GuiHelper.NORMAL_COLOUR, false);
		IProgressBar leftbartop = new ElementHorizontalProgressBar(65, 23, 8, 2, GuiHelper.WHITE, GuiHelper.NORMAL_COLOUR, true);
		IProgressBar rightbartop = new ElementHorizontalProgressBar(73, 23, 8, 2, GuiHelper.WHITE, GuiHelper.NORMAL_COLOUR, false);
		IProgressBar pointertop = new ElementVerticalProgressBar(67, 27, Icons.ProgressDown.getLocation(), false);
		IProgressBar bartop = new DoubleProgressBar(leftbartop, rightbartop);
		IProgressBar combo1top = new JoinProgressBar(bartop, rectdowntop);
		IProgressBar combo2top = new JoinProgressBar(combo1top, pointertop);
		IProgressBar topbottom = new DoubleProgressBar(combo2top, combo2bottom);
		IProgressBar bar = new JoinProgressBar(topbottom, createFinal());
		return new ProgressHandler(progressSupplier, maxProgressSupplier, bar);
	}
	
	private IProgressBar createFinal () {
		IProgressBar pointerright = new ElementHorizontalProgressBar(100, 39, Icons.ProgressRight.getLocation());
		IProgressBar barright = new ElementHorizontalProgressBar(92, 44, 8, 2, GuiHelper.WHITE, GuiHelper.NORMAL_COLOUR);
		return new JoinProgressBar(barright, pointerright);
	}
}
