package leppa.planarartifice.blocks.fluid;

import leppa.planarartifice.registry.PABlocks;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class PAFluidBlock extends BlockFluidClassic {

	public PAFluidBlock(Fluid fluid, Material material) {
		super(fluid, material);
		
		this.setRegistryName(fluid.getName());
		this.setTranslationKey(fluid.getName());
		
		PABlocks.BLOCKS.add(this);
	}
}
