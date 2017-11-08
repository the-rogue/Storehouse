
package therogue.storehouse.tile.multiblock;

public interface IMultiBlockTile {
	
	public boolean isFormed ();
	
	public IMultiBlockController getController ();
	
	public void setController (IMultiBlockController controller);
}
