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

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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
import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.network.GuiUpdateTEPacket;

public abstract class StorehouseBaseTileEntity extends TileEntity implements IWorldNameable {
	
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
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		return false;
	}
	
	public void breakBlock (World worldIn, BlockPos pos, IBlockState state) {
	}
	
	public GuiUpdateTEPacket getGUIPacket () {
		return new GuiUpdateTEPacket(this.getPos(), new NBTTagCompound());
	}
	
	public void processGUIPacket (GuiUpdateTEPacket packet) {
	}
	
	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (this.hasCustomName())
		{
			nbt.setString("CustomName", this.getName());
		}
		return nbt;
	}
	
	@Override
	public void readFromNBT (NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("CustomName", 8))
		{
			this.setCustomName(nbt.getString("CustomName"));
		}
	}
}
