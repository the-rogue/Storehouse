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

public interface IRecipeComponent {
	
	public static final IRecipeComponent NOTHING = new Nothing();
	
	public boolean isUnUsed ();
	
	public boolean matches (IRecipeWrapper component);
	
	public int getSize ();
	
	public IRecipeWrapper getWrapper ();
	
	public IRecipeComponent copy ();
	
	public IRecipeWrapper getResidue ();
	
	public class Nothing implements IRecipeComponent {
		
		private Nothing () {
		}
		
		@Override
		public boolean isUnUsed () {
			return true;
		}
		
		@Override
		public boolean matches (IRecipeWrapper component) {
			return false;
		}
		
		@Override
		public int getSize () {
			return 0;
		}
		
		@Override
		public IRecipeWrapper getWrapper () {
			return IRecipeWrapper.NOTHING;
		}
		
		@Override
		public IRecipeComponent copy () {
			return this;
		}
		
		@Override
		public IRecipeWrapper getResidue () {
			return IRecipeWrapper.NOTHING;
		}
	}
}
