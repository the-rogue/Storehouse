
package therogue.storehouse.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.init.grouped.CraftingBlocks;
import therogue.storehouse.init.grouped.MultiblockBlocks;
import therogue.storehouse.multiblock.block.BasicMultiBlockBlock;
import therogue.storehouse.multiblock.block.IMultiBlockStateMapper;
import therogue.storehouse.multiblock.structure.MultiBlockPartBuilder;
import therogue.storehouse.multiblock.structure.MultiBlockStructure;
import therogue.storehouse.multiblock.structure.MultiBlockStructure.Builder;
import therogue.storehouse.multiblock.structure.NormalBlock;
import therogue.storehouse.multiblock.structure.NormalPart;
import therogue.storehouse.multiblock.structure.VariableBlock;

public class ModMultiBlocks {
	
	/**
	 * Initialises a new array to hold all the blocks
	 */
	public static List<IStorehouseBaseBlock> blocklist = new ArrayList<IStorehouseBaseBlock>();
	@SuppressWarnings ("unused")
	private static ArrayList<NormalPart> variablePartList = new ArrayList<NormalPart>();
	/**
	 * MultiBlockBlockStates
	 */
	public static BasicMultiBlockBlock burnerMultiblockstates;
	public static BasicMultiBlockBlock IOMultiBlockStates;
	public static BasicMultiBlockBlock carbonCompressorMultiBlockStates;
	public static BasicMultiBlockBlock solarGeneratorMultiBlockStates;
	/**
	 * MultiBlockStructures
	 */
	public static MultiBlockStructure burnerStructure;
	public static MultiBlockStructure carbonCompressorStructure;
	public static MultiBlockStructure basicSolarGeneratorStructure;
	public static MultiBlockStructure advancedSolarGeneratorStructure;
	public static MultiBlockStructure enderSolarGeneratorStructure;
	
	/**
	 * Adds all the blocks to the array
	 */
	public static void preInit () {
		burnerMultiblockstates = getBurnerStates();
		blocklist.add(burnerMultiblockstates);
		IOMultiBlockStates = getIOStates();
		blocklist.add(IOMultiBlockStates);
		carbonCompressorMultiBlockStates = getCarbonCompressorStates();
		blocklist.add(carbonCompressorMultiBlockStates);
		solarGeneratorMultiBlockStates = getSolarPanelStates();
		blocklist.add(solarGeneratorMultiBlockStates);
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
		processSolarGenerator(multiblockbuilder);
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
		block.addBlocks(ModBlocks.burner);
		block.addMatchStates(CraftingBlocks.MACHINE_CASING.getState());
		block.addMatchStates(CraftingBlocks.CONTROL_CIRCUITRY.getState());
		block.addBlocks(Blocks.NETHER_BRICK);
		block.addBlocks(Blocks.NETHERRACK);
		block.addBlocks(Blocks.DIAMOND_BLOCK);
		block.addBlocks(Blocks.STAINED_HARDENED_CLAY);
		block.addBlocks(Blocks.HARDENED_CLAY);
		block.addMatchStates(MultiblockBlocks.EJECTOR.getState());
		block.addBlocks(Blocks.FIRE);
		block.addBlocks(Blocks.MAGMA);
		block.addMatchStates(CraftingBlocks.FAN.getState());
		block.addMatchStates(CraftingBlocks.REGULATOR.getState());
		return block;
	}
	
	private static BasicMultiBlockBlock getIOStates () {
		BasicMultiBlockBlock block = new BasicMultiBlockBlock("io_mb");
		block.addMatchStates(CraftingBlocks.MACHINE_CASING.getState(), MultiblockBlocks.ENERGY_CONNECTOR.getState(), MultiblockBlocks.CHUTE.getState(), MultiblockBlocks.EJECTOR.getState(), MultiblockBlocks.ITEM_IO.getState(), MultiblockBlocks.ADVANCED_CONNECTOR.getState());
		return block;
	}
	
