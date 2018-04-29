
package therogue.storehouse.tile;

public interface IDataHandler {
	
	/**
	 * Number of gui fields required
	 * 
	 * @return the number of fields
	 */
	public int fieldCount ();
	
	/**
	 * Get a gui Field
	 * 
	 * @param index
	 * 
	 * @return the field
	 */
	public int getField (int index);
}
