package leppa.planarartifice.blocks;

import leppa.planarartifice.registry.PABlocks;
import leppa.planarartifice.tiles.TileStarvingChest;
import leppa.planarartifice.util.OreUtils;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftInvHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockStarvingChest extends BlockPA implements ITileEntityProvider {
    public static final int MAX_UPGRADES = 4;
    public static final PropertyDirection FACING;
    public static final PropertyInteger UPGRADES;

    public BlockStarvingChest(String name) {
        super(Material.WOOD, name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(UPGRADES, 1));
        variantNames = new String[]{"1", "2", "3", "4"};
        PABlocks.BLOCKS.remove(this);
        PABlocks.METABLOCKS.add(this);
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
    }

    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getStateFromMeta(meta).withProperty(FACING, placer.getHorizontalFacing());
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing enumfacing = EnumFacing.byHorizontalIndex(MathHelper.floor((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
        state = this.getStateFromMeta(stack.getMetadata()).withProperty(FACING, enumfacing);
        worldIn.setBlockState(pos, state, 3);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof IInventory) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof IInventory)
                player.displayGUIChest((IInventory) tileentity);
        }
        return true;
    }

    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        Object te = world.getTileEntity(pos);
        if (te != null && !world.isRemote && entity instanceof EntityItem && !entity.isDead) {
            ItemStack leftovers = ThaumcraftInvHelper.insertStackAt(world, pos, EnumFacing.UP, ((EntityItem) entity).getItem(), false);
            if (leftovers == null || leftovers.isEmpty() || leftovers.getCount() != ((EntityItem) entity).getItem().getCount()) {
                entity.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.25F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
            }
            if (leftovers != null && !leftovers.isEmpty()) ((EntityItem) entity).setItem(leftovers);
            else entity.setDead();
            ((TileStarvingChest) te).markDirty();
        }
    }

    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState state, World worldIn, BlockPos pos) {
        Object te = worldIn.getTileEntity(pos);
        return te instanceof TileStarvingChest ? Container.calcRedstoneFromInventory((IInventory) te) : 0;
    }

    public int damageDropped(IBlockState state) {
        return state.getValue(UPGRADES);
    }

    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (int i = 0; i < MAX_UPGRADES; ++i) items.add(OreUtils.meta(this, i));
    }

    public IBlockState getStateFromMeta(int meta) {
        if (meta >= MAX_UPGRADES) meta = MAX_UPGRADES - 1;
        return this.getDefaultState().withProperty(UPGRADES, meta + 1);
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(UPGRADES) - 1;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, UPGRADES);
    }

    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        return OreUtils.meta(this, getMetaFromState(state));
    }

    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileStarvingChest();
    }

    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, null, stack);
    }

    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    protected boolean isInvalidNeighbor(World worldIn, BlockPos pos, EnumFacing facing) {
        return worldIn.getBlockState(pos.offset(facing)).getMaterial() == Material.CACTUS;
    }

    protected boolean hasInvalidNeighbor(World worldIn, BlockPos pos) {
        return this.isInvalidNeighbor(worldIn, pos, EnumFacing.NORTH) || this.isInvalidNeighbor(worldIn, pos, EnumFacing.SOUTH) || this.isInvalidNeighbor(worldIn, pos, EnumFacing.WEST) || this.isInvalidNeighbor(worldIn, pos, EnumFacing.EAST);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels() {
        for (int i = 0; i < MAX_UPGRADES; ++i)
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, new ModelResourceLocation(this.getRegistryName(), "facing=north,upgrade=" + (i + 1)));
    }

    static {
        FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
        UPGRADES = PropertyInteger.create("upgrade", 1, MAX_UPGRADES);
    }
}