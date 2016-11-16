package eUpdater.deob;
/**
 * Created by Kyle on 1/12/2015.
 */

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static eUpdater.misc.JarHandler.CLASSES;

public final class Method extends DeobFrame {

    static ArrayList<MethodInfo> totalMethods = new ArrayList();
    static ArrayList<MethodInfo> goodMethods = new ArrayList();

    private static void getInterfaces(ClassNode Class) {
        if (Class.interfaces.size() > 0) {
            List<String> Interfaces = Class.interfaces;
            for (String Interface : Interfaces) {
                ClassNode interfaceClass;
                interfaceClass = new ClassNode();
                if (!Interface.contains("java"))
                    interfaceClass = CLASSES.get(Interface);
                List<MethodNode> Methods = interfaceClass.methods;
                for (MethodNode Method : Methods)
                    Add(new MethodInfo(Class.name, Method.name, Method.desc), goodMethods);
            }
        }
    }

    private static boolean isOverridden(ClassNode Class, MethodNode Method) {
        MethodInfo methodInfo = new MethodInfo(Class.name, Method.name, Method.desc);
        String superClassName = Class.superName;
        while (!superClassName.equals("java/lang/Object")) {
            ClassNode superClass;
            if (superClassName.contains("java")) {
                superClass = new ClassNode();
                try {
                    ClassReader cr = new ClassReader(superClassName);
                    cr.accept(superClass, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
                superClass = CLASSES.get(superClassName);
            if (hasMethod(superClass, methodInfo.name, methodInfo.desc))
                return true;
            superClassName = superClass.superName;
        }
        return false;
    }

    private static void getInvoked(ClassNode Class) {
        List<MethodNode> Methods = Class.methods;
        for (MethodNode Method : Methods) {
            AbstractInsnNode[] Instructions = Method.instructions.toArray();
            for (AbstractInsnNode Instruction : Instructions) {
                if (Instruction instanceof MethodInsnNode) {
                    MethodInsnNode methodInstruction = (MethodInsnNode) Instruction;
                    MethodInfo instructionInfo = new MethodInfo(methodInstruction.owner, methodInstruction.name, methodInstruction.desc);
                    if (!instructionInfo.owner.contains("java")) {
                        if (hasMethod(CLASSES.get(instructionInfo.owner), instructionInfo.name, instructionInfo.desc))
                            Add(instructionInfo, goodMethods);
                        else {
                            String supperClassName = CLASSES.get(instructionInfo.owner).superName;
                            while (!supperClassName.contains("java")) {
                                ClassNode superClass = CLASSES.get(supperClassName);
                                if (hasMethod(superClass, instructionInfo.name, instructionInfo.desc)) {
                                    MethodInfo superMethod = new MethodInfo(superClass.name, instructionInfo.name, instructionInfo.desc);
                                    Add(superMethod, goodMethods);
                                    break;
                                }
                                supperClassName = superClass.superName;
                            }
                        }
                    }
                }
            }
        }
    }

    public static ArrayList<MethodInfo> findRedundantMethods() {
        ArrayList<MethodInfo> methodsToRemove = new ArrayList();
        for (MethodInfo mi : totalMethods)
            if (!goodMethods.contains(mi))
                methodsToRemove.add(mi);
        return methodsToRemove;
    }

    private static void Add(MethodInfo info, ArrayList<MethodInfo> usedMethods) {
        if (!usedMethods.contains(info)) {
            usedMethods.add(info);
        }
    }

    private static boolean hasMethod(ClassNode Class, String methodName, String methodDesc) {
        List<MethodNode> Methods = Class.methods;
        for (MethodNode Method : Methods)
            if (Method.name.equals(methodName) && Method.desc.equals(methodDesc))
                return true;
        return false;
    }

    private static int removeDummyMethods() {
        int tempResult = 0;
        ArrayList<MethodInfo> removeMethods;
        removeMethods = Method.findRedundantMethods();
        for (ClassNode classNode : CLASSES.values()) {
            for (int I = 0; I < removeMethods.size(); ++I) {
                if (classNode.name.equals(removeMethods.get(I).owner)) {
                    for (int C = 0; C < classNode.methods.size(); ++C) {
                        MethodNode Method = (MethodNode) classNode.methods.get(C);
                        if (Method.name.equals(removeMethods.get(I).name)) {
                            if (Method.desc.equals(removeMethods.get(I).desc)) {
                                classNode.methods.remove(Method);
                                ++tempResult;
                            }
                        }
                    }
                }
            }
        }
        return tempResult;
    }

    public int Run() {
        for (ClassNode Class : CLASSES.values()) {
            getInterfaces(Class);
            getInvoked(Class);
            List<MethodNode> Methods = Class.methods;
            for (MethodNode Method : Methods) {
                totalMethods.add(new MethodInfo(Class.name, Method.name, Method.desc));
                if (Method.name.length() > 2 || Modifier.isAbstract(Method.access) || isOverridden(Class, Method)) {
                    MethodInfo mInfo = new MethodInfo(Class.name, Method.name, Method.desc);
                    Add(mInfo, goodMethods);
                }
            }
        }
        return removeDummyMethods();
    }

    public int Deob() {
        int Total = 0;
        int Fixed = 10;
        while (Fixed != 0) {
            Fixed = Run();
            Total = Total + Fixed;
        }
        System.out.print("Removed " + Total + " Dummy Methods");
        return Total;
    }

}
