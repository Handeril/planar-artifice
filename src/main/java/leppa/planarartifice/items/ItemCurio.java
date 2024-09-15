package leppa.planarartifice.items;

import leppa.planarartifice.main.PlanarArtifice;
import leppa.planarartifice.registry.PAItems;
import leppa.planarartifice.util.LocalizationHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.common.lib.SoundsTC;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemCurio extends ItemPA {

    public String category;
    public String name;

    public ItemCurio(String name, String category) {
        super(name);
        this.name = name;
        this.category = category;
    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        world.playSound(null, player.posX, player.posY, player.posZ, SoundsTC.learn, SoundCategory.NEUTRAL, 0.5f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f));
        if (!world.isRemote) {

            ResearchCategory[] rc = (ResearchCategory[]) ResearchCategories.researchCategories.values().toArray((Object[]) new ResearchCategory[0]);
            int oProg = IPlayerKnowledge.EnumKnowledgeType.OBSERVATION.getProgression();
            int tProg = IPlayerKnowledge.EnumKnowledgeType.THEORY.getProgression();

            if (this == PAItems.dimensional_curiosity) {
                ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.THEORY, rc[player.getRNG().nextInt(rc.length)], MathHelper.getInt(player.getRNG(), tProg * 2, tProg / 2));

            } else {
                ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, ResearchCategories.getResearchCategory(category), MathHelper.getInt(player.getRNG(), oProg / 2, oProg));
                ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.THEORY, ResearchCategories.getResearchCategory(category), MathHelper.getInt(player.getRNG(), tProg / 3, tProg / 2));
                ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, rc[player.getRNG().nextInt(rc.length)], MathHelper.getInt(player.getRNG(), oProg / 2, oProg));
                ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.THEORY, rc[player.getRNG().nextInt(rc.length)], MathHelper.getInt(player.getRNG(), tProg / 3, tProg / 2));
            }

            if (!player.capabilities.isCreativeMode)
                player.getHeldItem(hand).shrink(1);

            player.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + LocalizationHelper.localize("tc.knowledge.gained")));
            player.addStat(StatList.getObjectUseStats(this));

        }

        return super.onItemRightClick(world, player, hand);
    }

    @SuppressWarnings("deprecation")
    public EnumRarity getRarity(@Nonnull ItemStack itemstack) {
        return PlanarArtifice.rarityPA;
    }

    public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(LocalizationHelper.localize("planarartifice." + name));
    }
}
