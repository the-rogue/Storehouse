
package therogue.storehouse.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotAdapter extends Slot {
	
	private static IInventory emptyInventory = new InventoryBasic("[Null]", true, 0);
	private final IInventorySlot slot;
	
	public SlotAdapter (IInventorySlot slot) {
		super(emptyInventory, slot.getIndex(), slot.getXPosition(), slot.getYPosition());
		this.slot = slot;
	}
	
	/**
	 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
	 */
	@Override
	public boolean isItemValid (@Nonnull ItemStack stack) {
		if (stack.isEmpty())
			return false;
		ItemStack remainder = slot.insertItem(stack.copy(), true);
		return remainder.isEmpty() || remainder.getCount() < stack.getCount();
	}
	
	/**
	 * Helper fnct to get the stack in the slot.
	 */
	@Override
	@Nonnull
	public ItemStack getStack () {
		return slot.getStack().copy();
	}
	
	// Override if your IItemHandler does not implement IItemHandlerModifiable
	/**
	 * Helper method to put a stack in the slot.
	 */
	@Override
	public void putStack (@Nonnull ItemStack stack) {
		slot.extractItem(-1, false);
		slot.insertItem(stack, false);
	}
	
	/**
	 * if par2 has more items than par1, onCrafting(item,countIncrease) is called
	 */
	@Override
	public void onSlotChange (@Nonnull ItemStack p_75220_1_, @Nonnull ItemStack p_75220_2_) {
	}
	
	/**
	 * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
	 * of armor slots)
	 */
	@Override
	public int getSlotStackLimit () {
		return slot.getSlotLimit(ItemStack.EMPTY);
	}
	
	@Override
	public int getItemStackLimit (@Nonnull ItemStack stack) {
		return slot.getSlotLimit(stack);
	}
	
	/**
	 * Return whether this slot's stack can be taken from this slot.
	 */
	@Override
	public boolean canTakeStack (EntityPlayer playerIn) {
		return !slot.extractItem(-1, true).isEmpty();
	}
	
	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
	 * stack.
	 */
	@Override
	@Nonnull
	public ItemStack decrStackSize (int amount) {
		return slot.extractItem(amount, false);
	}
	
	@Override
	public boolean isSameInventory (Slot other) {
		return other instanceof SlotAdapter && ((SlotAdapter) other).slot == this.slot;
	}
}
