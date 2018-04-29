
package therogue.storehouse.container;

import javax.annotation.Nonnull;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.StorehouseBaseTileEntity;

public class SlotItemHandlerFix extends SlotItemHandler {
	
	private final StorehouseBaseTileEntity te;
	protected static IInventory emptyInventory = new InventoryBasic("[Null]", true, 0);
	
	public SlotItemHandlerFix (StorehouseBaseTileEntity inventory, int index, int xPosition, int yPosition) {
		super(inventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.GUI), index, xPosition, yPosition);
		this.te = inventory;
	}
	
	// Override if your IItemHandler does not implement IItemHandlerModifiable
	/**
	 * Helper method to put a stack in the slot.
	 */
	@Override
	public void putStack (@Nonnull ItemStack stack) {
		IItemHandler internalHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL);
		internalHandler.extractItem(this.getSlotIndex(), -1, false);
		internalHandler.insertItem(this.getSlotIndex(), stack, false);
		this.onSlotChanged();
	}
}
