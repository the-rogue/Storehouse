/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.block.machine;

import java.util.List;

import org.lwjgl.input.Keyboard;

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
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import therogue.storehouse.block.IStorehouseVariantBlock;
import therogue.storehouse.core.Storehouse;
import therogue.storehouse.item.StorehouseBaseVariantItemBlock;
import therogue.storehouse.reference.General;
import therogue.storehouse.reference.IDs;
import therogue.storehouse.reference.MachineStats;
import therogue.storehouse.tile.MachineTier;
import therogue.storehouse.tile.machine.generator.GeneratorUtils;
import therogue.storehouse.tile.machine.generator.TileLiquidGenerator;
import therogue.storehouse.util.LOG;

public class BlockLiquidGenerator extends StorehouseBaseMachine implements IStorehouseVariantBlock {
	
	public static final PropertyEnum<MachineTier> TYPE = PropertyEnum.create("type", MachineTier.class);
	
	public BlockLiquidGenerator (String name) {
		super(name);
		this.setDefaultState(this.getBlockState().getBaseState().withProperty(TYPE, MachineTier.basic));
		this.addShiftInfo(TextFormatting.WHITE + "from any burnable liquid.");
	}
	
	@Override
	public void addInformation (ItemStack itemStack, EntityPlayer player, List<String> list, boolean debug) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			list.add(TextFormatting.WHITE + "This machine generates " + GeneratorUtils.getRecieve(itemStack.getMetadata(), MachineStats.LIQUIDGENPERTICK) + " RF/t");
			for (String s : getShiftInfo())
			{
				list.add(s);
			}
		}
		else
		{
			list.add(General.SHIFTINFO);
		}
	}
	
	@Override
	public BlockStateContainer createBlockState () {
		return new BlockStateContainer(this, new IProperty[] { TYPE });
	}
	
	@Override
	public int getMetaFromState (IBlockState state) {
		return GeneratorUtils.getMeta(state.getValue(TYPE));
	}
	
	@Override
	public IBlockState getStateFromMeta (int meta) {
		return this.getDefaultState().withProperty(TYPE, GeneratorUtils.getTypeFromMeta(meta));
	}
	
	@Override
	public void getSubBlocks (Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
		for (MachineTier g : MachineTier.values())
		{
			list.add(new ItemStack(item, 1, GeneratorUtils.getMeta(g)));
		}
	}
	
	@Override
	public ItemStack getPickBlock (IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(state));
	}
	
	@Override
	public int damageDropped (IBlockState state) {
		return getMetaFromState(state);
	}
	
	/**
	 * Returns the Properly Formatted Unlocalised Name
	 */
	@Override
	public String getUnlocalizedName (ItemStack stack) {
		return super.getUnlocalizedName() + "_" + GeneratorUtils.getTypeFromMeta(stack.getMetadata()).toString();
	}
	
	/**
	 * Registers this block easily
	 */
	public void registerblock () {
		LOG.log("trace", "Registering StorehouseBaseBlock: " + getName());
		GameRegistry.register(this);
		GameRegistry.register(new StorehouseBaseVariantItemBlock(MachineTier.values().length, this).setRegistryName(getRegistryName()));
	}
	
	/**
	 * Registers any Model variants
	 */
	@Override
	public void registervariants () {
		for (MachineTier g : MachineTier.values())
		{
			ModelBakery.registerItemVariants(ItemBlock.getItemFromBlock(this), new ResourceLocation(getUnlocalizedName().substring(5) + "_" + g.getName()));
		}
	}
	
	@Override
	public void registertexture () {
		for (MachineTier g : MachineTier.values())
		{
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(this), GeneratorUtils.getMeta(g), new ModelResourceLocation(getUnlocalizedName().substring(5) + "_" + g.toString(), "inventory"));
		}
	}
	
	@Override
	public TileEntity createNewTileEntity (World worldIn, int meta) {
		MachineTier tier = GeneratorUtils.getTypeFromMeta(meta);
		switch (tier) {
			case advanced:
				return new TileLiquidGenerator.TileLiquidGeneratorAdvanced();
			case basic:
				return new TileLiquidGenerator.TileLiquidGeneratorBasic();
			case ender:
				return new TileLiquidGenerator.TileLiquidGeneratorEnder();
			case infused:
				return new TileLiquidGenerator.TileLiquidGeneratorInfused();
			case ultimate:
				return new TileLiquidGenerator.TileLiquidGeneratorUltimate();
			default:
				return null;
		}
	}
	
	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote)
		{
			player.openGui(Storehouse.instance, IDs.LIQUIDGENERATORGUI, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
}
