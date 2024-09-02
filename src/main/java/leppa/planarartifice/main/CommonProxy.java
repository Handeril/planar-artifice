package leppa.planarartifice.main;

import leppa.planarartifice.compat.PACompatHandler;
import static leppa.planarartifice.main.PlanarArtifice.MODID;
import leppa.planarartifice.network.MessageProjectingAttack;
import leppa.planarartifice.network.PacketRequestUpdateTeleporter;
import leppa.planarartifice.network.PacketUpdateTeleporter;
import leppa.planarartifice.recipe.CrucibleRecipeRandomCrystal;
import leppa.planarartifice.registry.PAAspects;
import leppa.planarartifice.registry.PAResearch;
import leppa.planarartifice.tiles.*;
import leppa.planarartifice.util.Aspects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import thaumcraft.api.research.ResearchCategories;

public class CommonProxy {

    public static SimpleNetworkWrapper network;

    public void preInit(FMLPreInitializationEvent e) {
        PAAspects.registerAspects();
        PACompatHandler.setup();
        PACompatHandler.preInit(e);

        PAResearch.catPA = ResearchCategories.registerCategory("PLANARARTIFICE", "FIRSTSTEPS",
                new Aspects("spatio", 20, "tempus", 20, "vitreus", 10, "auram", 10, "ordo", 5),
                new ResourceLocation(MODID, "textures/meta/logo_icon.png"),
                new ResourceLocation(MODID, "textures/research/gui_research_back_2.jpg"));

        GameRegistry.registerTileEntity(TileTeleporter.class, new ResourceLocation(MODID, "mirrorTeleporter"));
        GameRegistry.registerTileEntity(TileFluxScrubber.class, new ResourceLocation(MODID, "fluxScrubber"));
        GameRegistry.registerTileEntity(TileAlkimiumSmeltery.class, new ResourceLocation(MODID, "alkimiumSmeltery"));
        GameRegistry.registerTileEntity(TileAlkimiumCentrifuge.class, new ResourceLocation(MODID, "alkimiumCentrifuge"));
        GameRegistry.registerTileEntity(TileStarvingChest.class, new ResourceLocation(MODID, "starvingChest"));

        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        int id = 0;
        network.registerMessage(new PacketUpdateTeleporter.Handler(), PacketUpdateTeleporter.class, id++, Side.CLIENT);
        network.registerMessage(new PacketRequestUpdateTeleporter.Handler(), PacketRequestUpdateTeleporter.class, id++, Side.SERVER);
        network.registerMessage(new MessageProjectingAttack.Handler(), MessageProjectingAttack.class, id++, Side.SERVER);

        NetworkRegistry.INSTANCE.registerGuiHandler(PlanarArtifice.instance, new PAGuiHandler());

    }

    public void init(FMLInitializationEvent e) {
        CrucibleRecipeRandomCrystal.registerAspectList();
        PAResearch.registerResearch();
        PACompatHandler.init(e);
    }

    public void postInit(FMLPostInitializationEvent e) {
        PACompatHandler.postInit(e);
    }

    public EntityPlayer getPlayerEntityFromContext(MessageContext ctx) {
        return ctx.getServerHandler().player;
    }


}
