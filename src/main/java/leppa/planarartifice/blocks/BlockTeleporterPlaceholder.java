package leppa.planarartifice.blocks;

import leppa.planarartifice.registry.PABlocks;
import leppa.planarartifice.tiles.TileTeleporter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.lib.SoundsTC;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockTeleporterPlaceholder extends BlockPA {

	public BlockTeleporterPlaceholder(String name){
		super(Material.ROCK, name);
		setHardness(3);
	}
	
	public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ){
		if (hand == EnumHand.OFF_HAND) return false;
		if (world.getBlockState(pos.down()).getBlock() == PABlocks.teleporter) {
			TileTeleporter tep = (TileTeleporter)world.getTileEntity(pos.down());
			TileTeleporter dep = null;
			assert tep != null;
			if (tep.getAspect() == null) return false;
			int count = 0;
			List<TileEntity> allTEs = world.loadedTileEntityList;
			for (TileEntity e : allTEs) {
				if (e instanceof TileTeleporter) {
					TileTeleporter t = (TileTeleporter)e;
					if(tep.getAspect() == t.getAspect() && tep.getPos() != t.getPos()){
						dep = t;
						count++;
					}
				}
			}
			if (count == 1) {
				int diffX = dep.getPos().getX() - tep.getPos().getX();
				int diffY = dep.getPos().getY() - tep.getPos().getY();
				int diffZ = dep.getPos().getZ() - tep.getPos().getZ();
				player.setPositionAndUpdate(player.getPosition().getX() + diffX + 0.5, player.getPosition().getY() + diffY + 0.5, player.getPosition().getZ() + diffZ + 0.5);
				if (world.isRemote) {
					world.playSound(dep.getPos().getX(), dep.getPos().getY(), dep.getPos().getZ(), SoundsTC.wandfail, SoundCategory.BLOCKS, 0.5f, 1f, false);
					world.playSound(tep.getPos().getX(), tep.getPos().getY(), tep.getPos().getZ(), SoundsTC.wandfail, SoundCategory.BLOCKS, 0.5f, 1f, false);
				}
			} else if (count == 0) {
				if (world.isRemote) player.sendMessage(new TextComponentString(I18n.translateToLocal("planarartifice.waystone.none")));
			} else {
				if (world.isRemote) player.sendMessage(new TextComponentString(I18n.translateToLocal("planarartifice.waystone.toomuch")));
			}
		}
		return false;
	}

	@Nonnull
	@Override
	public ItemStack getPickBlock(@Nonnull IBlockState state, @Nonnull RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player)
	{
		if (world.getBlockState(pos.down()).getBlock() == PABlocks.teleporter) return new ItemStack(PABlocks.teleporter_matrix);
		else return new ItemStack(BlocksTC.stoneArcaneBrick);
	}
	
	public void breakBlock(World worldIn, BlockPos pos, @Nonnull IBlockState state){
		if (worldIn.getBlockState(pos.down()).getBlock() == PABlocks.teleporter) {
			worldIn.setBlockState(pos, PABlocks.teleporter_matrix.getDefaultState());
			worldIn.setBlockState(pos.down(), BlocksTC.stoneArcaneBrick.getDefaultState());
			worldIn.setBlockState(pos.down(2), BlocksTC.stoneArcaneBrick.getDefaultState());
			InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), ((TileTeleporter)(worldIn.getTileEntity(pos.down()))).crystal.getStackInSlot(0));
			worldIn.removeTileEntity(pos.down());
		} else if (worldIn.getBlockState(pos.up()).getBlock() == PABlocks.teleporter) {
			worldIn.setBlockState(pos, BlocksTC.stoneArcaneBrick.getDefaultState());
			worldIn.setBlockState(pos.up(), BlocksTC.stoneArcaneBrick.getDefaultState());
			worldIn.setBlockState(pos.up(2), PABlocks.teleporter_matrix.getDefaultState());
			InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), ((TileTeleporter)(worldIn.getTileEntity(pos.up()))).crystal.getStackInSlot(0));
			worldIn.removeTileEntity(pos.up());
		}
	}

	@Override
	public Block setLightLevel(float value) {
		return super.setLightLevel(value);
	}

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
}