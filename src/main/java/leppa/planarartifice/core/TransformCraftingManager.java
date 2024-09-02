package leppa.planarartifice.core;

import static leppa.planarartifice.core.PATransformer.*;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class TransformCraftingManager extends Transformer {
    void transform(ClassNode node) {
        PlanarArtificeCore.LOGGER.info("[PA, CORE] Patching in generateTagsFromRecipes");
        MethodNode m = findMethod(node, "generateTagsFromRecipes", "(Lnet/minecraft/item/ItemStack;Ljava/util/ArrayList;)Lthaumcraft/api/aspects/AspectList;");
        assert m != null;
        int offset = findMethodCall(m, 0, "generateTagsFromCraftingRecipes", "(Lnet/minecraft/item/ItemStack;Ljava/util/ArrayList;)Lthaumcraft/api/aspects/AspectList;", "thaumcraft/common/lib/crafting/ThaumcraftCraftingManager");
        if (offset != -1) offset = findOpcode(m, offset, Opcodes.ARETURN);
        AbstractInsnNode q = m.instructions.get(offset); // this should be areturn
        PlanarArtificeCore.LOGGER.info("[PA, CORE] Offset: " + offset + ", " + q.getOpcode() + " > " + q.getNext().getOpcode());
        // insert if (ret == null) return generateTagsFromRecipes(stack, history);
        m.instructions.insert(q, new InsnNode(Opcodes.ARETURN));
        m.instructions.insert(q, new MethodInsnNode(Opcodes.INVOKESTATIC, "leppa/planarartifice/util/AspectUtils", "generateTagsFromRecipes", "(Lnet/minecraft/item/ItemStack;Ljava/util/ArrayList;)Lthaumcraft/api/aspects/AspectList;", false));
        m.instructions.insert(q, new VarInsnNode(Opcodes.ALOAD, 1));
        m.instructions.insert(q, new VarInsnNode(Opcodes.ALOAD, 0));
        LabelNode label = new LabelNode(new Label());
        m.instructions.insert(q, label);
        q = q.getPrevious().getPrevious();
        m.instructions.insert(q, new JumpInsnNode(Opcodes.IFNULL, label));
        m.instructions.insert(q, new VarInsnNode(Opcodes.ALOAD, 2));
        PlanarArtificeCore.LOGGER.info("[PA, CORE] Successfully patched in generateTagsFromRecipes");
    }
}
