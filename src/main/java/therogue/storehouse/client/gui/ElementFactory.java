
package therogue.storehouse.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import therogue.storehouse.LOG;
import therogue.storehouse.client.gui.element.ElementBase;
import therogue.storehouse.crafting.MachineCraftingHandler.CapabilityCrafter;
import therogue.storehouse.tile.ClientButton.CapabilityButton;
import therogue.storehouse.tile.ITile;
import therogue.storehouse.tile.TileData.CapabilityDataHandler;

public class ElementFactory {
	
	private static final Map<String, BiFunction<ITile, List<String>, ? extends ElementBase>> elementCodes = new HashMap<>();
	public static final Map<String, Capability<?>> capabilityMapper = new HashMap<>();
	private static final Map<String, BiFunction<ITile, Integer, Supplier<Integer>>> supplierMapper = new HashMap<>();
	private static final Map<String, BiFunction<ITile, Integer, Consumer<Integer>>> consumerMapper = new HashMap<>();
	private static final Map<Class<? extends ElementBase>, Map<String, BiFunction<ITile, List<String>, ?>>> subElementCodes = new HashMap<>();
	
	public static void addFunction (String code, BiFunction<ITile, Integer, Supplier<Integer>> func) {
		if (code == null || func == null) throw new NullPointerException(code + func);
		supplierMapper.put(code, func);
	}
	
	public static void addConsumer (String code, BiFunction<ITile, Integer, Consumer<Integer>> func) {
		if (code == null || func == null) throw new NullPointerException(code + func);
		consumerMapper.put(code, func);
	}
	
	public static void addElementBuilder (String code, BiFunction<ITile, List<String>, ? extends ElementBase> func) {
		if (code == null || func == null) throw new NullPointerException(code + func);
		elementCodes.put(code, func);
	}
	
	public static BiFunction<ITile, List<String>, ? extends ElementBase> getElement (String code) {
		return elementCodes.get(code);
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
					BiFunction<ITile, List<String>, ? extends ElementBase> func = elementCodes.get(code);
					elementList.add(func.apply(moduleSupplier, seperateByDelimeter(element, " ")));
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
	
	public static List<String> seperateByDelimeter (String string, String delimeter) {
		List<String> elements = new ArrayList<>();
		while (true)
		{
			int quotePos = string.indexOf("\"");
			int pos = string.indexOf(delimeter);
			if (quotePos != -1 && quotePos < pos)
			{
				String afterQuote = string.substring(quotePos + 1);
				int nextQuote = afterQuote.indexOf("\"");
				if (nextQuote != -1)
				{
					String afterQuote2 = afterQuote.substring(nextQuote + 1);
					pos = afterQuote2.indexOf(delimeter);
					if (pos != -1)
					{
						pos += quotePos + nextQuote + 2;
					}
				}
			}
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
	
	public static List<List<String>> seperateListByItem (List<String> stringList, String item) {
		List<List<String>> seperation = new ArrayList<>();
		int prevIndex = 0;
		while (true)
		{
			int minIndex = stringList.indexOf(item);
			if (minIndex == -1) break;
			seperation.add(new ArrayList<>(stringList.subList(prevIndex, minIndex)));
			stringList = stringList.subList(minIndex + 1, stringList.size());
		}
		seperation.add(new ArrayList<>(stringList));
		return seperation;
	}
	
	public static List<List<String>> splitListAtItem (List<String> stringList, String... items) {
		List<List<String>> seperation = new ArrayList<>();
		int prevIndex = 0;
		while (true)
		{
			int minIndex = -1;
			for (String item : items)
			{
				int index = stringList.indexOf(item);
				if (index != -1 && index < minIndex) minIndex = index;
			}
			if (minIndex == -1) break;
			seperation.add(new ArrayList<>(stringList.subList(prevIndex, minIndex)));
			stringList = stringList.subList(minIndex, stringList.size());
		}
		seperation.add(new ArrayList<>(stringList));
		return seperation;
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
			return supplierMapper.get(initial).apply(tile, num);
		}
		catch (NullPointerException e)
		{
			LOG.warn(String.format("Caught an exception getting a function, initial: %s, exception: %s", initial, e.getMessage()));
			throw e;
		}
	}
	
	public static Consumer<Integer> getConsumer (String initial, ITile tile) {
		int num = 0;
		Matcher m = Pattern.compile("\\d+").matcher(initial);
		if (m.find())
		{
			num = Integer.parseInt(m.group());
		}
		initial = initial.replaceAll("\\d+", "");
		try
		{
			return consumerMapper.get(initial).apply(tile, num);
		}
		catch (NullPointerException e)
		{
			LOG.warn(String.format("Caught an exception getting a consumer, initial: %s, exception: %s", initial, e.getMessage()));
			throw e;
		}
	}
}
