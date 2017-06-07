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

public interface IRecipeWrapper {
	
	public static final IRecipeWrapper NOTHING = new Nothing();
	
	public boolean isUnUsed ();
	
	public boolean mergable (IRecipeWrapper component, int limit);
	
	public IRecipeWrapper merge (IRecipeWrapper component, int limit);
	
	public boolean canAddComponent (IRecipeComponent component, int limit);
	
	public IRecipeWrapper addComponent (IRecipeComponent component, int limit);
	
	public void increaseSize (int i);
	
	public void setSize (int i);
	
	public int getSize ();
	
	public IRecipeWrapper copy ();
	
	public class Nothing implements IRecipeWrapper {
		
		private Nothing () {
		}
		
		@Override
		public boolean isUnUsed () {
			return true;
		}
		
		@Override
		public boolean mergable (IRecipeWrapper component, int limit) {
			return component.getSize() <= limit;
		}
		
		@Override
		public IRecipeWrapper merge (IRecipeWrapper component, int limit) {
			return component;
		}
		
		@Override
		public boolean canAddComponent (IRecipeComponent component, int limit) {
			return component.getSize() <= limit;
		}
		
		@Override
		public IRecipeWrapper addComponent (IRecipeComponent component, int limit) {
			return component.getWrapper();
		}
		
		@Override
		public void increaseSize (int i) {
		}
		
		@Override
		public void setSize (int i) {
		}
		
		@Override
		public int getSize () {
			return 0;
		}
		
		@Override
		public IRecipeWrapper copy () {
			return this;
		}
	}
}
