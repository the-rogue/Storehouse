
package therogue.storehouse.crafting;

import therogue.storehouse.crafting.MachineCraftingHandler.ICraftingManager;
import therogue.storehouse.crafting.wrapper.IRecipeWrapper;

public interface IMachineRecipe<T> {
	
	public int timeTaken (ICraftingManager<T> machine);
	
	public boolean itemValidForRecipe (ICraftingManager<T> tile, int index, IRecipeWrapper stack);
	
	public boolean matches (ICraftingManager<T> machine);
	
	public default Result end (ICraftingManager<T> machine) {
		return Result.CONTINUE;
	}
	
	public default Result begin (ICraftingManager<T> machine) {
		return Result.CONTINUE;
	}
	
	public default Result doTick (ICraftingManager<T> machine) {
		return Result.CONTINUE;
	}
	
	public static enum Result {
		CONTINUE,
		PAUSE,
		RESET;
	}
}
