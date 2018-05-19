
package therogue.storehouse.init.grouped;

import java.util.ArrayList;
import java.util.List;

import therogue.storehouse.block.IStorehouseBaseBlock;
import therogue.storehouse.block.StorehouseBaseBlock;
import therogue.storehouse.block.StorehouseBaseSlab;
import therogue.storehouse.block.StorehouseBaseStair;

public class DecorativeBlockContainer {
	
	public final StorehouseBaseBlock block;
	public final StorehouseBaseStair stair;
	public final StorehouseBaseSlab.Half half_slab;
	public final StorehouseBaseSlab.Double double_slab;
	
	public DecorativeBlockContainer (String name) {
		block = new StorehouseBaseBlock(name);
		stair = new StorehouseBaseStair(block);
		half_slab = new StorehouseBaseSlab.Half(block);
		double_slab = new StorehouseBaseSlab.Double(block, half_slab);
	}
	
	public List<IStorehouseBaseBlock> returnAll () {
		List<IStorehouseBaseBlock> blocks = new ArrayList<IStorehouseBaseBlock>();
		blocks.add(block);
		blocks.add(stair);
		blocks.add(half_slab);
		blocks.add(double_slab);
		return blocks;
	}
}
