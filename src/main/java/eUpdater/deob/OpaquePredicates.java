package eUpdater.deob;
/**
 * Created by Kyle on 1/12/2015.
 */

import eUpdater.searchers.Searcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

import static eUpdater.misc.JarHandler.CLASSES;

public class OpaquePredicates extends DeobFrame {

    public static int Run() {
        int Fixed = 0;
        int Patterns[][] = Patterns = new int[][]{
                {Opcodes.ILOAD, Searcher.CONSTPUSH, Searcher.IF, Opcodes.RETURN},
                {Opcodes.ILOAD, Opcodes.LDC, Searcher.IF, Opcodes.RETURN},

        };

        for (ClassNode Class : CLASSES.values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                Searcher Search = new Searcher(Method);
                for (int[] Pattern : Patterns) {
                    int L = Search.find(Pattern, 0);
                    int Count = 0;
                    while (L != -1) {
                        LabelNode jmp = ((JumpInsnNode) Method.instructions.get(L + 2)).label;
                        Method.instructions.insertBefore(Method.instructions.get(L), new JumpInsnNode(Opcodes.GOTO, jmp));
                        for (int j = 1; j < Pattern.length; ++j)
                            Method.instructions.remove(Method.instructions.get(L + 1));
                        ++Count;
                        ++Fixed;
                        L = Search.find(Pattern, Count);
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
        System.out.print("Removed " + Total + " Opaque Predicates");
        return Total;
    }
}
