package eUpdater.deob;
/**
 * Created by Kyle on 1/12/2015.
 */

import eUpdater.searchers.Searcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;

import static eUpdater.misc.JarHandler.CLASSES;

public class RemoveExceptions extends DeobFrame {

    public static int Run() {
        int Patterns[][] = new int[][]{
                {Opcodes.ILOAD, Opcodes.LDC, Searcher.IF, Opcodes.NEW, Opcodes.DUP, Opcodes.INVOKESPECIAL, Opcodes.ATHROW},
                {Opcodes.ILOAD, Searcher.CONSTPUSH, Searcher.IF, Opcodes.NEW, Opcodes.DUP, Opcodes.INVOKESPECIAL, Opcodes.ATHROW},
                {Opcodes.ILOAD, Opcodes.ICONST_0, Opcodes.IF_ICMPEQ, Opcodes.NEW, Opcodes.DUP, Opcodes.INVOKESPECIAL, Opcodes.ATHROW},
                {Opcodes.ILOAD, Opcodes.ICONST_M1, Opcodes.IF_ICMPNE, Opcodes.NEW, Opcodes.DUP, Opcodes.INVOKESPECIAL, Opcodes.ATHROW},
        };
        int Fixed = 0;
        for (ClassNode Class : CLASSES.values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                Searcher Search = new Searcher(Method);
                for (int[] Pattern : Patterns) {
                    int L = Search.find(Pattern, 0);
                    int Count = 0;
                    int Check = 0;
                    out:
                    while (L != -1) {
                        ++Check;
                        if (Check > 100)
                            break out;
                        if (Method.instructions.get(L + 5) instanceof MethodInsnNode) {
                            LabelNode jmp = ((JumpInsnNode) Method.instructions.get(L + 2)).label;
                            Method.instructions.insertBefore(Method.instructions.get(L), new JumpInsnNode(Opcodes.GOTO, jmp));
                            for (int j = 0; j < Pattern.length; ++j)
                                Method.instructions.remove(Method.instructions.get(L + 1));
                            ++Count;
                            ++Fixed;
                            L = Search.find(Pattern, Count);
                        }
                    }
                }
            }
        }

        return Fixed;
    }

    public int Deob() {
        int Total = 0;
        int Fixed = 10;
        while (Fixed != 0) {
            Fixed = Run();
            Total = Total + Fixed;
        }
        System.out.print("Removed " + Total + " Exceptions");
        return Total;
    }
}
