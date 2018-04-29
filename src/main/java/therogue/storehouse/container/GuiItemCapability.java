
package therogue.storehouse.container;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.items.ItemStackHandler;
import therogue.storehouse.inventory.IInventoryItemHandler;

public class GuiItemCapability {
	
	@CapabilityInject (IInventoryItemHandler.class)
	public static Capability<IInventoryItemHandler> CAP = null;
	
	public static void register () {
		CapabilityManager.INSTANCE.register(IInventoryItemHandler.class, new Capability.IStorage<IInventoryItemHandler>() {
			
			@Override
			public NBTBase writeNBT (Capability<IInventoryItemHandler> capability, IInventoryItemHandler instance, EnumFacing side) {
				NBTTagList nbtTagList = new NBTTagList();
				int size = instance.getSlots();
				for (int i = 0; i < size; i++)
				{
					ItemStack stack = instance.getStackInSlot(i);
					if (!stack.isEmpty())
					{
						NBTTagCompound itemTag = new NBTTagCompound();
						itemTag.setInteger("Slot", i);
						stack.writeToNBT(itemTag);
						nbtTagList.appendTag(itemTag);
					}
				}
				return nbtTagList;
			}
			
			@Override
			public void readNBT (Capability<IInventoryItemHandler> capability, IInventoryItemHandler instance, EnumFacing side, NBTBase base) {
				NBTTagList tagList = (NBTTagList) base;
				for (int i = 0; i < tagList.tagCount(); i++)
				{
					NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
					int j = itemTags.getInteger("Slot");
					if (j >= 0 && j < instance.getSlots())
					{
						instance.swapStacks(j, new ItemStack(itemTags), false);
					}
				}
			}
		}, InventoryItemStackHandler::new);
	}
	
	public static class InventoryItemStackHandler extends ItemStackHandler implements IInventoryItemHandler {
		
		@Override
		public ItemStack swapStacks (int index, ItemStack toinsert, boolean simulate) {
			validateSlotIndex(index);
			ItemStack previous = stacks.get(index);
			if (ItemStack.areItemStacksEqual(previous, toinsert))
				return toinsert;
			stacks.set(index, toinsert);
			onContentsChanged(index);
			return previous;
		}
	}
}
