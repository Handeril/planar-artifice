package leppa.planarartifice.blocks;

import leppa.planarartifice.util.LocalizationHelper;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.common.blocks.IBlockFacingHorizontal;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.lib.utils.InventoryUtils;

import javax.annotation.Nullable;
import java.util.List;

public class BlockAlkimiumSmelteryAux extends BlockPA implements IBlockFacingHorizontal {

	public BlockAlkimiumSmelteryAux(String name) {
		super(Material.IRON, name);
		this.setSoundType(SoundType.METAL);
		this.setDefaultState(
				this.blockState.getBaseState().withProperty(IBlockFacingHorizontal.FACING, EnumFacing.NORTH));
		this.setHardness(1.0f);
		this.setResistance(10.0f);
	}

	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		return side.getAxis().isHorizontal()
				&& worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock() instanceof BlockAlkimiumSmeltery
				&& BlockStateUtils.getFacing(worldIn.getBlockState(pos.offset(side.getOpposite()))) != side;
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		InventoryUtils.dropItems(worldIn, pos);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if(tileentity instanceof IEssentiaTransport && !worldIn.isRemote) {
			int ess = ((IEssentiaTransport) tileentity).getEssentiaAmount(EnumFacing.UP);
			if(ess > 0)
				AuraHelper.polluteAura(worldIn, pos, ess, true);
		}
		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.GREEN + LocalizationHelper.localize("planarartifice.alkimium"));
	}

	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	public boolean isFullCube(IBlockState state) {
		return false;
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		if(!facing.getAxis().isHorizontal()) {
			facing = EnumFacing.NORTH;
		}
		return this.getDefaultState().withProperty(IBlockFacingHorizontal.FACING, facing.getOpposite());
	}

	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(IBlockFacingHorizontal.FACING,
				EnumFacing.byHorizontalIndex(BlockStateUtils.getFacing(meta).getHorizontalIndex()));
	}

	public int getMetaFromState(IBlockState state) {
		return state.getValue(IBlockFacingHorizontal.FACING).getIndex();
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, IBlockFacingHorizontal.FACING);
	}

}
