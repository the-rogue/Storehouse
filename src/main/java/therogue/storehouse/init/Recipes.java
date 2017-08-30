
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
import therogue.storehouse.tile.machine.TileCrystaliser;
import therogue.storehouse.tile.machine.TileThermalPress;
import therogue.storehouse.util.RecipeHelper;

public class Recipes {
	
	public static void Init () {
		RecipeHelper.registerTwoWayBlockRecipe(ModBlocks.azurite_crystal_block_itemstack, ModItems.azurite_crystal_itemstack, "blockazuritecrystal", null);
		RecipeHelper.registerTwoWayBlockRecipe(ModBlocks.azurite_dust_block_itemstack, ModItems.azurite_dust_itemstack, null, null);
		GameRegistry.addSmelting(ModBlocks.copper_ore_itemstack, ModItems.copper_ingot_itemstack, 0.6F);
		GameRegistry.addSmelting(ModBlocks.tin_ore_itemstack, ModItems.tin_ingot_itemstack, 0.7F);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.azurite_crystal_block_chiseled), ModBlocks.azurite_crystal_block_half_slab, ModBlocks.azurite_crystal_block_half_slab);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.azurite_crystal_block_pillar), ModBlocks.azurite_crystal_block, ModBlocks.azurite_crystal_block);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.azurite_dust_block), ModBlocks.azurite_dust_block_half_slab, ModBlocks.azurite_dust_block_half_slab);
		MachineCraftingHandler.register(TileCrystaliser.class, new MachineRecipe(MachineRecipe.ALWAYSMODE, 80, new ItemStackComponent(ModItems.azurite_crystal_itemstack), new ItemStackComponent(ModItems.azurite_dust_itemstack), new FluidStackComponent(new FluidStack(FluidRegistry.WATER, 1000))));
		MachineCraftingHandler.register(TileThermalPress.class, new MachineRecipe(TileThermalPress.Mode.HIGH_PRESSURE.modeTest, 40, new ItemStackComponent(Items.DIAMOND), new ItemStackComponent(ModItems.azurite_dust_itemstack), new ItemStackComponent(ModItems.azurite_dust_itemstack),
				new ItemStackComponent(Items.IRON_INGOT), new ItemStackComponent(Items.REDSTONE), new ItemStackComponent(Items.REDSTONE)));
	}
}
