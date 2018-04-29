package therogue.storehouse.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class SlotNull extends Slot {
	
	private static IInventory emptyInventory = new InventoryBasic("[Null]", true, 0);
	public static final SlotNull NULL_SLOT = new SlotNull();
	
	private SlotNull () {
		super(emptyInventory, 0, 0, 0);
	}
	@Override
	public ItemStack getStack () {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean getHasStack () {
		return false;
	}

	@Override
	public void putStack (ItemStack stack) {
	}

	@Override
	public void onSlotChanged () {
	}

	@Override
	public int getSlotStackLimit () {
		return 0;
	}

	@Override
	public ItemStack decrStackSize (int amount) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canTakeStack (EntityPlayer playerIn) {
		return false;
	}

	@Override
	public boolean canBeHovered () {
		return false;
	}

	@Override
	public boolean isSameInventory (Slot other) {
		return other == this;
	}


}
