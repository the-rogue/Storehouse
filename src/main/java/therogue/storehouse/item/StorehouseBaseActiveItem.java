/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.item;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import therogue.storehouse.Storehouse;

public abstract class StorehouseBaseActiveItem extends StorehouseBaseItem {
	
	private List<String> shiftInfo = new ArrayList<String>();
	
	public StorehouseBaseActiveItem (String name) {
		super(name);
	}
	
	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@SideOnly (Side.CLIENT)
	public void addInformation (ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			for (String s : shiftInfo)
			{
				tooltip.add(s);
			}
		}
		else
		{
			tooltip.add(Storehouse.SHIFTINFO);
		}
	}
	
	protected void addShiftInfo (String info) {
		shiftInfo.add(info);
	}
	
	public List<String> getShiftInfo () {
		return shiftInfo;
	}
}
