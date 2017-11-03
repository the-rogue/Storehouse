
package therogue.storehouse.tile.multiblock;

public interface IMultiBlockPart {
	
	public boolean isFormed ();
	
	public IMultiBlockController getController ();
	
	public void setController (IMultiBlockController controller);
}
