
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
import therogue.storehouse.container.multiblock.ContainerCarbonCompressor;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.init.ModMultiBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.tile.StorehouseBaseTileMultiBlock;
import therogue.storehouse.tile.multiblock.MultiBlockFormationHandler.MultiBlockStructure;

public class TileCarbonCompressor extends StorehouseBaseTileMultiBlock implements IMultiBlockController {
	
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
		if (super.onMultiBlockActivatedAt(world, pos, state, player, hand, side)) return true;
		if (!world.isRemote)
		{
			player.openGui(Storehouse.instance, GuiHandler.CARBONCOMPRESSOR, world, this.pos.getX(), this.pos.getY(), this.pos.getZ());
		}
		return true;
	}
	
	@Override
	public MultiBlockStructure getStructure () {
		return ModMultiBlocks.carbonCompressorStructure;
	}
	
	// -------------------------IInteractionObject-----------------------------------------------------------------
	@Override
	public Container createContainer (InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerCarbonCompressor(playerInventory, this);
	}
	
	@Override
	public String getGuiID () {
		return ModBlocks.carbon_compressor.getUnlocalizedName();
	}
}
