
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
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.tile.StorehouseBaseMachine;
import therogue.storehouse.tile.multiblock.MultiBlockFormationHandler.IMultiBlockElement;
import therogue.storehouse.util.LOG;

public class TileCarbonCompressor extends StorehouseBaseMachine implements IMultiBlockController {
	
	private boolean isFormed = false;
	
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
	
	// -----------------------IMultiBlockController Methods-----------------------------------
	@Override
	public boolean onMultiBlockActivatedAt (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side) {
		if (!isFormed)
		{
			tryFormMultiBlock();
			return true;
		} /*
			 * if (!world.isRemote) { // TODO Container and GUI player.openGui(Storehouse.instance, GuiHandler.CARBONCOMPRESSOR, world, pos.getX(), pos.getY(), pos.getZ()); }
			 */
		return true;
	}
	
	private void tryFormMultiBlock () {
		if (MultiBlockFormationHandler.formMultiBlock(getController()))
		{
			isFormed = true;
		}
	}
	
	@Override
	public BlockPos getPosition () {
		return getPos();
	}
	
	@Override
	public IMultiBlockElement[][][] getStructure () {
		return ModBlocks.carbon_compressor.getMultiBlockStructure();
	}
	
	@Override
	public void checkStructure () {
		if (isFormed) if (MultiBlockFormationHandler.checkStructure(this)) isFormed = false;
	}
	
	@Override
	public World getPositionWorld () {
		return getWorld();
	}
	
	@Override
	public boolean isFormed () {
		return isFormed;
	}
	
	@Override
	public IMultiBlockController getController () {
		return this;
	}
	
	@Override
	public void setController (IMultiBlockController controller) {
		// NOOP
	}
	
	// -------------------------IInteractionObject-----------------------------------------------------------------
	@Override
	public Container createContainer (InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return null;
	}
	
	@Override
	public String getGuiID () {
		return ModBlocks.carbon_compressor.getUnlocalizedName();
	}
}
