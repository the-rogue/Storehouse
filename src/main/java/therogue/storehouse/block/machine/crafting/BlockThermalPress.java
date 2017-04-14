/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.block.machine.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import therogue.storehouse.block.StorehouseBaseTileBlock;
import therogue.storehouse.client.gui.multisystem.IBoundedPage;
import therogue.storehouse.client.gui.multisystem.IEntry;
import therogue.storehouse.client.gui.multisystem.IInfoSupplier;
import therogue.storehouse.client.gui.multisystem.Page;
import therogue.storehouse.client.gui.multisystem.impl.ItemStackEntry;
import therogue.storehouse.tile.machine.crafting.TileThermalPress;
import therogue.storehouse.type.CategoryEnum;

public class BlockThermalPress extends StorehouseBaseTileBlock implements IInfoSupplier {
	
	public BlockThermalPress (String name) {
		super(name);
	}
	
	@Override
	public TileEntity createNewTileEntity (World worldIn, int meta) {
		TileThermalPress tile = new TileThermalPress();
		tile.setWorldObj(worldIn);
		return tile;
	}
	
	@Override
	public IEntry getEntry () {
		return new ItemStackEntry(CategoryEnum.MACHINES.category.name, "Thermal Press", new ItemStack(this, 1, 0)) {
			
			@Override
			public Page getPage (IBoundedPage bounds, int xStart, int yStart, int pageWidth, int pageHeight) {
				Page thisPage = new Page(1);
				return thisPage;
			}
		};
	}
}
