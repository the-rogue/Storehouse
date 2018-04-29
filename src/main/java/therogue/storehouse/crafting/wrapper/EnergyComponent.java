
package therogue.storehouse.crafting.wrapper;

public class EnergyComponent implements IRecipeComponent {
	
	private int energy;
	
	public EnergyComponent (int energy) {
	}
	
	@Override
	public boolean isUnUsed () {
		return energy <= 0;
	}
	
	@Override
	public boolean matches (IRecipeWrapper component) {
		return component instanceof EnergyWrapper;
	}
	
	@Override
	public int getSize () {
		return energy;
	}
	
	@Override
	public IRecipeWrapper getWrapper () {
		return new EnergyWrapper(energy);
	}
	
	@Override
	public IRecipeComponent copy () {
		return new EnergyComponent(energy);
	}
	
	@Override
	public IRecipeWrapper getResidue () {
		return IRecipeWrapper.NOTHING;
	}
}
