
package therogue.storehouse.client.gui.element;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import therogue.storehouse.client.gui.ElementFactory;
import therogue.storehouse.client.gui.GuiHelper;
import therogue.storehouse.client.gui.Icons;
import therogue.storehouse.crafting.MachineCraftingHandler.CapabilityCrafter;
import therogue.storehouse.crafting.MachineCraftingHandler.ICraftingManager;
import therogue.storehouse.tile.IDataHandler;
import therogue.storehouse.tile.ITile;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.TileData.CapabilityDataHandler;

public class ElementFunctions {
	
	public static void setup () {
		registerFunctions();
		registerSubElements();
		registerBuilders();
	}
	
	private static void registerFunctions () {
		ElementFactory.addFunction("CRFT_TL", (tile, num) -> {
			ICraftingManager<?> craft = tile.getCapability(CapabilityCrafter.CraftingManager, null, ModuleContext.GUI);
			if (craft == null) throw new NullPointerException("Given a tile with no crafter for CRFT_TL function: " + tile.toString());
			return () -> craft.getTimeElapsed();
		});
		ElementFactory.addFunction("CRFT_TT", (tile, num) -> {
			ICraftingManager<?> craft = tile.getCapability(CapabilityCrafter.CraftingManager, null, ModuleContext.GUI);
			if (craft == null) throw new NullPointerException("Given a tile with no crafter for CRFT_TT function: " + tile.toString());
			return () -> craft.getTotalCraftingTime();
		});
		ElementFactory.addFunction("SOLARGEN_ON", (tile, num) -> {
			IDataHandler data = tile.getCapability(CapabilityDataHandler.DATAHANDLER, null, ModuleContext.GUI);
			if (data == null) throw new NullPointerException("Given a tile with no IDataHandler for SOLARGEN_ON function: " + tile.toString());
			return () -> data.getField(3);
		});
		ElementFactory.addFunction("ITEM_CHARGE", (tile, num) -> {
			IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL);
			if (inventory == null) throw new NullPointerException("Given a tile with no IItemHandler for ITEMCHARGE function: " + tile.toString());
			return () -> {
				ItemStack stack = inventory.extractItem(num, -1, true);
				if (stack != null && !stack.isEmpty() && stack.hasCapability(CapabilityEnergy.ENERGY, null))
					return stack.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored();
				return 0;
			};
		});
		ElementFactory.addFunction("ITEM_MAXCHARGE", (tile, num) -> {
			IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL);
			if (inventory == null) throw new NullPointerException("Given a tile with no IItemHandler for ITEMCHARGE function: " + tile.toString());
			return () -> {
				ItemStack stack = inventory.extractItem(num, -1, true);
				if (stack != null && !stack.isEmpty() && stack.hasCapability(CapabilityEnergy.ENERGY, null))
					return stack.getCapability(CapabilityEnergy.ENERGY, null).getMaxEnergyStored();
				return 0;
			};
		});
	}
	
	private static void registerSubElements () {
		ElementFactory.addSubElement(ProgressHandler.class, "UP_PB", (tile, parameters) -> {
			if (parameters.size() != 3) throw new IllegalArgumentException("Wrong Number of parameters for the Up Progress Bar subbuilder:" + parameters.toString());
			return new ElementVerticalProgressBar(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)), new ResourceLocation(parameters.get(2)));
		});
		ElementFactory.addSubElement(ProgressHandler.class, "RIGHT_PB", (tile, parameters) -> {
			if (parameters.size() != 3) throw new IllegalArgumentException("Wrong Number of parameters for the Right Progress Bar subbuilder:" + parameters.toString());
			return new ElementHorizontalProgressBar(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)), new ResourceLocation(parameters.get(2)));
		});
		ElementFactory.addSubElement(ProgressHandler.class, "DOWN_PB", (tile, parameters) -> {
			if (parameters.size() != 3) throw new IllegalArgumentException("Wrong Number of parameters for the Down Progress Bar subbuilder:" + parameters.toString());
			return new ElementVerticalProgressBar(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)), new ResourceLocation(parameters.get(2)), false);
		});
		ElementFactory.addSubElement(ProgressHandler.class, "LEFT_PB", (tile, parameters) -> {
			if (parameters.size() != 3) throw new IllegalArgumentException("Wrong Number of parameters for the Left Progress Bar subbuilder:" + parameters.toString());
			return new ElementHorizontalProgressBar(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)), new ResourceLocation(parameters.get(2)), false);
		});
		ElementFactory.addSubElement(ProgressHandler.class, "FADING_PB", (tile, parameters) -> {
			if (parameters.size() != 7) throw new IllegalArgumentException("Wrong Number of parameters for the Fading Progress Bar subbuilder:" + parameters.toString());
			int x = Integer.parseInt(parameters.get(0)), y = Integer.parseInt(parameters.get(1));
			int width = Integer.parseInt(parameters.get(2)), height = Integer.parseInt(parameters.get(3));
			int red = Integer.parseInt(parameters.get(4)), green = Integer.parseInt(parameters.get(5)), blue = Integer.parseInt(parameters.get(6));
			return new ElementFadingProgressBar(x, y, width, height, new Color(red, green, blue));
		});
		ElementFactory.addSubElement(ProgressHandler.class, "UBAR", (tile, parameters) -> {
			if (parameters.size() != 4 && parameters.size() != 6) throw new IllegalArgumentException("Wrong Number of parameters for the Up Bar subbuilder:" + parameters.toString());
			int x = Integer.parseInt(parameters.get(0)), y = Integer.parseInt(parameters.get(1));
			int width = Integer.parseInt(parameters.get(2)), height = Integer.parseInt(parameters.get(3));
			if (parameters.size() == 4) return new ElementVerticalProgressBar(x, y, width, height, GuiHelper.WHITE, GuiHelper.NORMAL_COLOUR);
			return new ElementVerticalProgressBar(x, y, width, height, Integer.parseInt(parameters.get(4)), Integer.parseInt(parameters.get(5)));
		});
		ElementFactory.addSubElement(ProgressHandler.class, "RBAR", (tile, parameters) -> {
			if (parameters.size() != 4 && parameters.size() != 6) throw new IllegalArgumentException("Wrong Number of parameters for the Right Bar subbuilder:" + parameters.toString());
			int x = Integer.parseInt(parameters.get(0)), y = Integer.parseInt(parameters.get(1));
			int width = Integer.parseInt(parameters.get(2)), height = Integer.parseInt(parameters.get(3));
			if (parameters.size() == 4) return new ElementHorizontalProgressBar(x, y, width, height, GuiHelper.WHITE, GuiHelper.NORMAL_COLOUR);
			return new ElementHorizontalProgressBar(x, y, width, height, Integer.parseInt(parameters.get(4)), Integer.parseInt(parameters.get(5)));
		});
		ElementFactory.addSubElement(ProgressHandler.class, "DBAR", (tile, parameters) -> {
			if (parameters.size() != 4 && parameters.size() != 6) throw new IllegalArgumentException("Wrong Number of parameters for the Down Bar subbuilder:" + parameters.toString());
			int x = Integer.parseInt(parameters.get(0)), y = Integer.parseInt(parameters.get(1));
			int width = Integer.parseInt(parameters.get(2)), height = Integer.parseInt(parameters.get(3));
			if (parameters.size() == 4) return new ElementVerticalProgressBar(x, y, width, height, GuiHelper.WHITE, GuiHelper.NORMAL_COLOUR);
			return new ElementVerticalProgressBar(x, y, width, height, Integer.parseInt(parameters.get(4)), Integer.parseInt(parameters.get(5)), false);
		});
		ElementFactory.addSubElement(ProgressHandler.class, "LBAR", (tile, parameters) -> {
			if (parameters.size() != 4 && parameters.size() != 6) throw new IllegalArgumentException("Wrong Number of parameters for the Left Bar subbuilder:" + parameters.toString());
			int x = Integer.parseInt(parameters.get(0)), y = Integer.parseInt(parameters.get(1));
			int width = Integer.parseInt(parameters.get(2)), height = Integer.parseInt(parameters.get(3));
			if (parameters.size() == 4) return new ElementHorizontalProgressBar(x, y, width, height, GuiHelper.WHITE, GuiHelper.NORMAL_COLOUR);
			return new ElementHorizontalProgressBar(x, y, width, height, Integer.parseInt(parameters.get(4)), Integer.parseInt(parameters.get(5)), false);
		});
		ElementFactory.addSubElement(ProgressHandler.class, "UARROW", (tile, parameters) -> {
			if (parameters.size() != 2) throw new IllegalArgumentException("Wrong Number of parameters for the Up Arrow subbuilder:" + parameters.toString());
			return new ElementVerticalProgressBar(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)), Icons.ProgressUp.getLocation());
		});
		ElementFactory.addSubElement(ProgressHandler.class, "RARROW", (tile, parameters) -> {
			if (parameters.size() != 2) throw new IllegalArgumentException("Wrong Number of parameters for the Right Arrow subbuilder:" + parameters.toString());
			return new ElementHorizontalProgressBar(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)), Icons.ProgressRight.getLocation());
		});
		ElementFactory.addSubElement(ProgressHandler.class, "DARROW", (tile, parameters) -> {
			if (parameters.size() != 2) throw new IllegalArgumentException("Wrong Number of parameters for the Down Arrow subbuilder:" + parameters.toString());
			return new ElementVerticalProgressBar(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)), Icons.ProgressDown.getLocation(), false);
		});
		ElementFactory.addSubElement(ProgressHandler.class, "LARROW", (tile, parameters) -> {
			if (parameters.size() != 2) throw new IllegalArgumentException("Wrong Number of parameters for the Left Arrow subbuilder:" + parameters.toString());
			return new ElementHorizontalProgressBar(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)), Icons.ProgressLeft.getLocation(), false);
		});
		ElementFactory.addSubElement(ProgressHandler.class, "J(", (tile, parameters) -> {
			int splitter = parameters.indexOf(")J(");
			if (splitter == -1) throw new IllegalArgumentException("No Splitter for join progress bar: " + parameters.toString());
			parameters.remove(parameters.size() - 1);
			Map<String, BiFunction<ITile, List<String>, ?>> subElements = ElementFactory.getSubElements(ProgressHandler.class);
			List<String> list1 = new ArrayList<>(parameters.subList(0, splitter));
			List<String> list2 = new ArrayList<>(parameters.subList(splitter + 1, parameters.size()));
			IProgressBar bar1 = (IProgressBar) subElements.get(list1.remove(0)).apply(tile, list1);
			IProgressBar bar2 = (IProgressBar) subElements.get(list2.remove(0)).apply(tile, list2);
			return new JoinProgressBar(bar1, bar2);
		});
		ElementFactory.addSubElement(ProgressHandler.class, "D(", (tile, parameters) -> {
			int splitter = parameters.indexOf(")D(");
			if (splitter == -1) throw new IllegalArgumentException("No Splitter for double progress bar: " + parameters.toString());
			parameters.remove(parameters.size() - 1);
			Map<String, BiFunction<ITile, List<String>, ?>> subElements = ElementFactory.getSubElements(ProgressHandler.class);
			List<String> list1 = new ArrayList<>(parameters.subList(0, splitter));
			List<String> list2 = new ArrayList<>(parameters.subList(splitter + 1, parameters.size()));
			IProgressBar bar1 = (IProgressBar) subElements.get(list1.remove(0)).apply(tile, list1);
			IProgressBar bar2 = (IProgressBar) subElements.get(list2.remove(0)).apply(tile, list2);
			return new DoubleProgressBar(bar1, bar2);
		});
	}
	
	private static void registerBuilders () {
		ElementFactory.addElementBuilder("ENERGYBAR", (tile, parameters) -> {
			if (parameters.size() != 2 && parameters.size() != 3) throw new IllegalArgumentException("Wrong Number of parameters for the Energy Bar builder:" + parameters.toString());
			ResourceLocation icon;
			if (parameters.size() == 2) icon = Icons.EnergyBar.getLocation();
			else icon = new ResourceLocation(parameters.get(2));
			IEnergyStorage energy = tile.getCapability(CapabilityEnergy.ENERGY, null, ModuleContext.GUI);
			ElementEnergyBar energyBar = new ElementEnergyBar(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)), icon);
			return new ProgressHandler( () -> energy.getEnergyStored(), () -> energy.getMaxEnergyStored(), energyBar);
		});
		ElementFactory.addElementBuilder("ACTIVE_ICON", (tile, parameters) -> {
			if (parameters.size() != 4) throw new IllegalArgumentException("Wrong Number of parameters for the ActiveIcon builder:" + parameters.toString());
			return new ElementActiveIcon(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)), new ResourceLocation(parameters.get(2)), ElementFactory.getFunction(parameters.get(3), tile));
		});
		ElementFactory.addElementBuilder("PROGRESS_BAR", (tile, parameters) -> {
			if (parameters.size() < 3) throw new IllegalArgumentException("Wrong Number of paramenters for the progress bar builder");
			Supplier<Integer> progress = ElementFactory.getFunction(parameters.remove(0), tile);
			Supplier<Integer> maxProgress = ElementFactory.getFunction(parameters.remove(0), tile);
			Map<String, BiFunction<ITile, List<String>, ?>> subElements = ElementFactory.getSubElements(ProgressHandler.class);
			BiFunction<ITile, List<String>, ?> func = subElements.get(parameters.remove(0));
			if (func == null) throw new NullPointerException("Couldnt find a sub element function for: " + parameters);
			IProgressBar bar = (IProgressBar) func.apply(tile, parameters);
			return new ProgressHandler(progress, maxProgress, bar);
		});
	}
}
