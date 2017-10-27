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

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import com.google.common.collect.Lists;

import therogue.storehouse.crafting.wrapper.IRecipeComponent;

public class MachineRecipe {
	
	public static final Predicate<ICrafter> ALWAYSMODE = new Predicate<ICrafter>() {
		
		@Override
		public boolean test (ICrafter crafter) {
			return true;
		}
	};
	public final Predicate<ICrafter> correctMode;
	public final int timeTaken;
	private final List<IRecipeComponent> craftingOutputs;
	private final List<IRecipeComponent> craftingInputs;
	
	public MachineRecipe (int timeTaken, IRecipeComponent craftingOutputs, IRecipeComponent... craftingInputs) {
		this(ALWAYSMODE, timeTaken, Lists.newArrayList(craftingOutputs), craftingInputs);
	}
	
	public MachineRecipe (Predicate<ICrafter> correctMode, int timeTaken, IRecipeComponent craftingOutputs, IRecipeComponent... craftingInputs) {
		this(correctMode, timeTaken, Lists.newArrayList(craftingOutputs), craftingInputs);
	}
	
	public MachineRecipe (int timeTaken, List<IRecipeComponent> craftingOutputs, IRecipeComponent... craftingInputs) {
		this(ALWAYSMODE, timeTaken, Lists.newArrayList(craftingOutputs), craftingInputs);
	}
	
	// All ordered slots must have a placeholder of IRecipeComponent.Empty if they are unused
	public MachineRecipe (Predicate<ICrafter> correctMode, int timeTaken, List<IRecipeComponent> craftingOutputs, IRecipeComponent... craftingInputs) {
		this.correctMode = correctMode;
		this.timeTaken = timeTaken;
		this.craftingOutputs = craftingOutputs;
		this.craftingInputs = Arrays.asList(craftingInputs);
	}
	
	public IRecipeComponent getInputComponent (int index) {
		return craftingInputs.get(index);
	}
	
	public int getAmountOfInputs () {
		return craftingInputs.size();
	}
	
	public IRecipeComponent getOutputComponent (int index) {
		return craftingOutputs.get(index);
	}
	
	public int getAmountOfOutputs () {
		return craftingOutputs.size();
	}
	
	@Override
	public String toString () {
		return "Inputs: " + craftingInputs.toString() + "\nOutputs: " + craftingOutputs.toString();
	}
}
