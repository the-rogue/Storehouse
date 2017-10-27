
package therogue.storehouse.tile.machine;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import therogue.storehouse.crafting.ICrafter;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.crafting.MachineCraftingHandler.CraftingManager;
import therogue.storehouse.crafting.inventory.IRecipeInventory;
import therogue.storehouse.crafting.inventory.RangedItemInventory;
import therogue.storehouse.crafting.wrapper.ItemStackWrapper;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.init.ModItems;
import therogue.storehouse.inventory.IInventoryCapability;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.network.GuiUpdateTEPacket;
import therogue.storehouse.network.StorehousePacketHandler;
import therogue.storehouse.tile.StorehouseBaseTileEntity;
import therogue.storehouse.util.GeneralUtils;
import therogue.storehouse.util.ItemStackUtils;

public class TileForge extends StorehouseBaseTileEntity implements IInventoryCapability, ICrafter {
	
	protected InventoryManager inventory;
	private CraftingManager theCrafter = MachineCraftingHandler.getHandler(this.getClass()).newNonTickingCrafter(this);
	
	public TileForge () {
		super(ModBlocks.forge);
		inventory = new InventoryManager(this, 2, new Integer[0], new Integer[] { 1 }, new Integer[0]) {
			
			protected boolean isItemValidForSlotChecks (int index, ItemStack stack) {
				if (!this.getStackInSlot(0).isEmpty()) return false;
				return theCrafter.checkItemValidForSlot(index - 1, new ItemStackWrapper(stack));
			}
			
			@Override
			public int getSlotLimit (int slot) {
				return 1;
			}
		};
	}
	
	// -------------------------Tile Specific Utility Methods-------------------------------------------
	public boolean onBlockActivated (EntityPlayer player, EnumHand hand) {
		IItemHandlerModifiable containerCapability = getContainerCapability();
		if (!containerCapability.getStackInSlot(1).isEmpty() && player.getHeldItem(hand).getItem() == ModItems.hammer && !(player instanceof FakePlayer))
		{
			if (theCrafter.craft())
			{
				ItemStack heldStack = player.getHeldItem(hand);
				heldStack.setItemDamage(heldStack.getItemDamage() + 1);
			}
		}
		if (containerCapability.getStackInSlot(1).isEmpty())
		{
			ItemStack newStack = containerCapability.insertItem(1, player.getHeldItem(hand), false);
			if (!ItemStack.areItemStacksEqual(newStack, player.getHeldItem(hand)))
			{
				player.setHeldItem(hand, newStack);
			}
		}
		if (!containerCapability.getStackInSlot(0).isEmpty())
		{
			ItemStack machineStack = containerCapability.getStackInSlot(0);
			ItemStack newStack1 = ItemStackUtils.mergeStacks(64, false, player.getHeldItem(hand), machineStack);
			if (!ItemStack.areItemStacksEqual(newStack1, player.getHeldItem(hand)))
			{
				player.setHeldItem(hand, ItemStackUtils.mergeStacks(64, true, player.getHeldItem(hand), machineStack));
				containerCapability.setStackInSlot(0, machineStack);
			}
		}
		return true;
	}
	
	// -------------------------ICrafter Methods-----------------------------------
	@Override
	public Set<Integer> getOrderMattersSlots () {
		return new HashSet<Integer>();
	}
	
	@Override
	public IRecipeInventory getCraftingInventory () {
		return new RangedItemInventory(getInventory(), 1, 2);
	}
	
	@Override
	public IRecipeInventory getOutputInventory () {
		return new RangedItemInventory(getInventory(), 0, 1);
	}
	
	@Override
	public boolean isRunning () {
		return true;
	}
	
	@Override
	public void doRunTick () {
	}
	
	// -------------------------Inventory Methods-----------------------------------
	@Override
	public IItemHandlerModifiable getInventory () {
		if (inventory == null) { throw new NullPointerException("inventory is null for machine: " + getName()); }
		return inventory;
	}
	
	@Override
	public IItemHandlerModifiable getContainerCapability () {
		if (inventory == null) { throw new NullPointerException("inventory is null for machine: " + getName()); }
		return inventory.containerCapability;
	}
	
	@Override
	public void onInventoryChange () {
		this.markDirty();
		theCrafter.checkRecipes();
		if (GeneralUtils.isServerSide(world))
		{
			StorehousePacketHandler.INSTANCE.sendToAll(this.getGUIPacket());
		}
	}
	
	// -------------------------Standard TE methods-----------------------------------
	@Override
	public GuiUpdateTEPacket getGUIPacket () {
		GuiUpdateTEPacket packet = super.getGUIPacket();
		inventory.writeToNBT(packet.getNbt());
		return packet;
	}
	
	@Override
	public void processGUIPacket (GuiUpdateTEPacket packet) {
		super.processGUIPacket(packet);
		inventory.readFromNBT(packet.getNbt());
	}
	
	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		inventory.writeToNBT(nbt);
		return nbt;
	}
	
	@Override
	public void readFromNBT (NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		inventory.readFromNBT(nbt);
	}
	
	@Override
	public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}
	
	@SuppressWarnings ("unchecked")
	@Override
	public <T> T getCapability (Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) inventory;
		return super.getCapability(capability, facing);
	}
}
