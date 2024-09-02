package leppa.planarartifice.items;

import leppa.planarartifice.main.PlanarArtifice;
import leppa.planarartifice.registry.PAItems;
import leppa.planarartifice.util.LocalizationHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.casters.FocusPackage;
import thaumcraft.api.casters.IFocusBlockPicker;
import thaumcraft.api.casters.IFocusElement;
import thaumcraft.common.items.casters.ItemCaster;
import thaumcraft.common.items.casters.ItemFocus;

import java.util.List;

public class ItemBismuthCaster extends ItemCaster {
	
	float costMultiplier = 1.5f;
	
	public ItemBismuthCaster(String name){
		super(name, 0);
		this.setCreativeTab(PlanarArtifice.creativetab);
		PAItems.ITEMS.add(this);
	}
	
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
		if(world.isRemote){ 
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand)); 
		}
		
		ItemStack focusStack = this.getFocusStack(player.getHeldItem(hand));
		ItemFocus focus = this.getFocus(player.getHeldItem(hand));
		if(focus != null && !PAFocusEngine.isOnCooldown(player)){
			PAFocusEngine.setCooldown(player, focus.getActivationTime(focusStack));
			FocusPackage core = ItemFocus.getPackage(focusStack);
			if(player.isSneaking()){
				for(IFocusElement fe : core.nodes){
					if(!(fe instanceof IFocusBlockPicker) || !player.isSneaking())
						continue;
					return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
				}
			}

			if(this.consumeVis(player.getHeldItem(hand), player, focus.getVisCost(focusStack) * costMultiplier, false, false)){
				PAFocusEngine.castFocusPackageWithMultiplier(player, core, 2);
				player.swingArm(hand);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
			}
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, player.getHeldItem(hand));
		}
		return super.onItemRightClick(world, player, hand);
	}
	
	@SideOnly(value=Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add("2x" + LocalizationHelper.localize("tooltip.casterpower"));
		tooltip.add("1.5x" + LocalizationHelper.localize("tooltip.castercost"));
    }
}