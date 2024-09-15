package leppa.planarartifice.registry;

import leppa.planarartifice.enchantment.EnumInfusionEnchantmentII;
import leppa.planarartifice.enchantment.InfusionEnchantmentRecipeII;
import static leppa.planarartifice.main.PlanarArtifice.MODID;
import leppa.planarartifice.recipe.RecipeTransmutation;
import leppa.planarartifice.util.Aspects;
import leppa.planarartifice.util.OreUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import thaumcraft.api.ThaumcraftApi;
import static thaumcraft.api.ThaumcraftApiHelper.makeCrystal;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.*;
import thaumcraft.api.items.ItemsTC;

import java.util.ArrayList;
import java.util.List;

public class PARecipes {

    static ResourceLocation defaultGroup = new ResourceLocation("");

    public static void registerRecipes(Register<IRecipe> e) {
        registerArcaneRecipes(e);
        registerCrucibleRecipes(e);
        registerInfusionRecipes(e);
//    	registerPotionMixerRecipes(e);
        registerTransmutationRecipes(e);
    }


    private static void registerArcaneRecipes(Register<IRecipe> e) {
        registerShapedArcaneRecipe("alkimium_smeltery", "PA_ALKIMIUM_APPLICATIONS@2", 100, new Aspects("aqua"), PABlocks.alkimium_smeltery, "#C#", "ADA", "AAA", '#', PAItems.alkimium_plate, 'A', "plateBrass", 'C', BlocksTC.smelterBasic, 'D', PABlocks.alchemical_alkimium_construct);
        registerShapedArcaneRecipe("alkimium_smeltery_thaumium", "PA_ALKIMIUM_THAUMIUM_SMELTERY", 300, new Aspects("aqua"), PABlocks.alkimium_smeltery_thaumium, "#C#", "ADA", "AAA", '#', PAItems.alkimium_plate, 'A', OreUtils.meta(ItemsTC.plate, 2), 'C', PABlocks.alkimium_smeltery, 'D', PABlocks.alchemical_alkimium_construct);
        registerShapedArcaneRecipe("alkimium_smeltery_void", "PA_ALKIMIUM_VOID_SMELTERY@2", 1000, new Aspects("aqua"), PABlocks.alkimium_smeltery_void, "#C#", "ADA", "AAA", '#', PAItems.alkimium_plate, 'A', OreUtils.meta(ItemsTC.plate, 3), 'C', PABlocks.alkimium_smeltery_thaumium, 'D', BlocksTC.metalAlchemicalAdvanced);
        registerShapedArcaneRecipe("alkimium_centrifuge", "PA_ALKIMIUM_APPLICATIONS", 100, new Aspects("perditio", "perditio"), PABlocks.alkimium_centrifuge, " P ", "C#M", " P ", '#', BlocksTC.centrifuge, 'P', BlocksTC.tube, 'C', PABlocks.alchemical_alkimium_construct, 'M', ItemsTC.mechanismComplex);
        registerShapedArcaneRecipe("alchemical_alkimium_construct", "PA_ALKIMIUM@2", 100, new Aspects("aqua"), PABlocks.alchemical_alkimium_construct, "#v#", "pwp", "#v#", '#', PAItems.alkimium_plate, 'p', BlocksTC.tube, 'v', BlocksTC.tubeValve, 'w', BlocksTC.plankSilverwood);
        registerShapedArcaneRecipe("teleporter_matrix", "PA_MIRROR_TELEPORTER@2", 300, new Aspects("ordo", "perditio"), PABlocks.teleporter_matrix, "AHA", "MDM", "AHA", 'A', BlocksTC.stoneArcaneBrick, 'M', ItemsTC.mirroredGlass, 'H', ItemsTC.alumentum, 'D', PAItems.dimensional_singularity);
//        registerShapedArcaneRecipe("teleporter_matrix2", "PA_MIRROR_TELEPORTER@2", 300, new Aspects("ordo", "perditio"), PABlocks.teleporter_matrix, "AHA", "MDM", "AHA", 'A', BlocksTC.stoneArcaneBrick, 'M', ItemsTC.mirroredGlass, 'H', ItemsTC.alumentum, 'D', PAItems.dimensional_curiosity);
        registerShapedArcaneRecipe("flux_scrubber", "PA_FLUX_SCRUBBER", 275, Aspects.getPrimals(), PABlocks.flux_scrubber, "IGI", "IAI", "BQB", 'B', PABlocks.bismuth_block, 'A', PABlocks.alchemical_alkimium_construct, 'G', ItemsTC.mechanismSimple, 'I', "plateIron", 'Q', ItemsTC.alumentum);
        registerShapelessArcaneRecipe("alkimium_improved_distillation_aux_1", "PA_ALKIMIUM_DISTILLATION", 70, new Aspects("ordo"), PABlocks.smelter_aux, BlocksTC.smelterAux, PABlocks.alchemical_alkimium_construct);
        registerShapelessArcaneRecipe("alkimium_improved_distillation_aux_2", "PA_ALKIMIUM_DISTILLATION", 70, new Aspects("ordo"), PABlocks.smelter_vent, BlocksTC.smelterVent, PABlocks.alchemical_alkimium_construct);
        registerShapelessArcaneRecipe("alkimium_improved_distillation_aux_3", "PA_ALKIMIUM_DISTILLATION", 70, new Aspects("perditio"), BlocksTC.smelterAux, PABlocks.smelter_aux);
        registerShapelessArcaneRecipe("alkimium_improved_distillation_aux_4", "PA_ALKIMIUM_DISTILLATION", 70, new Aspects("perditio"), BlocksTC.smelterVent, PABlocks.smelter_vent);
        registerShapedArcaneRecipe("flux_vent", "PA_BISMUTH_CASTERS_GAUNTLET@1", 75, new Aspects(), PAItems.flux_venting_circuit, " # ", "QRV", '#', makeCrystal(Aspect.FLUX), 'Q', Items.QUARTZ, 'R', Items.REPEATER, 'V', Blocks.REDSTONE_TORCH);
        registerShapedArcaneRecipe("bismuth_caster", "PA_BISMUTH_CASTERS_GAUNTLET", 175, Aspects.getPrimals(), PAItems.bismuth_caster, " VO", "MGB", "BB ", 'B', PAItems.bismuth_plate, 'V', PAItems.flux_venting_circuit, 'M', ItemsTC.mirroredGlass, 'O', ItemsTC.visResonator, 'G', ItemsTC.casterBasic);
        registerShapedArcaneRecipe("aura_meter", "PA_BISMUTH", 25, Aspects.getPrimals(), PAItems.aura_meter, "A", "B", "C", 'A', Blocks.GLASS_PANE, 'B', makeCrystal(Aspect.AIR), 'C', PAItems.bismuth_plate);
        registerShapedArcaneRecipe("bismuth_claymore", "PA_BISMUTH", 125, new Aspects("ignis"), PAItems.bismuth_claymore, " B ", "BSB", " S ", 'B', PAItems.bismuth_ingot, 'S', Items.STICK);
    }

