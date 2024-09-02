package leppa.planarartifice.main;

import leppa.planarartifice.containers.ContainerFluxScrubber;
import leppa.planarartifice.containers.gui.GuiAlkimiumSmeltery;
import leppa.planarartifice.containers.gui.GuiFluxScrubber;
import leppa.planarartifice.tiles.TileAlkimiumSmeltery;
import leppa.planarartifice.tiles.TileFluxScrubber;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import thaumcraft.common.container.ContainerSmelter;

public class PAGuiHandler implements IGuiHandler {

    public static final int ID_ALKIMIUM_SMELTERY = 0;
    public static final int ID_FLUX_SCRUBBER = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        switch (ID) {
            case ID_ALKIMIUM_SMELTERY:
                return new ContainerSmelter(player.inventory, (TileAlkimiumSmeltery) world.getTileEntity(new BlockPos(x, y, z)));
            case ID_FLUX_SCRUBBER:
                return new ContainerFluxScrubber(player.inventory, (TileFluxScrubber) world.getTileEntity(new BlockPos(x, y, z)));
        }

        return null;

    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        switch (ID) {
            case ID_ALKIMIUM_SMELTERY:
                return new GuiAlkimiumSmeltery(player.inventory, (TileAlkimiumSmeltery) world.getTileEntity(new BlockPos(x, y, z)));
            case ID_FLUX_SCRUBBER:
                return new GuiFluxScrubber(player.inventory, (TileFluxScrubber) world.getTileEntity(new BlockPos(x, y, z)));
        }

        return null;
    }
}
