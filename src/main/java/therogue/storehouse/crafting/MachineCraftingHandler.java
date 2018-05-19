/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import therogue.storehouse.GeneralUtils;
import therogue.storehouse.LOG;
import therogue.storehouse.crafting.IMachineRecipe.Result;
import therogue.storehouse.crafting.IRecipeInventory.IRecipeInventoryConverter;
import therogue.storehouse.crafting.wrapper.IRecipeWrapper;
import therogue.storehouse.energy.TileEnergyStorage;
import therogue.storehouse.tile.ITile;
import therogue.storehouse.tile.ITileModule;
import therogue.storehouse.tile.ModuleContext;

public class MachineCraftingHandler<T extends ITile> {
	
	private static final Map<Class<?>, MachineCraftingHandler<?>> CRAFTING_HANDLERS = new HashMap<>();
	private static final List<IRecipeInventoryConverter> CONVERTERS = new ArrayList<>();
	
	public static void registerInventoryConverter (IRecipeInventoryConverter converter) {
		CONVERTERS.add(converter);
	}
	
	static
	{
		CONVERTERS.add(ItemInventory.CONVERTER);
		CONVERTERS.add(FluidTankInventory.CONVERTER);
		CONVERTERS.add(EnergyInventory.CONVERTER);
	}
	
	@SuppressWarnings ("unchecked")
	public static <U extends ITile> MachineCraftingHandler<U> getHandler (Class<U> crafterClass) {
		if (!CRAFTING_HANDLERS.containsKey(crafterClass))
		{
			CRAFTING_HANDLERS.put(crafterClass, new MachineCraftingHandler<U>());
		}
		return (MachineCraftingHandler<U>) CRAFTING_HANDLERS.get(crafterClass);
	}
	
	public static <T extends ITile> void register (Class<T> crafterClass, IMachineRecipe<T> recipe) {
		getHandler(crafterClass).register(recipe);
	}
	
	private final List<CraftingManager> CRAFTERS = new ArrayList<>();
	private List<IMachineRecipe<T>> RECIPES = Lists.newArrayList();
	
	private MachineCraftingHandler () {
	}
	
	public CraftingManager newCrafter (T attachedTile, String inventoryString, String outputString) {
		CraftingManager cm = new CraftingManager(attachedTile, inventoryString, outputString);
		CRAFTERS.add(cm);
		return cm;
	}
	
	public CraftingManager newCrafter (T attachedTile, String inventoryString, String outputString, TileEnergyStorage energy) {
		CraftingManager cm = new CraftingManager(attachedTile, inventoryString, outputString, energy);
		CRAFTERS.add(cm);
		return cm;
	}
	
	public CraftingManager newCrafter (T attachedTile, String inventoryString, String outputString, Supplier<Result> canRun, Runnable doRunTick) {
		CraftingManager cm = new CraftingManager(attachedTile, inventoryString, outputString, canRun, doRunTick);
		CRAFTERS.add(cm);
		return cm;
	}
	
	public CraftingManager newNonTickingCrafter (T attachedTile, String inventoryString, String outputString) {
		CraftingManager cm = new CraftingManager(attachedTile, inventoryString, outputString);
		return cm;
	}
	
	public void register (IMachineRecipe<T> recipe) {
		if (!RECIPES.contains(recipe))
		{
			RECIPES.add(recipe);
		}
		else
		{
			LOG.warn("Tried to register a duplicate recipe: " + recipe);
		}
	}
	
