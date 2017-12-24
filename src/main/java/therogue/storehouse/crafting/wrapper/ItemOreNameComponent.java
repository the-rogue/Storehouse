
package therogue.storehouse.crafting.wrapper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemOreNameComponent implements IRecipeComponent {
	
	protected String oreName;
	protected int amount;
	
	public ItemOreNameComponent (String oreName, int amount) {
		this.oreName = oreName;
		this.amount = amount;
	}
	
	@Override
	public boolean isUnUsed () {
		return false;
	}
	
	@Override
	public boolean matches (IRecipeWrapper component) {
		if (component.isUnUsed() || !(component instanceof ItemStackWrapper)) return false;
		ItemStack stack = ((ItemStackWrapper) component).getStack();
		int[] IDs = OreDictionary.getOreIDs(stack);
		for (int id : IDs)
		{
			String name = OreDictionary.getOreName(id);
			if (name.contains(oreName)) return true;
		}
		return false;
	}
	
	@Override
	public int getSize () {
		return amount;
	}
	
	@Override
	public IRecipeWrapper getWrapper () {
		throw new UnsupportedOperationException("An ItemOreNameComponent should never be used for an output");
	}
	
	@Override
	public IRecipeComponent copy () {
		return new ItemOreNameComponent(oreName, amount);
	}
	
	@Override
	public IRecipeWrapper getResidue () {
		return IRecipeWrapper.NOTHING;
	}
}
