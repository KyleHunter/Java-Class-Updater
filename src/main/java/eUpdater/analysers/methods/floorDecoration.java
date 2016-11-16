package eUpdater.analysers.methods;

import eUpdater.frame.classFrame;
import eUpdater.frame.hook;
import eUpdater.misc.classes;
import eUpdater.searchers.Searcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.Arrays;

import static eUpdater.misc.JarHandler.CLASSES;

/**
 * Created by Kyle on 11/16/2015.
 */
public class floorDecoration extends methodAnalyserFrame {

    public void identify() {
        this.setParent("GameObject", classes.myGameObject);
        this.setNeededHooks(Arrays.asList("ID", "Flags", "Plane", "Render", "LocalX", "LocalY"));

        MethodNode method = null;
        for (classFrame c : CLASSES.values()) {
            method = c.getMethod(true, "(IIIIL" + classes.myRenderable.getName() + ";II)V");
            if (method != null)
                break;
        }
        Searcher search = new Searcher(method);
        AbstractInsnNode[] Instructions = method.instructions.toArray();
        int L = 0;
        int S = 0;
        for (int I = 0; L != -1; ++I) {
            L = search.find(new int[]{Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.PUTFIELD}, I);
            if (((VarInsnNode) Instructions[L]).var == 8 && ((VarInsnNode) Instructions[L + 1]).var == 5) {
                S = L;
                L = -1;
            }

        }
        addHook(new hook("Render", Instructions, S + 2));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, S, 50, 1);
        addHook(new hook("LocalX", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("LocalY", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("Plane", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("ID", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("Flags", Instructions, L));
    }
}
