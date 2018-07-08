
package therogue.storehouse.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import therogue.storehouse.crafting.MachineRecipe;
import therogue.storehouse.crafting.RecipeHelper;
import therogue.storehouse.crafting.wrapper.ItemStackComponent;
import therogue.storehouse.crafting.wrapper.ItemStackComponent.ItemStackRemainComponent;
import therogue.storehouse.init.grouped.Materials;
import therogue.storehouse.init.grouped.Ores;
import therogue.storehouse.init.grouped.Resources;
import therogue.storehouse.tile.machine.TileCarbonCompressor;
import therogue.storehouse.tile.machine.TileCombustionGenerator;
import therogue.storehouse.tile.machine.TileLiquidGenerator;
import therogue.storehouse.tile.machine.TilePotionBrewer;

public class Recipes {
	
	public static void Init () {
		GameRegistry.addSmelting(Ores.COPPER.createStack(), Resources.COPPER_INGOT.createStack(), 0.6F);
		GameRegistry.addSmelting(Ores.TIN.createStack(), Resources.TIN_INGOT.createStack(), 0.7F);
		GameRegistry.addSmelting(Ores.ALUMINIUM.createStack(), Resources.ALUMINIUM_INGOT.createStack(), 0.7F);
		GameRegistry.addSmelting(Ores.SILVER.createStack(), Resources.SILVER_INGOT.createStack(), 0.7F);
		GameRegistry.addSmelting(Ores.LEAD.createStack(), Resources.LEAD_INGOT.createStack(), 0.7F);
		GameRegistry.addSmelting(Ores.TITANIUM.createStack(), Resources.TITANIUM_INGOT.createStack(), 0.7F);
		RecipeHelper.registerCrystaliserRecipe(80, Resources.AZURITE_CRYSTAL.createStack(), Resources.AZURITE_DUST.createStack());
		RecipeHelper.registerForgeRecipe(Materials.IRON_PLATE.createStack(), new ItemStack(Items.IRON_INGOT));
		RecipeHelper.registerThermalPressHighPressureRecipe(40, new ItemStack(Items.DIAMOND), Resources.AZURITE_DUST.createStack(), Resources.AZURITE_DUST.createStack(), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.REDSTONE, 2));
		RecipeHelper.registerAlloyFurnaceRecipe(160, Resources.BRONZE_INGOT.createStack(4), Resources.COPPER_INGOT.createStack(3), Resources.TIN_INGOT.createStack());
		RecipeHelper.registerAlloyFurnaceRecipe(160, Resources.BRASS_INGOT.createStack(8), Resources.COPPER_INGOT.createStack(6), Resources.TIN_INGOT.createStack(1), new ItemStack(Items.GOLD_INGOT));
		RecipeHelper.registerAlloyFurnaceRecipe(1200, Resources.STEEL_INGOT.createStack(), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.COAL));
		RecipeHelper.registerAlloyFurnaceRecipe(160, Resources.DURALUMIN_INGOT.createStack(6), Resources.ALUMINIUM_INGOT.createStack(4), Resources.COPPER_INGOT.createStack(), new ItemStack(Items.DIAMOND));
		RecipeHelper.registerThermalPressStampRecipe(160, Materials.COPPER_PLATE.createStack(), new ItemStackComponent(Resources.COPPER_INGOT.createStack()), new ItemStackRemainComponent(Materials.PLATE_TOOL.createStack()));
		RecipeHelper.registerThermalPressStampRecipe(160, Materials.IRON_PLATE.createStack(), new ItemStackComponent(new ItemStack(Items.IRON_INGOT)), new ItemStackRemainComponent(Materials.PLATE_TOOL.createStack()));
		RecipeHelper.registerThermalPressStampRecipe(160, Materials.GOLD_PLATE.createStack(), new ItemStackComponent(new ItemStack(Items.GOLD_INGOT)), new ItemStackRemainComponent(Materials.PLATE_TOOL.createStack()));
		RecipeHelper.registerThermalPressStampRecipe(160, Materials.TIN_PLATE.createStack(), new ItemStackComponent(Resources.TIN_INGOT.createStack()), new ItemStackRemainComponent(Materials.PLATE_TOOL.createStack()));
		RecipeHelper.registerThermalPressStampRecipe(160, Materials.STEEL_PLATE.createStack(), new ItemStackComponent(Resources.STEEL_INGOT.createStack()), new ItemStackRemainComponent(Materials.PLATE_TOOL.createStack()));
		InitBurner();
		MachineRecipe.create(TileCarbonCompressor.class, 400, new ItemStackComponent(Items.DIAMOND), new ItemStackComponent(Materials.CARBON.createStack(16)));
		TileCombustionGenerator.CombustionRecipe.registerRecipes();
		TileLiquidGenerator.LiquidRecipe.registerRecipes();
		TilePotionBrewer.PotionRecipe.registerRecipes();
	}
	
	private static void InitBurner () {
		RecipeHelper.registerBurnerRecipe(600, Materials.CARBON.createStack(), "crop", 32);
		RecipeHelper.registerBurnerRecipe(300, Materials.CARBON.createStack(), "tree", 16);
		RecipeHelper.registerBurnerRecipe(300, Materials.CARBON.createStack(), "vine", 16);
		RecipeHelper.registerBurnerRecipe(150, Materials.CARBON.createStack(), "sugarcane", 8);
		RecipeHelper.registerBurnerRecipe(300, Materials.CARBON.createStack(), "cactus", 16);
	}
}
