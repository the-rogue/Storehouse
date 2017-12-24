
package therogue.storehouse.tile.machine;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import therogue.storehouse.Storehouse;
import therogue.storehouse.container.GuiHandler;
import therogue.storehouse.container.machine.ContainerCarbonCompressor;
import therogue.storehouse.crafting.ICrafter;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.crafting.MachineCraftingHandler.CraftingManager;
import therogue.storehouse.crafting.inventory.IRecipeInventory;
import therogue.storehouse.crafting.inventory.RangedItemInventory;
import therogue.storehouse.energy.EnergyStorageAdv;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.init.ModMultiBlocks;
import therogue.storehouse.init.grouped.Materials;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.multiblock.structure.MultiBlockStructure;
import therogue.storehouse.multiblock.tile.IMultiBlockController;
import therogue.storehouse.network.GuiUpdateTEPacket;
import therogue.storehouse.tile.StorehouseBaseTileMultiBlock;
import therogue.storehouse.util.ItemStackUtils;

public class TileCarbonCompressor extends StorehouseBaseTileMultiBlock implements IMultiBlockController, ICrafter {
	
	private CraftingManager theCrafter = MachineCraftingHandler.getHandler(TileCarbonCompressor.class).newCrafter(this);
	public static final int RFPerTick = 640;
	
	public TileCarbonCompressor () {
		super(ModBlocks.carbonCompressor);
		energyStorage = new EnergyStorageAdv(1200000, 640, 0);
		inventory = new InventoryManager(this, 2, new Integer[] { 1 }, new Integer[] { 0 }) {
			
			protected boolean isItemValidForSlotChecks (int index, ItemStack stack) {
				if (ItemStackUtils.areStacksMergable(stack, Materials.CARBON.createStack())) return true;
				return false;
			}
		};
	}
	
	// -----------------------IMultiBlockController Methods-----------------------------------
	@Override
	public boolean onMultiBlockActivatedAt (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side) {
		if (super.onMultiBlockActivatedAt(world, pos, state, player, hand, side)) return true;
		if (!world.isRemote && isFormed())
		{
			player.openGui(Storehouse.instance, GuiHandler.CARBONCOMPRESSOR, world, this.pos.getX(), this.pos.getY(), this.pos.getZ());
			return true;
		}
		return false;
	}
	
	@Override
	public MultiBlockStructure getStructure () {
		return ModMultiBlocks.carbonCompressorStructure;
	}
	
	// -------------------------ICrafter Methods-----------------------------------
	@Override
	public Set<Integer> getOrderMattersSlots () {
		return Sets.newHashSet();
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
		return energyStorage.getEnergyStored() >= RFPerTick;
	}
	
	@Override
	public void doRunTick () {
		if (isRunning())
		{
			energyStorage.modifyEnergyStored(-RFPerTick);
		}
	}
	
	// -------------------------Inventory Methods-----------------------------------
	@Override
	public void onInventoryChange () {
		super.onInventoryChange();
		theCrafter.checkRecipes();
	}
	
	// -------------------------Gui Methods----------------------------------------------------
	@Override
	public int getField (int id) {
		switch (id) {
			case 4:
				return theCrafter.totalCraftingTime - theCrafter.craftingTime;
			case 5:
				return theCrafter.totalCraftingTime;
			default:
				return super.getField(id);
		}
	}
	
	@Override
	public void setField (int id, int value) {
		switch (id) {
			default:
				super.setField(id, value);
		}
	}
	
	@Override
	public int getFieldCount () {
		return super.getFieldCount() + 2;
	}
	
	// -------------------------IInteractionObject-----------------------------------------------------------------
	@Override
	public Container createContainer (InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerCarbonCompressor(playerInventory, this);
	}
	
	@Override
	public String getGuiID () {
		return ModBlocks.carbonCompressor.getUnlocalizedName();
	}
	// -------------------------Standard TE methods-----------------------------------
	
	@Override
	public GuiUpdateTEPacket getGUIPacket () {
		GuiUpdateTEPacket packet = super.getGUIPacket();
		packet.getNbt().setInteger("maxCraftingTime", theCrafter.totalCraftingTime);
		packet.getNbt().setInteger("craftingTime", theCrafter.craftingTime);
		return packet;
	}
	
	@Override
	public void processGUIPacket (GuiUpdateTEPacket packet) {
		super.processGUIPacket(packet);
		theCrafter.totalCraftingTime = packet.getNbt().getInteger("maxCraftingTime");
		theCrafter.craftingTime = packet.getNbt().getInteger("craftingTime");
	}
}
