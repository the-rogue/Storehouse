/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.tile;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.network.CGuiUpdateTEPacket;
import therogue.storehouse.network.SGuiUpdateTEPacket;

public abstract class StorehouseBaseTileEntity extends TileEntity implements IWorldNameable, ITile {
	
	protected List<ITileModule> modules = new ArrayList<>();
	private String customName;
	protected IStorehouseBaseBlock block;
	
	public StorehouseBaseTileEntity (IStorehouseBaseBlock block) {
		this.block = block;
	}
	
	// -------------------------IWorldNamable Methods-----------------------------------
	/**
	 * Get the formatted ChatComponent that will be used for the sender's username in chat
	 */
	@Override
	public ITextComponent getDisplayName () {
		return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
	}
	
	/**
	 * Returns true if this thing is named
	 */
	@Override
	public boolean hasCustomName () {
		return this.customName != null && !this.customName.isEmpty();
	}
	
	/**
	 * Get the name of this object.
	 */
	@Override
	public String getName () {
		return hasCustomName() ? customName : "container.storehouse:" + block.getName();
	}
	
	/**
	 * Set the name of this object.
	 */
	public void setCustomName (String customName) {
		this.customName = customName;
	}
	
	// -------------------------Standard TE methods-----------------------------------
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX,
			float hitY, float hitZ) {
		return false;
	}
	
	public boolean canOpenGui (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX,
			float hitY, float hitZ) {
		return true;
	}
	
	/**
	 * Called when this is first added to the world (by {@link World#addTileEntity(TileEntity)}).
	 * Override instead of adding {@code if (firstTick)} stuff in update.
	 */
	@Override
	public void onLoad () {
		super.onLoad();
		this.modules = ImmutableList.copyOf(modules);
		modules.forEach(module -> module.setTileData(this, modules.indexOf(module)));
	}
	
	public void breakBlock (World worldIn, BlockPos pos, IBlockState state) {
	}
	
	public CGuiUpdateTEPacket getCGUIPacket () {
		CGuiUpdateTEPacket packet = new CGuiUpdateTEPacket(this.getPos(), new NBTTagCompound());
		modules.forEach(module -> module.writeModuleToNBT(packet.getNbt()));
		return packet;
	}
	
	public void processCGUIPacket (CGuiUpdateTEPacket packet) {
		modules.forEach(module -> module.readModuleFromNBT(packet.getNbt()));
	}
	
	public void processSGUIPacket (SGuiUpdateTEPacket message, EntityPlayerMP from) {
		modules.get(message.getItem()).readModuleFromNBT(message.getNbt());
	}
	
	@Override
	public void notifyChange (Capability<?> changedCapability) {
		if (world.isRemote) return;
		modules.forEach(module -> module.onOtherChange(changedCapability));
	}
	
	@Override
	public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
		for (ITileModule module : modules)
			if (module.hasCapability(capability, facing)) return true;
		return super.hasCapability(capability, facing);
	}
	
	@Deprecated // Use Context Sensitive version below
	@Override
	public final <T> T getCapability (Capability<T> capability, EnumFacing facing) {
		return this.getCapability(capability, facing, ModuleContext.SIDE);
	}
	
	@Override
	public <T> T getCapability (Capability<T> capability, EnumFacing facing, ModuleContext context) {
		for (ITileModule module : modules)
			if (module.hasCapability(capability, facing)) return module.getCapability(capability, facing, context);
		return super.getCapability(capability, facing);
	}
	
	public BlockPos getTilePosition () {
		return this.pos;
	}
	
	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (this.hasCustomName())
		{
			nbt.setString("CustomName", this.getName());
		}
		modules.forEach(module -> module.writeModuleToNBT(nbt));
		return nbt;
	}
	
	@Override
	public void readFromNBT (NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("CustomName", 8))
		{
			this.setCustomName(nbt.getString("CustomName"));
		}
		modules.forEach(module -> module.readModuleFromNBT(nbt));
	}
}
