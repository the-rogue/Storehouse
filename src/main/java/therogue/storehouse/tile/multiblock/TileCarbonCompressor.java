
package therogue.storehouse.tile.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import therogue.storehouse.Storehouse;
import therogue.storehouse.container.GuiHandler;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.tile.StorehouseBaseMachine;

public class TileCarbonCompressor extends StorehouseBaseMachine implements IMultiBlockController {
	
	private boolean isFormed;
	
	public TileCarbonCompressor () {
		super(ModBlocks.carbon_compressor);
		inventory = new InventoryManager(this, 6, new Integer[] { 1, 2, 3, 4, 5 }, new Integer[] { 0 }) {
			
			protected boolean isItemValidForSlotChecks (int index, ItemStack stack) {
				int[] IDs = OreDictionary.getOreIDs(stack);
				for (int id : IDs)
				{
					String name = OreDictionary.getOreName(id);
					if (name.contains("crop") || name.contains("tree") || name.contains("vine") || name.contains("sugarcane") || name.contains("cactus")) return true;
				}
				return false;
			}
		};
	}
	//-----------------------IMultiBlockController Methods-----------------------------------
	@Override
	public boolean onMultiBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!isFormed)
		{
			tryFormMultiBlock();
			return true;
		}
		if (!world.isRemote)
		{
			// TODO Container and GUI
			player.openGui(Storehouse.instance, GuiHandler.CARBONCOMPRESSOR, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
	
	private void tryFormMultiBlock () {
		// TODO
	}
	
	@Override
	public String getGuiID () {
		return ModBlocks.carbon_compressor.getUnlocalizedName();
	}
	
	@Override
	public BlockPos getPosition () {
		return getPos();
	}
	
	@Override
	public boolean isFormed () {
		return isFormed;
	}
	
	@Override
	public IMultiBlockController getController () {
		return this;
	}
	
	// -------------------------IInteractionObject-----------------------------------------------------------------
	@Override
	public Container createContainer (InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return null;
	}
}
