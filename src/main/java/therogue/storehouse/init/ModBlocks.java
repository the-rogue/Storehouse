/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.init;

import java.util.ArrayList;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.oredict.OreDictionary;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.block.StorehouseBaseBlock;
import therogue.storehouse.block.StorehouseBaseFacingMachine;
import therogue.storehouse.block.StorehouseBaseMachine;
import therogue.storehouse.block.StorehouseBaseRotatedBlock;
import therogue.storehouse.init.grouped.CraftingBlocks;
import therogue.storehouse.init.grouped.DecorativeBlockContainer;
import therogue.storehouse.init.grouped.MultiblockBlocks;
import therogue.storehouse.init.grouped.Ores;
import therogue.storehouse.tile.machine.TileAlloyFurnace;
import therogue.storehouse.tile.machine.TileBurner;
import therogue.storehouse.tile.machine.TileCarbonCompressor;
import therogue.storehouse.tile.machine.TileCombustionGenerator.TileCombustionGeneratorAdvanced;
import therogue.storehouse.tile.machine.TileCombustionGenerator.TileCombustionGeneratorBasic;
import therogue.storehouse.tile.machine.TileCombustionGenerator.TileCombustionGeneratorEnder;
import therogue.storehouse.tile.machine.TileCrystaliser;
import therogue.storehouse.tile.machine.TileForge;
import therogue.storehouse.tile.machine.TileLiquidGenerator.TileLiquidGeneratorAdvanced;
import therogue.storehouse.tile.machine.TileLiquidGenerator.TileLiquidGeneratorBasic;
import therogue.storehouse.tile.machine.TileLiquidGenerator.TileLiquidGeneratorEnder;
import therogue.storehouse.tile.machine.TilePotionBrewer;
import therogue.storehouse.tile.machine.TilePotionInjector;
import therogue.storehouse.tile.machine.TileSolarGenerator.TileSolarGeneratorAdvanced;
import therogue.storehouse.tile.machine.TileSolarGenerator.TileSolarGeneratorBasic;
import therogue.storehouse.tile.machine.TileSolarGenerator.TileSolarGeneratorEnder;
import therogue.storehouse.tile.machine.TileThermalPress;

public class ModBlocks {
	
	/**
	 * Initialises a new array to hold all the blocks
	 */
	public static ArrayList<IStorehouseBaseBlock> blocklist = new ArrayList<IStorehouseBaseBlock>();
	
