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

import net.minecraftforge.oredict.OreDictionary;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.block.StorehouseBaseBlock;
import therogue.storehouse.block.Decorative.StorehouseBaseRotatedBlock;
import therogue.storehouse.block.machine.BlockCombustionGenerator;
import therogue.storehouse.block.machine.BlockCrystaliser;
import therogue.storehouse.block.machine.BlockLiquidGenerator;
import therogue.storehouse.block.machine.BlockSolarGenerator;
import therogue.storehouse.block.machine.BlockStamper;
import therogue.storehouse.block.machine.BlockThermalPress;
import therogue.storehouse.init.grouped.CraftingBlocks;
import therogue.storehouse.init.grouped.DecorativeBlockContainer;
import therogue.storehouse.init.grouped.Ores;

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
		Ores.addMaterials();
		CraftingBlocks.addMaterials();
		/**
		 * Add Machines
		 */
		solar_generator = new BlockSolarGenerator("solar_generator");
		combustion_generator = new BlockCombustionGenerator("combustion_generator");
		liquid_generator = new BlockLiquidGenerator("liquid_generator");
		thermal_press = new BlockThermalPress("thermal_press");
		crystaliser = new BlockCrystaliser("crystaliser");
		stamper = new BlockStamper("stamper");
		blocklist.add(solar_generator);
		blocklist.add(combustion_generator);
		blocklist.add(liquid_generator);
		blocklist.add(thermal_press);
		blocklist.add(crystaliser);
		blocklist.add(stamper);
		/**
		 * PreInit Blocks
		 */
		for (IStorehouseBaseBlock block : blocklist)
		{
			block.preInit();
		}
	}
	
	public static void Init () {
		/**
		 * Init Blocks
		 */
		for (IStorehouseBaseBlock block : blocklist)
		{
			block.Init();
		}
		/**
		 * Register Ore Dictionary Names
		 */
		Ores.Init();
		CraftingBlocks.Init();
		OreDictionary.registerOre("blockazuritecrystal", azurite_crystal_block.block);
		OreDictionary.registerOre("blockazuritecrystal", azurite_crystal_block_chiseled);
		OreDictionary.registerOre("blockazuritecrystal", azurite_crystal_block_pillar);
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
	
	/**
	 * Blocks
	 */
	public static DecorativeBlockContainer azurite_dust_block;
	public static DecorativeBlockContainer azurite_crystal_block;
	public static StorehouseBaseBlock azurite_crystal_block_chiseled;
	public static StorehouseBaseRotatedBlock azurite_crystal_block_pillar;
	public static BlockSolarGenerator solar_generator;
	public static BlockCombustionGenerator combustion_generator;
	public static BlockLiquidGenerator liquid_generator;
	public static BlockThermalPress thermal_press;
	public static BlockCrystaliser crystaliser;
	public static BlockStamper stamper;/**
										 * public static BlockPainter painter; public static BlockAlloySmelter alloy_smelter; public static BlockCarbonCompressor carbon_compressor; public static BlockBioMachine bio_machine; public static BlockPotionBrewer potion_brewer; public static
										 * BlockPotionInjector potion_injector; public static BlockCraftingController crafting_controller; public static BlockCraftingCable crafting_cable; public static BlockShelfStocker shelf_stocker; public static BlockStockController stock_controller; public
										 * static BlockStockRequester stock_requester; public static BlockStockTransfer stock_transfer; public static BlockPlayerStocker player_stocker; public static BlockPlayerUnstocker player_unstocker; public static BlockPlayerRemoteFeeder player_remote_feeder;
										 * public static BlockPlayerHealthRegenerator player_health_regenerator; public static BlockAirConUnit air_con_unit; public static BlockFan fan; public static BlockInterDimensionalProvider inter_dimensional_provider; public static BlockWirelessRedstoneManager
										 * wireless_redstone_manager; public static BlockWirelessTransmitter wireless_transmitter; public static BlockWirelessReciever wireless_reciever; public static BlockBlackHoleStabiliser black_hole_stabiliser; public static BlockSingularityCore singularity_core;
										 * public static BlockBlackHoleEnergyGatherer black_hole_energy_gatherer; public static BlockBlackHoleEnergyTap black_hole_energy_tap; public static BlockBlackHoleContainmentFieldGenerator black_hole_containment_field_generator; public static
										 * BlockBlackHoleContainmentController black_hole_containment_controller; public static BlockBlackHoleMatterExtractor black_hole_matter_extractor; public static BlockBlackHoleMatterShaper black_hole_matter_shaper; public static BlockBlackHoleMatterConverter
										 * black_hole_matter_converter; public static BlockBlackHoleEnergyCannon black_hole_energy_cannon; public static BlockDragonAttractor dragon_attractor; public static BlockDragonContainmentField dragon_containment_field; public static BlockDragonGrinder
										 * dragon_grinder; public static BlockEnderVacuum ender_vacuum; public static BlockResourceConstructor resource_constructor; public static BlockForceField force_field; public static BlockPlacer block_placer; public static BlockWoodStorehouse wood_storehouse;
										 * public static BlockStoneStorehouse stone_storehouse; public static BlockIronStorehouse iron_storhouse; public static BlockSteelStorehouse steel_storehouse; public static BlockTitaniumStorehouse titanium_storehouse;
										 */
}
