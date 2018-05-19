
package therogue.storehouse.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import therogue.storehouse.inventory.ItemStackUtils;

public class LegacySlot implements IInventorySlot {
	
	/** The index of the slot in the inventory. */
	private final int slotIndex;
	/** The inventory we want to extract a slot from. */
	public final IInventory inventory;
	/** display position of the inventory slot on the screen x axis */
	public int xPos;
	/** display position of the inventory slot on the screen y axis */
	public int yPos;
	
	public LegacySlot (IInventory inventoryIn, int index, int xPosition, int yPosition) {
		this.inventory = inventoryIn;
		this.slotIndex = index;
		this.xPos = xPosition;
		this.yPos = yPosition;
	}
	
	@Override
	public int getXPosition () {
		return xPos;
	}
	
	@Override
	public int getYPosition () {
		return yPos;
	}
	
	@Override
	public int getIndex () {
		return slotIndex;
	}
	
	@Override
	public ItemStack getStack () {
		return inventory.getStackInSlot(slotIndex).copy();
	}
	
	@Override
	public ItemStack insertItem (ItemStack stack, boolean simulate) {
		ItemStack inInv = inventory.getStackInSlot(slotIndex);
		ItemStack toSet = ItemStackUtils.mergeStacks(inventory.getInventoryStackLimit(), true, inInv.copy(), stack);
		if (!simulate) inventory.setInventorySlotContents(slotIndex, toSet);
		return stack;
	}
	
	@Override
	public ItemStack extractItem (int amount, boolean simulate) {
		if (amount == -1)
		{
			amount = getStack().getCount();
		}
		if (simulate)
		{
			ItemStack inventoryStack = inventory.getStackInSlot(slotIndex);
			inventoryStack.setCount(amount);
			return inventoryStack;
		}
		return inventory.decrStackSize(slotIndex, amount);
	}
	
	@Override
	public int getSlotLimit (ItemStack testStack) {
		return inventory.getInventoryStackLimit();
	}
	
	@Override
	public ItemStack swapStacks (ItemStack toinsert, boolean simulate) {
		ItemStack previous = inventory.getStackInSlot(slotIndex);
		if (!simulate)
		{
			inventory.setInventorySlotContents(slotIndex, toinsert);
		}
		return previous;
	}
}
