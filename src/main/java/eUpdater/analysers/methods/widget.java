package eUpdater.analysers.methods;

import eUpdater.frame.classFrame;
import eUpdater.frame.hook;
import eUpdater.misc.classes;
import eUpdater.searchers.Searcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;
import java.util.List;

import static eUpdater.misc.JarHandler.CLASSES;

/**
 * Created by Kyle on 11/9/2015.
 */
public class widget extends methodAnalyserFrame {

    public void identify() {
        this.setParent("Widget", classes.myWidget);
        this.setNeededHooks(Arrays.asList("Children", "ItemID", "ItemAmount", "WidgetID", "Name", "Text", "IsHidden",
                "AbsoluteY", "AbsoluteX", "RelativeX", "RelativeY", "Width", "Height", "ParentID", "ScrollY", "ScrollX",
                "InvIDs", "BoundsIndex", "StackSizes"));

        addHook(new hook("Children", parentClass.getFields("[L" + classes.myWidget.getName() + ";").get(0)));

        MethodNode method = null;
        for (classFrame c : CLASSES.values()) {
            List<MethodNode> methodList = c.methods;
            for (MethodNode m : methodList) {
                int L = 0;
                AbstractInsnNode[] Instructions = m.instructions.toArray();
                Searcher search = new Searcher(m);
                for (int I = 0; L != -1; ++I) {
                    L = search.find(new int[]{Opcodes.SIPUSH}, I);
                    if (L != -1)
                        if ((((IntInsnNode) Instructions[L]).operand == 2100))
                            method = m;
                }
            }
        }

        AbstractInsnNode[] Instructions = method.instructions.toArray();
        Searcher search = new Searcher(method);
        int L = search.findSingleIntValue(Opcodes.SIPUSH, 1701);/*
        L = search.find(new int[]{Opcodes.GETFIELD}, 0, L);
        addHook(new hook("ItemID", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETFIELD, L + 5, 15, 0);
        addHook(new hook("ItemAmount", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 1121);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETFIELD, L, 10, 0);
        addHook(new hook("WidgetID", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 1305);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L, 10, 0);
        addHook(new hook("Name", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 2602);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETFIELD, L, 15, 0);
        addHook(new hook("Text", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 2504);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETFIELD, L, 20, 0);
        addHook(new hook("IsHidden", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 1501);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETFIELD, L, 10, 0);
        addHook(new hook("AbsoluteY", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 1500, L - 100);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETFIELD, L, 10, 0);
        addHook(new hook("AbsoluteX", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 1601);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETFIELD, L, 10, 0);
        addHook(new hook("RelativeY", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 1600, L - 100);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETFIELD, L, 20, 0);
        addHook(new hook("RelativeX", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 2502);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETFIELD, L, 20, 0);
        addHook(new hook("Width", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 2503);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETFIELD, L, 10, 0);
        addHook(new hook("Height", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 2505);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETFIELD, L, 20, 0);
        addHook(new hook("ParentID", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 1601);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETFIELD, L, 10, 0);
        addHook(new hook("ScrollY", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 1600, L - 50);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETFIELD, L, 20, 0);
        addHook(new hook("ScrollX", Instructions, L));
*/
        for (classFrame c : CLASSES.values()) {
            method = c.getMethod(true, "([L" + classes.myWidget.getName() + ";IIIIIIII)V");
            if (method != null) {
                search = new Searcher(method);
                Instructions = method.instructions.toArray();
                break;
            }
        }

        L = search.find(new int[]{Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.ILOAD, Opcodes.IALOAD,
                Opcodes.ICONST_1, Opcodes.ISUB}, 0);
        if (L != -1)
            addHook(new hook("InvIDs", Instructions, L + 1));

        for (int I = 0; L != -1; ++I) {
            L = search.find(new int[]{Opcodes.GETSTATIC, Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.LDC,
                    Opcodes.IMUL, Opcodes.ICONST_1}, I);

            if (((FieldInsnNode) Instructions[L]).desc.equals("[Z")) {
                addHook(new hook("BoundsIndex", Instructions, L + 2));
                L = -1;
            }
        }

        List<MethodNode> methodList = parentClass.methods;
        for (MethodNode Method : methodList) {
            if (!Method.desc.contains("(II") || !Method.desc.contains("V"))
                continue;
            Searcher Search = new Searcher(Method);
            for (int I = 0; I < 10; ++I) {
                L = Search.findSingle(Opcodes.GETFIELD, I);
                if (L != -1) {
                    AbstractInsnNode[] instructions = Method.instructions.toArray();
                    if (!((FieldInsnNode) instructions[L]).name.contains(classes.myWidget.getMethodAnalyser().getHook("InvIDs").getName())) {
                        addHook(new hook("StackSizes", instructions, L));
                        break;
                    }
                }
            }
        }


    }
}

