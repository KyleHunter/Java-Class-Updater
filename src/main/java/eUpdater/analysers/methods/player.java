package eUpdater.analysers.methods;

import eUpdater.frame.classFrame;
import eUpdater.frame.hook;
import eUpdater.misc.classes;
import eUpdater.searchers.Searcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;
import java.util.List;

import static eUpdater.misc.JarHandler.CLASSES;

/**
 * Created by Kyle on 11/10/2015.
 */
public class player extends methodAnalyserFrame {

    public void identify() {
        this.setParent("Player", classes.myPlayer);
        this.setNeededHooks(Arrays.asList("Name", "Definition", "CombatLevel"));

        addHook(new hook("Name", parentClass.getFields("Ljava/lang/String;").get(0)));

        List<MethodNode> methods = parentClass.getMethods(false, "L" + classes.myModel.getName() + ";");
        for (MethodNode m : methods) {
            Searcher search = new Searcher(m);
            AbstractInsnNode[] instruction = m.instructions.toArray();
            int L = search.find(new int[]{Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.ALOAD}, 0);
            if (L != -1) {
                addHook(new hook("Definition", instruction, L + 1));
                break;
            }
        }

        for (classFrame c : CLASSES.values()) {
            methods = c.getMethods(true, "(L" + classes.myWidget.getName() + ";I)I");
            for (MethodNode m : methods) {
                Searcher search = new Searcher(m);
                if (search.findSingleIntValue(Opcodes.BIPUSH, 100) != -1) {
                    AbstractInsnNode[] Instructions = m.instructions.toArray();
                    int L = search.findSingleIntValue(Opcodes.BIPUSH, 8);
                    L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETFIELD, L, 10, 0);
                    addHook(new hook("CombatLevel", Instructions, L));

                }
            }
        }
    }
}
