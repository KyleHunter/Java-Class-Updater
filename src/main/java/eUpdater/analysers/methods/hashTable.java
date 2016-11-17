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
 * Created by Kyle on 11/10/2015.
 */
public class hashTable extends methodAnalyserFrame {

    public void identify() {
        this.setParent("HashTable", classes.myHashTable);
        this.setNeededHooks(Arrays.asList("Buckets", "Size", "Index"));

        addHook(new hook("Buckets", parentClass.getFields("[L" + classes.myNode.getName() + ";").get(0)));

        List<MethodNode> methodList = parentClass.methods;
        for (MethodNode m : methodList) {
            Searcher Search = new Searcher(m);
            int L = Search.find(new int[]{Opcodes.GETFIELD}, 0);
            if (L != -1) {
                AbstractInsnNode[] Instructions = m.instructions.toArray();
                if (((FieldInsnNode) Instructions[L]).name.equals(methods.myHashTable.getHook("Buckets").getName())) {
                    L = Search.find(new int[]{Opcodes.GETFIELD}, 0, L + 1);
                    if (L != -1 && ((FieldInsnNode) Instructions[L]).desc.equals("I")) {
                        addHook(new hook("Size", Instructions, L));
                        break;
                    }
                }
            }
        }

        List<FieldNode> fs = parentClass.getFields("I");
        for (FieldNode f : fs) {
            if (!f.name.equals(methods.myHashTable.getHook("Size").getName()))
                addHook(new hook("Index", f));
        }
    }
}
