/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.block.machine.generator;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import org.lwjgl.input.Keyboard;

import therogue.storehouse.block.IStorehouseVariantBlock;
import therogue.storehouse.block.StorehouseBaseTileBlock;
import therogue.storehouse.block.state.GeneratorType;
import therogue.storehouse.client.gui.multisystem.BlockEntry;
import therogue.storehouse.client.gui.multisystem.IEntry;
import therogue.storehouse.core.Storehouse;
import therogue.storehouse.item.StorehouseBaseVariantItemBlock;
import therogue.storehouse.reference.General;
import therogue.storehouse.reference.IDs;
import therogue.storehouse.reference.MachineStats;
import therogue.storehouse.tile.generator.placeholder.TileSolarGeneratorAdvanced;
import therogue.storehouse.tile.generator.placeholder.TileSolarGeneratorBasic;
import therogue.storehouse.tile.generator.placeholder.TileSolarGeneratorEnder;
import therogue.storehouse.tile.generator.placeholder.TileSolarGeneratorInfused;
import therogue.storehouse.tile.generator.placeholder.TileSolarGeneratorUltimate;
import therogue.storehouse.util.loghelper;

public class BlockSolarGenerator extends StorehouseBaseTileBlock implements IStorehouseVariantBlock
{
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
    public static final PropertyEnum<GeneratorType> TYPE = PropertyEnum.create("type", GeneratorType.class);

	public BlockSolarGenerator(String name)
	{
		super(name);
		this.setDefaultState(this.getBlockState().getBaseState().withProperty(TYPE, GeneratorType.basic));
		
		this.addShiftInfo(TextFormatting.WHITE + "from sunlight. It must have a clear view of the sky and it only works during the day!");
	}
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean debug) {
    	if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
    		list.add(TextFormatting.WHITE + "This machine generates " + GeneratorType.getTypeFromMeta(itemStack.getMetadata()).getRecieve(MachineStats.SOLARGENPERTICK) + " RF/t");
    		for (String s : getShiftInfo()) {
    			list.add(s);
    		}
    	} else {
    		list.add(General.SHIFTINFO);
    	}
    }
	
	@Override
	public BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, new IProperty[] {TYPE});
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).getMeta();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(TYPE, GeneratorType.getTypeFromMeta(meta));
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {

		for (GeneratorType g : GeneratorType.values()) {
			list.add(new ItemStack(item, 1, g.getMeta()));
		}
	}
	@Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
	    return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(state));
	}
	
	@Override
	public int damageDropped(IBlockState state) {
	    return getMetaFromState(state);
	}
	
	/**
	 * Returns the Properly Formatted Unlocalised Name
	 */
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName()+ "_" + GeneratorType.getTypeFromMeta(stack.getMetadata()).toString();
	}
	
	/**
	 * Registers this block easily
	 */
	public void registerblock()
	{
		loghelper.log("trace", "Registering StorehouseBaseBlock: " + getName());
		GameRegistry.register(this);
		GameRegistry.register(new StorehouseBaseVariantItemBlock(GeneratorType.values().length, this).setRegistryName(getRegistryName()));
	}
	
	/**
	 * Registers any Model variants 
	 */
	@Override
	public void registervariants()
	{
		for (GeneratorType g : GeneratorType.values()){
			ModelBakery.registerItemVariants(ItemBlock.getItemFromBlock(this), new ResourceLocation(getUnlocalizedName().substring(5) + "_" + g.getName()));
		}
	}
	
	@Override
	public void registertexture() {
		for (GeneratorType g : GeneratorType.values()){
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(this), g.getMeta(), new ModelResourceLocation(getUnlocalizedName().substring(5) + "_" + g.toString(), "inventory"));
		}
	}

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state)
    {
    	return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return AABB;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
	{
    	GeneratorType type = GeneratorType.getTypeFromMeta(meta);
    	switch(type){
		case advanced:
			return new TileSolarGeneratorAdvanced();
		case basic:
			return new TileSolarGeneratorBasic();
		case ender:
			return new TileSolarGeneratorEnder();
		case infused:
			return new TileSolarGeneratorInfused();
		case ultimate:
			return new TileSolarGeneratorUltimate();
		default:
			throw new IllegalArgumentException("Meta Out of Range");
    	}
	}

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(Storehouse.instance, IDs.SOLARGENERATORGUI, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
    
	@Override
	public IEntry getEntry()
	{
		return new BlockEntry();
	}
}
