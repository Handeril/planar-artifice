package leppa.planarartifice.client.event;

import leppa.planarartifice.blocks.BlockAlkimiumSmeltery;
import leppa.planarartifice.client.ClientProxy;
import leppa.planarartifice.client.gui.GuiAuraMeterHud;
import leppa.planarartifice.items.ItemAlkimiumGoggles;
import leppa.planarartifice.items.baubles.ItemAuraMeter;
import leppa.planarartifice.tiles.TileAlkimiumSmeltery;
import leppa.planarartifice.util.LocalizationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import thaumcraft.api.aspects.IEssentiaTransport;

@EventBusSubscriber(Side.CLIENT)
public class HudEventHandler {

    @SubscribeEvent
    public static void onRenderLast(RenderGameOverlayEvent.Post e) {

        if (e.getType() == ElementType.EXPERIENCE && ItemAuraMeter.shouldRenderHud(ClientProxy.getClientPlayer()))
            new GuiAuraMeterHud(Minecraft.getMinecraft(), e.getPartialTicks(), ClientProxy.getClientPlayer());

        RayTraceResult mop = Minecraft.getMinecraft().objectMouseOver;

        renderBurnTime(mop);
        renderSuctionInformation(mop);


    }

    public static void renderBurnTime(RayTraceResult mop) {
        if (mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK && ItemAlkimiumGoggles.shouldRenderHud(ClientProxy.getClientPlayer())) {
            TileEntity tileEntity = Minecraft.getMinecraft().world.getTileEntity(mop.getBlockPos());
            if (Minecraft.getMinecraft().world.getBlockState(mop.getBlockPos())
                    .getBlock() instanceof BlockAlkimiumSmeltery && tileEntity instanceof TileAlkimiumSmeltery) {
                TileAlkimiumSmeltery tile = (TileAlkimiumSmeltery) tileEntity;
                String toDraw = LocalizationHelper.localize("tooltip.willlastforseconds", tile.furnaceBurnTime / 20);
                if (tile.furnaceBurnTime > 1200) {
                    toDraw = LocalizationHelper.localize("tooltip.willlastforminutes",
                            (int) Math.floor(tile.furnaceBurnTime / 1200), (tile.furnaceBurnTime % 1200) / 20);// "Will
                }
                Minecraft.getMinecraft().fontRenderer.drawString(toDraw,
                        (new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() / 2)
                                - Minecraft.getMinecraft().fontRenderer.getStringWidth(toDraw) / 2,
                        (new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() / 2) - 12, 0xffffff);
            }
        }
    }

    public static void renderSuctionInformation(RayTraceResult mop) {
        if (mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK && ItemAlkimiumGoggles.shouldRenderHud(ClientProxy.getClientPlayer())) {
            if (Minecraft.getMinecraft().world.getTileEntity(mop.getBlockPos()) instanceof IEssentiaTransport) {
                String toDraw2 = "Has "
                        + ((IEssentiaTransport) Minecraft.getMinecraft().world.getTileEntity(mop.getBlockPos())).getSuctionAmount(mop.sideHit)
                        + " suction on this opposite side.";
                String toDraw = "Has "
                        + ((IEssentiaTransport) Minecraft.getMinecraft().world.getTileEntity(mop.getBlockPos())).getSuctionAmount(mop.sideHit.getOpposite())
                        + " suction on the opposite side.";
                Minecraft.getMinecraft().fontRenderer.drawString(toDraw,
                        (new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() / 2)
                                - Minecraft.getMinecraft().fontRenderer.getStringWidth(toDraw) / 2,
                        (new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() / 2) - 12, 0xffffff);
                Minecraft.getMinecraft().fontRenderer.drawString(toDraw2,
                        (new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() / 2)
                                - Minecraft.getMinecraft().fontRenderer.getStringWidth(toDraw2) / 2,
                        (new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() / 2) + 4, 0xffffff);
            }
        }
    }

}
