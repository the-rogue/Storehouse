
package therogue.storehouse.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import therogue.storehouse.LOG;
import therogue.storehouse.client.gui.element.ElementBase;
import therogue.storehouse.container.GuiItemCapability;
import therogue.storehouse.crafting.MachineCraftingHandler.CapabilityCrafter;
import therogue.storehouse.tile.ClientButton.CapabilityButton;
import therogue.storehouse.tile.ITile;
import therogue.storehouse.tile.TileData.CapabilityDataHandler;

public class ElementFactory {
	
	private static final Map<String, BiFunction<ITile, List<String>, ? extends ElementBase>> elementCodes = new HashMap<>();
	public static final Map<String, Capability<?>> capabilityMapper = new HashMap<>();
	private static final Map<String, BiFunction<ITile, Integer, Supplier<Integer>>> functionMapper = new HashMap<>();
	private static final Map<Class<? extends ElementBase>, Map<String, BiFunction<ITile, List<String>, ?>>> subElementCodes = new HashMap<>();
	
	public static void addFunction (String code, BiFunction<ITile, Integer, Supplier<Integer>> func) {
		if (code == null || func == null) throw new NullPointerException(code + func);
		functionMapper.put(code, func);
	}
	
	public static void addElementBuilder (String code, BiFunction<ITile, List<String>, ? extends ElementBase> func) {
		if (code == null || func == null) throw new NullPointerException(code + func);
		elementCodes.put(code, func);
	}
	
	public static void addSubElement (Class<? extends ElementBase> elementClass, String code, BiFunction<ITile, List<String>, ?> func) {
		if (!subElementCodes.containsKey(elementClass))
		{
			subElementCodes.put(elementClass, new HashMap<>());
		}
		subElementCodes.get(elementClass).put(code, func);
	}
	
	public static Map<String, BiFunction<ITile, List<String>, ?>> getSubElements (Class<? extends ElementBase> elementClass) {
		if (subElementCodes.containsKey(elementClass)) return subElementCodes.get(elementClass);
		return new HashMap<>();
	}
	
	static
	{
		capabilityMapper.put("ITM", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
		capabilityMapper.put("FLD", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
		capabilityMapper.put("ENG", CapabilityEnergy.ENERGY);
		capabilityMapper.put("GITM", GuiItemCapability.CAP);
		capabilityMapper.put("CRFT", CapabilityCrafter.CraftingManager);
		capabilityMapper.put("BTTN", CapabilityButton.BUTTON);
		capabilityMapper.put("DATA", CapabilityDataHandler.DATAHANDLER);
	}
	
	public static void makeElements (GuiBase gui, List<ElementBase> elementList, ITile moduleSupplier, String elementString, Object... stringParams) {
		elementString = String.format(elementString, stringParams);
		String original = elementString;
		for (String element : seperateByDelimeter(elementString, ","))
		{
			element = element.trim();
			try
			{
				String code;
				int pos = element.indexOf(" ");
				if (pos != -1)
				{
					code = element.substring(0, pos);
					element = element.substring(pos + 1);
					elementList.add(elementCodes.get(code).apply(moduleSupplier, seperateByDelimeter(element, " ")));
				}
				else
				{
					elementList.add(elementCodes.get(element).apply(moduleSupplier, new ArrayList<>()));
				}
			}
			catch (NullPointerException e)
			{
				LOG.warn(String.format("NullPointerException for element %s in string %s, exception: %s", element, original, e.getMessage()));
			}
			catch (IllegalArgumentException e)
			{
				LOG.warn(String.format("Incorrect element string for element %s, string: %s, threw an IllegalArgumentException: %s", element, original, e.getMessage()));
			}
		}
		elementList.forEach(element -> element.setGUI(gui));
	}
	
	private static List<String> seperateByDelimeter (String string, String delimeter) {
		List<String> elements = new ArrayList<>();
		while (true)
		{
			int pos = string.indexOf(delimeter);
			if (pos != -1)
			{
				elements.add(string.substring(0, pos));
				string = string.substring(pos + 1);
			}
			else
			{
				elements.add(string);
				break;
			}
		}
		return elements;
	}
	
	public static Supplier<Integer> getFunction (String initial, ITile tile) {
		int num = 0;
		Matcher m = Pattern.compile("\\d+").matcher(initial);
		if (m.find())
		{
			num = Integer.parseInt(m.group());
		}
		initial = initial.replaceAll("\\d+", "");
		try
		{
			return functionMapper.get(initial).apply(tile, num);
		}
		catch (NullPointerException e)
		{
			LOG.warn(String.format("Caught an exception getting a function, initial: %s, exception: %s", initial, e.getMessage()));
			throw e;
		}
	}
}
