
package therogue.storehouse.multiblock.tile;

public interface IMultiBlockTile {
	
	public boolean isFormed ();
	
	public IMultiBlockController getController ();
	
	public void setController (IMultiBlockController controller);
}
