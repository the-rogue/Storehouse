
package therogue.storehouse.block;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import therogue.storehouse.client.connectedtextures.ConnectionState;
import therogue.storehouse.client.connectedtextures.ConnectionState.RenderProperty;
import therogue.storehouse.item.StorehouseBaseVariantItemBlock;

public class StorehouseBaseVariantBlock extends StorehouseBaseBlock {
	
	protected final Map<Integer, String> blocks = new HashMap<Integer, String>();
	protected final Map<Integer, ItemDrop> unique_drops = new HashMap<Integer, ItemDrop>();
	public static final PropertyInteger META = PropertyInteger.create("meta", 0, 15);
	
	public StorehouseBaseVariantBlock (String name) {
		super(name);
		this.setDefaultState(this.getDefaultState().withProperty(META, 0));
	}
	
	public StorehouseBaseVariantBlock addSubBlock (int id, String name) {
		blocks.put(id, name);
		return this;
	}
	
	public void addDrop (int id, ItemStack drop) {
		unique_drops.put(id, new ItemDrop(drop));
	}
	
	public void addDrop (int id, ItemStack drop, int min_quantity, int max_quantity) {
		unique_drops.put(id, new ItemDrop(drop, min_quantity, max_quantity));
	}
	
	@Override
	public Item getItemDropped (IBlockState blockstate, Random random, int fortune) {
		ItemDrop drop = unique_drops.get(getMetaFromState(blockstate));
		if (drop != null) return drop.stack.getItem();
		return super.getItemDropped(blockstate, random, fortune);
	}
	
	@Override
	public int quantityDropped (IBlockState blockstate, int fortune, Random random) {
		ItemDrop drop = unique_drops.get(getMetaFromState(blockstate));
		if (drop != null)
		{
			if (drop.min_quantity >= drop.max_quantity) return drop.min_quantity;
			return drop.min_quantity + random.nextInt(drop.max_quantity - drop.min_quantity + fortune + 1);
		}
		return super.quantityDropped(blockstate, fortune, random);
	}
	
	/**
	 * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It returns the metadata of the dropped item based on the old metadata of the block.
	 */
	@Override
	public int damageDropped (IBlockState state) {
		ItemDrop drop = unique_drops.get(getMetaFromState(state));
		if (drop != null) return drop.stack.getMetadata();
		return getMetaFromState(state);
	}
	
	@Override
	public BlockStateContainer createBlockState () {
		return new BlockStateContainer(this, new IProperty[] { META });
	}
	
	@Override
	public int getMetaFromState (IBlockState state) {
		return state.getValue(META);
	}
	
	@Override
	public IBlockState getStateFromMeta (int meta) {
		return this.getDefaultState().withProperty(META, meta);
	}
	
	@Override
	public void getSubBlocks (CreativeTabs itemIn, NonNullList<ItemStack> tab) {
		for (int i = 0; i < blocks.size(); i++)
		{
			tab.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public ItemStack getPickBlock (IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(state));
	}
	
	/**
	 * Returns the Properly Formatted Unlocalised Name
	 */
	@Override
	public String getUnlocalizedName (ItemStack stack) {
		return super.getUnlocalizedName(stack) + "_" + blocks.get(stack.getMetadata());
	}
	
	@Override
	public Item getItemBlock () {
		return new StorehouseBaseVariantItemBlock(blocks.size(), this).setRegistryName(getRegistryName());
	}
	
	/**
	 * Registers any Model variants
	 */
	@Override
	public void registerModels () {
		for (Entry<Integer, String> block : blocks.entrySet())
		{
			ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(this), block.getKey(), new ModelResourceLocation(getUnlocalizedName().substring(5), "meta="
					+ block.getKey()));
		}
	}
	
	private static class ItemDrop {
		
		public final ItemStack stack;
		public final int min_quantity;
		public final int max_quantity;
		
		private ItemDrop (ItemStack drop) {
			this(drop, 1, 1);
		}
		
		private ItemDrop (ItemStack stack, int min_quantity, int max_quantity) {
			this.stack = stack;
			this.min_quantity = min_quantity;
			this.max_quantity = max_quantity;
		}
	}
	
	public static class VBCT extends StorehouseBaseVariantBlock {
		
		public VBCT (String name) {
			super(name);
		}
		
		@Override
		public ExtendedBlockState createBlockState () {
			return new ExtendedBlockState(this, new IProperty[] { META }, new IUnlistedProperty[] { ConnectionState.RenderProperty.INSTANCE });
		}
		
		/**
		 * Can return IExtendedBlockState
		 */
		@Override
		public IBlockState getExtendedState (IBlockState state, IBlockAccess world, BlockPos pos) {
			return ((IExtendedBlockState) state).withProperty(RenderProperty.INSTANCE, new ConnectionState(world, pos));
		}
	}
}
