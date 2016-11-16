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
public class gameObject extends methodAnalyserFrame {

    public void identify() {
        this.setParent("GameObject", classes.myGameObject);
        this.setNeededHooks(Arrays.asList("ID", "Flags", "Plane", "WorldX", "WorldY", "Height",
                "Render", "Orientation", "LocalX", "LocalY", "OffsetX", "OffsetY"));

        MethodNode method = null;
        for (classFrame c : CLASSES.values()) {
            method = c.getMethod(true, "(IIIIIIIIL" + classes.myRenderable.getName() + ";IZII)Z");
            if (method != null)
                break;
        }
        Searcher search = new Searcher(method);
        AbstractInsnNode[] Instructions = method.instructions.toArray();
        int L = 0;
        int S = 0;
        for (int I = 0; L != -1; ++I) {
            L = search.find(new int[]{Opcodes.ALOAD, Opcodes.ILOAD, Opcodes.LDC, Opcodes.IMUL, Opcodes.PUTFIELD}, I);
            if (((VarInsnNode) Instructions[L + 1]).var == 12) {
                S = L;
                L = -1;
            }

        }
        addHook(new hook("ID", Instructions, S + 4));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, S, 50, 1);
        addHook(new hook("Flags", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("Plane", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("WorldX", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("WorldY", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("Height", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("Render", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("Orientation", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("LocalX", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("LocalY", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("OffsetX", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("OffsetY", Instructions, L));


    }
}

