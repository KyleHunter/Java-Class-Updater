package eUpdater.analysers.methods;

import eUpdater.frame.classFrame;
import eUpdater.frame.hook;
import eUpdater.misc.classes;
import eUpdater.searchers.Searcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.List;

import static eUpdater.misc.JarHandler.CLASSES;

/**
 * Created by Kyle on 11/5/2015.
 */
public class actor extends methodAnalyserFrame {

    public void identify() {
        this.setParent("Actor", classes.myActor);
        this.setNeededHooks(Arrays.asList("WorldX", "WorldY", "QueueX", "QueueY", "QueueSize",
                "Animation", "SpokenText", "CombatCycle", "Health", "MaxHealth", "InteractingIndex"));

        MethodNode method;
        Searcher search;
        AbstractInsnNode[] Instructions = null;


//  Causes duplicates
//        for (classFrame c : CLASSES.values()) {
//            method = c.getMethod(true, "(L" + classes.myActor.getName() + ";)V");
//            if (method != null) {
//                Instructions = method.instructions.toArray();
//                search = new Searcher(method);
//                int L = search.find(new int[]{Opcodes.GETFIELD}, 0);
//                addHook(new hook("WorldX", Instructions, L));
//                L = search.find(new int[]{Opcodes.GETFIELD}, 1);
//                addHook(new hook("WorldY", Instructions, L));
//            }
//        }

        int L;
        out:
        for (classFrame c : CLASSES.values()) {
            method = c.getMethod(true, "(L" + classes.myActor.getName() + ";)V");
            if (method != null) {
                Instructions = method.instructions.toArray();
                search = new Searcher(method);
                for (int I = 0; I < 1000; ++I) {
                    L = search.find(new int[]{Opcodes.IINC}, I);
                    if (L != -1)
                        if ((((IincInsnNode) Instructions[L]).incr) == -2048) {
                            L = search.find(new int[]{Opcodes.GETFIELD, Opcodes.LDC, Opcodes.IMUL,
                                    Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.ALOAD, Opcodes.GETFIELD,
                                    Opcodes.LDC, Opcodes.IMUL, Opcodes.ICONST_1}, 0);
                            addHook(new hook("QueueX", Instructions, L + 4));
                            L = search.find(new int[]{Opcodes.GETFIELD, Opcodes.LDC, Opcodes.IMUL,
                                    Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.ALOAD, Opcodes.GETFIELD,
                                    Opcodes.LDC, Opcodes.IMUL, Opcodes.ICONST_1}, 1);
                            addHook(new hook("QueueY", Instructions, L + 4));
                            addHook(new hook("QueueSize", Instructions, L + 6));
                            break out;
                        }
                }
            }
        }

        if (containsHook("QueueX")) {
            hook queueHook = getHook("QueueX");
            for (classFrame c : CLASSES.values()) {
                method = c.getMethod(true, "(L" + classes.myActor.getName() + ";)V");
                if (method != null && (method.access & Opcodes.ACC_STATIC) != 0) {
                    Instructions = method.instructions.toArray();
                    search = new Searcher(method);
                    for (int I = 0; I < 1000; ++I) {
                        L = search.find(new int[]{Opcodes.GETFIELD}, I);
                        if (L != -1) {
                            FieldInsnNode queueNode = (FieldInsnNode) Instructions[L];
                            if (queueNode != null && queueNode.name.equals(queueHook.getName())
                                    && queueNode.owner.equals(queueHook.getOwner())) {
                                int H = search.find(new int[]{Opcodes.PUTFIELD}, 0, L, L + 10);
                                if (H != -1) {
                                    FieldInsnNode xNode = (FieldInsnNode) Instructions[H];
                                    if (xNode != null && xNode.owner.equals(queueHook.getOwner())
                                            && xNode.desc.equals("I")) {
                                        addHook(new hook("WorldX", Instructions, H));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (containsHook("QueueY")) {
            hook queueHook = getHook("QueueY");
            for (classFrame c : CLASSES.values()) {
                method = c.getMethod(true, "(L" + classes.myActor.getName() + ";)V");
                if (method != null && (method.access & Opcodes.ACC_STATIC) != 0) {
                    Instructions = method.instructions.toArray();
                    search = new Searcher(method);
                    for (int I = 0; I < 1000; ++I) {
                        L = search.find(new int[]{Opcodes.GETFIELD}, I);
                        if (L != -1) {
                            FieldInsnNode queueNode = (FieldInsnNode) Instructions[L];
                            if (queueNode != null && queueNode.name.equals(queueHook.getName())
                                    && queueNode.owner.equals(queueHook.getOwner())) {
                                int H = search.find(new int[]{Opcodes.PUTFIELD}, 0, L, L + 10);
                                if (H != -1) {
                                    FieldInsnNode yNode = (FieldInsnNode) Instructions[H];
                                    if (yNode != null && yNode.owner.equals(queueHook.getOwner())
                                            && yNode.desc.equals("I")) {
                                        addHook(new hook("WorldY", Instructions, H));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (classFrame c : CLASSES.values()) {
            List<MethodNode> methodList = c.methods;
            for (MethodNode m : methodList) {
                search = new Searcher(m);
                L = search.findSingleIntValue(Opcodes.SIPUSH, 13184);
                if (L != -1) {
                    Instructions = m.instructions.toArray();
                    L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 25, 0);
                    addHook(new hook("Animation", Instructions, L));
                }
            }
        }


/* Depreciated due to gamepack update
        L = 0;
        boolean fail = false;
        for (classFrame c : CLASSES.values()) {
            method = c.getMethod(true, "(L" + classes.myActor.getName() + ";III)V");
            if (method != null) {
                Instructions = method.instructions.toArray();
                search = new Searcher(method);
                for (int I = 0; L != -1; ++I) {
                    L = search.find(new int[]{Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.IFNULL}, I);//ifnull or ACONST_NULL, SEARCHER.IF
                    if (L == -1) {
                        if (!fail) {
                            I = 0;
                            fail = true;
                        }
                        L = search.find(new int[]{Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.ACONST_NULL, Searcher.IF}, I);
                    }
                    if (Instructions[L + 1] instanceof FieldInsnNode && ((FieldInsnNode) Instructions[L + 1]).desc.equals("Ljava/lang/String;")) {
                        addHook(new hook("SpokenText", Instructions, L + 1));

                        L = search.find(new int[]{Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.LDC, Opcodes.IMUL,
                                Opcodes.GETSTATIC, Opcodes.LDC}, 0);
                        addHook(new hook("CombatCycle", Instructions, L + 1));

                        L = search.find(new int[]{Opcodes.ILOAD, Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.LDC,
                                Opcodes.IMUL, Opcodes.IMUL}, 0, 0);
                        addHook(new hook("Health", Instructions, L + 2));

                        L = search.find(new int[]{Opcodes.ALOAD, Opcodes.GETFIELD}, 0, L + 5);
                        addHook(new hook("MaxHealth", Instructions, L + 1));

                        L = -1;
                    }
                }
            }
        }
*/
        addHook(new hook("SpokenText", Instructions, -1));
        addHook(new hook("CombatCycle", Instructions, -1));
        addHook(new hook("Health", Instructions, -1));
        addHook(new hook("MaxHealth", Instructions, -1));

        for (classFrame c : CLASSES.values()) {
            L = 0;
            method = c.getMethod(true, "(L" + classes.myActor.getName() + ";)V");
            if (method != null) {
                Instructions = method.instructions.toArray();
                search = new Searcher(method);
                for (int I = 0; L != -1; ++I) {
                    L = search.find(new int[]{Opcodes.GETFIELD, Opcodes.LDC, Opcodes.IMUL, Opcodes.LDC}, I);
                    if (L != -1)
                        if ((((LdcInsnNode) Instructions[L + 3]).cst.equals(32768))) {
                            addHook(new hook("InteractingIndex", Instructions, L));
                            L = -1;
                        }
                }
            }
        }
    }
}

