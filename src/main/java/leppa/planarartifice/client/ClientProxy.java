package leppa.planarartifice.client;

import leppa.planarartifice.client.render.tile.TESRAlkimiumCentrifuge;
import leppa.planarartifice.client.render.tile.TESRStarvingChest;
import leppa.planarartifice.client.render.tile.TESRTeleporter;
import leppa.planarartifice.main.CommonProxy;
import leppa.planarartifice.tiles.TileAlkimiumCentrifuge;
import leppa.planarartifice.tiles.TileStarvingChest;
import leppa.planarartifice.tiles.TileTeleporter;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(value = Side.CLIENT)
public class ClientProxy extends CommonProxy{

	@Override
	public void preInit(FMLPreInitializationEvent e){
		super.preInit(e);
	}
	
	@Override
	public void init(FMLInitializationEvent e){
		super.init(e);
		ClientRegistry.bindTileEntitySpecialRenderer(TileTeleporter.class, new TESRTeleporter());
		ClientRegistry.bindTileEntitySpecialRenderer(TileAlkimiumCentrifuge.class, new TESRAlkimiumCentrifuge()); // hopium
		ClientRegistry.bindTileEntitySpecialRenderer(TileStarvingChest.class, new TESRStarvingChest());
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}

	public static EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().player;
	}
	
	@Override
	public EntityPlayer getPlayerEntityFromContext(MessageContext ctx) {
		return Minecraft.getMinecraft().player;
	}

}