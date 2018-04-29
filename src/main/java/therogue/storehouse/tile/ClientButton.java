
package therogue.storehouse.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import therogue.storehouse.network.SGuiUpdateTEPacket;
import therogue.storehouse.network.StorehousePacketHandler;
import therogue.storehouse.util.GeneralUtils;

public class ClientButton<T extends Enum<T>> implements IButton<T>, ITileModule {
	
	private StorehouseBaseTileEntity owner;
	private int moduleListPos = -1;
	public static final String NBT_KEY = "CBSetting";
	public final String name;
	public final Class<T> enumClass;
	public final T defaultMode;
	private T mode;
	private List<Runnable> updateProcedures = new ArrayList<>();
	
	public ClientButton (String name, Class<T> enumClass) {
		this(name, enumClass, GeneralUtils.getEnumFromNumber(enumClass, 0));
	}
	
	public ClientButton (String name, Class<T> enumClass, T defaultMode) {
		this.name = name;
		this.enumClass = enumClass;
		this.defaultMode = defaultMode;
		this.mode = defaultMode;
	}
	
	@Override
	public void setTileData (StorehouseBaseTileEntity owner, int moduleListPos) {
		this.owner = owner;
		this.moduleListPos = moduleListPos;
	}
	
	public ClientButton<T> setOnUpdate (Runnable updateProcedure) {
		updateProcedures.add(updateProcedure);
		return this;
	}
	
	public T getMode () {
		return mode;
	}
	
	@Override
	public int getOrdinal () {
		return getMode().ordinal();
	}
	
	public void setOrdinal (int field) {
		setMode(GeneralUtils.getEnumFromNumber(enumClass, field));
	}
	
	public void setMode (T mode) {
		if (mode != this.mode)
		{
			this.mode = mode != null ? mode : this.mode;
			updateProcedures.forEach(r -> r.run());
			if (owner != null) owner.notifyChange(CapabilityButton.BUTTON);
			if (moduleListPos != -1)
				StorehousePacketHandler.INSTANCE.sendToServer(new SGuiUpdateTEPacket(moduleListPos, owner.getPos(), writeModuleToNBT(new NBTTagCompound())));
		}
	}
	
	@Override
	public void pressed () {
		setOrdinal(mode.ordinal() + 1);
	}
	
	@Override
	public NBTTagCompound readModuleFromNBT (NBTTagCompound nbt) {
		setOrdinal(nbt.getInteger(NBT_KEY + name));
		return nbt;
	}
	
	@Override
	public NBTTagCompound writeModuleToNBT (NBTTagCompound nbt) {
		nbt.setInteger(NBT_KEY + name, mode.ordinal());
		return nbt;
	}
	
	/**
	 * @param capability the capability to test
	 * @param facing the direction to test it in
	 * @return whether the capability is applicable in this direction
	 */
	@Override
	public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityButton.BUTTON;
	}
	
	@SuppressWarnings ("unchecked")
	@Override
	public <R> R getCapability (Capability<R> capability, EnumFacing facing, ModuleContext capacity) {
		return (R) this;
	}
	
	public static enum DefaultButton {
		ONE,
		TWO,
		THREE,
		FOUR,
		FIVE,
		SIX,
		SEVEN,
		EIGHT,
		NINE,
		TEN;
	}
	
	public static class CapabilityButton {
		
		@CapabilityInject (IButton.class)
		public static Capability<IButton<?>> BUTTON = null;
		
		@SuppressWarnings ("rawtypes")
		public static void register () {
			CapabilityManager.INSTANCE.register(IButton.class, new IStorage<IButton>() {
				
				@Override
				public NBTBase writeNBT (Capability<IButton> capability, IButton instance, EnumFacing side) {
					return new NBTTagCompound();
				}
				
				@Override
				public void readNBT (Capability<IButton> capability, IButton instance, EnumFacing side, NBTBase nbt) {
				}
			}, () -> new ClientButton<DefaultButton>("factory", DefaultButton.class));
		}
	}
}
