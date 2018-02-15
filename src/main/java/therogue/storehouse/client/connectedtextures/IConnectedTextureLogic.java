
package therogue.storehouse.client.connectedtextures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public interface IConnectedTextureLogic {
	
	/**
	 * @return a function to determine whether this block can connect to the blockstate in the given direction
	 */
	public default IConnectionTest canConnect () {
		return IConnectionTest.DEFAULT;
	}
	
	/**
	 * If true, it will allow connections to other blockstate variations of the same block.
	 */
	public default boolean ignoreStates () {
		return true;
	}
	
	public ResourceLocation getModelLocation ();
	
	public List<ResourceLocation> getTextures ();
	
	public default Map<ResourceLocation, ResourceLocation> getConnectionTextureMap () {
		Map<ResourceLocation, ResourceLocation> map = new HashMap<>();
		for (ResourceLocation unconnected : getTextures())
		{
			map.put(unconnected, new ResourceLocation(unconnected.getResourceDomain(), unconnected.getResourcePath() + "-connected"));
		}
		return map;
	}
	
	public interface IConnectionTest {
		
		public static IConnectionTest DEFAULT = (ignoreStates, from, to, dir) -> (ignoreStates ? from.getBlock() == to.getBlock() : from == to);
		
		public boolean connects (boolean ignoreStates, IBlockState from, IBlockState to, EnumFacing dir);
	}
}
