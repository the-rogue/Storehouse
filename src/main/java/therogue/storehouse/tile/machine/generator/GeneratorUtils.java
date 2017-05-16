/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.tile.machine.generator;

import therogue.storehouse.energy.EnergyStorageAdv;
import therogue.storehouse.tile.MachineTier;
import therogue.storehouse.util.loghelper;

public class GeneratorUtils
{
	public static EnergyStorageAdv getAppropriateEnergyStored(MachineTier tier, int baseGeneration, boolean allowInsert)
	{
		int baseModifier = tier.ordinal();
		int modifier = (int) Math.pow(9, tier.ordinal());
		int baseCapacity = baseGeneration * 3600; // 3 Minutes Worth of capacity
		int baseExtract = baseCapacity / 400; // 20 Seconds to clear buffer
		int baseRecieve = allowInsert ? baseGeneration : 0;
		return new EnergyStorageAdv(baseCapacity * modifier + baseCapacity * baseModifier, baseRecieve * modifier + baseRecieve * baseModifier, baseExtract * modifier + baseExtract * baseModifier);
	}
	
	public static int getTotalModifier(int meta) {
		return getTotalModifier(getTypeFromMeta(meta));
	}
	
	public static int getTotalModifier(MachineTier tier) {
		return getModifier(tier) + getBaseModifier(tier);
	}
	
	public static int getModifier(int meta) {
		return getModifier(getTypeFromMeta(meta));
	}
	
	public static int getModifier(MachineTier tier) {
		return (int) Math.pow(9, tier.ordinal());
	}
	
	public static int getBaseModifier(int meta) {
		return getBaseModifier(getTypeFromMeta(meta));
	}
	
	public static int getBaseModifier(MachineTier tier) {
		return tier.ordinal();
	}
	
	public static int getRecieve(int meta, int baseRecieve)
	{
		return getRecieve(getTypeFromMeta(meta), baseRecieve);
	}
	
	public static int getRecieve(MachineTier tier, int baseRecieve)
	{
		int baseModifier = tier.ordinal();
		int modifier = (int) Math.pow(9, tier.ordinal());
		return baseRecieve * modifier + baseRecieve * baseModifier;
	}
	
	public static int getExtract(int meta, int baseExtract)
	{
		return getExtract(getTypeFromMeta(meta), baseExtract);
	}
	
	public static int getExtract(MachineTier tier, int baseExtract)
	{
		int baseModifier = tier.ordinal();
		int modifier = (int) Math.pow(9, tier.ordinal());
		return baseExtract * modifier + baseExtract * baseModifier;
	}
	
	public static int getCapacity(int meta, int baseCapacity)
	{
		return getCapacity(getTypeFromMeta(meta), baseCapacity);
	}
	
	public static int getCapacity(MachineTier tier, int baseCapacity)
	{
		int baseModifier = tier.ordinal();
		int modifier = (int) Math.pow(9, tier.ordinal());
		return baseCapacity * modifier + baseCapacity * baseModifier;
	}
	
	public static int getMeta(MachineTier tier)
	{
		return tier.ordinal();
	}
	
	public static MachineTier getTypeFromMeta(int meta)
	{
		for (MachineTier g : MachineTier.values())
		{
			if (getMeta(g) == meta) { return g; }
		}
		loghelper.log("warn", "Meta was out of range for Generator Type, This is bad!");
		return MachineTier.basic;
	}
}