	private static BasicMultiBlockBlock getCarbonCompressorStates () {
		BasicMultiBlockBlock block = new BasicMultiBlockBlock("carbon_compressor_mb");
		block.addBlocks(Blocks.DIAMOND_BLOCK);
		block.addBlocks(ModBlocks.carbonCompressor);
		block.addMatchStates(CraftingBlocks.MACHINE_CASING.getState());
		return block;
	}
	
	private static BasicMultiBlockBlock getSolarPanelStates () {
		BasicMultiBlockBlock block = new BasicMultiBlockBlock("solar_panel_mb");
		block.addBlocks(ModBlocks.solar_generator_basic);
		block.addBlocks(ModBlocks.solar_generator_advanced);
		block.addMatchStates(CraftingBlocks.MACHINE_CASING.getState());
		block.addMatchStates(CraftingBlocks.CONTROL_CIRCUITRY.getState());
		return block;
	}
	
	private static void assembleBurner (Builder multiblockbuilder) {
		IMultiBlockStateMapper BMBS = burnerMultiblockstates;
		NormalBlock B = new NormalBlock(ModBlocks.burner, BMBS);
		NormalBlock MC = new NormalBlock(CraftingBlocks.MACHINE_CASING.getState(), BMBS);
		NormalBlock CC = new NormalBlock(CraftingBlocks.CONTROL_CIRCUITRY.getState(), BMBS);
		NormalBlock NB = new NormalBlock(Blocks.NETHER_BRICK, BMBS);
		NormalBlock NR = new NormalBlock(Blocks.NETHERRACK, BMBS);
		NormalBlock DB = new NormalBlock(Blocks.DIAMOND_BLOCK, BMBS);
		VariableBlock HC = new VariableBlock(BMBS, Blocks.STAINED_HARDENED_CLAY, Blocks.HARDENED_CLAY);
		NormalBlock EJ = new NormalBlock(MultiblockBlocks.EJECTOR.getState(), BMBS);
		NormalBlock FR = new NormalBlock(Blocks.FIRE, BMBS);
		NormalBlock MB = new NormalBlock(Blocks.MAGMA, BMBS);
		NormalBlock FN = new NormalBlock(CraftingBlocks.FAN.getState(), BMBS);
		NormalBlock RG = new NormalBlock(CraftingBlocks.REGULATOR.getState(), BMBS);
		VariableBlock IO = new VariableBlock(IOMultiBlockStates, CraftingBlocks.MACHINE_CASING.getState(), MultiblockBlocks.ENERGY_CONNECTOR.getState(), MultiblockBlocks.CHUTE.getState(), MultiblockBlocks.EJECTOR.getState(), MultiblockBlocks.ITEM_IO.getState(),
				MultiblockBlocks.ADVANCED_CONNECTOR.getState());
		MultiBlockPartBuilder burner = MultiBlockPartBuilder.newBuilder().setWrapper(burnerMultiblockstates);
		for (int i = 0; i < 7; i++)
		{
			burner.newRow();
			for (int j = 0; j < 7; j++)
			{
				burner.addBlocksToRow(Blocks.IRON_BLOCK);
			}
		}
		burner.goUp().newRow().addBlocksToRow(MC, MC, CC, B, CC, MC, MC);
		burner.newRow().addBlocksToRow(MC, NB, NB, NB, NB, NB, MC);
		burner.newRow().addBlocksToRow(IO, NB, NR, DB, NR, NB, IO);
		burner.newRow().addBlocksToRow(CC, NB, DB, NR, DB, NB, CC);
		burner.newRow().addBlocksToRow(IO, NB, NR, DB, NR, NB, IO);
		burner.newRow().addBlocksToRow(MC, NB, NB, NB, NB, NB, MC);
		burner.newRow().addBlocksToRow(MC, MC, IO, CC, IO, MC, MC);
		burner.goUp().newRow().addBlocksToRow(null, MC, CC, B, CC, MC, null);
		burner.newRow().addBlocksToRow(MC, HC, HC, EJ, HC, HC, MC);
		burner.newRow().addBlocksToRow(IO, HC, FR, null, FR, HC, IO);
		burner.newRow().addBlocksToRow(CC, EJ, null, FR, null, EJ, CC);
		burner.newRow().addBlocksToRow(IO, HC, FR, null, FR, HC, IO);
		burner.newRow().addBlocksToRow(MC, HC, HC, EJ, HC, HC, MC);
		burner.newRow().addBlocksToRow(null, MC, IO, CC, IO, MC, null);
		burner.goUp().newRow().addBlocksToRow(null, null, MC, MC, MC, null, null);
		burner.newRow().addBlocksToRow(null, MC, HC, MB, HC, MC, null);
		burner.newRow().addBlocksToRow(MC, HC, null, null, null, HC, MC);
		burner.newRow().addBlocksToRow(MC, MB, null, null, null, MB, MC);
		burner.newRow().addBlocksToRow(MC, HC, null, null, null, HC, MC);
		burner.newRow().addBlocksToRow(null, MC, HC, MB, HC, MC, null);
		burner.newRow().addBlocksToRow(null, null, MC, MC, MC, null, null);
		burner.goUp().newRow(2).addBlocksToRow(null, null, MC, MC, MC, null, null);
		burner.newRow().addBlocksToRow(null, MC, null, null, null, MC, null);
		burner.newRow().addBlocksToRow(null, MC, null, null, null, MC, null);
		burner.newRow().addBlocksToRow(null, MC, null, null, null, MC, null);
		burner.newRow().addBlocksToRow(null, null, MC, MC, MC, null, null);
		burner.goUp().newRow(3).addBlocksToRow(null, null, FN, FN, FN, null, null);
		burner.newRow().addBlocksToRow(null, null, FN, RG, FN, null, null);
		burner.newRow().addBlocksToRow(null, null, FN, FN, FN, null, null);
		multiblockbuilder.addPart(burner.build());
	}
	
