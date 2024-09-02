package leppa.planarartifice.registry;

import leppa.planarartifice.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class PABlocks {

	public static final ArrayList<Block> BLOCKS = new ArrayList<>();
	public static final ArrayList<Block> METABLOCKS = new ArrayList<>();

	public static final Block alkimium_block = new BlockMetal("alkimium_block");
	public static final Block bismuth_block = new BlockMetal("bismuth_block");
	public static final Block alchemical_alkimium_construct = new BlockPA(Material.IRON, "alchemical_alkimium_construct");
	
	public static final Block alkimium_smeltery = new BlockAlkimiumSmeltery("alkimium_smeltery", 14, 0.85F, 375);
	public static final Block alkimium_smeltery_thaumium = new BlockAlkimiumSmeltery("alkimium_smeltery_thaumium", 7, 0.85F, 375);
	public static final Block alkimium_smeltery_void = new BlockAlkimiumSmeltery("alkimium_smeltery_void", 10, 0.95F, 375);
	public static final Block alkimium_centrifuge = new BlockAlkimiumCentrifuge("alkimium_centrifuge");

	public static final Block smelter_aux = new BlockAlkimiumSmelteryAux("smelter_aux");
	public static final Block smelter_vent = new BlockAlkimiumSmelteryVent("smelter_vent");

	public static final Block teleporter = new BlockTeleporterMiddle("teleporter").setCreativeTab(null);
	public static final Block teleporter_matrix = new BlockPA(Material.ROCK,"teleporter_matrix");
	public static final Block teleporter_placeholder = new BlockTeleporterPlaceholder("teleporter_placeholder").setCreativeTab(null);
	
//	public static final Block potion_mixer = new BlockPotionMixer("potion_mixer");
	public static final Block flux_scrubber = new BlockFluxScrubber("flux_scrubber");
	public static final Block starving_chest = new BlockStarvingChest("starving_chest");

	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		BLOCKS.forEach(b -> event.getRegistry().register(b));
		METABLOCKS.forEach(b -> event.getRegistry().register(b));
	}

	public static void registerItemBlocks(RegistryEvent.Register<Item> event)
	{
		BLOCKS.forEach(b -> event.getRegistry().register(new ItemBlock(b).setRegistryName(b.getRegistryName())));
		METABLOCKS.forEach(b -> { if (b instanceof BlockPA) event.getRegistry().register(new ItemMultiTexture(b, null, ((BlockPA)b).variantNames).setRegistryName(b.getRegistryName())); });
	}

	@SideOnly(Side.CLIENT)
	public static void registerModels() {
		BLOCKS.forEach(PABlocks::registerRender);
		METABLOCKS.forEach(b -> { if (b instanceof BlockPA) ((BlockPA)b).registerModels(); });
	}

	@SideOnly(Side.CLIENT)
	public static void registerRender(Block block) {
		if(block instanceof BlockFluidBase)
			ModelLoader.setCustomStateMapper(block, new StateMap.Builder().ignore(BlockFluidBase.LEVEL).build());
		else ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}

	public interface BlockFunction { Block run(Block block); }
}
