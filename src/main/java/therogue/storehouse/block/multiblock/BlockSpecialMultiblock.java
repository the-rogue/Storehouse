/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.block.multiblock;

import java.util.List;

import net.minecraftforge.common.capabilities.Capability;
import therogue.storehouse.block.StorehouseBaseVariantBlock;

public class BlockSpecialMultiblock extends StorehouseBaseVariantBlock implements ICapabilityMultiblock {
	
	private IMultiblockCapabilityProvider[] blocks;
	
	public BlockSpecialMultiblock (String name, IMultiblockCapabilityProvider[] blocks) {
		super(name);
		this.blocks = blocks;
	}
	
	public List<Capability<?>> getCapabilities (int meta) {
		if (meta < 0 || meta >= blocks.length) throw new RuntimeException("Cannot get the right tile, meta out of range given to BlockSpecialMultiblock");
		return blocks[meta].getCapabilities();
	}
}
