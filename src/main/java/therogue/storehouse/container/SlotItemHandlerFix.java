
package therogue.storehouse.container;

import net.minecraftforge.items.SlotItemHandler;
import therogue.storehouse.inventory.IInventoryCapability;

public class SlotItemHandlerFix extends SlotItemHandler {
	
	protected IInventoryCapability inventory;
	
	public SlotItemHandlerFix (IInventoryCapability inventory, int index, int xPosition, int yPosition) {
		super(inventory.getContainerCapability(), index, xPosition, yPosition);
		this.inventory = inventory;
	}
	
	/**
	 * Called when the stack in a Slot changes
	 */
	@Override
	public void onSlotChanged () {
		this.inventory.onInventoryChange();;
	}
}
