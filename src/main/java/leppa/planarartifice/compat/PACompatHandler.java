
package leppa.planarartifice.compat;

import leppa.planarartifice.compat.tconstruct.TConstructHandler;
import leppa.planarartifice.compat.thaumicadditions.ThaumicAdditionsHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.HashMap;

public class PACompatHandler {

	public static final HashMap<String, Class<? extends ICompatModule>> CLASSES = new HashMap<>();
	public static final ArrayList<ICompatModule> MODULES = new ArrayList<ICompatModule>();

	static {
		// thaum additions
		CLASSES.put("thaumadditions", ThaumicAdditionsHandler.class);
		CLASSES.put("tconstruct", TConstructHandler.class);
	}

	public static void setup() {
		CLASSES.forEach((modid, clazz) -> {
			if(Loader.isModLoaded(modid))
				try {
					MODULES.add(clazz.newInstance());
				} catch(Exception e) {
					e.printStackTrace();
				}
		});
	}

	public static void preInit(FMLPreInitializationEvent e) {
		MODULES.forEach(p -> p.preInit(e));
	}
	public static void init(FMLInitializationEvent e) {
		MODULES.forEach(p -> p.init(e));
	}
	public static void postInit(FMLPostInitializationEvent e) {
		MODULES.forEach(p -> p.postInit(e));
	}
	public static void registerAspects() { MODULES.forEach(ICompatModule::registerAspects); }
	public static void registerBlocks(RegistryEvent.Register<Block> e) { MODULES.forEach(p -> p.registerBlocks(e)); }
	public static void registerItems(RegistryEvent.Register<Item> e) { MODULES.forEach(p -> p.registerItems(e)); }
	public static void registerRecipes(RegistryEvent.Register<IRecipe> e) { MODULES.forEach(p -> p.registerRecipes(e)); }
	public static void registerModels(ModelRegistryEvent e) { MODULES.forEach(p -> p.registerModels(e)); }
	public static void registerOres() { MODULES.forEach(ICompatModule::registerOres); }

	public interface ICompatModule {
		default void preInit(FMLPreInitializationEvent e) {}
		default void init(FMLInitializationEvent e) {}
		default void postInit(FMLPostInitializationEvent e) {}
		default void registerAspects() {}
		default void registerBlocks(RegistryEvent.Register<Block> e) {}
		default void registerItems(RegistryEvent.Register<Item> e) {}
		default void registerRecipes(RegistryEvent.Register<IRecipe> e) {}
		default void registerModels(ModelRegistryEvent e) {}
		default void registerOres() {}
		default ItemStack getModItem(String name, int amount, int meta) {
			return GameRegistry.makeItemStack(name, meta, amount, null);
		}

	}

}