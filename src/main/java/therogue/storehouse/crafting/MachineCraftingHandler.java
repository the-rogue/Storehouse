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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import therogue.storehouse.crafting.IMachineRecipe.Result;
import therogue.storehouse.crafting.wrapper.IRecipeWrapper;
import therogue.storehouse.tile.ITileModule;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.util.LOG;

public class MachineCraftingHandler<T> {
	
	private static final Map<Class<?>, MachineCraftingHandler<?>> CRAFTING_HANDLERS = new HashMap<>();
	
	@SuppressWarnings ("unchecked")
	public static <U> MachineCraftingHandler<U> getHandler (Class<U> crafterClass) {
		if (!CRAFTING_HANDLERS.containsKey(crafterClass))
		{
			CRAFTING_HANDLERS.put(crafterClass, new MachineCraftingHandler<U>());
		}
		return (MachineCraftingHandler<U>) CRAFTING_HANDLERS.get(crafterClass);
	}
	
	public static <T> void register (Class<T> crafterClass, IMachineRecipe<T> recipe) {
		getHandler(crafterClass).register(recipe);
	}
	
	private final List<CraftingManager> CRAFTERS = new ArrayList<>();
	private List<IMachineRecipe<T>> RECIPES = Lists.newArrayList();
	
	private MachineCraftingHandler () {
	}
	
	public CraftingManager newCrafter (T attachedTile) {
		CraftingManager cm = new CraftingManager(attachedTile);
		CRAFTERS.add(cm);
		return cm;
	}
	
	public CraftingManager newNonTickingCrafter (T attachedTile) {
		CraftingManager cm = new CraftingManager(attachedTile);
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
	
	public interface ICraftingManager {
		
		public int getTimeElapsed ();
		
		public int getTotalCraftingTime ();
	}
	
	public static class CapabilityCrafter {
		
		@CapabilityInject (ICraftingManager.class)
		public static Capability<ICraftingManager> CraftingManager = null;
		
		public static void register () {
			CapabilityManager.INSTANCE.register(ICraftingManager.class, new IStorage<ICraftingManager>() {
				
				@Override
				public NBTBase writeNBT (Capability<ICraftingManager> capability, ICraftingManager instance, EnumFacing side) {
					return new NBTTagCompound();
				}
				
				@Override
				public void readNBT (Capability<ICraftingManager> capability, ICraftingManager instance, EnumFacing side, NBTBase nbt) {
				}
			}, () -> MachineCraftingHandler.getHandler(ICrafter.class).newNonTickingCrafter((ICrafter) null));
		}
	}
	
	public class CraftingManager implements ICraftingManager, ITileModule {
		
		private final T attachedTile;
		private IMachineRecipe<T> currentCrafting = null;
		public int totalCraftingTime = 0;
		public int craftingTime = 0;
		private boolean craftingLock = false;
		
		private CraftingManager (T attachedTile) {
			this.attachedTile = attachedTile;
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
				if (recipe.itemValidForRecipe(attachedTile, index, stack)) return true;
			}
			return false;
		}
		
		public void checkRecipes () {
			if (currentCrafting != null || craftingLock) return;
			resetCraftingStatus();
			for (IMachineRecipe<T> recipe : RECIPES)
			{
				if (recipe.matches(attachedTile))
				{
					currentCrafting = recipe;
					totalCraftingTime = recipe.timeTaken(attachedTile);
					craftingTime = recipe.timeTaken(attachedTile);
					if (currentCrafting.begin(attachedTile) != Result.RESET)
					{
						break;
					}
				}
			}
		}
		
		private void updateCraftingStatus () {
			if (currentCrafting != null)
			{
				Result res = currentCrafting.doTick(attachedTile);
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
			Result crafted = currentCrafting.end(attachedTile);
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
