package leppa.planarartifice.core;

import static leppa.planarartifice.core.PATransformer.findMethod;
import static leppa.planarartifice.core.PATransformer.findMethodCall;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class TransformDrawKnowledge extends Transformer {
    void transform(ClassNode node) {
        PlanarArtificeCore.LOGGER.info("[PA, CORE] Patching in drawKnowledges rows");
        MethodNode m = findMethod(node, "drawKnowledgesInsert", "(II)V");
        assert m != null;
        int offset = findMethodCall(m, 0, "drawKnowledges", "(IIIIZ)V", "thaumcraft/client/gui/GuiResearchPage");
        AbstractInsnNode q = m.instructions.get(offset).getPrevious().getPrevious(); // this should be iload 6
        PlanarArtificeCore.LOGGER.info("[PA, CORE] Offset: " + offset + ", " + q.getOpcode() + " > " + q.getNext().getOpcode());
        // replace TC.drawKnowledges(..., true) with PA.drawKnowledges(..., this.playerKnowledge, this)
        m.instructions.remove(q.getNext());
        m.instructions.remove(q.getNext());
        m.instructions.insert(q, new MethodInsnNode(Opcodes.INVOKESTATIC, "leppa/planarartifice/client/gui/DrawKnowledges", "drawKnowledges", "(Lthaumcraft/client/gui/GuiResearchPage;IIIILthaumcraft/api/capabilities/IPlayerKnowledge;)V", false));
        m.instructions.insert(q, new FieldInsnNode(Opcodes.GETFIELD, "thaumcraft/client/gui/GuiResearchPage", "playerKnowledge", "Lthaumcraft/api/capabilities/IPlayerKnowledge;"));
        m.instructions.insert(q, new VarInsnNode(Opcodes.ALOAD, 0));
        PlanarArtificeCore.LOGGER.info("[PA, CORE] Successfully patched in drawKnowledges rows");
    }
}
