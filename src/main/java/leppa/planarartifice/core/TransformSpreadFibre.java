package leppa.planarartifice.core;

import static leppa.planarartifice.core.PATransformer.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class TransformSpreadFibre extends Transformer {
    public TransformSpreadFibre() {}
    void transform(ClassNode node) {
        PlanarArtificeCore.LOGGER.info("[PA, CORE] Patching in EventFibreSpread event");
        MethodNode m = findMethod(node, "spreadFibres", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Z)V");
        assert m != null;
        int offset = findMethodCall(m, 0, "<init>", "(III)V", "net/minecraft/util/math/BlockPos");
        if (offset != -1) offset = findOpcode(m, offset, Opcodes.ASTORE);
        AbstractInsnNode q = m.instructions.get(offset); // Extreme hopium
        PlanarArtificeCore.LOGGER.info("[PA, CORE] Offset: " + offset + ", " + q.getOpcode() + " > " + q.getNext().getOpcode());
        // insert: MinecraftForge.EVENT_BUS.post(new EventFibreSpread(world(0), pos(1), t(7)));
        m.instructions.insert(q, new InsnNode(Opcodes.POP));
        m.instructions.insert(q, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));
        m.instructions.insert(q, new MethodInsnNode(Opcodes.INVOKESPECIAL, "leppa/planarartifice/api/event/EventFibreSpread", "<init>", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;)V", false));
        m.instructions.insert(q, new VarInsnNode(Opcodes.ALOAD, 7));
        m.instructions.insert(q, new VarInsnNode(Opcodes.ALOAD, 1));
        m.instructions.insert(q, new VarInsnNode(Opcodes.ALOAD, 0));
        m.instructions.insert(q, new InsnNode(Opcodes.DUP));
        m.instructions.insert(q, new TypeInsnNode(Opcodes.NEW, "leppa/planarartifice/api/event/EventFibreSpread"));
        m.instructions.insert(q, new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
        PlanarArtificeCore.LOGGER.info("[PA, CORE] Successfully patched in EventFibreSpread event");
    }
}
