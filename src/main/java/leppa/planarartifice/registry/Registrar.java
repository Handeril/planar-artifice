package leppa.planarartifice.registry;

import leppa.planarartifice.compat.PACompatHandler;
import leppa.planarartifice.main.PlanarArtifice;
import leppa.planarartifice.util.AspectUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.aspects.AspectRegistryEvent;

@EventBusSubscriber(modid = PlanarArtifice.MODID)
public class Registrar {

	@SubscribeEvent
	public static void registerBlocks(Register<Block> event) {
		PABlocks.registerBlocks(event);
		PACompatHandler.registerBlocks(event);
	}

	@SubscribeEvent
	public static void registerItems(Register<Item> event) {
		PABlocks.registerItemBlocks(event);
		PAItems.registerItems(event);
		PACompatHandler.registerItems(event);
		Registrar.registerOres();
	}

	@SubscribeEvent
	public static void registerRecipes(Register<IRecipe> event) {
		PARecipes.registerRecipes(event);
		PAMultiblocks.registerMultiblocks();
		PACompatHandler.registerRecipes(event);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event) {
		PABlocks.registerModels();
		PAItems.registerModels();
		PACompatHandler.registerModels(event);
	}

	public static void registerOres() {
		OreDictionary.registerOre("blockAlkimium", PABlocks.alkimium_block);
		OreDictionary.registerOre("ingotAlkimium", PAItems.alkimium_ingot);
		OreDictionary.registerOre("plateAlkimium", PAItems.alkimium_plate);
		OreDictionary.registerOre("nuggetAlkimium", PAItems.alkimium_nugget);

		OreDictionary.registerOre("blockBismuth", PABlocks.bismuth_block);
		OreDictionary.registerOre("ingotBismuth", PAItems.bismuth_ingot);
		OreDictionary.registerOre("plateBismuth", PAItems.bismuth_plate);
		OreDictionary.registerOre("nuggetBismuth", PAItems.bismuth_nugget);

		PACompatHandler.registerOres();
	}

	@SubscribeEvent
	public static void registerAspects(AspectRegistryEvent event) {
		AspectUtils.event = event.register;
		PAAspects.registerItemAspects();
		PACompatHandler.registerAspects();
	}
}
