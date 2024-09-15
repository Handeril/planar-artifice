package leppa.planarartifice.items;

import leppa.planarartifice.main.PlanarArtifice;
import leppa.planarartifice.registry.PAItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemPAContainer extends Item {

    public ItemPAContainer(String name) {
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setCreativeTab(PlanarArtifice.creativetab);

        PAItems.ITEMS.add(this);
    }

    public boolean hasContainerItem(ItemStack stack)
    {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(PAItems.dimensional_curiosity);
    }

    @Override
    public Item setContainerItem(Item containerItem) {
        return PAItems.dimensional_curiosity;
    }
}
