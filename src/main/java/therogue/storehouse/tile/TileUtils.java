
package therogue.storehouse.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileUtils {
	
	public static boolean tryInteractLiquidContainer (World world, BlockPos pos, EntityPlayer player, EnumHand hand) {
		if (player.getHeldItem(hand).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
		{
			IFluidHandler tank = world.getTileEntity(pos).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
			FluidActionResult result = FluidUtil.interactWithFluidHandler(player.getHeldItem(hand), tank, player);
			if (result.isSuccess())
			{
				player.setHeldItem(hand, result.result);
				return true;
			}
		}
		return false;
	}
}
