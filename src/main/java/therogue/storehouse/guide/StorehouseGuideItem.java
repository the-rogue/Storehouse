/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.guide;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import therogue.storehouse.core.Storehouse;
import therogue.storehouse.item.StorehouseBaseActiveItem;
import therogue.storehouse.reference.IDs;

public class StorehouseGuideItem extends StorehouseBaseActiveItem {
	
	public StorehouseGuideItem (String name) {
		super(name);
		this.setMaxStackSize(1);
		this.addShiftInfo("A Guide to the Workings of Storehouse");
		this.addShiftInfo("Right Click to Open");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick (ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		player.openGui(Storehouse.instance, IDs.STOREHOUSEGUIDEGUI, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}
	
	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public EnumActionResult onItemUse (ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return super.onItemUse(stack, player, world, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public boolean shouldCauseReequipAnimation (ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}
}
