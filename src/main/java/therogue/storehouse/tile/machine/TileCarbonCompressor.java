
package therogue.storehouse.tile.machine;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import therogue.storehouse.crafting.ICrafter;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.crafting.inventory.IRecipeInventory;
import therogue.storehouse.crafting.inventory.RangedItemInventory;
import therogue.storehouse.energy.TileEnergyStorage;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.init.ModMultiBlocks;
import therogue.storehouse.init.grouped.Materials;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.multiblock.structure.MultiBlockStructure;
import therogue.storehouse.multiblock.tile.IMultiBlockController;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.StorehouseBaseTileMultiBlock;
import therogue.storehouse.util.ItemStackUtils;

public class TileCarbonCompressor extends StorehouseBaseTileMultiBlock implements IMultiBlockController, ICrafter {
	
	private MachineCraftingHandler<TileCarbonCompressor>.CraftingManager theCrafter = MachineCraftingHandler.getHandler(TileCarbonCompressor.class).newCrafter(this);
	public static final int RFPerTick = 640;
	
	public TileCarbonCompressor () {
		super(ModBlocks.carbonCompressor);
		modules.add(theCrafter);
		this.setEnergyStorage(new TileEnergyStorage(1200000, 640, 0));
		this.setInventory(new InventoryManager(this, 2, new Integer[] { 1 }, new Integer[] { 0 }) {
			
			protected boolean isItemValidForSlotChecks (int index, ItemStack stack) {
				if (ItemStackUtils.areStacksMergable(stack, Materials.CARBON.createStack())) return true;
				return false;
			}
		});
	}
	
	// -----------------------IMultiBlockController Methods-----------------------------------
	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX,
			float hitY, float hitZ) {
		return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
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
		return new RangedItemInventory(this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL), 1, 2);
	}
	
	@Override
	public IRecipeInventory getOutputInventory () {
		return new RangedItemInventory(this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL), 0, 1);
	}
	
	@Override
	public boolean isRunning () {
		return energyStorage.hasSufficientRF();
	}
	
	@Override
	public void doRunTick () {
		energyStorage.runTick();
	}
}
