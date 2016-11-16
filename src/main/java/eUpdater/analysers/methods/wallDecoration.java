package eUpdater.analysers.methods;

import eUpdater.frame.classFrame;
import eUpdater.frame.hook;
import eUpdater.misc.classes;
import eUpdater.searchers.Searcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;

import static eUpdater.misc.JarHandler.CLASSES;

/**
 * Created by Kyle on 11/16/2015.
 */
public class wallDecoration extends methodAnalyserFrame {

    public void identify() {
        this.setParent("GameObject", classes.myGameObject);
        this.setNeededHooks(Arrays.asList("ID", "Flags", "LocalX", "LocalY", "Plane", "Renderable",
                "Renderable2", "Orientation", "Height", "RelativeX", "RelativeY"));

        MethodNode method = null;
        for (classFrame c : CLASSES.values()) {
            method = c.getMethod(true,"(IIIIL" + classes.myRenderable.getName() +
                    ";L" + classes.myRenderable.getName() + ";IIIIII)V");
            if (method != null)
                break;
        }
        Searcher search = new Searcher(method);
        AbstractInsnNode[] Instructions = method.instructions.toArray();
        int L = 0;
        L = search.find(new int[]{Opcodes.NEW, Opcodes.DUP, Opcodes.INVOKESPECIAL}, 0);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 0);
        addHook(new hook("ID", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("Flags", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("LocalX", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("LocalY", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("Plane", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("Renderable", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("Renderable2", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("Orientation", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("Height", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("RelativeX", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 50, 1);
        addHook(new hook("RelativeY", Instructions, L));
    }

}
