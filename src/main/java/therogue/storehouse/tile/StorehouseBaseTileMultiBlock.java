/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.tile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import therogue.storehouse.Storehouse;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.multiblock.block.EnergyCapabilityWrapper;
import therogue.storehouse.multiblock.block.ICapabilityWrapper;
import therogue.storehouse.multiblock.block.ItemCapabilityWrapper;
import therogue.storehouse.multiblock.structure.MultiBlockStructure;
import therogue.storehouse.multiblock.tile.IMultiBlockController;
import therogue.storehouse.multiblock.tile.InWorldUtils;
import therogue.storehouse.multiblock.tile.InWorldUtils.MultiBlockFormationResult;
import therogue.storehouse.multiblock.tile.WorldStates;
import therogue.storehouse.multiblock.tile.WorldStates.MultiBlockData;
import therogue.storehouse.network.CGuiUpdateTEPacket;
import therogue.storehouse.network.StorehousePacketHandler;

public abstract class StorehouseBaseTileMultiBlock extends StorehouseBaseMachine implements IMultiBlockController {
	
	private boolean isFormed = false;
	private boolean breaking = false;
	protected List<WorldStates> components = null;
	protected MultiBlockData multiBlockStructureData = null;
	protected Map<BlockPos, Map<Capability<?>, ICapabilityWrapper<?>>> multiblockCapabilities = new HashMap<>();
	protected boolean activationLock = false;
	protected List<MultiBlockStructure> structures;
	
	public StorehouseBaseTileMultiBlock (IStorehouseBaseBlock block) {
		super(block);
		structures = block.getMultiblockStructures();
	}
	
