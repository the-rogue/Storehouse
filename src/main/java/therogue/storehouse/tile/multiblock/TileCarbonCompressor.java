
package therogue.storehouse.tile.multiblock;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.init.ModMultiBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.tile.StorehouseBaseMachine;
import therogue.storehouse.tile.multiblock.MultiBlockFormationHandler.MultiBlockFormationResult;
import therogue.storehouse.tile.multiblock.MultiBlockFormationHandler.MultiBlockStructure;
import therogue.storehouse.tile.multiblock.MultiBlockFormationHandler.PositionStateChanger;

public class TileCarbonCompressor extends StorehouseBaseMachine implements IMultiBlockController {
	
	private boolean isFormed = false;
	private boolean breaking = false;
	private List<PositionStateChanger> components = null;
	
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
		if (!world.isRemote)
		{
			if (!isFormed)
			{
				if (tryFormMultiBlock()) player.sendStatusMessage(new TextComponentTranslation("chatmessage.storehouse:multiblock_formed"), false);
				return true;
			} /*
				 * if (!world.isRemote) { // TODO Container and GUI player.openGui(Storehouse.instance, GuiHandler.CARBONCOMPRESSOR, world, pos.getX(), pos.getY(), pos.getZ()); }
				 */
		}
		return true;
	}
	
	private boolean tryFormMultiBlock () {
		if (!world.isRemote)
		{
			MultiBlockFormationResult result = MultiBlockFormationHandler.formMultiBlock(getController());
			if (result.formed)
			{
				isFormed = true;
				components = result.components;
			}
		}
		return isFormed;
	}
	
	@Override
	public BlockPos getPosition () {
		return getPos();
	}
	
	@Override
	public MultiBlockStructure getStructure () {
		return ModMultiBlocks.carbonCompressorStructure;
	}
	
	@Override
	public void onBlockBroken (@Nullable BlockPos at) {
		if (!world.isRemote && isFormed && !breaking)
		{
			breaking = true;
			MultiBlockFormationHandler.removeMultiBlock(this, at);
			breaking = false;
			isFormed = false;
			components = null;
		}
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
	
	@Override
	public List<PositionStateChanger> getComponents () {
		return components;
	}
	
	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("formed", isFormed);
		if (isFormed) nbt.setTag("PositionStateChangers", MultiBlockFormationHandler.PositionStateChanger.writeToNBT(components));
		return nbt;
	}
	
	@Override
	public void readFromNBT (NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		isFormed = nbt.getBoolean("formed");
		if (isFormed) components = MultiBlockFormationHandler.PositionStateChanger.readFromNBT((NBTTagList) nbt.getTag("PositionStateChangers"));
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
