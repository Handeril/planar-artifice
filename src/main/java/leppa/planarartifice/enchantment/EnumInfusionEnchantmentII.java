package leppa.planarartifice.enchantment;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import leppa.planarartifice.registry.PAItems;
import leppa.planarartifice.util.OreUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import thaumcraft.api.items.ItemsTC;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public enum EnumInfusionEnchantmentII {
    TRANSMUTATIVE(ImmutableSet.of("axe", "pickaxe", "shovel", "weapon"), 1, "PA_INFUSION_ENCHANTMENT_2"),
    AURAINFUSING(ImmutableSet.of("weapon"), 1, "PA_INFUSION_ENCHANTMENT_2"),
    PROJECTING(ImmutableSet.of("weapon", "axe", "pickaxe", "shovel"), 5, "PA_INFUSION_ENCHANTMENT_2"),
    CURIOUS(ImmutableSet.of("weapon", "axe", "pickaxe", "shovel"), 5, "PA_INFUSION_ENCHANTMENT_2");

    public Set<String> toolClasses;
    public int maxLevel;
    public String research;

    public static final ItemStack[] CURIOS = {new ItemStack(ItemsTC.curio), OreUtils.meta(ItemsTC.curio, 1), OreUtils.meta(ItemsTC.curio, 2), OreUtils.meta(ItemsTC.curio, 3), OreUtils.meta(ItemsTC.curio, 4), OreUtils.meta(ItemsTC.curio, 5), new ItemStack(PAItems.dimensional_curiosity), new ItemStack(PAItems.fundamental_curiosity)};

    EnumInfusionEnchantmentII(Set<String> toolClasses, int ml, String research) {
        this.toolClasses = toolClasses;
        this.maxLevel = ml;
        this.research = research;
    }

    public static NBTTagList getInfusionEnchantmentTagList(ItemStack stack) {
        return stack == null || stack.isEmpty() || stack.getTagCompound() == null ? null : stack.getTagCompound().getTagList("infenchplanar", 10);
    }

    public static List<EnumInfusionEnchantmentII> getInfusionEnchantments(ItemStack stack) {
        NBTTagList nbttaglist = getInfusionEnchantmentTagList(stack);
        ArrayList<EnumInfusionEnchantmentII> list = new ArrayList<EnumInfusionEnchantmentII>();
        if (nbttaglist != null) {
            for (int j = 0; j < nbttaglist.tagCount(); ++j) {
                short k = nbttaglist.getCompoundTagAt(j).getShort("id");
                short l = nbttaglist.getCompoundTagAt(j).getShort("lvl");
                if (k < 0 || k >= EnumInfusionEnchantmentII.values().length)
                    continue;
                list.add(EnumInfusionEnchantmentII.values()[k]);
            }
        }
        return list;
    }

    public static int getInfusionEnchantmentLevel(ItemStack stack, EnumInfusionEnchantmentII enchantment) {
        NBTTagList nbttaglist = EnumInfusionEnchantmentII.getInfusionEnchantmentTagList(stack);
        if (nbttaglist != null) {
            for (int j = 0; j < nbttaglist.tagCount(); ++j) {
                short k = nbttaglist.getCompoundTagAt(j).getShort("id");
                short l = nbttaglist.getCompoundTagAt(j).getShort("lvl");
                if (k < 0 || k >= EnumInfusionEnchantmentII.values().length || EnumInfusionEnchantmentII.values()[k] != enchantment)
                    continue;
                return l;
            }
        }
        return 0;
    }

    public static void addInfusionEnchantment(ItemStack stack, EnumInfusionEnchantmentII ie, int level) {
        if (stack == null || stack.isEmpty() || level > ie.maxLevel) {
            System.out.println("uwu");
            return;
        }
        NBTTagList nbttaglist = EnumInfusionEnchantmentII.getInfusionEnchantmentTagList(stack);
        if (nbttaglist != null) {
            for (int j = 0; j < nbttaglist.tagCount(); ++j) {
                short k = nbttaglist.getCompoundTagAt(j).getShort("id");
                short l = nbttaglist.getCompoundTagAt(j).getShort("lvl");
                if (k != ie.ordinal())
                    continue;
                if (level <= l) {
                    return;
                }
                nbttaglist.getCompoundTagAt(j).setShort("lvl", (short) level);
                stack.setTagInfo("infenchplanar", nbttaglist);
                if (ie == PROJECTING)
                    handleProjecting(stack);
                return;
            }
        } else {
            nbttaglist = new NBTTagList();
        }
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setShort("id", (short) ie.ordinal());
        nbttagcompound.setShort("lvl", (short) level);
        nbttaglist.appendTag(nbttagcompound);
        if (nbttaglist.tagCount() > 0) {
            stack.setTagInfo("infenchplanar", nbttaglist);
        }

        if (ie == PROJECTING)
            handleProjecting(stack);
    }

    private static void handleProjecting(ItemStack stack) {
        // Add an nbt tag to give Projecting items extended reach.
        NBTTagCompound nbt = stack.getTagCompound();

        Multimap<String, AttributeModifier> map = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
        Collection<AttributeModifier> reachCollection = map.get(EntityPlayer.REACH_DISTANCE.getName());
        Collection<AttributeModifier> damageCollection = map.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
        Collection<AttributeModifier> speedCollection = map.get(SharedMonsterAttributes.ATTACK_SPEED.getName());
        double reach;
        try {
            AttributeModifier reachModifier = (AttributeModifier) reachCollection.toArray()[0];
            System.out.println(reachModifier.getAmount());
            reach = reachModifier.getAmount() + 2;
        } catch (ArrayIndexOutOfBoundsException e) {
            reach = 2;
            System.out.println("WHAT");
        }
        AttributeModifier reachDistance = new AttributeModifier("reachDistance", reach, 0);
        AttributeModifier attackDamage = (AttributeModifier) damageCollection.toArray()[0];
        AttributeModifier attackSpeed = (AttributeModifier) speedCollection.toArray()[0];
        NBTTagCompound reachNBT = writeAttributeModifierToNBT(EntityPlayer.REACH_DISTANCE, reachDistance, EntityEquipmentSlot.MAINHAND);
        NBTTagCompound damageNBT = writeAttributeModifierToNBT(SharedMonsterAttributes.ATTACK_DAMAGE, attackDamage, EntityEquipmentSlot.MAINHAND);
        NBTTagCompound speedNBT = writeAttributeModifierToNBT(SharedMonsterAttributes.ATTACK_SPEED, attackSpeed, EntityEquipmentSlot.MAINHAND);
        NBTTagList list = new NBTTagList();
        list.appendTag(reachNBT);
        list.appendTag(damageNBT);
        list.appendTag(speedNBT);
        nbt.setTag("AttributeModifiers", list);
    }

    private static NBTTagCompound writeAttributeModifierToNBT(IAttribute attribute, AttributeModifier modifier, EntityEquipmentSlot slot) {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setString("AttributeName", attribute.getName());
        nbt.setString("Name", modifier.getName());
        nbt.setString("Slot", slot.getName());
        nbt.setDouble("Amount", modifier.getAmount());
        nbt.setInteger("Operation", modifier.getOperation());
        nbt.setLong("UUIDMost", modifier.getID().getMostSignificantBits());
        nbt.setLong("UUIDLeast", modifier.getID().getLeastSignificantBits());

        return nbt;
    }
}