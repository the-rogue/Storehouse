
package therogue.storehouse.client.gui.element;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import therogue.storehouse.GeneralUtils;
import therogue.storehouse.client.gui.ElementFactory;
import therogue.storehouse.client.gui.GuiHelper;
import therogue.storehouse.crafting.MachineCraftingHandler.CapabilityCrafter;
import therogue.storehouse.crafting.MachineCraftingHandler.ICraftingManager;
import therogue.storehouse.tile.ClientButton.CapabilityButton;
import therogue.storehouse.tile.IButton;
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
		ElementFactory.addFunction("BUTTON", (tile, num) -> {
			IButton<?> button = tile.getCapability(CapabilityButton.BUTTON, null, ModuleContext.GUI);
			if (button == null) throw new NullPointerException("Given a tile with no button for BUTTON function: " + tile.toString());
			return () -> button.getOrdinal();
		});
		ElementFactory.addConsumer("BUTTON", (tile, num) -> {
			IButton<?> button = tile.getCapability(CapabilityButton.BUTTON, null, ModuleContext.GUI);
			if (button == null) throw new NullPointerException("Given a tile with no button for BUTTON function: " + tile.toString());
			return (i) -> button.pressed();
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
			if (parameters.size() == 4) return new ElementVerticalProgressBar(x, y, width, height, GuiHelper.WHITE, GuiHelper.NORMAL_COLOUR, false);
			return new ElementVerticalProgressBar(x, y, width, height, Integer.parseInt(parameters.get(4)), Integer.parseInt(parameters.get(5)), false);
		});
		ElementFactory.addSubElement(ProgressHandler.class, "LBAR", (tile, parameters) -> {
			if (parameters.size() != 4 && parameters.size() != 6) throw new IllegalArgumentException("Wrong Number of parameters for the Left Bar subbuilder:" + parameters.toString());
			int x = Integer.parseInt(parameters.get(0)), y = Integer.parseInt(parameters.get(1));
			int width = Integer.parseInt(parameters.get(2)), height = Integer.parseInt(parameters.get(3));
			if (parameters.size() == 4) return new ElementHorizontalProgressBar(x, y, width, height, GuiHelper.WHITE, GuiHelper.NORMAL_COLOUR, false);
			return new ElementHorizontalProgressBar(x, y, width, height, Integer.parseInt(parameters.get(4)), Integer.parseInt(parameters.get(5)), false);
		});
		ElementFactory.addSubElement(ProgressHandler.class, "UARROW", (tile, parameters) -> {
			if (parameters.size() != 2) throw new IllegalArgumentException("Wrong Number of parameters for the Up Arrow subbuilder:" + parameters.toString());
			return new ElementVerticalProgressBar(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)), GuiHelper.makeStorehouseLocation("textures/gui/icons/progressbar/up.png"));
		});
		ElementFactory.addSubElement(ProgressHandler.class, "RARROW", (tile, parameters) -> {
			if (parameters.size() != 2) throw new IllegalArgumentException("Wrong Number of parameters for the Right Arrow subbuilder:" + parameters.toString());
			return new ElementHorizontalProgressBar(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)), GuiHelper.makeStorehouseLocation("textures/gui/icons/progressbar/right.png"));
		});
		ElementFactory.addSubElement(ProgressHandler.class, "DARROW", (tile, parameters) -> {
			if (parameters.size() != 2) throw new IllegalArgumentException("Wrong Number of parameters for the Down Arrow subbuilder:" + parameters.toString());
			return new ElementVerticalProgressBar(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)), GuiHelper.makeStorehouseLocation("textures/gui/icons/progressbar/down.png"), false);
		});
		ElementFactory.addSubElement(ProgressHandler.class, "LARROW", (tile, parameters) -> {
			if (parameters.size() != 2) throw new IllegalArgumentException("Wrong Number of parameters for the Left Arrow subbuilder:" + parameters.toString());
			return new ElementHorizontalProgressBar(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)), GuiHelper.makeStorehouseLocation("textures/gui/icons/progressbar/left.png"), false);
		});
		ElementFactory.addSubElement(ProgressHandler.class, "J(", (tile, parameters) -> {
			int splitter = parameters.indexOf(")J(");
			if (splitter == -1) throw new IllegalArgumentException("No Splitter for join progress bar: " + parameters.toString());
			int splitterPos = 0;
			List<String> parameterCopy = new ArrayList<>(parameters);
			while (parameterCopy.indexOf("J(") != -1)
			{
				if (parameterCopy.indexOf("J(") > splitter) break;
				parameterCopy.remove(splitter);
				parameterCopy.remove(parameterCopy.indexOf("J("));
				splitterPos += 2;
				splitter = parameterCopy.indexOf(")J(");
				if (splitter == -1) throw new IllegalArgumentException("No Splitter for join progress bar after other Joins: " + parameters.toString());
			}
			splitter += splitterPos;
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
			int splitterPos = 0;
			List<String> parameterCopy = new ArrayList<>(parameters);
			while (parameterCopy.indexOf("D(") != -1)
			{
				if (parameterCopy.indexOf("D(") > splitter) break;
				parameterCopy.remove(splitter);
				parameterCopy.remove(parameterCopy.indexOf("D("));
				splitterPos += 2;
				splitter = parameterCopy.indexOf(")D(");
				if (splitter == -1) throw new IllegalArgumentException("No Splitter for double progress bar after other Doubles: " + parameters.toString());
			}
			splitter += splitterPos;
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
			if (parameters.size() == 2) icon = GuiHelper.makeStorehouseLocation("textures/gui/icons/energybar.png");
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
		ElementFactory.addElementBuilder("ELEMENT_MODE", (tile, parameters) -> {
			if (parameters.size() < 1) throw new IllegalArgumentException("Wrong Number of paramenters for the mode builder");
			Supplier<Integer> mode = ElementFactory.getFunction(parameters.remove(0), tile);
			List<List<String>> seperateParams = ElementFactory.seperateListByItem(parameters, "E");
			List<ElementBase> elements = seperateParams.stream().map( (param) -> ElementFactory.getElement(param.remove(0)).apply(tile, param)).collect(Collectors.toList());
			ElementBase modeElement = new ElementMode(mode, elements.toArray(new ElementBase[0]));
			return modeElement;
		});
		ElementFactory.addElementBuilder("FLUID_TANK", (tile, parameters) -> {
			if (parameters.size() < 2 || parameters.size() > 4) throw new IllegalArgumentException("Wrong Number of parameters for the Fluid Tank builder:" + parameters.toString());
			IFluidHandler tank = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null, ModuleContext.GUI);
			int tankNo = 0;
			ResourceLocation tankImage = GuiHelper.makeStorehouseLocation("textures/gui/icons/fluidtank.png");
			if (parameters.size() == 3)
			{
				if (GeneralUtils.isInteger(parameters.get(2))) tankNo = Integer.parseInt(parameters.get(2));
				else tankImage = new ResourceLocation(parameters.get(2));
			}
			else if (parameters.size() == 4)
			{
				tankNo = Integer.parseInt(parameters.get(2));
				tankImage = new ResourceLocation(parameters.get(3));
			}
			return new ElementFluidTank(tankImage, Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)), tank, tankNo);
		});
		ElementFactory.addElementBuilder("ELEMENT_BUTTON", (tile, parameters) -> {
			if (parameters.size() < 14 || (parameters.size() - 12) % 2 != 0) throw new IllegalArgumentException("Wrong Number of parameters for the ElementButton builder:" + parameters.toString());
			int x = Integer.parseInt(parameters.get(0)), y = Integer.parseInt(parameters.get(1)), width = Integer.parseInt(parameters.get(2)), height = Integer.parseInt(parameters.get(3));
			int innerX = Integer.parseInt(parameters.get(6)), innerY = Integer.parseInt(parameters.get(7)), innerWidth = Integer.parseInt(parameters.get(8)),
					innerHeight = Integer.parseInt(parameters.get(9));
			Supplier<Integer> modeSupplier = ElementFactory.getFunction(parameters.get(10), tile);
			Consumer<Integer> consumer = ElementFactory.getConsumer(parameters.get(11), tile);
			Runnable onPress = () -> consumer.accept(0);
			ResourceLocation[] iconLocations = new ResourceLocation[(parameters.size() - 12) / 2];
			String[] tooltips = new String[(parameters.size() - 12) / 2];
			for (int i = 12; i < parameters.size(); i += 2)
			{
				iconLocations[(i - 12) / 2] = new ResourceLocation(parameters.get(i));
				tooltips[(i - 12) / 2] = parameters.get(i + 1);
			}
			return new ElementButton(x, y, width, height, new ResourceLocation(parameters.get(4)), parameters.get(5), innerX, innerY, innerWidth, innerHeight, iconLocations, tooltips, modeSupplier, onPress);
		});
	}
}
