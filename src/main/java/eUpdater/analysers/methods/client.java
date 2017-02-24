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
 * Created by Kyle on 11/10/2015.
 */
public class client extends methodAnalyserFrame {

    public void identify() {
        this.setParent("Client", classes.myClient);
        this.setNeededHooks(Arrays.asList("LoopCycle", "IsMenuOpen", "MenuX", "MenuY", "MenuHeight", "MenuCount", "MenuWidth",
                "MenuActions", "MenuOptions", "LocalPlayers", "Region", "Plane", "DestinationY", "DestinationX", "LocalPlayer", "BaseX",
                "BaseY", "Widgets", "GameSettings", "CurrentLevels", "RealLevels", "Experiences", "Weight", "Energy", "CurrentWorld",
                "WidgetNodeCache", "TileSettings", "TileHeights", "LocalNpcs", "NpcIndices", "CrossHairColor", "MapAngle", "MapOffset", "MapScale",
                "Sine", "Cosine", "CameraScale", "CameraPitch", "CameraYaw", "CameraZ", "CameraY", "CameraX", "ViewportWidth", "ViewportHeight",
                "GroundItems", "LoginState", "PlayerIndex", "WidgetPositionX", "WidgetPositionY", "WidgetWidths", "WidgetHeights"));

        for (ClassNode c : CLASSES.values()) {
            if (c.name.equals(classes.myAnimable.getName())) {
                List<MethodNode> methodList = c.methods;
                for (MethodNode m : methodList) {
                    if (m.name.equals("<init>")) {
                        AbstractInsnNode[] Instructions = m.instructions.toArray();
                        Searcher search = new Searcher(m);
                        int L = search.find(new int[]{Opcodes.IMUL, Opcodes.LDC, Opcodes.ISUB, Opcodes.PUTFIELD}, 0);
                        L = search.find(new int[]{Opcodes.GETSTATIC}, 0, L - 3);
                        addHook(new hook("LoopCycle", Instructions, L));
                    }
                }
            }
        }

        for (ClassNode c : CLASSES.values()) {
            List<MethodNode> methodList = c.methods;
            for (MethodNode m : methodList) {
                if (m.desc.contains("([L" + classes.myWidget.getName() + ";IIIIII")) {
                    AbstractInsnNode[] Instructions = m.instructions.toArray();
                    Searcher searcher = new Searcher(m);

                    int L = searcher.findSingleFieldDesc(Opcodes.GETSTATIC, "[[L" + classes.myWidget.getName() + ";");
                    addHook(new hook("Widgets", Instructions, L));

                    L = searcher.find(new int[]{Opcodes.GETSTATIC, Opcodes.ICONST_0, Opcodes.LDC, Opcodes.AASTORE}, 0);
                    if (L != -1)
                        addHook(new hook("MenuOptions", Instructions, L));
                    L = searcher.find(new int[]{Opcodes.GETSTATIC, Opcodes.ICONST_0, Opcodes.GETSTATIC, Opcodes.AASTORE}, 0);
                    if (L != -1)
                        addHook(new hook("MenuActions", Instructions, L));
                }
            }
        }

        int L;
        AbstractInsnNode[] Instructions = null;
        MethodNode method = null;
        for (ClassNode c : CLASSES.values()) {
            List<MethodNode> methodList = c.methods;
            for (MethodNode m : methodList) {
                if (!m.desc.contains("(II") || !m.desc.endsWith(")V"))
                    continue;
                Searcher search = new Searcher(m);
                for (int I = 0; I < 100; ++I) {
                    L = search.find(new int[]{Opcodes.IINC}, I);
                    if (L != -1) {
                        Instructions = m.instructions.toArray();
                        if (((IincInsnNode) Instructions[L]).incr == 8 && ((IincInsnNode) Instructions[L]).var == 3) {
                            method = m;
                            break;
                        }
                    } else
                        break;
                }
            }
        }
        Searcher search = new Searcher(method);
        Instructions = method.instructions.toArray();
        L = search.find(new int[]{Opcodes.ICONST_1, Opcodes.PUTSTATIC}, 0);
        if (L != -1)
            addHook(new hook("IsMenuOpen", Instructions, L + 1));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTSTATIC, L, 50, 1);
        addHook(new hook("MenuX", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTSTATIC, L, 50, 1);
        addHook(new hook("MenuY", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTSTATIC, L, 50, 1);
        addHook(new hook("MenuWidth", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTSTATIC, L, 50, 1);
        addHook(new hook("MenuHeight", Instructions, L));

        for (ClassNode Class : CLASSES.values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                if (Method.desc.contains("(Ljava/lang/String;Ljava/lang/String;IIII")) {
                    search = new Searcher(Method);
                    L = search.find(new int[]{Opcodes.GETSTATIC, Opcodes.LDC, Opcodes.IMUL, Opcodes.SIPUSH, Searcher.IF}, 0);
                    if (L != -1) {
                        Instructions = Method.instructions.toArray();
                        addHook(new hook("MenuCount", Instructions, L));
                        break;
                    }
                }
            }
        }

        for (classFrame c : CLASSES.values()) {
            List<MethodNode> methods = c.getMethods(false, ";)V");
            for (MethodNode m : methods) {
                search = new Searcher(m);
                if (search.findSingleIntValue(Opcodes.SIPUSH, 200) != -1 &&
                        search.findSingleIntValue(Opcodes.BIPUSH, 50) != -1)
                    method = m;//Various, m.ae Rev 98
            }
        }
        Instructions = method.instructions.toArray();
        search = new Searcher(method);

        boolean found = false;
        for (int I = 0; I < 3; ++I) {
            L = search.find(new int[]{Opcodes.GETSTATIC, Opcodes.GETSTATIC}, I);
            if (((FieldInsnNode) Instructions[L]).desc.contains("["))
                addHook(new hook("LocalPlayers", Instructions, L));
            else {
                if (!found) {
                    addHook(new hook("Region", Instructions, L));
                    addHook(new hook("Plane", Instructions, L + 1));
                    found = true;
                }
            }
        }

        L = search.find(new int[]{Opcodes.ICONST_0, Opcodes.PUTSTATIC}, 0);
        int S = L;
        for (int I = 0; I < 2; ++I) {
            L = search.find(new int[]{Opcodes.GETSTATIC, Opcodes.LDC, Opcodes.IMUL, Opcodes.GETSTATIC,
                    Opcodes.GETFIELD, Opcodes.LDC, Opcodes.IMUL, Opcodes.BIPUSH, Opcodes.ISHR}, I);
            if (((FieldInsnNode) Instructions[L]).name.equals(((FieldInsnNode) Instructions[S + 1]).name))
                continue;
            else
                addHook(new hook("DestinationY", Instructions, L));
            addHook(new hook("DestinationX", Instructions, S + 1));

        }

        L = search.findSingleFieldDesc(Opcodes.GETSTATIC, "L" + classes.myPlayer.getName() + ";", 0);
        addHook(new hook("LocalPlayer", Instructions, L));

        for (classFrame c : CLASSES.values()) {
            List<MethodNode> methods = c.getMethods(false, "(");
            for (MethodNode m : methods) {
                search = new Searcher(m);
                if (search.findSingleIntValue(Opcodes.SIPUSH, 3308) != -1 &&
                        search.findSingleIntValue(Opcodes.SIPUSH, 3305) != -1)
                    method = m;//ei.q Rev 98
            }
        }
        search = new Searcher(method);
        Instructions = method.instructions.toArray();
        L = search.findSingleIntValue(Opcodes.SIPUSH, 3308);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETSTATIC, L, 15, 1);
        addHook(new hook("BaseX", Instructions, L));

        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETSTATIC, L, 15, 2);
        addHook(new hook("BaseY", Instructions, L));

