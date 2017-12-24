/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.block;

import java.util.List;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.capabilities.Capability;
import therogue.storehouse.capabilitywrapper.ICapabilityWrapper;
import therogue.storehouse.multiblock.block.IMultiBlockCapabilityBlock;

public class StorehouseBaseCapabilityVariantBlock extends StorehouseBaseVariantBlock implements IMultiBlockCapabilityBlock {
	
	private List<Map<Capability<?>, ICapabilityWrapper<?>>> capabilities;
	
	public StorehouseBaseCapabilityVariantBlock (String name, List<Map<Capability<?>, ICapabilityWrapper<?>>> capabilities) {
		super(name);
		this.capabilities = capabilities;
	}
	
	@Override
	public Map<Capability<?>, ICapabilityWrapper<?>> getCapabilities (IBlockState state) {
		int meta = state.getBlock().getMetaFromState(state);
		if (meta < 0 || meta >= capabilities.size()) throw new RuntimeException("Cannot get the right tile, meta out of range given to BlockSpecialMultiblock");
		return capabilities.get(meta);
	}
}
