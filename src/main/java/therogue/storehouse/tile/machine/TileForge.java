
package therogue.storehouse.tile.machine;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import therogue.storehouse.crafting.ICrafter;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.crafting.inventory.IRecipeInventory;
import therogue.storehouse.crafting.inventory.RangedItemInventory;
import therogue.storehouse.crafting.wrapper.ItemStackWrapper;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.init.ModItems;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.network.StorehousePacketHandler;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.StorehouseBaseTileEntity;
import therogue.storehouse.util.GeneralUtils;
import therogue.storehouse.util.ItemStackUtils;

public class TileForge extends StorehouseBaseTileEntity implements ICrafter {
	
	protected InventoryManager inventory;
	private MachineCraftingHandler<TileForge>.CraftingManager theCrafter = MachineCraftingHandler.getHandler(TileForge.class).newNonTickingCrafter(this);
	
	public TileForge () {
		super(ModBlocks.forge);
		inventory = new InventoryManager(this, 2, new Integer[0], new Integer[] { 1 }, new Integer[0]) {
			
			protected boolean isItemValidForSlotChecks (int index, ItemStack stack) {
				if (!this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL).getStackInSlot(0).isEmpty())
					return false;
				return theCrafter.checkItemValidForSlot(index - 1, new ItemStackWrapper(stack));
			}
			
			@Override
			public int getSlotLimit (int slot) {
				return 1;
			}
		};
		modules.add(inventory);
		modules.add(theCrafter);
	}
	
	// -------------------------Tile Specific Utility Methods-------------------------------------------
	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX,
			float hitY, float hitZ) {
		if (super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ)) return true;
		IItemHandler inventory = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL);
		if (!inventory.getStackInSlot(1).isEmpty() && player.getHeldItem(hand).getItem() == ModItems.hammer && !(player instanceof FakePlayer))
		{
			if (theCrafter.craft())
			{
				ItemStack heldStack = player.getHeldItem(hand);
				heldStack.setItemDamage(heldStack.getItemDamage() + 1);
			}
		}
		if (inventory.getStackInSlot(1).isEmpty())
		{
			ItemStack newStack = inventory.insertItem(1, player.getHeldItem(hand), false);
			if (!ItemStack.areItemStacksEqual(newStack, player.getHeldItem(hand)))
			{
				player.setHeldItem(hand, newStack);
			}
		}
		if (!inventory.getStackInSlot(0).isEmpty())
		{
			ItemStack machineStack = inventory.extractItem(0, -1, false);
			ItemStack newStack1 = ItemStackUtils.mergeStacks(64, false, player.getHeldItem(hand), machineStack);
			if (!ItemStack.areItemStacksEqual(newStack1, player.getHeldItem(hand)))
			{
				player.setHeldItem(hand, ItemStackUtils.mergeStacks(64, true, player.getHeldItem(hand), machineStack));
				inventory.insertItem(0, machineStack, false);
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
		return new RangedItemInventory(this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL), 1, 2);
	}
	
	@Override
	public IRecipeInventory getOutputInventory () {
		return new RangedItemInventory(this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL), 0, 1);
	}
	
	// -------------------------Inventory Methods-----------------------------------
	@Override
	public void notifyChange (Capability<?> changedCapability) {
		super.notifyChange(changedCapability);
		// this.markDirty();
		if (changedCapability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && world != null && GeneralUtils.isServerSide(world))
		{
			StorehousePacketHandler.INSTANCE.sendToAll(this.getCGUIPacket());
		}
	}
}
