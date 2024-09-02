package leppa.planarartifice.registry;

import leppa.planarartifice.main.PlanarArtifice;
import static leppa.planarartifice.util.AspectUtils.*;
import leppa.planarartifice.util.Aspects;
import leppa.planarartifice.util.OreUtils;
import magicbees.integration.thaumcraft.IntegrationThaumcraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import thaumcraft.api.aspects.Aspect;

import java.awt.*;

public class PAAspects {

	public static Aspect DIMENSIONS;
	public static Aspect TIME;
	public static Aspect COLOR;

	public static void registerAspects() {
		DIMENSIONS = new Aspect("spatio", 0x4AF755, new Aspect[]{Aspect.VOID, Aspect.ENTROPY}, new ResourceLocation(PlanarArtifice.MODID, "textures/aspects/spatio.png"), 1);
		if (Loader.isModLoaded("magicbees") && IntegrationThaumcraft.ASPECT_TIME != null) TIME = IntegrationThaumcraft.ASPECT_TIME;
		else TIME = new Aspect("tempus", 0xD6DB43, new Aspect[]{DIMENSIONS, Aspect.EXCHANGE}, new ResourceLocation(PlanarArtifice.MODID, "textures/aspects/tempus.png"), 1);
		COLOR = new Aspect("tinctura", 0xFFFFFF, new Aspect[]{Aspect.EXCHANGE, Aspect.SENSES}, new ResourceLocation(PlanarArtifice.MODID, "textures/aspects/tinctura.png"), 1) {
			@Override
			public int getColor() {
				return Color.HSBtoRGB(MinecraftServer.getCurrentTimeMillis() * 2 % 3600 / 3600F, 0.6F, 0.8F); // less saturation please
			}
		};
		for (Aspect a : Aspect.aspects.values()) Aspects.register(a);
		Aspects.register(DIMENSIONS, TIME);
	}

	public static void registerItemAspects() {
		PlanarArtifice.LOGGER.info("[PA, ASPECT] Start registering Aspects");

		add(Items.ENDER_PEARL, new Aspects("spatio", 10, "tempus", 5));
		add(Blocks.ENDER_CHEST, new Aspects("spatio", 12));
		add(PABlocks.flux_scrubber, new Aspects("spatio", 6));
		add(PAItems.dimensional_curiosity, new Aspects("spatio", 25));
		add(PAItems.bismuth_ingot, new Aspects("spatio", 3));
		add(PAItems.dimensional_singularity, new Aspects("spatio", 30, "tempus", 30));

		set(PAItems.condensed_crystal_cluster, Aspects.getPrimals().multiply(7));
		addOreDict("sand", new Aspects("tempus", 1));
		addByRegex(".*hour.*", new Aspects("tempus", 5));
		addByRegex("old.*", new Aspects("tempus", 10));
		addByRegex(".*ancient.*", new Aspects("tempus", 5));
		addByRegex(".*futur.*", new Aspects("tempus", 5));
		addByRegex(".*time.*", new Aspects("tempus", 4));
		addByRegex(".*spatial.*", new Aspects("spatio", 8));
		addByRegex(".*compass.*", new Aspects("spatio", 6));
		addByRegex(".*dimens.*", new Aspects("spatio", 4));
		addByRegex(".*chest(?!nut)(?!plate).*", new Aspects("spatio", 6));
		addByRegex(".*map(?!le).*", new Aspects("spatio", 5));
		addByRegex(".*clock.*", new Aspects("tempus", 10));
		addByRegex(".*temporal.*", new Aspects("tempus", 10));
		addByRegex(".*portal.*", new Aspects("spatio", 16));
		addByRegex(".*mossy.*", new Aspects("tempus", 2));
		add(OreUtils.meta(Blocks.STONEBRICK, 1), new Aspects("tempus", 2));
		addByRegex(".*cracked.*", new Aspects("tempus", 2));
		add(OreUtils.meta(Blocks.STONEBRICK, 2), new Aspects("tempus", 2));

		add(Items.ARMOR_STAND, new Aspects("praemunio", 5, "imperium", 5));
		add(Blocks.COMMAND_BLOCK, new Aspects("machina", 25, "praecantatio", 25).add("caeles", 25, "desiderium").add("imperium", 25, "instrumentum"));
		add(Blocks.STRUCTURE_BLOCK, new Aspects("machina", 25, "praecantatio", 25, "spatio", 25).add("caeles", 25, "desiderium"));
		add(Blocks.CHAIN_COMMAND_BLOCK, get(Blocks.COMMAND_BLOCK));
		add(Blocks.REPEATING_COMMAND_BLOCK, get(Blocks.COMMAND_BLOCK));
		add(Items.COMMAND_BLOCK_MINECART, get(Blocks.COMMAND_BLOCK).add(get(Items.MINECART)).multiply(0.75F));
		add(Items.KNOWLEDGE_BOOK, get(Items.BOOK).add("cognitio", 25).add("sensus", 25));
		add(Blocks.BARRIER, new Aspects("auram", 25, "vacuos", 25, "spiritus", 25, "spatio", 25));
		add(Blocks.STRUCTURE_VOID, new Aspects("spatio", 25, "vacuos", 25, "praecantatio", 25, "imperium", 25));
		set(Items.ITEM_FRAME, new Aspects("fabrico", 5, "sensus", 5, "herba", 5));
		addByRegex(".*sign", new Aspects("sensus", 4));
		add(Blocks.HOPPER, new Aspects("spatio", 4));
		add(Blocks.GRAVEL, new Aspects("tempus", 1));
	}
}