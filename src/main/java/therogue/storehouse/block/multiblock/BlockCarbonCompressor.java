
package therogue.storehouse.block.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import therogue.storehouse.block.BlockUtils;
import therogue.storehouse.block.machine.StorehouseBaseFacingMachine;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.tile.MachineTier;
import therogue.storehouse.tile.multiblock.IMultiBlockTile;
import therogue.storehouse.tile.multiblock.MultiBlockFormationHandler.ChoiceBlock;
import therogue.storehouse.tile.multiblock.MultiBlockFormationHandler.IMultiBlockElement;
import therogue.storehouse.tile.multiblock.MultiBlockFormationHandler.MultiBlockPartBuilder;
import therogue.storehouse.tile.multiblock.TileCarbonCompressor;

public class BlockCarbonCompressor extends StorehouseBaseFacingMachine {
	
	private IMultiBlockElement[][][] structure;
	public BlockMultiBlockWrapper multiblockstates = new BlockMultiBlockWrapper("carbon_compressor_mb", Blocks.IRON_BLOCK, Blocks.DIAMOND_BLOCK, ModBlocks.thermal_press).addMatchStates(ModBlocks.solar_generator.getStateFromMeta(MachineTier.advanced.ordinal()));
	
	public BlockCarbonCompressor (String name) {
		super(name);
	}
	
	@Override
	public TileEntity createNewTileEntity (World worldIn, int meta) {
		TileCarbonCompressor tile = new TileCarbonCompressor();
		tile.setWorld(worldIn);
		return tile;
	}
	
	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		return BlockUtils.onMultiBlockActivated(world, pos, state, player, hand, side);
	}
	
	@Override
	public void breakBlock (World worldIn, BlockPos pos, IBlockState state) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof IMultiBlockTile)
		{
			((IMultiBlockTile) te).getController().onBlockBroken(pos);
		}
		super.breakBlock(worldIn, pos, state);
	}
	
	public IMultiBlockElement[][][] getMultiBlockStructure () {
		return structure;
	}
	
	@Override
	public void Init () {
		MultiBlockPartBuilder carbon_compressor = MultiBlockPartBuilder.newBuilder();
		carbon_compressor.newRow().addBlocksToRow(multiblockstates, Blocks.IRON_BLOCK, this, Blocks.IRON_BLOCK);
		carbon_compressor.newRow().addBlocksToRow(multiblockstates, Blocks.DIAMOND_BLOCK, ModBlocks.thermal_press).addBlocksToRow(new ChoiceBlock(multiblockstates, Blocks.DIAMOND_BLOCK, Blocks.IRON_BLOCK)).goUp();
		carbon_compressor.newRow().addBlocksToRow(multiblockstates, null, ModBlocks.solar_generator.getStateFromMeta(MachineTier.advanced.ordinal()));
		carbon_compressor.build();
		structure = carbon_compressor.getBlockArray();
	}
}