        L = search.find(new int[]{Opcodes.ILOAD, Opcodes.ICONST_2, Opcodes.IF_ICMPNE}, 0);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETSTATIC, L, 15, 0);
        addHook(new hook("GameSettings", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 3305);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETSTATIC, L, 15, 2);
        addHook(new hook("CurrentLevels", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 3306);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETSTATIC, L, 15, 2);
        addHook(new hook("RealLevels", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 3307);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETSTATIC, L, 15, 2);
        addHook(new hook("Experiences", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 3322);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETSTATIC, L, 15, 1);
        addHook(new hook("Weight", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 3321);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETSTATIC, L, 15, 1);
        addHook(new hook("Energy", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 3318);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETSTATIC, L, 15, 1);
        addHook(new hook("CurrentWorld", Instructions, L));

        L = search.findSingleIntValue(Opcodes.SIPUSH, 2702);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETSTATIC, L, 15, 1);
        addHook(new hook("WidgetNodeCache", Instructions, L));

        out:
        for (classFrame c : CLASSES.values()) {
            List<MethodNode> methods = c.getMethods(false, ")V");
            for (MethodNode m : methods) {
                search = new Searcher(m);
                Instructions = m.instructions.toArray();
                L = search.find(new int[]{Opcodes.GETSTATIC, Opcodes.ICONST_1,
                        Opcodes.AALOAD}, 0);
                if (L != -1 && ((FieldInsnNode) Instructions[L]).desc.equals("[[[B")) {
                    addHook(new hook("TileSettings", Instructions, L)); //TODO FIX TEH BROKE
                    method = m;
                    break out;
                }
            }
        }

        out:
        for (ClassNode Class : CLASSES.values()) {
            List<MethodNode> methodList = Class.methods;
            for (MethodNode Method : methodList) {
                Instructions = Method.instructions.toArray();
                search = new Searcher(Method);
                L = search.find(new int[]{Opcodes.GETSTATIC, Opcodes.ILOAD, Opcodes.AALOAD, Opcodes.ASTORE}, 0);
                if (L != -1) {
                    if (((FieldInsnNode) Instructions[L]).desc.equals("[[[I")) {
                        addHook(new hook("TileHeights", Instructions, L));
                        break out;
                    }
                }
            }
        }

        for (classFrame c : CLASSES.values()) {
            List<MethodNode> methods = c.getMethods(true, "(Z)V");
            for (MethodNode m : methods) {
                search = new Searcher(m);
                if (search.findSingleIntValue(Opcodes.BIPUSH, 104) != -1 &&
                        search.findSingleIntValue(Opcodes.BIPUSH, 127) != -1 && m.instructions.size() < 500)
                    method = m;// k.ai Rev 98
            }
        }

        search = new Searcher(method);
        Instructions = method.instructions.toArray();
        L = search.findSingleFieldDesc(Opcodes.GETSTATIC, "[L" + classes.myNpc.getName() + ";");
        addHook(new hook("LocalNpcs", Instructions, L));
        L = search.findSingleFieldDesc(Opcodes.GETSTATIC, "[I", L);
        addHook(new hook("NpcIndices", Instructions, L));

        for (classFrame c : CLASSES.values()) {
            List<MethodNode> methods = c.getMethods(true, "(IIIILjava/lang/String;Ljava/lang/String;II)V");
            for (MethodNode m : methods) {
                search = new Searcher(m);
                Instructions = m.instructions.toArray();
                for (int I = 0; L != -1; ++I) {
                    L = search.find(new int[]{Opcodes.ILOAD, Opcodes.BIPUSH, Opcodes.IF_ICMPNE}, I);
                    if (L != -1 && ((IntInsnNode) Instructions[L + 1]).operand == 20) {
                        S = search.findSingleJump(Opcodes.GOTO, Opcodes.PUTSTATIC, L, 50, 2);
                        addHook(new hook("CrossHairColor", Instructions, S));
                        L = -1;
                    }
                }
            }
        }


        for (classFrame c : CLASSES.values()) {
            List<MethodNode> methods = c.getMethods(false, "([Lfd;IIIIIIII)V");
            for (MethodNode m : methods) {
                method = m;
            }
        }
        search = new Searcher(method);
        Instructions = method.instructions.toArray();

        for (classFrame c : CLASSES.values()) {
            List<MethodNode> methods = c.getMethods(false, "(IIIIL");
            for (MethodNode m : methods) {
                search = new Searcher(m);
                if (search.findSingleIntValue(Opcodes.SIPUSH, 2047) != -1 &&
                        search.findSingleIntValue(Opcodes.SIPUSH, 2500) != -1) {
                    method = m;// a.dl Rev 99, TileToMM
                }
            }
        }
        search = new Searcher(method);
        Instructions = method.instructions.toArray();
        L = search.findSingleJump(Opcodes.IFNONNULL, Opcodes.GETSTATIC, 0, 25, 0);
        addHook(new hook("MapOffset", Instructions, L));
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETSTATIC, L, 15, 1);
        addHook(new hook("MapAngle", Instructions, L));
        L = search.findSingleIntValue(Opcodes.SIPUSH, 256);
        L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETSTATIC, L, 15, 0);
        addHook(new hook("MapScale", Instructions, L));


        for (classFrame c : CLASSES.values()) {
            List<MethodNode> methods = c.getMethods(false, "(III");
            for (MethodNode m : methods) {
                search = new Searcher(m);
                if (search.findSingleIntValue(Opcodes.SIPUSH, 13056) != -1 &&
                        search.findSingleIntValue(Opcodes.SIPUSH, 128) != -1) {
                    method = m;// client.fg Rev 99, TileToMS
                }
            }
        }
        search = new Searcher(method);
        Instructions = method.instructions.toArray();
        for (int I = 0; I < 10; ++I) {
            L = search.find(new int[]{Opcodes.GETSTATIC, Opcodes.GETSTATIC, Opcodes.LDC, Opcodes.IMUL,
                    Opcodes.IALOAD, Opcodes.ISTORE}, I);
            if (L != -1 && ((VarInsnNode) Instructions[L + 5]).var == 5) {
                addHook(new hook("CameraPitch", Instructions, L + 1));
                addHook(new hook("Sine", Instructions, L));
            }
            if (L != -1 && ((VarInsnNode) Instructions[L + 5]).var == 7)
                addHook(new hook("CameraYaw", Instructions, L + 1));
            if (L != -1 && ((VarInsnNode) Instructions[L + 5]).var == 6)
                addHook(new hook("Cosine", Instructions, L));
        }
        for (int I = 0; I < 10; ++I) {
            L = search.find(new int[]{Opcodes.GETSTATIC, Opcodes.LDC, Opcodes.IMUL, Opcodes.ISUB, Opcodes.ISTORE}, I);
            if (L != -1 && ((VarInsnNode) Instructions[L + 4]).var == 0)
                addHook(new hook("CameraX", Instructions, L));
            if (L != -1 && ((VarInsnNode) Instructions[L + 4]).var == 1)
                addHook(new hook("CameraY", Instructions, L));
            if (L != -1 && ((VarInsnNode) Instructions[L + 4]).var == 4)
                addHook(new hook("CameraZ", Instructions, L));
        }
        for (int I = 0; I < 10; ++I) {
            L = search.find(
                    new int[]{
                            Opcodes.GETSTATIC, Opcodes.LDC, Opcodes.IMUL, Opcodes.ICONST_2, Opcodes.IDIV, Opcodes.ILOAD,
                            Opcodes.GETSTATIC, Opcodes.LDC, Opcodes.IMUL, Opcodes.IMUL, Opcodes.ILOAD, Opcodes.IDIV,
                            Opcodes.IADD, Opcodes.LDC, Opcodes.IMUL, Opcodes.PUTSTATIC
                    }, I);
            if (L != -1) {
                addHook(new hook("ViewportWidth", Instructions, L));
                addHook(new hook("CameraScale", Instructions, L + 6));
                break;
            }
        }
        for (int I = 0; I < 10; ++I) {
            L = search.find(
                    new int[]{
                            Opcodes.GETSTATIC, Opcodes.LDC, Opcodes.IMUL, Opcodes.ILOAD, Opcodes.IMUL, Opcodes.ILOAD,
                            Opcodes.IDIV, Opcodes.GETSTATIC, Opcodes.LDC, Opcodes.IMUL, Opcodes.ICONST_2, Opcodes.IDIV,
                            Opcodes.IADD, Opcodes.IMUL, Opcodes.PUTSTATIC
                    }, I);
            if (L != -1) {
                hook scale = getHook("CameraScale");
                FieldInsnNode potential = (FieldInsnNode) Instructions[L];
                if (scale != null && potential.name.equals(scale.getName()) && potential.owner.equals(scale.getOwner())) {
                    addHook(new hook("ViewportHeight", Instructions, L + 7));
                    break;
                }
            }
        }

        for (classFrame c : CLASSES.values()) {
            if (c.getFields("[[[L" + classes.myLinkedList.getName() + ";").size() != 0) {
                hook temp = new hook("GroundItems", c.getFields("[[[L" + classes.myLinkedList.getName() + ";").get(0));
                temp.setOwner(c.name);
                addHook(temp);
            }
        }

        for (classFrame c : CLASSES.values()) {
            List<MethodNode> methods = c.getMethods(true, "(L" + classes.myGameShell.getName() + ";)V");
            for (MethodNode m : methods) {
                search = new Searcher(m);
                if (search.findSingleIntValue(Opcodes.BIPUSH, 75) != -1) {
                    L = 0;
                    for (int I = 0; L != -1; ++I) {
                        search = new Searcher(m); // y.f Rev 98
                        Instructions = m.instructions.toArray();
                        L = search.find(new int[]{Opcodes.GETSTATIC, Opcodes.LDC, Opcodes.IMUL, Opcodes.BIPUSH, Searcher.IF}, I);
                        if (((IntInsnNode) Instructions[L + 3]).operand == 11) {
                            addHook(new hook("LoginState", Instructions, L));
                            L = -1;
                        }
                    }
                }
            }
        }

        for (classFrame c : CLASSES.values()) {
            List<MethodNode> methods = c.getMethods(false, "(L");
            for (MethodNode m : methods) {
                search = new Searcher(m);
                if (search.findSingleIntValue(Opcodes.SIPUSH, 2048) != -1 &&
                        search.findSingleIntValue(Opcodes.BIPUSH, 28) != -1) {
                    search = new Searcher(m); // j.z Rev 98
                    Instructions = m.instructions.toArray();
                    L = search.findSingleJump(Opcodes.GOTO, Opcodes.GETSTATIC, 0, 10, 0);
                    if (L != -1) //TODO Fix
                        addHook(new hook("PlayerIndex", Instructions, L));
                }
            }
        }

        for (classFrame c : CLASSES.values()) {
            MethodNode method1 = c.getMethod(true, "([L" + classes.myWidget.getName() + ";IIIIIIII)V");
            if (method1 != null)
                method = method1;
        }
        search = new Searcher(method);
        Instructions = method.instructions.toArray();
        for (int I = 0; I < 10; ++I) {
            L = search.find(new int[]{Opcodes.ILOAD, Opcodes.ICONST_M1, Opcodes.IF_ICMPNE}, I);
            if (L != -1) {
                if (((VarInsnNode) Instructions[L]).var == 8) {
                    S = search.findSingleJump(Opcodes.GOTO, Opcodes.GETSTATIC, L, 50, 0);
                    if (S != -1)
                        addHook(new hook("WidgetPositionX", Instructions, S));
                    S = search.findSingleJump(Opcodes.GOTO, Opcodes.GETSTATIC, L, 50, 2);
                    if (S != -1)
                        addHook(new hook("WidgetPositionY", Instructions, S));
                    S = search.findSingleJump(Opcodes.GOTO, Opcodes.GETSTATIC, L, 50, 4);
                    if (S != -1)
                        addHook(new hook("WidgetWidths", Instructions, S));
                    S = search.findSingleJump(Opcodes.GOTO, Opcodes.GETSTATIC, L, 50, 6);
                    if (S != -1)
                        addHook(new hook("WidgetHeights", Instructions, S));
                }
            }
        }
    }

}