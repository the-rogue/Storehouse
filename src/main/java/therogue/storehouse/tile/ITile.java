
package therogue.storehouse.tile;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public interface ITile {
	
	public void notifyChange (Capability<?> changedCapability);
	
	public boolean hasCapability (Capability<?> capability, EnumFacing facing, ModuleContext context);
	
	public <T> T getCapability (Capability<T> capability, EnumFacing facing, ModuleContext context);
	
	public BlockPos getTilePosition ();
	
	public World getTileWorld ();
}
