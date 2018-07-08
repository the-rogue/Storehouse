
package therogue.storehouse.tile;

import net.minecraft.util.ITickable;

public interface ITickableModule extends ITickable, ITileModule {
	
	public boolean stillTicking ();
	
	public boolean shouldRemove ();
}
