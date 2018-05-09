
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
import net.minecraftforge.oredict.OreDictionary;
import therogue.storehouse.crafting.ICrafter;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.crafting.inventory.IRecipeInventory;
import therogue.storehouse.crafting.inventory.RangedItemInventory;
import therogue.storehouse.energy.TileEnergyStorage;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.init.ModMultiBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.multiblock.structure.MultiBlockStructure;
import therogue.storehouse.multiblock.tile.IMultiBlockController;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.StorehouseBaseTileMultiBlock;

public class TileBurner extends StorehouseBaseTileMultiBlock implements IMultiBlockController, ICrafter {
	
	private MachineCraftingHandler<TileBurner>.CraftingManager theCrafter = MachineCraftingHandler.getHandler(TileBurner.class).newCrafter(this);
	public static final int RFPerTick = 640;
	
	public TileBurner () {
		super(ModBlocks.burner);
		modules.add(theCrafter);
		this.setEnergyStorage(new TileEnergyStorage(this, 1200000, 640, 0));
		this.setInventory(new InventoryManager(this, 6, new Integer[] { 1, 2, 3, 4, 5 }, new Integer[] { 0 }) {
			
			protected boolean isItemValidForSlotChecks (int index, ItemStack stack) {
				int[] IDs = OreDictionary.getOreIDs(stack);
				for (int id : IDs)
				{
					String name = OreDictionary.getOreName(id);
					if (name.contains("crop") || name.contains("tree")
							|| name.contains("vine")
							|| name.contains("sugarcane")
							|| name.contains("cactus"))
						return true;
				}
				return false;
			}
		});
		energyStorage.setRFPerTick(40);
	}
	
	// -----------------------IMultiBlockController Methods-----------------------------------
	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX,
			float hitY, float hitZ) {
		return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
	}
	
	@Override
	public MultiBlockStructure getStructure () {
		return ModMultiBlocks.burnerStructure;
	}
	
	// -------------------------ICrafter Methods-----------------------------------
	@Override
	public Set<Integer> getOrderMattersSlots () {
		return Sets.newHashSet();
	}
	
	@Override
	public IRecipeInventory getCraftingInventory () {
		return new RangedItemInventory(this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL), 1, 6);
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
