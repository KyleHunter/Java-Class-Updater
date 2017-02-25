package eUpdater.analysers.methods;

import eUpdater.frame.classFrame;
import eUpdater.frame.hook;
import eUpdater.misc.classes;
import eUpdater.searchers.Searcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.Arrays;
import java.util.List;

import static eUpdater.misc.JarHandler.CLASSES;

/**
 * Created by Kyle on 11/16/2015.
 */
public class item extends methodAnalyserFrame {

    public void identify() {
        this.setParent("Item", classes.myItem);
        this.setNeededHooks(Arrays.asList("ID", "StackSizes"));

        MethodNode method = null;
        for (classFrame c : CLASSES.values()) {
            List<MethodNode> methods = c.getMethods(true, "(II)V");
            for (MethodNode m : methods) {
                Searcher search = new Searcher(m);
                if (search.findSingleLdcValue(Opcodes.LDC, (long) -99999999) != -1)
                    method = m;

            }
        }
        AbstractInsnNode[] Instructions = method.instructions.toArray();
        Searcher search = new Searcher(method);
        hook temp1 = new hook(), temp2 = new hook();
        boolean run = false;
        int L = 0;
        for (int I = 0; L != -1; ++I) {
            L = search.find(new int[]{Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.LDC, Opcodes.IMUL}, I);
            if (L != -1 && ((VarInsnNode) Instructions[L]).var == 7) {
                if (run && !((FieldInsnNode) Instructions[L + 1]).name.equals(temp1.getName()))
                    temp2 = new hook("ID", Instructions, L + 1);
                else
                    temp1 = new hook("StackSizes", Instructions, L + 1);
                run = true;
            }

        }
        addHook(temp1);
        addHook(temp2);


    }

}
