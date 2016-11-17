package eUpdater.misc;

import eUpdater.frame.classFrame;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;

public class Misc {

    public static int mode(int a[]) {
        int maxValue = 0, maxCount = 0;

        for (int i = 0; i < a.length; ++i) {
            int count = 0;
            for (int j = 0; j < a.length; ++j) {
                if (a[j] == a[i]) ++count;
            }
            if (count > maxCount) {
                maxCount = count;
                maxValue = a[i];
            }
        }
        return maxValue;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static FieldNode getField(String id, classFrame c) {
        List<FieldNode> fields = c.fields;
        for (FieldNode f : fields) {
            if (f.name.equals(id))
                return f;
        }
        return null;
    }

    public static String get(int opcode) {
        return org.objectweb.asm.util.Printer.OPCODES[opcode];
    }

    public static void printMethod(MethodNode method) {
        System.out.println("METHOD: " + method.name + method.desc + "\n");
        AbstractInsnNode[] instructions = method.instructions.toArray();
        for (int I = 0; I < instructions.length; ++I) {
            AbstractInsnNode instruction = instructions[I];
            if (instruction == null)
                System.out.println("Null Opcode");
            else {
                if (instruction.getOpcode() != -1) {
                    String value = null;
                    switch (instruction.getOpcode()) {
                        case Opcodes.ILOAD:
                        case Opcodes.ALOAD:
                            if (instruction instanceof VarInsnNode) {
                                value = '_' + String.valueOf(((VarInsnNode) instruction).var);
                            }
                            break;
                        case Opcodes.BIPUSH:
                        case Opcodes.SIPUSH:
                            value = "  " + String.valueOf(((IntInsnNode) instruction).operand);
                            break;
                        case Opcodes.LDC:
                            if (((LdcInsnNode) instruction).cst instanceof String) {
                                value = "  \"" + String.valueOf(((LdcInsnNode) instruction).cst) + "\"";
                            } else {
                                value = "  " + String.valueOf(((LdcInsnNode) instruction).cst);
                            }
                            break;
                        case Opcodes.GOTO:
                            value = "  " + method.instructions.indexOf(((JumpInsnNode) instruction).label);
                            break;
                    }
                    if (value != null) {
                        System.out.println(" -> " + get(instruction.getOpcode()) + value);
                    } else {
                        if (instruction instanceof MethodInsnNode) {
                            MethodInsnNode m = (MethodInsnNode) instruction;
                            System.out.println(" -> " + get(instruction.getOpcode()) + "   " + m.owner + "/" + m.name + m.desc);
                        } else if (instruction instanceof FieldInsnNode) {
                            FieldInsnNode f = (FieldInsnNode) instruction;
                            System.out.println(" -> " + get(instruction.getOpcode()) + "   " + f.owner + "/" + f.name + " " + f.desc);
                        } else {
                            System.out.println(" -> " + get(instruction.getOpcode()));
                        }
                    }
                } else {
                    System.out.println("-1");
                }
            }
        }
    }

}
