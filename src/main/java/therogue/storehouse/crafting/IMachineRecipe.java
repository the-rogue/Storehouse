
package therogue.storehouse.crafting;

import therogue.storehouse.crafting.wrapper.IRecipeWrapper;

public interface IMachineRecipe<T> {
	
	public int timeTaken (T machine);
	
	public boolean itemValidForRecipe (T tile, int index, IRecipeWrapper stack);
	
	public boolean matches (T machine);
	
	public default Result end (T machine) {
		return Result.CONTINUE;
	}
	
	public default Result begin (T machine) {
		return Result.CONTINUE;
	}
	
	public default Result doTick (T machine) {
		return Result.CONTINUE;
	}
	
	public static enum Result {
		CONTINUE,
		PAUSE,
		RESET;
	}
}
