package eUpdater.analysers.methods;

/**
 * Created by Kyle on 11/10/2015.
 */

import eUpdater.frame.classFrame;
import eUpdater.frame.hook;
import eUpdater.misc.classes;
import eUpdater.searchers.Searcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.Collections;

import static eUpdater.misc.JarHandler.CLASSES;

/**
 * Created by Kyle on 11/5/2015.
 */
public class widgetNode extends methodAnalyserFrame {

    public void identify() {
        this.setParent("WidgetNode", classes.myWidgetNode);
        this.setNeededHooks(Collections.singletonList("Id"));
        out:
        for (classFrame c : CLASSES.values()) {
            MethodNode m = c.getMethod(true, "([L" + classes.myWidget.getName() + ";I)V");
            if (m != null) {
                int L = 0;
                for (int I = 0; L != -1; ++I) {
                    Searcher search = new Searcher(m);
                    AbstractInsnNode[] Instructions = m.instructions.toArray();
                    L = search.find(new int[]{Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.LDC, Opcodes.IMUL}, I);
                    if (L != -1 && ((VarInsnNode) m.instructions.get(L)).var == 5 && ((FieldInsnNode) m.instructions.get(L + 1)).owner.equals(classes.myWidgetNode.getName())) {
                        addHook(new hook("Id", Instructions, L + 1));
                        break out;
                    }
                }
            }
        }


    }

}