	// -------------------------IMultiblockController methods-----------------------------------
	/**
	 * Forms the Multiblock
	 */
	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX,
			float hitY, float hitZ) {
		if (activationLock) return false;
		if (super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ)) return true;
		if (!isFormed)
		{
			MultiBlockFormationResult result = InWorldUtils.formMultiBlock(getController());
			if (result.formed)
			{
				isFormed = true;
				components = result.components;
				multiBlockStructureData = result.info;
				for (WorldStates worldState : components)
				{
					multiblockCapabilities.put(worldState.position, worldState.capabilites.stream().collect(Collectors.toMap( (ICapabilityWrapper<?> c) -> c.getSupportedCapability(), (
							ICapabilityWrapper<?> w) -> w)));
				}
				initTickingCapabilities();
				if (!world.isRemote)
				{
					player.sendStatusMessage(new TextComponentTranslation("chatmessage.storehouse:multiblock_formed"), false);
					StorehousePacketHandler.INSTANCE.sendTo(getCGUIPacket(), (EntityPlayerMP) player);
				}
				return true;
			}
			return false;
		}
		if (!pos.equals(this.pos))
		{
			activationLock = true;
			IBlockState thisState = this.world.getBlockState(this.pos);
			boolean flag = thisState.getBlock().onBlockActivated(this.world, this.pos, thisState, player, hand, side, hitX, hitY, hitZ);
			activationLock = false;
			if (flag) return true;
		}
		return false;
	}
	
	/**
	 * Breaks the MultiBlock
	 */
	@Override
	public void breakBlock (World worldIn, BlockPos at, IBlockState state) {
		if (isFormed && !breaking)
		{
			breaking = true;
			InWorldUtils.removeMultiBlock(this, components, at);
			breaking = false;
			isFormed = false;
			components = null;
			multiBlockStructureData = null;
			multiblockCapabilities = new HashMap<>();
			initTickingCapabilities();
			if (!world.isRemote) StorehousePacketHandler.INSTANCE.sendToAll(getCGUIPacket());
		}
		super.breakBlock(worldIn, at, state);
	}
	
	private void initTickingCapabilities () {
		if (world != null && world.isRemote) return;
		energyStorage.sendEnergyFrom.clear();
		inventory.sendItemsFrom.clear();
		if (components == null) return;
		for (WorldStates state : components)
		{
			for (ICapabilityWrapper<?> cap : state.capabilites)
			{
				if (cap == EnergyCapabilityWrapper.EXTRACT)
				{
					energyStorage.sendEnergyFrom.add(state.position);
					TileTickingRegistry.INSTANCE.registerTickingModule(energyStorage);
				}
				if (cap == ItemCapabilityWrapper.EXTRACT || cap == ItemCapabilityWrapper.BOTH)
				{
					inventory.sendItemsFrom.add(state.position);
					TileTickingRegistry.INSTANCE.registerTickingModule(inventory);
				}
			}
		}
	}
	
	/**
	 * Called when this is first added to the world (by {@link World#addTileEntity(TileEntity)}).
	 * Override instead of adding {@code if (firstTick)} stuff in update.
	 */
	@Override
	public void onLoad () {
		super.onLoad();
		if (!world.isRemote) StorehousePacketHandler.INSTANCE.sendToAll(getCGUIPacket());
	}
	
	public boolean canOpenGui (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX,
			float hitY, float hitZ) {
		return isFormed;
	}
	
	/**
	 * @return if this multiblock is formed
	 */
	@Override
	public boolean isFormed () {
		return isFormed;
	}
	
	/**
	 * Internal method for IMultiBlockController
	 */
	@Override
	public StorehouseBaseTileEntity getTile () {
		return this;
	}
	
	@Override
	public List<MultiBlockStructure> getPossibleStructures () {
		return structures;
	}
	
	// --------------------------IGuiSupplier Methods-----------------------------------
	@Override
	public String getGuiName () {
		return "multiblock." + Storehouse.RESOURCENAMEPREFIX + block.getName() + ".name";
	}
	
	// -------------------------Standard TE methods-----------------------------------
	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("formed", isFormed);
		if (isFormed)
		{
			nbt.setTag("MultiBlockData", MultiBlockData.writeToNBT(multiBlockStructureData));
			nbt.setTag("PositionStateChangers", WorldStates.writeToNBT(components));
		}
		return nbt;
	}
	
	@Override
	public void readFromNBT (NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		isFormed = nbt.getBoolean("formed");
		if (isFormed)
		{
			multiBlockStructureData = MultiBlockData.readFromNBT(nbt.getCompoundTag("MultiBlockData"));
			components = WorldStates.readFromNBT(multiBlockStructureData, this, (NBTTagList) nbt.getTag("PositionStateChangers"));
			for (WorldStates worldState : components)
			{
				multiblockCapabilities.put(worldState.position, worldState.capabilites.stream().collect(Collectors.toMap( (ICapabilityWrapper<?> c) -> c.getSupportedCapability(), (
						ICapabilityWrapper<?> w) -> w)));
			}
			initTickingCapabilities();
		}
	}
	
	public CGuiUpdateTEPacket getCGUIPacket () {
		CGuiUpdateTEPacket packet = super.getCGUIPacket();
		packet.getNbt().setBoolean("formed", isFormed);
		return packet;
	}
	
	public void processCGUIPacket (CGuiUpdateTEPacket packet) {
		super.processCGUIPacket(packet);
		isFormed = packet.getNbt().getBoolean("formed");
	}
	
	@Override
	public boolean hasCapability (Capability<?> capability, EnumFacing facing, ModuleContext context) {
		return hasCapability(this.pos, capability, facing, context);
	}
	
	@Override
	public <T> T getCapability (Capability<T> capability, EnumFacing facing, ModuleContext context) {
		return getCapability(this.pos, capability, facing, context);
	}
	
	@Override
	public boolean hasCapability (BlockPos pos, Capability<?> capability, EnumFacing facing, ModuleContext context) {
		if (context != ModuleContext.SIDE && context != ModuleContext.OTHER) return super.hasCapability(capability, facing, context);
		if (!isFormed) return false;
		if (multiblockCapabilities.containsKey(pos) && multiblockCapabilities.get(pos).containsKey(capability))
			return super.hasCapability(capability, facing, context);
		return false;
	}
	
	@Override
	public <T> T getCapability (BlockPos pos, Capability<T> capability, EnumFacing facing, ModuleContext context) {
		if (!hasCapability(pos, capability, facing, context)) return null;
		if (context != ModuleContext.SIDE && context != ModuleContext.OTHER) return super.getCapability(capability, facing, context);
		@SuppressWarnings ("unchecked")
		ICapabilityWrapper<T> wrapper = (ICapabilityWrapper<T>) multiblockCapabilities.get(pos).get(capability);
		return wrapper.getWrappedCapability(super.getCapability(capability, facing, context));
	}
}
