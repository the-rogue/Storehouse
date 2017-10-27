
package therogue.storehouse.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import therogue.storehouse.crafting.RecipeHelper;
import therogue.storehouse.crafting.wrapper.ItemStackComponent;
import therogue.storehouse.crafting.wrapper.ItemStackComponent.ItemStackRemainComponent;
import therogue.storehouse.init.grouped.Materials;
import therogue.storehouse.init.grouped.Ores;
import therogue.storehouse.init.grouped.Resources;

public class Recipes {
	
	public static void Init () {
		RecipeHelper.registerTwoWayBlockRecipe(new ItemStack(ModBlocks.azurite_crystal_block.block), Resources.AZURITE_CRYSTAL.createStack(), "blockazuritecrystal", null);
		RecipeHelper.registerTwoWayBlockRecipe(new ItemStack(ModBlocks.azurite_dust_block.block), Resources.AZURITE_DUST.createStack(), null, null);
		GameRegistry.addSmelting(Ores.COPPER.createStack(), Resources.COPPER_INGOT.createStack(), 0.6F);
		GameRegistry.addSmelting(Ores.TIN.createStack(), Resources.TIN_INGOT.createStack(), 0.7F);
		GameRegistry.addSmelting(Ores.ALUMINIUM.createStack(), Resources.ALUMINIUM_INGOT.createStack(), 0.7F);
		GameRegistry.addSmelting(Ores.SILVER.createStack(), Resources.SILVER_INGOT.createStack(), 0.7F);
		GameRegistry.addSmelting(Ores.LEAD.createStack(), Resources.LEAD_INGOT.createStack(), 0.7F);
		GameRegistry.addSmelting(Ores.TITANIUM.createStack(), Resources.TITANIUM_INGOT.createStack(), 0.7F);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.azurite_crystal_block_chiseled), ModBlocks.azurite_crystal_block.half_slab, ModBlocks.azurite_crystal_block.half_slab);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.azurite_crystal_block_pillar), ModBlocks.azurite_crystal_block.block, ModBlocks.azurite_crystal_block.block);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.azurite_dust_block.block), ModBlocks.azurite_dust_block.half_slab, ModBlocks.azurite_dust_block.half_slab);
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
	}
}
