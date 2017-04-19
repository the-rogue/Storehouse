/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.block.machine;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import therogue.storehouse.block.StorehouseBaseBlock;
import therogue.storehouse.reference.General;
import therogue.storehouse.tile.StorehouseBaseTileEntity;

public abstract class StorehouseBaseMachine extends StorehouseBaseBlock implements ITileEntityProvider
{
	private List<String> shiftInfo = new ArrayList<String>();
	
	public StorehouseBaseMachine(String name)
	{
		super(name, 6.0F, 20.0F);
	}
	
	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
		if (worldIn.getTileEntity(pos) instanceof IInventory) {
			InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)worldIn.getTileEntity(pos));
		}
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }
    
	@Override
    @SuppressWarnings("deprecation")
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
    }
    
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean debug) {
    	if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
    		for (String s : shiftInfo) {
    			list.add(s);
    		}
    	} else {
    		list.add(General.SHIFTINFO);
    	}
    }
    
    protected void addShiftInfo(String info) {
    	shiftInfo.add(info);
    }

	public List<String> getShiftInfo()
	{
		return shiftInfo;
	}
	
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            ((StorehouseBaseTileEntity) worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());
        }
    }
}
