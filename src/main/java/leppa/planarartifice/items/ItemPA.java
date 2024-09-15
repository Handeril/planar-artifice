package leppa.planarartifice.items;

import leppa.planarartifice.main.PlanarArtifice;
import leppa.planarartifice.registry.PAItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemPA extends Item {

    public ItemPA(String name) {
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setCreativeTab(PlanarArtifice.creativetab);

        PAItems.ITEMS.add(this);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return this == PAItems.dimensional_singularity;
    }

    public Item setContainerItem(Item containerItem) {
        if (this == PAItems.dimensional_singularity)
            return PAItems.dimensional_curiosity;
        return this;
    }
}
