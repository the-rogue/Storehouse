
package therogue.storehouse.container;

import net.minecraft.item.ItemStack;
import therogue.storehouse.inventory.IInventoryItemHandler;

public class InventorySlot implements IInventorySlot {
	
	private final IInventoryItemHandler inventory;
	protected int index;
	protected int xPos;
	protected int yPos;
	
	public InventorySlot (IInventoryItemHandler inventory, int index, int xPos, int yPos) {
		this.inventory = inventory;
		this.index = index;
		this.xPos = xPos;
		this.yPos = yPos;
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
		return index;
	}
	
	@Override
	public ItemStack getStack () {
		return inventory.getStackInSlot(index);
	}
	
	@Override
	public ItemStack insertItem (ItemStack stack, boolean simulate) {
		return inventory.insertItem(index, stack, simulate);
	}
	
	@Override
	public ItemStack extractItem (int amount, boolean simulate) {
		return inventory.extractItem(index, amount, simulate);
	}
	
	@Override
	public int getSlotLimit (ItemStack testStack) {
		return inventory.getSlotLimit(index);
	}
	
	@Override
	public ItemStack swapStacks (ItemStack toinsert, boolean simulate) {
		return inventory.swapStacks(index, toinsert, simulate);
	}
}
