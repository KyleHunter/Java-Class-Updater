package eUpdater.deob;
/**
 * Created by Kyle on 1/12/2015.
 */

import eUpdater.searchers.Searcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Modifier;
import java.util.*;

import static eUpdater.misc.JarHandler.CLASSES;

public class MethodName extends DeobFrame{
    public static void removeLastParam(MethodNode Method) {
        String Desc = Method.desc;
        StringBuilder descBuilder = new StringBuilder(Desc);
        int Index = Desc.indexOf(")");
        String c = Character.toString(descBuilder.charAt(Index - 1));
        if (!c.equals(";")) {
            descBuilder.deleteCharAt(Index - 1);
            Method.desc = descBuilder.toString();
        }
    }

    public static void removeLastParam(MethodInsnNode Method) {
        String Desc = Method.desc;
        StringBuilder descBuilder = new StringBuilder(Desc);
        int Index = Desc.indexOf(")");
        descBuilder.deleteCharAt(Index - 1);
        Method.desc = descBuilder.toString();
    }

    public static ArrayList<MethodInfo> dummyParamMethods = new ArrayList();
    public static int Run() {
        int fixedParams = 0;
        for (ClassNode classNode : CLASSES.values()) {
            List<MethodNode> methodList = classNode.methods;
            for (MethodNode Method : methodList) {
                if (Method.access != Opcodes.ACC_ABSTRACT) {
                    if (Method.name.contains("<"))
                        continue;
                    int paramCount;
                    boolean hasDummy = true;
                    Type[] types = Type.getArgumentTypes(Method.desc);
                    paramCount = types.length;
                    String lastParam;
                    if (paramCount > 1) {
                        lastParam = types[paramCount - 2].toString();
                    } else
                    if (paramCount == 1)
                        lastParam = types[paramCount - 1].toString();
                    else
                        continue;
                    if (Modifier.isStatic(Method.access))
                        continue;
                    if (lastParam.equals("B") || lastParam.equals("I") || lastParam.equals("S") || lastParam.equals("Z")) {
                        Searcher Search = new Searcher(Method);
                        int L = Search.findMultiPatterns(new int[][]{{Opcodes.ALOAD}, {Opcodes.ILOAD}}, 0);
                        if (L == -1)
                            hasDummy = false;
                        for (int I = 0; I < Method.instructions.size(); ++I) {
                            if (Method.instructions.get(I) instanceof VarInsnNode) {
                                if (((VarInsnNode) (Method.instructions.get(I))).var == paramCount) {
                                    hasDummy = false;
                                }
                            }
                        }
                        if (hasDummy) {
                            dummyParamMethods.add(new MethodInfo(classNode.name, Method.name, Method.desc));
                            removeLastParam(Method);
                            ++fixedParams;
                        }
                    }
                }
            }
        }

        for (ClassNode classNode : CLASSES.values()) {
            List<MethodNode> methodList = classNode.methods;
            for (MethodNode Method : methodList) {
                if (Method.access != Opcodes.ACC_ABSTRACT) {
                    int paramCount;
                    boolean hasDummy = true;
                    if (Method.name.contains("<"))
                        continue;
                    Type[] types = Type.getArgumentTypes(Method.desc);
                    paramCount = types.length;
                    if (paramCount > 0) {
                        String lastParam = types[paramCount - 1].toString();
                        if (!Modifier.isStatic(Method.access))
                            continue;
                        if (lastParam.equals("B") || lastParam.equals("I") || lastParam.equals("S")) {
                            Searcher Search = new Searcher(Method);
                            int L = Search.findMultiPatterns(new int[][]{{Opcodes.ALOAD}, {Opcodes.ILOAD}}, 0);
                            if (L == -1)
                                hasDummy = false;
                            for (int I = 0; I < Method.instructions.size(); ++I) {
                                if (Method.instructions.get(I) instanceof VarInsnNode) {
                                    if (((VarInsnNode) (Method.instructions.get(I))).var == paramCount - 1) {
                                        hasDummy = false;
                                    }
                                }
                            }
                            if (hasDummy) {
                                dummyParamMethods.add(new MethodInfo(classNode.name, Method.name, Method.desc));
                                removeLastParam(Method);
                                ++fixedParams;
                            }
                        }
                    }
                }
            }
        }

        for (ClassNode classNode : CLASSES.values()) {
            List<MethodNode> methodList = classNode.methods;
            for (MethodNode Method : methodList) {
                for (int I = 0; I < Method.instructions.size(); ++I) {
                    if (Method.instructions.get(I) instanceof MethodInsnNode) {
                        MethodInfo Temp = new MethodInfo(classNode.name, ((MethodInsnNode) Method.instructions.get(I)).name, ((MethodInsnNode) Method.instructions.get(I)).desc);
                        if (dummyParamMethods.contains(Temp)) {
                            removeLastParam(((MethodInsnNode) Method.instructions.get(I)));
                            Method.instructions.remove(Method.instructions.get(I - 1));
                        }
                    }
                }
            }
        }
        return fixedParams;
    }

    public int Deob() {
        int fSafe = 0;
        int Total = 0;
        int Fixed = 10;
        while (Fixed != 0 && fSafe < 5) {
            ++ fSafe;
            Fixed = Run();
            Total = Total + Fixed;
        }
        System.out.print("Removed " + Total + " Dummy Parameters");
        return Total;
    }

}