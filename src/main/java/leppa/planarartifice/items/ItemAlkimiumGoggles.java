package leppa.planarartifice.items;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.render.IRenderBauble;
import leppa.planarartifice.main.PlanarArtifice;
import leppa.planarartifice.registry.PAItems;
import leppa.planarartifice.util.LocalizationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.IGoggles;
import thaumcraft.api.items.IRevealer;
import thaumcraft.api.items.IVisDiscountGear;
import thaumcraft.client.lib.UtilsFX;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemAlkimiumGoggles extends ItemArmor implements IRevealer, IGoggles, IVisDiscountGear, IBauble, IRenderBauble {

	public static final ResourceLocation BAUBLE_TEXTURE = new ResourceLocation(PlanarArtifice.MODID, "textures/items/alkimium/alkimium_goggles_bauble.png");

	public ItemAlkimiumGoggles(String name, ArmorMaterial mat) {
		super(mat, 3, EntityEquipmentSlot.HEAD);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(PlanarArtifice.creativetab);
		PAItems.ITEMS.add(this);
	}

	public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}
	public int getVisDiscount(ItemStack stack, EntityPlayer player) {
		return 10;
	}
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.HEAD;
	}
	@SuppressWarnings("deprecation")
	public EnumRarity getRarity(@Nonnull ItemStack stack) {
		return PlanarArtifice.rarityPA;
	}
	@Override
	public boolean showNodes(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}
	@SideOnly(Side.CLIENT)
	public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GREEN + LocalizationHelper.localize("planarartifice.alkimium"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float ticks) {
		if(type == IRenderBauble.RenderType.HEAD) {
			player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
			Minecraft.getMinecraft().renderEngine.bindTexture(BAUBLE_TEXTURE);
			IRenderBauble.Helper.translateToHeadLevel(player);
			IRenderBauble.Helper.translateToFace();
			IRenderBauble.Helper.defaultTransforms();
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.translate(-0.5D, -0.5D, 0.11999999731779099D);
			UtilsFX.renderTextureIn3D(0.0F, 0.0F, 1.0F, 1.0F, 16, 26, 0.1F);
		}
	}
	
	public static boolean shouldRenderHud(EntityPlayer player) {
		return player.inventory.armorItemInSlot(3).getItem() == PAItems.alkimium_goggles
		|| BaublesApi.isBaubleEquipped(player, PAItems.alkimium_goggles) != -1;
	}
}