    public static void registerShapedArcaneRecipe(String resource, String research, int vis, Aspects aspects, Item stack, Object... recipes) {
        registerShapedArcaneRecipe(resource, research, vis, aspects, new ItemStack(stack), recipes);
    }

    public static void registerShapedArcaneRecipe(String resource, String research, int vis, Aspects aspects, Block stack, Object... recipes) {
        registerShapedArcaneRecipe(resource, research, vis, aspects, new ItemStack(stack), recipes);
    }

    public static void registerShapedArcaneRecipe(String resource, String research, int vis, Aspects aspects, ItemStack stack, Object... recipes) {
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(MODID, resource), new ShapedArcaneRecipe(defaultGroup, research, vis, aspects, stack, CraftingHelper.parseShaped(recipes)));
    }

    public static void registerShapelessArcaneRecipe(String resource, String research, int vis, Aspects aspects, Item stack, Object... recipes) {
        registerShapelessArcaneRecipe(resource, research, vis, aspects, new ItemStack(stack), recipes);
    }

    public static void registerShapelessArcaneRecipe(String resource, String research, int vis, Aspects aspects, Block stack, Object... recipes) {
        registerShapelessArcaneRecipe(resource, research, vis, aspects, new ItemStack(stack), recipes);
    }

    public static void registerShapelessArcaneRecipe(String resource, String research, int vis, Aspects aspects, ItemStack stack, Object... recipes) {
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(MODID, resource), new ShapelessArcaneRecipe(defaultGroup, research, vis, aspects, stack, recipes));
    }

    private static void registerCrucibleRecipes(Register<IRecipe> e) {
        registerCrucibleRecipe("alkimium_ingot", "METALLURGY@2", PAItems.alkimium_ingot, OreUtils.meta(ItemsTC.ingots, 2), new Aspects("alkimia", 5, "ordo", 5));
        registerCrucibleRecipe("magic_apple", "PA_RARE_ITEMS@4", PAItems.magic_apple, Items.APPLE, new Aspects("praecantatio", 10, "victus", 70, "bestia", 25, "terra", 15));
        registerCrucibleRecipe("alchemical_scribing_tools", "PA_ALCHEMICAL_SCRIBING_TOOLS", PAItems.alchemical_scribing_tools, ItemsTC.scribingTools, new Aspects("auram", 15, "alkimia", 15));
        registerCrucibleRecipe("bismuth", "!Portal", PAItems.bismuth_ingot, OreUtils.meta(ItemsTC.ingots, 0), new Aspects("auram", 20, "potentia", 20));
        //registerCrucibleRecipe("condensed_crystal_cluster1", "", PAItems.condensed_crystal_cluster, ItemsTC.salisMundus, new Aspects("ignis", 50));
        //registerCrucibleRecipe("condensed_crystal_cluster2", "", PAItems.condensed_crystal_cluster, ItemsTC.salisMundus, new Aspects(Aspect.WATER, 50));
        //registerCrucibleRecipe("condensed_crystal_cluster3", "", PAItems.condensed_crystal_cluster, ItemsTC.salisMundus, new Aspects("aer", 50));
        //registerCrucibleRecipe("condensed_crystal_cluster4", "", PAItems.condensed_crystal_cluster, ItemsTC.salisMundus, new Aspects("terra", 50));
        //registerCrucibleRecipe("condensed_crystal_cluster5", "", PAItems.condensed_crystal_cluster, ItemsTC.salisMundus, new Aspects("ordo", 50));
        //registerCrucibleRecipe("condensed_crystal_cluster6", "", PAItems.condensed_crystal_cluster, ItemsTC.salisMundus, new Aspects("perditio", 50));
    }

    public static void registerCrucibleRecipe(String resource, String research, Item output, Object catalyst, AspectList tags) {
        registerCrucibleRecipe(resource, research, new ItemStack(output), catalyst, tags);
    }

    public static void registerCrucibleRecipe(String resource, String research, Block output, Object catalyst, AspectList tags) {
        registerCrucibleRecipe(resource, research, new ItemStack(output), catalyst, tags);
    }

    public static void registerCrucibleRecipe(String resource, String research, ItemStack output, Object catalyst, AspectList tags) {
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(MODID, resource), new CrucibleRecipe(research, output, catalyst, tags));
    }

    private static void registerInfusionRecipes(Register<IRecipe> e) {
        registerInfusionRecipe("alkimium_goggles", "PA_ALKIMIUM_GOGGLES", PAItems.alkimium_goggles, 1, new Aspects("alkimia", 50, "auram", 25), ItemsTC.goggles, PAItems.alkimium_plate, PAItems.alkimium_plate, PAItems.alkimium_plate, makeCrystal(Aspect.AURA));
        registerInfusionRecipe("belt_of_suspension", "PA_BELT_OF_SUSPENSION", PAItems.belt_of_suspension, 5, new Aspects("auram", 50, "aer", 75, "machina", 15, "motus", 75, "potentia", 65, "volatus", 125, "instrumentum", 15), OreUtils.meta(ItemsTC.baubles, 2), Items.FEATHER, ItemsTC.ringCloud, Items.SUGAR, ItemsTC.alumentum, BlocksTC.levitator, BlocksTC.pavingStoneBarrier, Blocks.PISTON, BlocksTC.crystalAir);
        registerInfusionRecipe("dimensional_singularity", "PA_DIMENSIONAL_SINGULARITY", PAItems.dimensional_singularity, 5, new Aspects("auram", 75, "perditio", 15, "spatio", 45, "tempus", 10, "permutatio", 25, "potentia", 200), ItemsTC.salisMundus, ItemsTC.mirroredGlass, Items.ENDER_PEARL, Blocks.OBSIDIAN, Blocks.GOLDEN_RAIL, ItemsTC.alumentum, Blocks.REDSTONE_BLOCK, ItemsTC.visResonator);
        registerInfusionRecipe("mirrored_amulet", "PA_MIRRORED_AMULET", PAItems.mirrored_amulet, 8, new Aspects("auram", 50, "vitreus", 25, "potentia", 35, "instrumentum", 20, "spatio", 65, "permutatio", 64), OreUtils.meta(ItemsTC.baubles, 4), PAItems.dimensional_singularity, Blocks.HOPPER, BlocksTC.hungryChest, BlocksTC.crystalOrder, Items.NAME_TAG, BlocksTC.mirror);
        registerInfusionRecipe("mirrored_amulet2", "PA_MIRRORED_AMULET", PAItems.mirrored_amulet, 8, new Aspects("auram", 50, "vitreus", 25, "potentia", 35, "instrumentum", 20, "spatio", 65, "permutatio", 64), OreUtils.meta(ItemsTC.baubles, 4), PAItems.dimensional_curiosity, Blocks.HOPPER, BlocksTC.hungryChest, BlocksTC.crystalOrder, Items.NAME_TAG, BlocksTC.mirror);
        registerInfusionRecipe("mirromirous_headband", "PA_MIRROMIROUS_HEADBAND", PAItems.mirromirous_headband, 7, new Aspects("cognitio", 175, "vitreus", 50, "vinculum", 125), ItemsTC.bandCuriosity, PAItems.bismuth_plate, Items.ENCHANTED_BOOK, PAItems.bismuth_plate, Items.ENCHANTED_BOOK, PAItems.bismuth_plate, Items.ENCHANTED_BOOK, PAItems.bismuth_plate, Items.ENCHANTED_BOOK);
        registerInfusionEnchantment("IETransmutative", "TRANSMUTATIVE", EnumInfusionEnchantmentII.TRANSMUTATIVE, new Aspects("alkimia", 60, "vitium", 45), ingredientNBT(Items.ENCHANTED_BOOK), PABlocks.alkimium_block);
        registerInfusionEnchantment("IEAuraInfusing", "AURAINFUSING", EnumInfusionEnchantmentII.AURAINFUSING, new Aspects("auram", 50, "potentia", 60), ingredientNBT(Items.ENCHANTED_BOOK), ItemsTC.visResonator);
        registerInfusionEnchantment("IEProjecting", "PROJECTING", EnumInfusionEnchantmentII.PROJECTING, new Aspects("instrumentum", 15, "aversio", 15, "motus", 15), ingredientNBT(Items.ENCHANTED_BOOK), Items.ENDER_PEARL);
        registerInfusionEnchantment("IECurious", "CURIOUS", EnumInfusionEnchantmentII.CURIOUS, new Aspects("cognitio", 30, "instrumentum", 30), OreUtils.meta(ItemsTC.curio, 1), Items.EXPERIENCE_BOTTLE);
        registerInfusionRecipe("starving_chest_1", "PA_STARVING_CHEST", PABlocks.starving_chest, 3, new Aspects("vinculum", 25, "spatio", 25), BlocksTC.hungryChest, Blocks.HOPPER, ItemsTC.filter, Blocks.HOPPER, ItemsTC.filter);
        registerInfusionRecipe("starving_chest_2", "PA_STARVING_CHEST", OreUtils.meta(PABlocks.starving_chest, 1), 6, new Aspects("vinculum", 50, "spatio", 50), OreUtils.meta(PABlocks.starving_chest, 0), Blocks.HOPPER, ItemsTC.filter, Blocks.HOPPER, ItemsTC.filter);
        registerInfusionRecipe("starving_chest_3", "PA_STARVING_CHEST", OreUtils.meta(PABlocks.starving_chest, 2), 9, new Aspects("vinculum", 75, "spatio", 75), OreUtils.meta(PABlocks.starving_chest, 1), Blocks.HOPPER, ItemsTC.filter, Blocks.HOPPER, ItemsTC.filter);
        registerInfusionRecipe("starving_chest_4", "PA_STARVING_CHEST", OreUtils.meta(PABlocks.starving_chest, 3), 12, new Aspects("vinculum", 99, "spatio", 99), OreUtils.meta(PABlocks.starving_chest, 2), Blocks.HOPPER, ItemsTC.filter, Blocks.HOPPER, ItemsTC.filter);
    }

    public static void registerInfusionRecipe(String resource, String research, Item stack, int inst, Aspects aspects, Object catalyst, Object... recipe) {
        registerInfusionRecipe(resource, research, new ItemStack(stack), inst, aspects, catalyst, recipe);
    }

    public static void registerInfusionRecipe(String resource, String research, Block stack, int inst, Aspects aspects, Object catalyst, Object... recipe) {
        registerInfusionRecipe(resource, research, new ItemStack(stack), inst, aspects, catalyst, recipe);
    }

    public static void registerInfusionRecipe(String resource, String research, ItemStack stack, int inst, Aspects aspects, Object catalyst, Object... recipe) {
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(MODID, resource), new InfusionRecipe(research, stack, inst, aspects, catalyst, recipe));
    }

    public static void registerInfusionEnchantment(String resource, String text, EnumInfusionEnchantmentII ench, Aspects aspects, Object... recipe) {
        InfusionEnchantmentRecipeII IEEnchantment = new InfusionEnchantmentRecipeII(ench, aspects, recipe);
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(MODID, resource), IEEnchantment);
        ItemStack recipeStack = new ItemStack(ItemsTC.thaumiumSword);
        recipeStack.setStackDisplayName(TextFormatting.RESET + recipeStack.getDisplayName() + " +" + TextFormatting.GOLD + I18n.translateToLocal("enchantment.infusion." + text));
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation(MODID, resource + "Fake"), new InfusionEnchantmentRecipeII(IEEnchantment, recipeStack));
    }

    public static IngredientNBTTC ingredientNBT(Item stack) {
        return ingredientNBT(new ItemStack(stack));
    }

    public static IngredientNBTTC ingredientNBT(Block stack) {
        return ingredientNBT(new ItemStack(stack));
    }

    public static IngredientNBTTC ingredientNBT(ItemStack stack) {
        return new IngredientNBTTC(stack);
    }
    private static void registerTransmutationRecipes(Register<IRecipe> e) {
        registerTransmutationRecipe(e, "transmutation_rotten_flesh", Items.ROTTEN_FLESH, Items.CHICKEN);
        registerTransmutationRecipe(e, "transmutation_gunpowder", Items.GUNPOWDER, Items.GLOWSTONE_DUST);
        registerTransmutationRecipe(e, "transmutation_string", Items.STRING, Items.FEATHER);
        registerTransmutationRecipe(e, "transmutation_ender_pearl", Items.ENDER_PEARL, Items.ENDER_EYE);
        registerTransmutationRecipe(e, "transmutation_spider_eye", Items.SPIDER_EYE, Items.FERMENTED_SPIDER_EYE);
        registerTransmutationRecipe(e, "transmutation_slime_ball", Items.SLIME_BALL, Items.MAGMA_CREAM);
        registerTransmutationRecipe(e, "transmutation_glowstone", Items.GLOWSTONE_DUST, Items.BLAZE_POWDER);
        registerTransmutationRecipe(e, "transmutation_iron_ingot", Items.IRON_INGOT, Items.GOLD_INGOT);
        registerTransmutationRecipe(e, "transmutation_gold_ingot", Items.GOLD_INGOT, Items.IRON_INGOT);
        registerTransmutationRecipe(e, "transmutation_iron_nugget", Items.IRON_NUGGET, Items.GOLD_NUGGET);
        registerTransmutationRecipe(e, "transmutation_gold_nugget", Items.GOLD_NUGGET, Items.IRON_NUGGET);
        registerTransmutationRecipe(e, "transmutation_sugar", Items.SUGAR, Items.REDSTONE);
        registerTransmutationRecipe(e, "transmutation_redstone", Items.REDSTONE, ItemsTC.salisMundus);
        registerTransmutationRecipe(e, "transmutation_bone", Items.BONE, Items.COAL);
        registerTransmutationRecipe(e, "transmutation_diamond", Items.DIAMOND, Items.EMERALD);
        registerTransmutationRecipe(e, "transmutation_emerald", Items.EMERALD, Items.DIAMOND);
        registerTransmutationRecipe(e, "transmutation_beetroot_seeds", Items.BEETROOT_SEEDS, Items.WHEAT_SEEDS);
        registerTransmutationRecipe(e, "transmutation_seeds", Items.WHEAT_SEEDS, Items.MELON_SEEDS);
    }

    public static void registerTransmutationRecipe(Register<IRecipe> e, String resource, ItemStack stack, Item stack2) {
        registerTransmutationRecipe(e, resource, stack, new ItemStack(stack2));
    }

    public static void registerTransmutationRecipe(Register<IRecipe> e, String resource, ItemStack stack, Block stack2) {
        registerTransmutationRecipe(e, resource, stack, new ItemStack(stack2));
    }

    public static void registerTransmutationRecipe(Register<IRecipe> e, String resource, Item stack, ItemStack stack2) {
        registerTransmutationRecipe(e, resource, new ItemStack(stack), stack2);
    }

    public static void registerTransmutationRecipe(Register<IRecipe> e, String resource, Block stack, ItemStack stack2) {
        registerTransmutationRecipe(e, resource, new ItemStack(stack), stack2);
    }

    public static void registerTransmutationRecipe(Register<IRecipe> e, String resource, Item stack, Item stack2) {
        registerTransmutationRecipe(e, resource, new ItemStack(stack), new ItemStack(stack2));
    }

    public static void registerTransmutationRecipe(Register<IRecipe> e, String resource, Item stack, Block stack2) {
        registerTransmutationRecipe(e, resource, new ItemStack(stack), new ItemStack(stack2));
    }

    public static void registerTransmutationRecipe(Register<IRecipe> e, String resource, Block stack, Item stack2) {
        registerTransmutationRecipe(e, resource, new ItemStack(stack), new ItemStack(stack2));
    }

    public static void registerTransmutationRecipe(Register<IRecipe> e, String resource, Block stack, Block stack2) {
        registerTransmutationRecipe(e, resource, new ItemStack(stack), new ItemStack(stack2));
    }

    public static void registerTransmutationRecipe(Register<IRecipe> e, String resource, ItemStack stack, ItemStack stack2) {
        e.getRegistry().register(new RecipeTransmutation(new ResourceLocation(MODID, resource), stack, stack2));
    }

    public static <T> List<T> getRecipesOfType(Class<T> clazz) {
        ArrayList<T> recipes = new ArrayList<>();
        ForgeRegistries.RECIPES.forEach(r -> {
            if (r.getClass() == clazz)
                recipes.add((T) r);
        });
        return recipes;
    }

}
