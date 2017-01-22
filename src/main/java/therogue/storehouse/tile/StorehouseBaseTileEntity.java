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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorldNameable;
import therogue.storehouse.block.IStorehouseBaseBlock;


/*
 * FIELDS
 */
public abstract class StorehouseBaseTileEntity extends TileEntity implements IWorldNameable
{
	private String customName;
	protected IStorehouseBaseBlock block;

	public StorehouseBaseTileEntity(IStorehouseBaseBlock block)
	{
		this.block = block;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
	}

	@Override
	public boolean hasCustomName()
	{
		return this.customName != null && !this.customName.isEmpty();
	}

	@Override
	public String getName()
	{
		return hasCustomName() ? customName : "container.storehouse:" + block.getName();
	}

	public void setCustomName(String customName)
	{
		this.customName = customName;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		if (this.hasCustomName())
		{
			nbt.setString("CustomName", this.getName());
		}
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		if (nbt.hasKey("CustomName", 8))
		{
			this.setCustomName(nbt.getString("CustomName"));
		}

	}
}
