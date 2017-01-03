/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.energy;

import net.minecraftforge.energy.EnergyStorage;

public class EnergyStorageAdv extends EnergyStorage
{

	public EnergyStorageAdv(int capacity) { super(capacity); }

	public EnergyStorageAdv(int capacity, int maxTransfer) { super(capacity, maxTransfer); }

	public EnergyStorageAdv(int capacity, int maxReceive, int maxExtract) { super(capacity, maxReceive, maxExtract); }
	
	public void increase() {
		this.receiveEnergy(maxReceive, false);
	}

}
