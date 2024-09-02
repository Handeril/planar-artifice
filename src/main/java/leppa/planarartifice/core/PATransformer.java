package leppa.planarartifice.core;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.HashMap;

public class PATransformer implements IClassTransformer {
    public static HashMap<String, Transformer> transformers = new HashMap<>();

    static {
        transformers.put("thaumcraft.common.blocks.world.taint.TaintHelper", new TransformSpreadFibre());
        transformers.put("thaumcraft.client.gui.GuiResearchPage", new TransformDrawKnowledge());
        transformers.put("thaumcraft.common.lib.crafting.ThaumcraftCraftingManager", new TransformCraftingManager());
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!PlanarArtificeCore.enabled || !transformers.containsKey(transformedName)) return basicClass;
        PlanarArtificeCore.LOGGER.info("[PA, CORE] Transforming via " + transformers.get(transformedName).getClass().getName());
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(basicClass);
        reader.accept(node, 0);
        // transform
        try { transformers.get(transformedName).transform(node); }
        catch (Throwable e) { throw new RuntimeException(e); }
        // end transform
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES) {
            @Override
            protected String getCommonSuperClass(String type1, String type2) {
                return "java/lang/Object";
            }
        };
        node.accept(writer);
        return writer.toByteArray();
    }

    // utils, also stolen from aug

    static MethodNode findMethod(ClassNode classNode, String name, String desc) {
        for (MethodNode m : classNode.methods) if (m.name.equals(name) && m.desc.equals(desc)) return m;
        return null;
    }
    static int findMethodCall(MethodNode node, int startIndex, String name, String desc, String owningClass) {
        if (startIndex < 0 || startIndex >= node.instructions.size()) return -1;
        for (int i = startIndex; i < node.instructions.size(); ++i) {
            AbstractInsnNode insn = node.instructions.get(i);
            if (insn instanceof MethodInsnNode) {
                MethodInsnNode method = (MethodInsnNode) insn;
                if (method.name.equals(name) && method.desc.equals(desc) && method.owner.equals(owningClass)) return i;
            }
        }
        return -1;
    }
    static int findField(MethodNode node, int startIndex, String name, String desc, String owningClass) {
        if (startIndex < 0 || startIndex >= node.instructions.size())
            return -1;
        for (int i = startIndex; i < node.instructions.size(); ++i) {
            AbstractInsnNode insn = node.instructions.get(i);
            if (insn instanceof FieldInsnNode) {
                FieldInsnNode field = (FieldInsnNode) insn;
                if (field.name.equals(name) && field.desc.equals(desc) && field.owner.equals(owningClass)) return i;
            }
        }
        return -1;
    }
    static int findOpcode(MethodNode node, int startIndex, int opcode) {
        if (startIndex < 0 || startIndex >= node.instructions.size()) return -1;
        for (int i = startIndex; i < node.instructions.size(); ++i) if (node.instructions.get(i).getOpcode() == opcode) return i;
        return -1;
    }
    static String remapFieldName(String internalName, String fieldName) {
        String internal = FMLDeobfuscatingRemapper.INSTANCE.unmap(internalName);
        return FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(internal, fieldName, null);
    }
    static String remapMethodName(String internalName, String methodName, Type returnType, Type... parameterTypes) {
        String internal = FMLDeobfuscatingRemapper.INSTANCE.unmap(internalName);
        String desc = Type.getMethodDescriptor(returnType, parameterTypes);
        return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(internal, methodName, desc);
    }
    static int findLabel(MethodNode node, int startIndex) {
        if (startIndex < 0 || startIndex >= node.instructions.size()) return -1;
        for (int i = startIndex; i < node.instructions.size(); ++i) {
            AbstractInsnNode insn = node.instructions.get(i);
            if (insn instanceof LabelNode) return i;
        }
        return -1;
    }
}
