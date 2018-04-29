
package therogue.storehouse.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class TileData implements IDataHandler, ITileModule {
	
	private List<Supplier<Integer>> fields;
	
	public TileData (List<Supplier<Integer>> fields) {
		this.fields = fields;
	}
	
	public TileData () {
		fields = new ArrayList<>();
	}
	
	public void addField (Supplier<Integer> field) {
		fields.add(field);
	}
	
	/**
	 * Number of gui fields required
	 * 
	 * @return the number of fields
	 */
	@Override
	public int fieldCount () {
		return fields.size();
	}
	
	/**
	 * Get a gui Field
	 * 
	 * @param index
	 * 
	 * @return the field
	 */
	@Override
	public int getField (int index) {
		if (index < 0 || index >= fields.size()) return 0;
		return fields.get(index).get();
	}
	
	/**
	 * @param capability the capability to test
	 * @param facing the direction to test it in
	 * @return whether the capability is applicable in this direction
	 */
	@Override
	public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityDataHandler.DATAHANDLER;
	}
	
	@SuppressWarnings ("unchecked")
	@Override
	public <T> T getCapability (Capability<T> capability, EnumFacing facing, ModuleContext capacity) {
		return (T) this;
	}
	
	public static class CapabilityDataHandler {
		
		@CapabilityInject (IDataHandler.class)
		public static Capability<IDataHandler> DATAHANDLER = null;
		
		public static void register () {
			CapabilityManager.INSTANCE.register(IDataHandler.class, new IStorage<IDataHandler>() {
				
				@Override
				public NBTBase writeNBT (Capability<IDataHandler> capability, IDataHandler instance, EnumFacing side) {
					return new NBTTagCompound();
				}
				
				@Override
				public void readNBT (Capability<IDataHandler> capability, IDataHandler instance, EnumFacing side, NBTBase nbt) {
				}
			}, () -> new TileData());
		}
	}
}
