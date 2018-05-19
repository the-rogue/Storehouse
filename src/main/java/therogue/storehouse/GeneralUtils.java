/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

public class GeneralUtils {
	
	public static boolean isServerSide (World world) {
		return !world.isRemote;
	}
	
	public static boolean isClientSide (World world) {
		return world.isRemote;
	}
	
	@SuppressWarnings ("unchecked")
	public static <E extends Enum<E>> E getEnumFromNumber (Class<? extends Enum<E>> enumtype, int number) {
		List<E> constants = Arrays.asList(enumtype.getEnumConstants());
		for (E element : constants)
		{
			if (element.ordinal() == number) return element;
		}
		return null;
	}
	
	public static <T> List<T> copyList (List<T> toCopy) {
		List<T> toReturn = new ArrayList<T>();
		for (T element : toCopy)
		{
			toReturn.add(element);
		}
		return toReturn;
	}
	
	public static boolean isInteger (String str) {
		if (str == null) return false;
		int length = str.length();
		if (length == 0) return false;
		int i = 0;
		if (str.charAt(0) == '-')
		{
			if (length == 1) return false;
			i = 1;
		}
		for (; i < length; i++)
		{
			char c = str.charAt(i);
			if (c < '0' || c > '9') return false;
		}
		return true;
	}
}
