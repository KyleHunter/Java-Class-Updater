package eUpdater.refactor;

import eUpdater.analysers.classes.classAnalyserFrame;
import eUpdater.analysers.mainAnalyser;
import eUpdater.frame.classFrame;
import eUpdater.frame.hook;
import eUpdater.main.eUpdater;
import eUpdater.misc.JarHandler;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

import static eUpdater.misc.JarHandler.CLASSES;

/**
 * Created by Kyle on 12/5/2015.
 */
public class refactor {

    public static void run() {

        for (classFrame c : CLASSES.values()) {
            List<FieldNode> fieldNodes = c.fields;
            for (FieldNode f : fieldNodes) {
                for (classAnalyserFrame cf : mainAnalyser.access.getClassAnalysers()) {
                    if (f.desc.contains("L" + cf.getName())) {
                        String original = f.desc;
                        f.desc = original.replace(cf.getName(), cf.getId());
                    }
                }
            }
        }

        for (classAnalyserFrame cf : mainAnalyser.access.getClassAnalysers()) {
            for (classFrame c : CLASSES.values()) {
                if (c.name.equals(cf.getName())) {
                    c.name = cf.getId();
                    if (cf.hasMethodAnalyser) {
                        for (hook h : cf.getMethodAnalyser().getHooks()) {
                            List<FieldNode> fieldNodes = c.fields;
                            for (FieldNode f : fieldNodes) {
                                if (f.name.equals(h.getName())) {
                                    f.name = h.getId();
                                }
                            }
                        }
                    }
                }
            }
        }

        for (classAnalyserFrame cf : mainAnalyser.access.getClassAnalysers()) {
            if (cf.hasMethodAnalyser) {
                for (hook h : cf.getMethodAnalyser().getHooks()) {
                    for (classFrame c : CLASSES.values()) {
                        List<FieldNode> fieldNodes = c.fields;
                        for (FieldNode f : fieldNodes) {
                            if (h.getName().equals(f.name) && h.getOwner() != null && h.getOwner().equals(c.name))
                                f.name = h.getId();
                        }
                    }
                }
            }
        }

        for (classFrame c : CLASSES.values()) {
            for (MethodNode m : (List<MethodNode>) c.methods) {
                AbstractInsnNode[] instructions = m.instructions.toArray();
                for (AbstractInsnNode instruction : instructions) {
                    if (instruction instanceof FieldInsnNode) {
                        for (classAnalyserFrame cf : mainAnalyser.access.getClassAnalysers()) {
                            if (((FieldInsnNode) instruction).owner.equals(cf.getName())) {
                                ((FieldInsnNode) instruction).owner = (cf.getId());
                                if (cf.hasMethodAnalyser) {
                                    for (hook h : cf.getMethodAnalyser().getHooks()) {
                                        if (((FieldInsnNode) instruction).name.equals(h.getName()))
                                            ((FieldInsnNode) instruction).name = (h.getId());
                                    }
                                }
                            }
                        }
                    }
                }

            }

        }

        JarHandler.save("H:/Documents/Updaters/Current/IDE Version/eUpdater/res/Gamepacks/" + eUpdater.Revision + "/Refactor.jar");
    }
}


