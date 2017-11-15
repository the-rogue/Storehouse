
package therogue.storehouse.init;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.block.multiblock.BlockMultiBlockWrapper;
import therogue.storehouse.tile.MachineTier;
import therogue.storehouse.tile.multiblock.MultiBlockFormationHandler.ChoicePart;
import therogue.storehouse.tile.multiblock.MultiBlockFormationHandler.MultiBlockPartBuilder;
import therogue.storehouse.tile.multiblock.MultiBlockFormationHandler.MultiBlockStructure;
import therogue.storehouse.tile.multiblock.MultiBlockFormationHandler.NormalPart;
import therogue.storehouse.tile.multiblock.MultiBlockFormationHandler.StructureHolder;

public class ModMultiBlocks {
	
	/**
	 * Initialises a new array to hold all the blocks
	 */
	public static ArrayList<IStorehouseBaseBlock> blocklist = new ArrayList<IStorehouseBaseBlock>();
	
	/**
	 * Adds all the blocks to the array
	 */
	public static void preInit () {
		carbonCompressorMultiblockstates = new BlockMultiBlockWrapper("carbon_compressor_mb", Blocks.IRON_BLOCK, Blocks.DIAMOND_BLOCK, ModBlocks.thermal_press, ModBlocks.carbon_compressor).addMatchStates(ModBlocks.solar_generator.getStateFromMeta(MachineTier.advanced.ordinal()));
		blocklist.add(carbonCompressorMultiblockstates);
		/**
		 * PreInit Blocks
		 */
		for (IStorehouseBaseBlock block : blocklist)
		{
			block.preInit();
		}
	}
	
	public static void Init () {
		assembleCarbonCompresser();
		/**
		 * Init Blocks
		 */
		for (IStorehouseBaseBlock block : blocklist)
		{
			block.Init();
		}
	}
	
	public static void postInit () {
		/**
		 * PostInit Blocks
		 */
		for (IStorehouseBaseBlock block : blocklist)
		{
			block.postInit();
		}
	}
	
	public static BlockMultiBlockWrapper carbonCompressorMultiblockstates;
	public static MultiBlockStructure carbonCompressorStructure;
	
	private static void assembleCarbonCompresser () {
		StructureHolder holder = StructureHolder.newHolder();
		// Main Part
		MultiBlockPartBuilder carbon_compressor = MultiBlockPartBuilder.newBuilder().setWrapper(carbonCompressorMultiblockstates);
		carbon_compressor.newRow().addBlocksToRow(Blocks.IRON_BLOCK, ModBlocks.carbon_compressor, Blocks.IRON_BLOCK);
		carbon_compressor.newRow().addBlocksToRow((Block) null, ModBlocks.thermal_press, null).goUp();
		carbon_compressor.newRow().addBlocksToRow((Block) null, ModBlocks.carbon_compressor, null).goUp();
		carbon_compressor.newRow().addBlocksToRow((IBlockState) null, ModBlocks.solar_generator.getStateFromMeta(MachineTier.advanced.ordinal())).build();
		holder.addPart(new NormalPart(0, 0, 0, carbon_compressor));
		// Optional Part
		MultiBlockPartBuilder backBlocks1 = MultiBlockPartBuilder.newBuilder().setWrapper(carbonCompressorMultiblockstates);
		backBlocks1.newRow().addBlocksToRow(Blocks.IRON_BLOCK, null, Blocks.IRON_BLOCK).build();
		MultiBlockPartBuilder backBlocks2 = MultiBlockPartBuilder.newBuilder().setWrapper(carbonCompressorMultiblockstates);
		backBlocks2.newRow().addBlocksToRow(Blocks.DIAMOND_BLOCK, null, Blocks.DIAMOND_BLOCK).build();
		holder.addPart(new ChoicePart(new BlockPos(0, 0, 1), backBlocks1, backBlocks2));
		// Final Structure
		carbonCompressorStructure = holder.getStructure();
	}
}