	public static void tickCrafters (TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) return;
		for (Entry<Class<?>, MachineCraftingHandler<?>> craftinghandler : CRAFTING_HANDLERS.entrySet())
		{
			for (MachineCraftingHandler<?>.CraftingManager manager : craftinghandler.getValue().CRAFTERS)
			{
				manager.updateCraftingStatus();
			}
		}
	}
	
	public interface ICraftingManager<T> {
		
		public int getTimeElapsed ();
		
		public void doSpecifiedRunTick ();
		
		public Result canRun ();
		
		public T getAttachedTile ();
		
		public Set<Integer> getOrderMattersSlots ();
		
		public IRecipeInventory getCraftingInventory ();
		
		public IRecipeInventory getOutputInventory ();
		
		public int getTotalCraftingTime ();
	}
	
	public static class CapabilityCrafter {
		
		@CapabilityInject (ICraftingManager.class)
		public static Capability<ICraftingManager<?>> CraftingManager = null;
		
		@SuppressWarnings ("rawtypes")
		public static void register () {
			CapabilityManager.INSTANCE.register(ICraftingManager.class, new IStorage<ICraftingManager>() {
				
				@Override
				public NBTBase writeNBT (Capability<ICraftingManager> capability, ICraftingManager instance, EnumFacing side) {
					return new NBTTagCompound();
				}
				
				@Override
				public void readNBT (Capability<ICraftingManager> capability, ICraftingManager instance, EnumFacing side, NBTBase nbt) {
				}
			}, (Callable<ICraftingManager<?>>) () -> {
				return MachineCraftingHandler.getHandler(ITile.class).newNonTickingCrafter(null, "", "");
			});
		}
	}
	
	private static IRecipeInventory createInv (ITile tile, String s) {
		String original = s;
		Queue<String> elements = new LinkedList<>();
		while (true)
		{
			int pos = s.indexOf(" ");
			if (pos != -1)
			{
				elements.add(s.substring(0, pos));
				s = s.substring(pos + 1);
			}
			else
			{
				elements.add(s);
				break;
			}
		}
		Stack<IRecipeInventory> inventories = new Stack<>();
		while (elements.size() > 0)
		{
			Optional<IRecipeInventoryConverter> convert = CONVERTERS.stream().filter(conv -> conv.getString().equals(elements.peek())).findFirst();
			elements.poll();
			if (convert.isPresent())
			{
				int datas = convert.get().numOfDataItems();
				int[] data = new int[datas];
				for (int i = 0; i < datas; i++)
				{
					if (!GeneralUtils.isInteger(elements.peek())) throw new IllegalArgumentException("Inventory String with the wrong number of data items: " + original);
					data[i] = Integer.parseInt(elements.poll());
				}
				inventories.push(convert.get().getFromTile(tile, data));
			}
			// else throw new IllegalArgumentException("Inventory String with no converter match: " + original);
		}
		IRecipeInventory theInv = inventories.pop();
		while (!inventories.isEmpty())
		{
			theInv = new DoubleInventory(inventories.pop(), theInv);
		}
		return theInv;
	}
	
	public class CraftingManager implements ICraftingManager<T>, ITileModule {
		
		private final T attachedTile;
		private IMachineRecipe<T> currentCrafting = null;
		public int totalCraftingTime = 0;
		public int craftingTime = 0;
		private boolean craftingLock = false;
		private Supplier<Set<Integer>> orderMattersSlots = () -> Sets.newHashSet();
		private Supplier<IRecipeInventory> craftingInventory;
		private Supplier<IRecipeInventory> outputInventory;
		private final Supplier<Result> canRun;
		private final Runnable doRunTick;
		
		private CraftingManager (T attachedTile, String inventoryString, String outputString) {
			this(attachedTile, inventoryString, outputString, () -> Result.CONTINUE, () -> {
			});
		}
		
		private CraftingManager (T attachedTile, String inventoryString, String outputString, TileEnergyStorage energy) {
			this(attachedTile, inventoryString, outputString, () -> energy.canRun() ? Result.CONTINUE : Result.PAUSE, () -> energy.runTick());
		}
		
		private CraftingManager (T attachedTile, String inventoryString, String outputString, Supplier<Result> canRun, Runnable doRunTick) {
			this.attachedTile = attachedTile;
			this.craftingInventory = () -> createInv(attachedTile, inventoryString);
			this.outputInventory = () -> createInv(attachedTile, outputString);
			this.canRun = canRun;
			this.doRunTick = doRunTick;
		}
		
		public CraftingManager addOrderMattersSlots (Integer... slots) {
			Set<Integer> prevSlots = orderMattersSlots.get();
			prevSlots.addAll(Arrays.asList(slots));
			orderMattersSlots = () -> prevSlots;
			return this;
		}
		
		public CraftingManager setOrderMattersSlots (Supplier<Set<Integer>> slots) {
			this.orderMattersSlots = slots;
			return this;
		}
		
		public CraftingManager setDynamicCraftingInventory (Supplier<IRecipeInventory> craftingInv) {
			this.craftingInventory = craftingInv;
			return this;
		}
		
		public CraftingManager setDynamicOutputInventory (Supplier<IRecipeInventory> outputInv) {
			this.outputInventory = outputInv;
			return this;
		}
		
		private void resetCraftingStatus () {
			currentCrafting = null;
			totalCraftingTime = 0;
			craftingTime = 0;
			craftingLock = false;
		}
		
		public boolean checkItemValidForSlot (int index, IRecipeWrapper stack) {
			for (IMachineRecipe<T> recipe : RECIPES)
			{
				if (recipe.itemValidForRecipe(this, index, stack)) return true;
			}
			return false;
		}
		
		public void checkRecipes () {
			if (currentCrafting != null || craftingLock) return;
			resetCraftingStatus();
			for (IMachineRecipe<T> recipe : RECIPES)
			{
				if (recipe.matches(this))
				{
					currentCrafting = recipe;
					totalCraftingTime = recipe.timeTaken(this);
					craftingTime = recipe.timeTaken(this);
					if (currentCrafting.begin(this) != Result.RESET)
					{
						break;
					}
				}
			}
		}
		
		private void updateCraftingStatus () {
			if (currentCrafting != null)
			{
				Result res = currentCrafting.doTick(this);
				if (res == Result.CONTINUE)
				{
					if (craftingTime <= 1) craft();
					else--craftingTime;
				}
				else if (res == Result.RESET)
				{
					resetCraftingStatus();
				}
			}
		}
		
		public boolean craft () {
			craftingLock = true;
			Result crafted = currentCrafting.end(this);
			if (crafted != Result.PAUSE)
			{
				resetCraftingStatus();
				checkRecipes();
			}
			return crafted != Result.PAUSE;
		}
		
		// -----------------------------ITileModule------------------------
		@Override
		public void onOtherChange (Capability<?> changedCapability) {
			this.checkRecipes();
		}
		
		/**
		 * When the tile is removed from the world (i.e. invalidated)
		 */
		@Override
		public void onRemove () {
			CRAFTERS.remove(this);
		}
		
		@Override
		public int getTimeElapsed () {
			return totalCraftingTime - craftingTime;// Time elapsed
		}
		
		@Override
		public int getTotalCraftingTime () {
			return totalCraftingTime;// Total Time
		}
		
		@Override
		public Result canRun () {
			return canRun.get();
		}
		
		@Override
		public void doSpecifiedRunTick () {
			doRunTick.run();
		}
		
		@Override
		public Set<Integer> getOrderMattersSlots () {
			return orderMattersSlots.get();
		}
		
		@Override
		public IRecipeInventory getCraftingInventory () {
			return craftingInventory.get();
		}
		
		@Override
		public IRecipeInventory getOutputInventory () {
			return outputInventory.get();
		}
		
		@Override
		public T getAttachedTile () {
			return attachedTile;
		}
		
		/**
		 * @param capability the capability to test
		 * @param facing the direction to test it in
		 * @return whether the capability is applicable in this direction
		 */
		@Override
		public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
			return capability == CapabilityCrafter.CraftingManager;
		}
		
		@SuppressWarnings ("unchecked")
		@Override
		public <R> R getCapability (Capability<R> capability, EnumFacing facing, ModuleContext capacity) {
			return (R) this;
		}
		
		/**
		 * Writes the data of the inventory to nbt, to be loaded back later
		 * 
		 * @param nbt The NBTTagCompound to write to.
		 */
		@Override
		public NBTTagCompound writeModuleToNBT (NBTTagCompound nbt) {
			nbt.setInteger("maxCraftingTime", totalCraftingTime);
			nbt.setInteger("craftingTime", craftingTime);
			return nbt;
		}
		
		/**
		 * Reads Data back from an nbt tag, after it has been loaded back
		 * 
		 * @param nbt The nbt tag to read from.
		 */
		@Override
		public NBTTagCompound readModuleFromNBT (NBTTagCompound nbt) {
			totalCraftingTime = nbt.getInteger("maxCraftingTime");
			craftingTime = nbt.getInteger("craftingTime");
			return nbt;
		}
	}
}
