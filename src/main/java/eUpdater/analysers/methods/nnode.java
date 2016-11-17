package eUpdater.analysers.methods;

import eUpdater.frame.hook;
import eUpdater.misc.classes;
import eUpdater.searchers.FieldSearcher;
import eUpdater.searchers.Searcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Kyle on 7/27/2015.
 */
public class nnode extends methodAnalyserFrame {

    public void identify() {
        setParent("Node", classes.myNode);
        this.setNeededHooks(Arrays.asList("Next", "Prev", "UID"));

        List<MethodNode> methodList = parentClass.methods;
        for (MethodNode m : methodList) {
            if (m.instructions.size() < 11) {
                AbstractInsnNode[] Instructions = m.instructions.toArray();
                Searcher search = new Searcher(m);
                int L = search.findSingle(Opcodes.GETFIELD, 0);
                if (L != -1)
                    addHook(new hook("Prev", Instructions, L));
            }
        }
        FieldSearcher fs = new FieldSearcher(parentClass);
        addHook(new hook("UID", fs.findDesc("J")));

        for (int I = 0; I < 3; ++I) {
            if (getHook("Prev").getName().equals(fs.findDescInstance(String.format("L%s;", parentClass.name), I).name))
                continue;
            addHook(new hook("Next", fs.findDescInstance(String.format("L%s;", parentClass.name), I)));
            break;
        }
    }
}
