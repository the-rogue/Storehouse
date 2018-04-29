package therogue.storehouse.inventory;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;


public interface IInventoryItemHandler extends IItemHandler {
	/**
	 * Swaps the passed itemstack with the one in the slot, and returns the itemstack in the slot if successful,
	 * otherwise returns the passed itemstack
	 * 
	 * @param index The index to insert the stack into
	 * @param toinsert The ItemStack swap the one in the slot with
	 * @param simulate Whether the swap should be simulated
	 * @return the ItemStack not in the slot
	 */
	@Nonnull
	ItemStack swapStacks(int index, ItemStack toinsert, boolean simulate);
}
