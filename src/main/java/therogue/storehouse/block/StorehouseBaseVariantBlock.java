
package therogue.storehouse.block;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import therogue.storehouse.item.StorehouseBaseVariantItemBlock;

public class StorehouseBaseVariantBlock extends StorehouseBaseBlock {
	
	private final Map<Integer, SubBlock> blocks = new HashMap<Integer, SubBlock>();
	public static final PropertyInteger META = PropertyInteger.create("meta", 0, 15);
	
	public StorehouseBaseVariantBlock (String name, int numberOfBlocks) {
		super(name);
		this.setDefaultState(this.getBlockState().getBaseState().withProperty(META, 0));
	}
	
	public StorehouseBaseVariantBlock addSubBlock (int id, String name) {
		blocks.put(id, new SubBlock(name));
		return this;
	}
	
	public StorehouseBaseVariantBlock addSubBlock (int id, String name, ItemStack drop, int min_quantity, int max_quantity) {
		blocks.put(id, new SubBlock(name, new ItemDrop(drop, min_quantity, max_quantity)));
		return this;
	}
	
	@Override
	public Item getItemDropped (IBlockState blockstate, Random random, int fortune) {
		ItemDrop drop = blocks.get(getMetaFromState(blockstate)).drop;
		if (drop != null) return drop.stack.getItem();
		return super.getItemDropped(blockstate, random, fortune);
	}
	
	@Override
	public int quantityDropped (IBlockState blockstate, int fortune, Random random) {
		ItemDrop drop = blocks.get(getMetaFromState(blockstate)).drop;
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
		ItemDrop drop = blocks.get(getMetaFromState(state)).drop;
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
	public void getSubBlocks (Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
		for (int i = 0; i < blocks.size(); i++)
		{
			list.add(new ItemStack(item, 1, i));
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
	
	/**
	 * Registers this block easily
	 */
	@Override
	public void preInit () {
		GameRegistry.register(this);
		GameRegistry.register(new StorehouseBaseVariantItemBlock(blocks.size(), this).setRegistryName(getRegistryName()));
	}
	
	/**
	 * Registers any Model variants
	 */
	@Override
	@SideOnly (Side.CLIENT)
	public void preInitClient () {
		for (Entry<Integer, SubBlock> block : blocks.entrySet())
		{
			ModelBakery.registerItemVariants(ItemBlock.getItemFromBlock(this), new ResourceLocation(getUnlocalizedName().substring(5) + "_" + block.getValue()));
		}
	}
	
	@Override
	@SideOnly (Side.CLIENT)
	public void InitClient () {
		for (Entry<Integer, SubBlock> block : blocks.entrySet())
		{
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(this), block.getKey(), new ModelResourceLocation(getUnlocalizedName().substring(5) + "_" + block.getValue(), "inventory"));
		}
	}
	
	public static class SubBlock {
		
		public final String name;
		public final ItemDrop drop;
		
		public SubBlock (String name) {
			this.name = name;
			this.drop = null;
		}
		
		public SubBlock (String name, ItemDrop drop) {
			this.name = name;
			this.drop = drop;
		}
		
		@Override
		public String toString () {
			return name;
		}
	}
	
	public static class ItemDrop {
		
		public final ItemStack stack;
		public final int min_quantity;
		public final int max_quantity;
		
		public ItemDrop (ItemStack drop) {
			this(drop, 1, 1);
		}
		
		public ItemDrop (ItemStack stack, int min_quantity, int max_quantity) {
			this.stack = stack;
			this.min_quantity = min_quantity;
			this.max_quantity = max_quantity;
		}
	}
}
