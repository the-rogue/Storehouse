
package therogue.storehouse.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.init.grouped.MultiblockBlocks;
import therogue.storehouse.multiblock.block.BasicMultiBlockBlock;
import therogue.storehouse.multiblock.structure.MultiBlockPartBuilder;
import therogue.storehouse.multiblock.structure.MultiBlockStructure;
import therogue.storehouse.multiblock.structure.MultiBlockStructure.Builder;
import therogue.storehouse.multiblock.structure.NormalPart;
import therogue.storehouse.multiblock.structure.VariablePart;
import therogue.storehouse.tile.MachineTier;

public class ModMultiBlocks {
	
	/**
	 * Initialises a new array to hold all the blocks
	 */
	public static List<IStorehouseBaseBlock> blocklist = new ArrayList<IStorehouseBaseBlock>();
	private static ArrayList<NormalPart> variablePartList = new ArrayList<NormalPart>();
	/**
	 * MultiBlockBlockStates
	 */
	public static BasicMultiBlockBlock burnerMultiblockstates;
	public static BasicMultiBlockBlock carbonCompressorMultiBlockStates;
	/**
	 * MultiBlockStructures
	 */
	public static MultiBlockStructure burnerStructure;
	public static MultiBlockStructure carbonCompressorStructure;
	
	/**
	 * Adds all the blocks to the array
	 */
	public static void preInit () {
		burnerMultiblockstates = getBurnerStates();
		blocklist.add(burnerMultiblockstates);
		carbonCompressorMultiBlockStates = getCarbonCompressorStates ();
		blocklist.add(carbonCompressorMultiBlockStates);
		/**
		 * PreInit Blocks
		 */
		for (IStorehouseBaseBlock block : blocklist)
		{
			block.preInit();
		}
	}
	
	public static void Init () {
		final Builder multiblockbuilder = Builder.newBuilder();
		assembleBurner(multiblockbuilder);
		burnerStructure = multiblockbuilder.getStructure();
		assembleCarbonCompressor(multiblockbuilder);
		carbonCompressorStructure = multiblockbuilder.getStructure();
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
	
	private static BasicMultiBlockBlock getBurnerStates () {
		BasicMultiBlockBlock block = new BasicMultiBlockBlock("burner_mb");
		block.addBlocks(Blocks.IRON_BLOCK);
		block.addBlocks(Blocks.DIAMOND_BLOCK);
		block.addBlocks(ModBlocks.thermal_press);
		block.addBlocks(ModBlocks.burner);
		block.addMatchStates(ModBlocks.solar_generator.getStateFromMeta(MachineTier.advanced.ordinal()));
		block.addMatchStates(MultiblockBlocks.crafting_block.getStateFromMeta(MultiblockBlocks.POWER_CONNECTOR.ordinal()));
		return block;
	}
	
	private static BasicMultiBlockBlock getCarbonCompressorStates () {
		BasicMultiBlockBlock block = new BasicMultiBlockBlock("carbonCompressor_mb");
		block.addBlocks(Blocks.DIAMOND_BLOCK);
		block.addBlocks(ModBlocks.carbonCompressor);
		block.addMatchStates(MultiblockBlocks.crafting_block.getStateFromMeta(MultiblockBlocks.POWER_CONNECTOR.ordinal()));
		return block;
	}
	
	private static void assembleBurner (Builder multiblockbuilder) {
		// Main Part
		MultiBlockPartBuilder burner = MultiBlockPartBuilder.newBuilder().setWrapper(burnerMultiblockstates);
		burner.newRow().addBlocksToRow(Blocks.IRON_BLOCK, ModBlocks.burner, Blocks.IRON_BLOCK);
		burner.newRow().addBlocksToRow((Block) null, ModBlocks.thermal_press, null).goUp();
		burner.newRow().addBlocksToRow((Block) null, ModBlocks.burner, null).goUp();
		burner.newRow().addBlocksToRow(
			(IBlockState) MultiblockBlocks.crafting_block.getStateFromMeta(MultiblockBlocks.POWER_CONNECTOR.ordinal()),
			ModBlocks.solar_generator.getStateFromMeta(MachineTier.advanced.ordinal()));
		multiblockbuilder.addPart(burner.build());
		// Optional Part
		burner.setStart(0, 0, 1);
		variablePartList.add(burner.newRow().addBlocksToRow(Blocks.IRON_BLOCK, null, Blocks.IRON_BLOCK).build());
		variablePartList.add(burner.newRow().addBlocksToRow(Blocks.DIAMOND_BLOCK, null, Blocks.DIAMOND_BLOCK).build());
		multiblockbuilder.addPart(new VariablePart(variablePartList));
		variablePartList.clear();
	}
	private static void assembleCarbonCompressor (Builder multiblockbuilder) {
		// Main Part
		MultiBlockPartBuilder carbonCompressor = MultiBlockPartBuilder.newBuilder().setWrapper(carbonCompressorMultiBlockStates);
		carbonCompressor.newRow().addBlocksToRow(ModBlocks.carbonCompressor, ModBlocks.carbonCompressor);
		carbonCompressor.newRow().addBlocksToRow(Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_BLOCK).goUp();
		carbonCompressor.newRow().addBlocksToRow(ModBlocks.carbonCompressor, ModBlocks.carbonCompressor);
		carbonCompressor.newRow().addBlocksToRow(Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_BLOCK);
		multiblockbuilder.addPart(carbonCompressor.build());
	}
}
