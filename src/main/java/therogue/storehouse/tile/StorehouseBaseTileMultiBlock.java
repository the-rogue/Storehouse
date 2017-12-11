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

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.tile.multiblock.IMultiBlockController;
import therogue.storehouse.tile.multiblock.MultiBlockFormationHandler;
import therogue.storehouse.tile.multiblock.MultiBlockFormationHandler.MultiBlockFormationResult;
import therogue.storehouse.tile.multiblock.MultiBlockFormationHandler.PositionStateChanger;

public abstract class StorehouseBaseTileMultiBlock extends StorehouseBaseMachine implements IMultiBlockController {
	
	private boolean isFormed = false;
	private boolean breaking = false;
	protected List<PositionStateChanger> components = null;
	protected Map<BlockPos, List<Capability<?>>> multiblockCapabilites;
	
	public StorehouseBaseTileMultiBlock (IStorehouseBaseBlock block) {
		super(block);
	}
	
	// -------------------------IMultiblockController methods-----------------------------------
	/**
	 * Forms the Multiblock
	 */
	@Override
	public boolean onMultiBlockActivatedAt (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side) {
		if (!world.isRemote && !isFormed)
		{
			MultiBlockFormationResult result = MultiBlockFormationHandler.formMultiBlock(getController());
			if (result.formed)
			{
				isFormed = true;
				components = result.components;
				multiblockCapabilites = MultiBlockFormationHandler.getWorldMultiblockCapabilities(components);
				player.sendStatusMessage(new TextComponentTranslation("chatmessage.storehouse:multiblock_formed"), false);
			}
			return isFormed;
		}
		return false;
	}
	
	/**
	 * Breaks the MultiBlock
	 */
	@Override
	public void onBlockBroken (@Nullable BlockPos at) {
		if (!world.isRemote && isFormed && !breaking)
		{
			breaking = true;
			MultiBlockFormationHandler.removeMultiBlock(this, components, at);
			breaking = false;
			isFormed = false;
			components = null;
			multiblockCapabilites = null;
		}
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
	public TileEntity getTile () {
		return this;
	}
	
	// -------------------------Standard TE methods-----------------------------------
	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("formed", isFormed);
		if (isFormed) nbt.setTag("PositionStateChangers", MultiBlockFormationHandler.PositionStateChanger.writeToNBT(components));
		return nbt;
	}
	
	@Override
	public void readFromNBT (NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		isFormed = nbt.getBoolean("formed");
		if (isFormed) components = MultiBlockFormationHandler.PositionStateChanger.readFromNBT((NBTTagList) nbt.getTag("PositionStateChangers"));
	}
	
	@Override
	public boolean hasCapability (BlockPos pos, Capability<?> capability, EnumFacing facing) {
		if (multiblockCapabilites == null)
		{
			multiblockCapabilites = MultiBlockFormationHandler.getWorldMultiblockCapabilities(components);
		}
		if (multiblockCapabilites.containsKey(pos) && multiblockCapabilites.get(pos).contains(capability)) return super.hasCapability(capability, facing);
		return false;
	}
	
	@Override
	public <T> T getCapability (BlockPos pos, Capability<T> capability, EnumFacing facing) {
		if (!hasCapability(pos, capability, facing)) return null;
		return getTile().getCapability(capability, facing);
	}
}
