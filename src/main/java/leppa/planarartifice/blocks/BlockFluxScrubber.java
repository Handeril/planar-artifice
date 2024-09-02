package leppa.planarartifice.blocks;

import leppa.planarartifice.main.PAGuiHandler;
import leppa.planarartifice.main.PlanarArtifice;
import leppa.planarartifice.tiles.TileFluxScrubber;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.essentia.TileSmelter;

public class BlockFluxScrubber extends BlockPA {

    public BlockFluxScrubber(String name) {
        super(Material.IRON, name);
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return true;

        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileFluxScrubber) {
            player.openGui(PlanarArtifice.instance, PAGuiHandler.ID_FLUX_SCRUBBER, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }

    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileFluxScrubber();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileFluxScrubber)
            ((TileFluxScrubber)te).breakBlock(worldIn, pos);
    }
}
