package leppa.planarartifice.blocks;

import leppa.planarartifice.registry.PABlocks;
import leppa.planarartifice.tiles.TileTeleporter;
import leppa.planarartifice.util.OreUtils;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemGenericEssentiaContainer;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.items.resources.ItemCrystalEssence;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Random;

public class BlockTeleporterMiddle extends BlockPA implements ITileEntityProvider{
	
	public BlockTeleporterMiddle(String name){
		super(Material.ROCK, name);
		setHardness(3);
		setLightLevel(0.5F);
	}
	
	@Nonnull
	public Item getItemDropped(@Nonnull IBlockState state, @Nonnull Random rand, int fortune) {
		return Item.getItemFromBlock(Blocks.AIR);
	}
	
	public void breakBlock(World worldIn, BlockPos pos, @Nonnull IBlockState state) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (!(te instanceof TileTeleporter)) return;
		if (worldIn.getBlockState(pos.up()).getBlock() == PABlocks.teleporter_placeholder && worldIn.getBlockState(pos.down()).getBlock() == PABlocks.teleporter_placeholder) {
			worldIn.setBlockState(pos.down(), BlocksTC.stoneArcaneBrick.getDefaultState());
			worldIn.setBlockState(pos, BlocksTC.stoneArcaneBrick.getDefaultState());
			worldIn.setBlockState(pos.up(), PABlocks.teleporter_matrix.getDefaultState());
		}
		InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), ((TileTeleporter)te).crystal.getStackInSlot(0));
		worldIn.removeTileEntity(pos);
	}
	
	@Nonnull
	public BlockRenderLayer getRenderLayer(){
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	public boolean isFullyOpaque(IBlockState state){
        return false;
    }
	
	public boolean isNormalCube(@Nonnull IBlockState state){
		return false;
	}
	
	public boolean isOpaqueCube(@Nonnull IBlockState state){
		return false;
	}
	
	public boolean isBlockNormalCube(@Nonnull IBlockState state){
		return false;
	}
	
	public boolean isFullBlock(@Nonnull IBlockState state){
		return false;
	}
	
	public boolean isTranslucent(@Nonnull IBlockState state){
		return false;
	}
	
	public boolean isVisuallyOpaque(){
		return false;
	}
	
	public boolean isFullCube(@Nonnull IBlockState state){
        return false;
    }
	
	@Override
	public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta){
		return new TileTeleporter();
	}

	@Nonnull
	@Override
	public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player)
	{ return new ItemStack(BlocksTC.stoneArcaneBrick); }

	@Override
	public boolean onBlockActivated(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileTeleporter tile = (TileTeleporter) world.getTileEntity(pos);
			assert tile != null;
			IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
			assert itemHandler != null;
			ItemStack handStack = player.getHeldItem(hand);
			ItemStack tileStack = itemHandler.getStackInSlot(0);
			// BEHAVIOUR
			// if empty hand, try to extract
			// if crystal hand, try to insert, replace if necessary
			// if crystal hand of same type as existing, do nothing
			// if sneaking, try to extract
			if (handStack.isEmpty()) { // try to extract
				player.setHeldItem(hand, itemHandler.extractItem(0, 1, false));
			} else if (player.isSneaking()) {
				InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tileStack);
				itemHandler.extractItem(0, 1, false);
			} else if (handStack.getItem() instanceof ItemCrystalEssence) { // crystal hand
				if (!tileStack.isEmpty() && Arrays.equals(
						((ItemGenericEssentiaContainer) ItemsTC.crystalEssence).getAspects(handStack).getAspects(),
						((ItemGenericEssentiaContainer) ItemsTC.crystalEssence).getAspects(tileStack).getAspects()
				)) return true; // try to insert
				ItemStack temp = itemHandler.extractItem(0, 1, false);
				itemHandler.insertItem(0, OreUtils.count(handStack.copy(), 1), false);
				player.setHeldItem(hand, OreUtils.count(handStack.copy(), handStack.getCount() - 1));
				if (!player.inventory.addItemStackToInventory(temp))
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), temp);
			} else return true;
			tile.markDirty();
		}
		return true;
	}
}