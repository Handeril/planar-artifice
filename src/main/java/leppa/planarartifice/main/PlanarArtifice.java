package leppa.planarartifice.main;

import leppa.planarartifice.enchantment.EnumInfusionEnchantmentII;
import net.minecraft.item.EnumRarity;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thaumcraft.api.golems.EnumGolemTrait;

import java.util.List;

@SuppressWarnings("unchecked")
@EventBusSubscriber
@Mod(modid = PlanarArtifice.MODID, name = PlanarArtifice.NAME, dependencies = PlanarArtifice.DEPS)
public class PlanarArtifice implements LoadingCallback {
    public static final String MODID = "planarartifice";
    public static final String NAME = "Planar Artifice";
    public static final String DEPS =
            "required-after:thaumcraft;" +
                    "after:jei@[4.12.0.0,);" +
                    "after:thaumicadds;" +
                    "after:moretweaker;" +
                    "after:magicbees;" +
                    "after:tconstruct";
    public static final PlanarTab creativetab = new PlanarTab();

    public static boolean isSingleplayer;

    public static final EnumGolemTrait golemTraitTeleporter = EnumHelper.addEnum(EnumGolemTrait.class, "TELEPORTER",
            new Class[]{ResourceLocation.class},
            new ResourceLocation(MODID, "textures/misc/golem/tag_teleporter.png"));
    public static final EnumRarity rarityPA = EnumHelper.addRarity(MODID, TextFormatting.GREEN, NAME);

    @SidedProxy(serverSide = "leppa.planarartifice.main.CommonProxy", clientSide = "leppa.planarartifice.client.ClientProxy")
    public static CommonProxy proxy;

    @Instance(MODID)
    public static PlanarArtifice instance = new PlanarArtifice();
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    // stole from magical psi -p
    public PlanarArtifice() {
        super();
        if (!Loader.isModLoaded("xercapaint") || PAConfig.compat.disableXercaPaintCompat || FMLCommonHandler.instance().getSide().isServer())
            return;
        String classname = "leppa.planarartifice.compat.xercapaint.XercaResources";
        System.out.println(classname);
        try {
            Class<?> clazz = Class.forName(classname);
            clazz.getMethod("init").invoke(null);
        } catch (Throwable e) {
            System.out.println("XercaPaint resource error: " + e);
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);

        ForgeChunkManager.setForcedChunkLoadingCallback(this, this);
    }

    @SideOnly(value = Side.CLIENT)
    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event) {
        event.getItemStack();
        NBTTagList nbttaglist = EnumInfusionEnchantmentII.getInfusionEnchantmentTagList(event.getItemStack());
        if (nbttaglist != null) {
            for (int j = 0; j < nbttaglist.tagCount(); ++j) {
                short k = nbttaglist.getCompoundTagAt(j).getShort("id");
                short l = nbttaglist.getCompoundTagAt(j).getShort("lvl");
                if (k < 0 || k >= EnumInfusionEnchantmentII.values().length)
                    continue;
                String s = TextFormatting.GOLD + I18n
                        .translateToLocal("enchantment.infusion." + EnumInfusionEnchantmentII.values()[k].toString());
                if (EnumInfusionEnchantmentII.values()[k].maxLevel > 1) {
                    s = s + " " + I18n.translateToLocal("enchantment.level." + l);
                }
                event.getToolTip().add(1, s);
            }
        }
    }

    @Override
    public void ticketsLoaded(List<Ticket> tickets, World world) {
        // TODO Auto-generated method stub

    }
}
