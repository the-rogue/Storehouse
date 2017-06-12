/*
 * This file is part of Storehouse. Copyright (c) 2017, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.crafting.wrapper;

import net.minecraftforge.fluids.FluidStack;
import therogue.storehouse.util.FluidUtils;

public class FluidStackComponent implements IRecipeComponent {
	
	private FluidStack stack;
	
	public FluidStackComponent (FluidStack stack) {
		this.stack = stack;
	}
	
	public FluidStack getComponent () {
		return stack.copy();
	}
	
	@Override
	public boolean isUnUsed () {
		return stack.amount <= 0;
	}
	
	@Override
	public boolean matches (IRecipeWrapper component) {
		if (!(component instanceof FluidStackWrapper)) return false;
		return FluidUtils.areStacksMergable(stack, ((FluidStackWrapper) component).getStack()) && this.stack.amount <= ((FluidStackWrapper) component).getStack().amount;
	}
	
	@Override
	public int getSize () {
		return stack.amount;
	}
	
	@Override
	public IRecipeWrapper getWrapper () {
		return new FluidStackWrapper(stack);
	}
	
	@Override
	public IRecipeComponent copy () {
		return new FluidStackComponent(stack);
	}
	
	@Override
	public IRecipeWrapper getResidue () {
		return IRecipeWrapper.NOTHING;
	}
}
