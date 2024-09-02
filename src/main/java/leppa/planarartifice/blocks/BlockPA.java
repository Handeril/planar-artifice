package leppa.planarartifice.blocks;

import leppa.planarartifice.main.PlanarArtifice;
import leppa.planarartifice.registry.PABlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPA extends Block {
    public String[] variantNames;

    public BlockPA(Material material, String name) {
        super(material);
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setCreativeTab(PlanarArtifice.creativetab);
        this.setHardness(3);
        PABlocks.BLOCKS.add(this);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }


}
