
package therogue.storehouse.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import therogue.storehouse.crafting.MachineCraftingHandler;
import therogue.storehouse.crafting.MachineRecipe;
import therogue.storehouse.crafting.wrapper.FluidStackComponent;
import therogue.storehouse.crafting.wrapper.ItemStackComponent;
import therogue.storehouse.init.grouped.Materials;
import therogue.storehouse.init.grouped.Ores;
import therogue.storehouse.init.grouped.Resources;
import therogue.storehouse.tile.machine.TileCrystaliser;
import therogue.storehouse.tile.machine.TileForge;
import therogue.storehouse.tile.machine.TileThermalPress;
import therogue.storehouse.util.RecipeHelper;

public class Recipes {
	
	public static void Init () {
		RecipeHelper.registerTwoWayBlockRecipe(new ItemStack(ModBlocks.azurite_crystal_block.block), Resources.AZURITE_CRYSTAL.createStack(), "blockazuritecrystal", null);
		RecipeHelper.registerTwoWayBlockRecipe(new ItemStack(ModBlocks.azurite_dust_block.block), Resources.AZURITE_DUST.createStack(), null, null);
		GameRegistry.addSmelting(Ores.COPPER.createStack(), Resources.COPPER_INGOT.createStack(), 0.6F);
		GameRegistry.addSmelting(Ores.TIN.createStack(), Resources.TIN_INGOT.createStack(), 0.7F);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.azurite_crystal_block_chiseled), ModBlocks.azurite_crystal_block.half_slab, ModBlocks.azurite_crystal_block.half_slab);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.azurite_crystal_block_pillar), ModBlocks.azurite_crystal_block.block, ModBlocks.azurite_crystal_block.block);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.azurite_dust_block.block), ModBlocks.azurite_dust_block.half_slab, ModBlocks.azurite_dust_block.half_slab);
		MachineCraftingHandler.register(TileCrystaliser.class,
				new MachineRecipe(MachineRecipe.ALWAYSMODE, 80, new ItemStackComponent(Resources.AZURITE_CRYSTAL.createStack()), new ItemStackComponent(Resources.AZURITE_DUST.createStack()), new FluidStackComponent(new FluidStack(FluidRegistry.WATER, 1000))));
		MachineCraftingHandler.register(TileThermalPress.class, new MachineRecipe(TileThermalPress.Mode.HIGH_PRESSURE.modeTest, 40, new ItemStackComponent(Items.DIAMOND), new ItemStackComponent(Resources.AZURITE_DUST.createStack()), new ItemStackComponent(Resources.AZURITE_DUST.createStack()),
				new ItemStackComponent(Items.IRON_INGOT), new ItemStackComponent(Items.REDSTONE), new ItemStackComponent(Items.REDSTONE)));
		MachineCraftingHandler.register(TileForge.class, new MachineRecipe(MachineRecipe.ALWAYSMODE, 20, new ItemStackComponent(Materials.COPPER_PLATE.createStack()), new ItemStackComponent(Resources.COPPER_INGOT.createStack())));
		MachineCraftingHandler.register(TileForge.class, new MachineRecipe(MachineRecipe.ALWAYSMODE, 20, new ItemStackComponent(Materials.IRON_PLATE.createStack()), new ItemStackComponent(Items.IRON_INGOT)));
	}
}
