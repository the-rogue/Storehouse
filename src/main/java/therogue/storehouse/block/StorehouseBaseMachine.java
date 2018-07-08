/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import therogue.storehouse.LOG;
import therogue.storehouse.Storehouse;
import therogue.storehouse.multiblock.block.MultiBlockCreationHandler;
import therogue.storehouse.multiblock.structure.MultiBlockStructure;
import therogue.storehouse.network.StorehousePacketHandler;
import therogue.storehouse.tile.ModuleContext;
import therogue.storehouse.tile.StorehouseBaseTileEntity;
import therogue.storehouse.tile.TileUtils;

public class StorehouseBaseMachine<T extends TileEntity> extends StorehouseBaseBlock implements ITileEntityProvider {
	
	protected AxisAlignedBB AABB;
	private List<String> shiftInfo = new ArrayList<String>();
	protected BiFunction<World, Integer, T> createTile;
	protected List<ResourceLocation> multiBlockLocations = new ArrayList<>();
	protected List<MultiBlockStructure> multiBlockStructures = new ArrayList<>();
	protected int guiID = -1;
	protected Object mod = Storehouse.instance;
	protected boolean checkTile = false;
	protected boolean notifyTile = false;
	protected boolean fluidHandler = false;
	
	public StorehouseBaseMachine (String name, Class<T> tileClass) {
		this(name, tileClass, (AxisAlignedBB) null);
	}
	
	public StorehouseBaseMachine (String name, Class<T> tileClass, AxisAlignedBB boundingBox) {
		this(name, tileClass, (world, meta) -> {
			try
			{
				return tileClass.newInstance();
			}
			catch (InstantiationException | IllegalAccessException e)
			{
				LOG.warn("Could not create a new Tile Entity from tileclass: " + tileClass + " in block: " + name);
				return null;
			}
		}, boundingBox);
	}
	
	public StorehouseBaseMachine (String name, Class<T> tileClass, BiFunction<World, Integer, T> function) {
		this(name, tileClass, function, null);
	}
	
	public StorehouseBaseMachine (String name, Class<T> tileClass, BiFunction<World, Integer, T> function, AxisAlignedBB boundingBox) {
		super(name, 6.0F, 20.0F);
		GameRegistry.registerTileEntity(tileClass, this.getRegistryName());
		this.createTile = function;
		this.AABB = boundingBox;
	}
	
	public StorehouseBaseMachine<T> setNotifyTileActivation () {
		notifyTile = true;
		return this;
	}
	
	public StorehouseBaseMachine<T> setIsFluidHandler () {
		fluidHandler = true;
		return this;
	}
	
	public StorehouseBaseMachine<T> setGUI (int guiID) {
		this.guiID = guiID;
		return this;
	}
	
	public StorehouseBaseMachine<T> setGUI () {
		return setGUI(0);
	}
	
	public StorehouseBaseMachine<T> setGUI (Object mod, int guiID) {
		this.guiID = guiID;
		this.mod = mod;
		return this;
	}
	
	public StorehouseBaseMachine<T> setGUICheckTile (int guiID) {
		this.guiID = guiID;
		this.checkTile = true;
		return this;
	}
	
	public StorehouseBaseMachine<T> setGUICheckTile () {
		return setGUICheckTile(0);
	}
	
	public StorehouseBaseMachine<T> setGUICheckTile (Object mod, int guiID) {
		this.guiID = guiID;
		this.mod = mod;
		this.checkTile = true;
		return this;
	}
	
	public StorehouseBaseMachine<T> setHasDefaultMultiBlock () {
		this.addMultiBlockStructure(new ResourceLocation(Storehouse.MOD_ID, "multiblock/" + this.getName() + ".txt"));
		return this;
	}
	
	public StorehouseBaseMachine<T> addMultiBlockStructure (ResourceLocation location) {
		multiBlockLocations.add(location);
		return this;
	}
	
