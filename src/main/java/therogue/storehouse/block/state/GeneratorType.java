/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.block.state;

import net.minecraft.util.IStringSerializable;
import therogue.storehouse.energy.EnergyStorageAdv;
import therogue.storehouse.util.loghelper;

public enum GeneratorType implements IStringSerializable
{
	basic(0, 0),
	advanced(1, 1),
	infused(2, 2),
	ender(3, 3),
	ultimate(4, 4);
	
	private int meta;
	private int baseModifier;
	private int modifier;
	
	private GeneratorType(int meta, int baseModifier) 
	{
		this.meta = meta;
		this.baseModifier = baseModifier;
		this.modifier = (int)Math.pow(9, baseModifier);
	}
	
	public EnergyStorageAdv getAppropriateEnergyStored(int baseCapacity, int baseRecieve, int baseExtract) 
	{
		
		return new EnergyStorageAdv(baseCapacity * modifier + baseCapacity * baseModifier, baseRecieve * modifier + baseRecieve * baseModifier, baseExtract * modifier + baseExtract * baseModifier);
	}
	public int getRecieve(int baseRecieve) 
	{
		return baseRecieve * modifier + baseRecieve * baseModifier;
	}
	public int getExtract(int baseExtract) 
	{
		return baseExtract * modifier + baseExtract * baseModifier;
	}
	public int getCapacity(int baseCapacity) 
	{
		return baseCapacity * modifier + baseCapacity * baseModifier;
	}

	public int getMeta()
	{
		return meta;
	}

	@Override
	public String getName()
	{
		return this.toString();
	}
	
	public static GeneratorType getTypeFromMeta(int meta) 
	{
		for (GeneratorType g : values()) {
			if (g.getMeta() == meta) {
				return g;
			}
		}
		loghelper.log("warn", "Meta was out of range for Generator Type, This is bad!");
		return GeneratorType.basic;
	}
}
