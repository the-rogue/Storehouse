
package therogue.storehouse.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public interface ITileModule {
	
	/**
	 * Do Stuff Where accessing the world is needed
	 */
	public default void onLoad () {
	}
	
	/**
	 * When the tile is removed from the world (i.e. invalidated)
	 */
	public default void onRemove () {
	}
	/**
	 * Number of gui fields required
	 * 
	 * @return the number of fields
	 */
	// public default int fieldCount () {
	// return 0;
	// }
	/**
	 * Get a gui Field
	 * 
	 * @param index
	 * 
	 * @return the field
	 */
	// public default int getField (int index) {
	// return 0;
	// }
	
	/**
	 * Set a gui Field (if it is settable)
	 * 
	 * @param field the field value
	 */
	// public default void setField (int index, int field) {
	// }
	/**
	 * Writes the data of the inventory to nbt, to be loaded back later
	 * 
	 * @param nbt The NBTTagCompound to write to.
	 * @return TODO
	 */
	public default NBTTagCompound writeModuleToNBT (NBTTagCompound nbt) {
		return nbt;
	}
	
	/**
	 * Reads Data back from an nbt tag, after it has been loaded back
	 * 
	 * @param nbt The nbt tag to read from.
	 * @return TODO
	 */
	public default NBTTagCompound readModuleFromNBT (NBTTagCompound nbt) {
		return nbt;
	}
	
	/**
	 * @param capability the capability to test
	 * @param facing the direction to test it in
	 * @return whether the capability is applicable in this direction
	 */
	public default boolean hasCapability (Capability<?> capability, EnumFacing facing) {
		return false;
	}
	
	public default <T> T getCapability (Capability<T> capability, EnumFacing facing, ModuleContext capacity) {
		return null;
	}
	
	public default void onOtherChange (Capability<?> changedCapability) {
	}
	
	public default void setTileData (ITile owner, int moduleListPos) {
	}
}