	@Override
	public List<MultiBlockStructure> getMultiblockStructures () {
		return multiBlockStructures;
	}
	
	@Override
	public void setup () {
		multiBlockLocations.forEach(location -> {
			multiBlockStructures.add(MultiBlockCreationHandler.INSTANCE.getStructureFromFile(location));
		});
	}
	
	@Override
	public boolean onBlockActivated (
			World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (notifyTile)
		{
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof StorehouseBaseTileEntity
					&& ((StorehouseBaseTileEntity) te).onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ))
				return true;
		}
		if (fluidHandler)
		{
			if (TileUtils.tryInteractLiquidContainer(world, pos, player, hand)) return true;
		}
		if (guiID != -1 && mod != null)
		{
			if (checkTile)
			{
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof StorehouseBaseTileEntity
						&& ((StorehouseBaseTileEntity) te).canOpenGui(world, pos, state, player, hand, side, hitX, hitY, hitZ))
				{
					if (world.isRemote) return true;
					StorehousePacketHandler.INSTANCE.sendTo(((StorehouseBaseTileEntity) te).getCGUIPacket(), (EntityPlayerMP) player);
					player.openGui(mod, guiID, world, pos.getX(), pos.getY(), pos.getZ());
					return true;
				}
			}
			else
			{
				if (world.isRemote) return true;
				player.openGui(mod, guiID, world, pos.getX(), pos.getY(), pos.getZ());
				return true;
			}
		}
		return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
	}
	
	@Override
	public void breakBlock (World worldIn, BlockPos pos, IBlockState state) {
		TileEntity te = worldIn.getTileEntity(pos);
		IItemHandler inventory = null;
		if (te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
		{
			inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		}
		else if (te instanceof StorehouseBaseTileEntity)
		{
			inventory = ((StorehouseBaseTileEntity) te).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null, ModuleContext.INTERNAL);
		}
		if (inventory != null)
		{
			for (int i = 0; i < inventory.getSlots(); ++i)
			{
				ItemStack itemstack = inventory.getStackInSlot(i);
				if (!itemstack.isEmpty())
				{
					InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), itemstack);
				}
			}
		}
		if (notifyTile && te instanceof StorehouseBaseTileEntity) ((StorehouseBaseTileEntity) te).breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	@SuppressWarnings ("deprecation")
	public boolean eventReceived (IBlockState state, World worldIn, BlockPos pos, int id, int param) {
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
	}
	
	@Override
	@SideOnly (Side.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			for (String s : shiftInfo)
			{
				tooltip.add(s);
			}
		}
		else
		{
			tooltip.add(Storehouse.SHIFTINFO);
		}
	}
	
	protected void addShiftInfo (String info) {
		shiftInfo.add(info);
	}
	
	public List<String> getShiftInfo () {
		return shiftInfo;
	}
	
	@Override
	public void onBlockPlacedBy (World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (stack.hasDisplayName())
		{
			((StorehouseBaseTileEntity) worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());
		}
	}
	
	@Override
	public TileEntity createNewTileEntity (World worldIn, int meta) {
		TileEntity tile = createTile.apply(worldIn, meta);
		tile.setWorld(worldIn);
		return tile;
	}
	
	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox (IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		if (AABB != null) return AABB;
		return FULL_BLOCK_AABB;
	}
	
	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	@Override
	public boolean isOpaqueCube (IBlockState state) {
		if (AABB != null) return false;
		return true;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {
		if (AABB != null) return AABB;
		return FULL_BLOCK_AABB;
	}
	
	@Override
	public boolean isFullCube (IBlockState state) {
		if (AABB != null) return false;
		return true;
	}
	
	/**
	 * Should block use the brightest neighbor light value as its own
	 */
	@Override
	public boolean getUseNeighborBrightness (IBlockState state) {
		if (AABB != null) return true;
		return this.useNeighborBrightness;
	}
}
