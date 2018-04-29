
package therogue.storehouse.tile;

public interface IButton<T extends Enum<T>> {
	
	public T getMode ();
	
	public void setMode (T mode);
	
	public int getOrdinal ();
	
	public void pressed ();
}
