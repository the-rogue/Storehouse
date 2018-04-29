
package therogue.storehouse.crafting.wrapper;

public class EnergyWrapper implements IRecipeWrapper {
	
	private int energy;
	
	public EnergyWrapper (int energy) {
		this.energy = energy;
	}
	
	@Override
	public boolean isUnUsed () {
		return energy <= 0;
	}
	
	@Override
	public boolean mergable (IRecipeWrapper component, int limit) {
		return component instanceof EnergyWrapper && component.getSize() + energy <= limit;
	}
	
	@Override
	public EnergyWrapper merge (IRecipeWrapper component, int limit) {
		if (!mergable(component, limit)) return this;
		EnergyWrapper other = ((EnergyWrapper) component);
		if (other.getSize() + energy > limit && limit > 0)
		{
			other.setSize(other.getSize() + energy - limit);
			energy = limit;
		}
		else
		{
			energy += other.getSize();
			other.setSize(0);
		}
		return this;
	}
	
	@Override
	public boolean canAddComponent (IRecipeComponent component, int limit) {
		return component instanceof EnergyComponent && component.getSize() + energy <= limit;
	}
	
	@Override
	public EnergyWrapper addComponent (IRecipeComponent component, int limit) {
		if (!canAddComponent(component, limit)) return this;
		EnergyComponent other = ((EnergyComponent) component);
		energy += other.getSize();
		if (energy > limit)
		{
			energy = limit;
		}
		return this;
	}
	
	@Override
	public void increaseSize (int i) {
		energy += i;
	}
	
	@Override
	public void setSize (int i) {
		energy = i;
	}
	
	@Override
	public int getSize () {
		return energy;
	}
	
	@Override
	public EnergyWrapper copy () {
		return new EnergyWrapper(energy);
	}
}
