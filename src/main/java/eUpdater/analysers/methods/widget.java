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
                "InvIDs", "BoundsIndex", "StackSizes", "TextureID", "Parent"));

        addHook(new hook("Children", parentClass.getFields("[L" + classes.myWidget.getName() + ";").get(0)));

        MethodNode method = null;
        for (classFrame c : CLASSES.values()) {
            List<MethodNode> methodList = c.methods;
            for (MethodNode m : methodList) {
                if (c.name.equals(classes.myWidget.getName()) && m.name.equals("<init>"))
                    method = m;
            }
        }

        AbstractInsnNode[] Instructions = method.instructions.toArray();
        int L;
        Searcher search = new Searcher(method);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, 0, 10, 1);
        addHook(new hook("WidgetID", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L + 1, 50, 11);
        addHook(new hook("AbsoluteX", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L + 1, 50, 0);
        addHook(new hook("AbsoluteY", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L + 1, 50, 0);
        addHook(new hook("Width", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L + 1, 50, 0);
        addHook(new hook("Height", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L + 1, 50, 2);
        addHook(new hook("ParentID", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L + 1, 50, 0);
        addHook(new hook("IsHidden", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L + 1, 50, 0);
        addHook(new hook("RelativeX", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L + 1, 50, 0);
        addHook(new hook("RelativeY", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L + 1, 50, 12);
        addHook(new hook("TextureID", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L + 1, 150, 22);
        addHook(new hook("Text", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L + 1, 50, 8);
        addHook(new hook("Name", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L + 1, 50, 0);
        addHook(new hook("Parent", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L + 1, 50, 8);
        addHook(new hook("ItemID", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L + 1, 50, 0);
        addHook(new hook("ItemAmount", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, L + 1, 50, 8);
        addHook(new hook("BoundsIndex", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, 0, 200, 21);
        addHook(new hook("ScrollX", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTFIELD, 0, 200, 22);
        addHook(new hook("ScrollY", Instructions, L));

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

