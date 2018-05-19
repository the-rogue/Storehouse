/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.tile.machine;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import therogue.storehouse.init.ModBlocks;
import therogue.storehouse.inventory.InventoryManager;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.StorehouseBaseMachine;

public class TilePotionInjector extends StorehouseBaseMachine {
	
	public static final int RFPerTick = 80;
	private TilePotionBrewer theBrewer;
	
	public TilePotionInjector () {
		super(ModBlocks.potion_injector);
		this.setInventory(new InventoryManager(this, 1, new Integer[] { 0 }, new Integer[] { 0 }));
	}
	
	@Override
	public void invalidate () {
		super.invalidate();
		if (theBrewer == null) return;
		theBrewer.removeInjector(this);
	}
	
	@Override
	public void validate () {
		super.validate();
		if (theBrewer == null) return;
		theBrewer.addInjector(this);
	}
	
	/**
	 * Called when this is first added to the world (by {@link World#addTileEntity(TileEntity)}).
	 * Override instead of adding {@code if (firstTick)} stuff in update.
	 */
	@Override
	public void onLoad () {
		super.onLoad();
		IBlockState thisState = world.getBlockState(getPos());
		EnumFacing facing = thisState.getValue(BlockHorizontal.FACING);
		for (int i = 1; i < 5; i++)
		{
			BlockPos checkPos = this.getPos().offset(facing, i);
			TileEntity te = world.getTileEntity(checkPos);
			if (te instanceof TilePotionBrewer)
			{
				TilePotionBrewer tebrew = ((TilePotionBrewer) te);
				theBrewer = tebrew;
				theBrewer.addInjector(this);
			}
		}
	}
	
	protected ItemStack getNext () {
		return this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL).getStackInSlot(0);
	}
	
	protected ItemStack takeNext () {
		IItemHandler internalView = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL);
		ItemStack returnStack = internalView.extractItem(0, -1, false);
		for (int i = 1; i < inventory.getSlots(); i++)
		{
			internalView.insertItem(i - 1, internalView.extractItem(i, -1, false), false);
		}
		return returnStack;
	}
}
