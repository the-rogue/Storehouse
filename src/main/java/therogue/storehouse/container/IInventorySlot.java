
package therogue.storehouse.container;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;

public interface IInventorySlot {
	
	/**
	 * Returns the X Coordinate of the top left corner of this slot
	 * 
	 * @return The X Coordinate
	 */
	int getXPosition ();
	
	/**
	 * Returns the Y Coordinate of the top left corner of this slot
	 * 
	 * @return The Y Coordinate
	 */
	int getYPosition ();
	
	/**
	 * Returns the index of this slot
	 *
	 * @return The index of this slot
	 **/
	int getIndex ();
	
	/**
	 * Returns the ItemStack in this slot.
	 *
	 * The result's stack size may be greater than the itemstacks max size.
	 *
	 * If the result is null, then the slot is empty.
	 * If the result is not null but the stack size is zero, then it represents
	 * an empty slot that will only accept* a specific itemstack.
	 *
	 * <p/>
	 * IMPORTANT: This ItemStack MUST NOT be modified. This method is not for
	 * altering an inventories contents. Any implementers who are able to detect
	 * modification through this method should throw an exception.
	 * <p/>
	 * SERIOUSLY: DO NOT MODIFY THE RETURNED ITEMSTACK
	 *
	 * @return ItemStack in this slot.
	 **/
	@Nonnull
	ItemStack getStack ();
	
	/**
	 * Inserts an ItemStack into the slot and return the remainder.
	 * The ItemStack should not be modified in this function!
	 * Note: This behaviour is subtly different from IFluidHandlers.fill()
	 *
	 * @param stack ItemStack to insert.
	 * @param simulate If true, the insertion is only simulated
	 * @return The remaining ItemStack that was not inserted (if the entire stack is accepted, then return ItemStack.EMPTY).
	 *         May be the same as the input ItemStack if unchanged, otherwise a new ItemStack.
	 **/
	@Nonnull
	ItemStack insertItem (@Nonnull ItemStack stack, boolean simulate);
	
	/**
	 * Extracts an ItemStack from the slot. The returned value must be ItemStack.EMPTY
	 * if nothing is extracted, otherwise it's stack size must not be greater than amount or the
	 * itemstacks getMaxStackSize().
	 *
	 * @param amount Amount to extract (may be greater than the current stacks max limit) or -1 to indicate the max amount
	 * @param simulate If true, the extraction is only simulated
	 * @return ItemStack extracted from the slot, must be ItemStack.EMPTY, if nothing can be extracted
	 **/
	@Nonnull
	ItemStack extractItem (int amount, boolean simulate);
	
	/**
	 * Retrieves the maximum stack size allowed to exist in the slot for the given ItemStack.
	 *
	 * @param The ItemStack the stacksize is required for or ItemStack.EMPTY for a generic value
	 * @return The maximum stack size allowed in the slot.
	 */
	int getSlotLimit (ItemStack testStack);
	
	/**
	 * Swaps the passed itemstack with the one in the slot, and returns the itemstack in the slot if successful,
	 * otherwise returns the passed itemstack
	 * 
	 * @param toinsert The ItemStack swap the one in the slot with
	 * @param simulate Whether the swap should be simulated
	 * @return the ItemStack not in the slot
	 */
	@Nonnull
	ItemStack swapStacks (ItemStack toinsert, boolean simulate);
	
	/**
	 * Return whether this slot's stack can be extracted from the slot by the player.
	 * 
	 * @return Whether the specified player can take items from this slot
	 */
	default boolean canTakeStack (EntityPlayer playerIn) {
		return true;
	}
	
	default void detectAndSendChanges (List<IContainerListener> listeners) {
	}
}
