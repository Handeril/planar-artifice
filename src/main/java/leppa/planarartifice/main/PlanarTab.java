package leppa.planarartifice.main;

import leppa.planarartifice.registry.PAItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class PlanarTab extends CreativeTabs {

	public PlanarTab() {
		super(PlanarArtifice.MODID + ".tab.name");
		setBackgroundImageName("planar.png");
		this.setNoTitle();
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(PAItems.alchemical_universe);
	}
}
