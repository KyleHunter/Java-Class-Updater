package eUpdater.deob;
/**
 * Created by Kyle on 1/12/2015.
 */

import eUpdater.searchers.Searcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static eUpdater.misc.JarHandler.CLASSES;

public class Multipliers extends DeobFrame{

    public int Run() {
        int[][] Patterns = new int[][]{
                {Opcodes.LDC, Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.IMUL},
                {Opcodes.LDC, Opcodes.GETSTATIC, Opcodes.IMUL},
                {Opcodes.LDC, Opcodes.GETFIELD, Opcodes.IMUL},
                {Opcodes.LDC, Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.LMUL},
                {Opcodes.LDC, Opcodes.GETSTATIC, Opcodes.LMUL},
                {Opcodes.LDC, Opcodes.GETFIELD, Opcodes.LMUL}};
        int Fixed = 0;
        for (ClassNode Class : CLASSES.values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                Searcher Search = new Searcher(Method);
                ArrayList<AbstractInsnNode> Instructions = new ArrayList(Arrays.asList(Method.instructions.toArray()));

                for (int[] Pattern : Patterns) {
                    int L = Search.find(Pattern, 0);
                    int Count = 0;
                    while (L != -1) {
                        int afterField;
                        if (Method.instructions.get(L + 1) instanceof FieldInsnNode) {
                            afterField = 2;
                        } else {
                            afterField = 3;
                        }
                        Instructions.add(L + afterField, Instructions.get(L));
                        Instructions.remove(L);
                        ++ Count;
                        ++ Fixed;
                        L = Search.find(Pattern, Count);
                    }
                }

                Method.instructions.clear();
                for (AbstractInsnNode n : Instructions) {
                    Method.instructions.add(n);
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
        System.out.print("Reordered " + Total + " Multiplier Instructions");
        return Total;
    }

}
