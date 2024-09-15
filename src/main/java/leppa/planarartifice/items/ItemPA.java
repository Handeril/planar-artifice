package leppa.planarartifice.items;

import leppa.planarartifice.main.PlanarArtifice;
import leppa.planarartifice.registry.PAItems;
import net.minecraft.item.Item;

public class ItemPA extends Item {

    public ItemPA(String name) {
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setCreativeTab(PlanarArtifice.creativetab);

        PAItems.ITEMS.add(this);
    }
}