	/**
	 * Adds all the blocks to the array
	 */
	public static void preInit () {
		/**
		 * Adds Decorative blocks
		 */
		azurite_dust_block = new DecorativeBlockContainer("azurite_dust_block");
		azurite_crystal_block = new DecorativeBlockContainer("azurite_crystal_block");
		azurite_crystal_block_chiseled = new StorehouseBaseBlock("azurite_crystal_block_chiseled");
		azurite_crystal_block_pillar = new StorehouseBaseRotatedBlock(azurite_crystal_block.block, "pillar");
		blocklist.addAll(azurite_dust_block.returnAll());
		blocklist.addAll(azurite_crystal_block.returnAll());
		blocklist.add(azurite_crystal_block_chiseled);
		blocklist.add(azurite_crystal_block_pillar);
		/**
		 * Add Materials
		 */
		blocklist.add(Ores.addMaterials());
		blocklist.add(CraftingBlocks.addMaterials());
		blocklist.add(MultiblockBlocks.addMaterials());
		/**
		 * Add Machines
		 */
		solar_generator_basic = new StorehouseBaseMachine<TileSolarGeneratorBasic>("solar_generator_basic", TileSolarGeneratorBasic.class, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D));
		solar_generator_basic.setGUICheckTile().setNotifyTileActivation().setHasDefaultMultiBlock();
		solar_generator_advanced = new StorehouseBaseMachine<TileSolarGeneratorAdvanced>("solar_generator_advanced", TileSolarGeneratorAdvanced.class, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D));
		solar_generator_advanced.setGUICheckTile().setNotifyTileActivation().setHasDefaultMultiBlock().hasConnectedTexture();
		solar_generator_ender = new StorehouseBaseMachine<TileSolarGeneratorEnder>("solar_generator_ender", TileSolarGeneratorEnder.class, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D));
		solar_generator_ender.setGUICheckTile().setNotifyTileActivation();
		combustion_generator_basic = new StorehouseBaseFacingMachine<TileCombustionGeneratorBasic>("combustion_generator_basic", TileCombustionGeneratorBasic.class);
		combustion_generator_basic.setGUICheckTile().setNotifyTileActivation();
		combustion_generator_advanced = new StorehouseBaseFacingMachine<TileCombustionGeneratorAdvanced>("combustion_generator_advanced", TileCombustionGeneratorAdvanced.class);
		combustion_generator_advanced.setGUICheckTile().setNotifyTileActivation();
		combustion_generator_ender = new StorehouseBaseFacingMachine<TileCombustionGeneratorEnder>("combustion_generator_ender", TileCombustionGeneratorEnder.class);
		combustion_generator_ender.setGUICheckTile().setNotifyTileActivation();
		liquid_generator_basic = new StorehouseBaseFacingMachine<TileLiquidGeneratorBasic>("liquid_generator_basic", TileLiquidGeneratorBasic.class);
		liquid_generator_basic.setGUICheckTile().setNotifyTileActivation().setIsFluidHandler();
		liquid_generator_advanced = new StorehouseBaseFacingMachine<TileLiquidGeneratorAdvanced>("liquid_generator_advanced", TileLiquidGeneratorAdvanced.class);
		liquid_generator_advanced.setGUICheckTile().setNotifyTileActivation().setIsFluidHandler();
		liquid_generator_ender = new StorehouseBaseFacingMachine<TileLiquidGeneratorEnder>("liquid_generator_ender", TileLiquidGeneratorEnder.class);
		liquid_generator_ender.setGUICheckTile().setNotifyTileActivation().setIsFluidHandler();
		thermal_press = new StorehouseBaseFacingMachine<TileThermalPress>("thermal_press", TileThermalPress.class);
		thermal_press.setGUI();
		crystaliser = new StorehouseBaseFacingMachine<TileCrystaliser>("crystaliser", TileCrystaliser.class);
		crystaliser.setGUI().setIsFluidHandler();
		forge = new StorehouseBaseFacingMachine<TileForge>("forge", TileForge.class, new AxisAlignedBB(0.21875D, 0.0D, 0.21875D, 0.78125D, 0.5875D, 0.78125D));
		forge.setNotifyTileActivation();
		alloy_furnace = new StorehouseBaseFacingMachine<TileAlloyFurnace>("alloy_furnace", TileAlloyFurnace.class);
		alloy_furnace.setGUI();
		burner = new StorehouseBaseFacingMachine<TileBurner>("burner", TileBurner.class);
		burner.setGUICheckTile().setNotifyTileActivation().setHasDefaultMultiBlock().hasConnectedTextureGroup("cb");
		carbonCompressor = new StorehouseBaseFacingMachine<TileCarbonCompressor>("carbon_compressor", TileCarbonCompressor.class);
		carbonCompressor.setGUICheckTile().setNotifyTileActivation().setHasDefaultMultiBlock().hasConnectedTextureGroup("cb");
		potion_brewer = new StorehouseBaseFacingMachine<TilePotionBrewer>("potion_brewer", TilePotionBrewer.class);
		potion_brewer.setGUI();
		potion_injector = new StorehouseBaseFacingMachine<TilePotionInjector>("potion_injector", TilePotionInjector.class);
		potion_injector.setGUI();
		blocklist.add(solar_generator_basic);
		blocklist.add(solar_generator_advanced);
		blocklist.add(solar_generator_ender);
		blocklist.add(combustion_generator_basic);
		blocklist.add(combustion_generator_advanced);
		blocklist.add(combustion_generator_ender);
		blocklist.add(liquid_generator_basic);
		blocklist.add(liquid_generator_advanced);
		blocklist.add(liquid_generator_ender);
		blocklist.add(thermal_press);
		blocklist.add(crystaliser);
		blocklist.add(forge);
		blocklist.add(alloy_furnace);
		blocklist.add(burner);
		blocklist.add(carbonCompressor);
		blocklist.add(potion_brewer);
		blocklist.add(potion_injector);
	}
	
	public static void Init () {
		/**
		 * Register Ore Dictionary Names
		 */
		blocklist.forEach(block -> block.setup());
		Ores.Init();
		CraftingBlocks.Init();
		MultiblockBlocks.Init();
		OreDictionary.registerOre("blockazuritecrystal", azurite_crystal_block.block);
		OreDictionary.registerOre("blockazuritecrystal", azurite_crystal_block_chiseled);
		OreDictionary.registerOre("blockazuritecrystal", azurite_crystal_block_pillar);
	}
	
	public static void postInit () {
	}
	
	/**
	 * MultiBlocks
	 */
	/*
	 * Carbon crystaliser
	 * Carbon reactor
	 * Planitary resource gatherer
	 */
	/**
	 * Blocks
	 */
	public static DecorativeBlockContainer azurite_dust_block;
	public static DecorativeBlockContainer azurite_crystal_block;
	public static StorehouseBaseBlock azurite_crystal_block_chiseled;
	public static StorehouseBaseRotatedBlock azurite_crystal_block_pillar;
	public static StorehouseBaseMachine<TileSolarGeneratorBasic> solar_generator_basic;
	public static StorehouseBaseMachine<TileSolarGeneratorAdvanced> solar_generator_advanced;
	public static StorehouseBaseMachine<TileSolarGeneratorEnder> solar_generator_ender;
	public static StorehouseBaseFacingMachine<TileCombustionGeneratorBasic> combustion_generator_basic;
	public static StorehouseBaseFacingMachine<TileCombustionGeneratorAdvanced> combustion_generator_advanced;
	public static StorehouseBaseFacingMachine<TileCombustionGeneratorEnder> combustion_generator_ender;
	public static StorehouseBaseFacingMachine<TileLiquidGeneratorBasic> liquid_generator_basic;
	public static StorehouseBaseFacingMachine<TileLiquidGeneratorAdvanced> liquid_generator_advanced;
	public static StorehouseBaseFacingMachine<TileLiquidGeneratorEnder> liquid_generator_ender;
	public static StorehouseBaseFacingMachine<TileThermalPress> thermal_press;
	public static StorehouseBaseFacingMachine<TileCrystaliser> crystaliser;
	public static StorehouseBaseFacingMachine<TileForge> forge;
	public static StorehouseBaseFacingMachine<TileAlloyFurnace> alloy_furnace;
	public static StorehouseBaseFacingMachine<TileBurner> burner;
	public static StorehouseBaseFacingMachine<TileCarbonCompressor> carbonCompressor;
	public static StorehouseBaseFacingMachine<TilePotionBrewer> potion_brewer;
	public static StorehouseBaseFacingMachine<TilePotionInjector> potion_injector;
	/*
	 * public static BlockPainter painter; public static BlockBioMachine bio_machine; public static BlockPotionBrewer potion_brewer; public static BlockPotionInjector potion_injector; public static
	 * BlockCraftingController crafting_controller;
	 * public static BlockCraftingCable crafting_cable; public static BlockShelfStocker shelf_stocker; public static BlockStockController stock_controller; public static BlockStockRequester stock_requester; public
	 * static
	 * BlockStockTransfer
	 * stock_transfer; public static BlockPlayerStocker player_stocker; public static BlockPlayerUnstocker player_unstocker; public static BlockPlayerRemoteFeeder player_remote_feeder; public static
	 * BlockPlayerHealthRegenerator
	 * player_health_regenerator; public static BlockAirConUnit air_con_unit; public static BlockFan fan; public static BlockInterDimensionalProvider inter_dimensional_provider; public static
	 * BlockWirelessRedstoneManager
	 * wireless_redstone_manager; public static BlockWirelessTransmitter wireless_transmitter; public static BlockWirelessReciever wireless_reciever; public static BlockBlackHoleStabiliser black_hole_stabiliser;
	 * public
	 * static
	 * BlockSingularityCore singularity_core; public static BlockBlackHoleEnergyGatherer black_hole_energy_gatherer; public static BlockBlackHoleEnergyTap black_hole_energy_tap; public static
	 * BlockBlackHoleContainmentFieldGenerator
	 * black_hole_containment_field_generator; public static BlockBlackHoleContainmentController black_hole_containment_controller; public static BlockBlackHoleMatterExtractor black_hole_matter_extractor; public
	 * static
	 * BlockBlackHoleMatterShaper black_hole_matter_shaper; public static BlockBlackHoleMatterConverter black_hole_matter_converter; public static BlockBlackHoleEnergyCannon black_hole_energy_cannon; public static
	 * BlockDragonAttractor
	 * dragon_attractor; public static BlockDragonContainmentField dragon_containment_field; public static BlockDragonGrinder dragon_grinder; public static BlockEnderVacuum ender_vacuum; public static
	 * BlockResourceConstructor
	 * resource_constructor; public static BlockForceField force_field; public static BlockPlacer block_placer; public static BlockWoodStorehouse wood_storehouse; public static BlockStoneStorehouse stone_storehouse;
	 * public static
	 * BlockIronStorehouse iron_storhouse; public static BlockSteelStorehouse steel_storehouse; public static BlockTitaniumStorehouse titanium_storehouse;
	 */
}