	private static void assembleCarbonCompressor (Builder multiblockbuilder) {
		// Main Part
		MultiBlockPartBuilder carbonCompressor = MultiBlockPartBuilder.newBuilder().setWrapper(carbonCompressorMultiBlockStates);
		carbonCompressor.newRow().addBlocksToRow(ModBlocks.carbonCompressor, ModBlocks.carbonCompressor);
		carbonCompressor.newRow().addBlocksToRow(Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_BLOCK).goUp();
		carbonCompressor.newRow().addBlocksToRow(ModBlocks.carbonCompressor, ModBlocks.carbonCompressor);
		carbonCompressor.newRow().addBlocksToRow(Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_BLOCK).goUp();
		VariableBlock power = new VariableBlock(IOMultiBlockStates, CraftingBlocks.MACHINE_CASING.getState(), MultiblockBlocks.ENERGY_CONNECTOR.getState());
		carbonCompressor.newRow().addBlocksToRow(power, power);
		carbonCompressor.newRow().addBlocksToRow(power, power);
		multiblockbuilder.addPart(carbonCompressor.build());
	}
	
	private static void processSolarGenerator (Builder multiblockbuilder) {
		IMultiBlockStateMapper SGMBW = solarGeneratorMultiBlockStates;
		MultiBlockPartBuilder solargen = MultiBlockPartBuilder.newBuilder().setWrapper(solarGeneratorMultiBlockStates);
		solargen.newRow().addBlocksToRow(ModBlocks.solar_generator_basic);
		basicSolarGeneratorStructure = multiblockbuilder.addPart(solargen.build()).getStructure();
		NormalBlock advGen = new NormalBlock(ModBlocks.solar_generator_advanced, SGMBW);
		NormalBlock MC = new NormalBlock(CraftingBlocks.MACHINE_CASING.getState(), SGMBW);
		NormalBlock CC = new NormalBlock(CraftingBlocks.CONTROL_CIRCUITRY.getState(), SGMBW);
		solargen.newRow().addBlocksToRow(MC, CC);
		solargen.newRow().addBlocksToRow(CC, MC);
		solargen.goUp().newRow().addBlocksToRow(advGen, advGen);
		solargen.newRow().addBlocksToRow(advGen, advGen);
		advancedSolarGeneratorStructure = multiblockbuilder.addPart(solargen.build()).getStructure();
	}
}
