package eUpdater.analysers.methods;

import eUpdater.frame.hook;
import eUpdater.misc.classes;
import eUpdater.searchers.Searcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Kyle on 10/16/2015.
 */
public class cacheable extends methodAnalyserFrame{

    public void identify() {
        this.setParent("Cacheable", classes.myCacheable);
        this.setNeededHooks(Arrays.asList("Next", "Prev"));

        List<MethodNode> methodList = parentClass.methods;
        for (MethodNode method : methodList) {
            if (method.name.equals("<init>"))
                continue;

            AbstractInsnNode[] instructions = method.instructions.toArray();
            Searcher search = new Searcher(method);
            int L =search.find(new int[] {Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.IFNONNULL}, 0);
            if (instructions[L + 1] instanceof FieldInsnNode) {
                addHook(new hook("Next", instructions, L + 1));

                List<FieldNode> fields = parentClass.fields;
                for (FieldNode field : fields) {
                    if (field.name.contains(classes.myCacheable.getMethodAnalyser().getHook("Next").getName()))
                        continue;
                    addHook(new hook("Prev", field));
                    break;
                }
            }
        }
    }
}
