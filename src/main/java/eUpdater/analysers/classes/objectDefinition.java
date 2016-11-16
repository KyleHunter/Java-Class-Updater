package eUpdater.analysers.classes;

import eUpdater.analysers.methods.methods;
import eUpdater.frame.classFrame;
import eUpdater.misc.classes;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

/**
 * Created by Kyle on 7/22/2015.
 */
public class objectDefinition extends classAnalyserFrame {

    public void identify(classFrame c) {
        classes.myObjectDefinition.setId("ObjectDefinition");
        classes.myObjectDefinition.setMethodAnalyser(methods.myObjectDefinition);
        if (c.superName.equals(classes.myCacheable.getName())) {
            List<MethodNode> methodList = c.methods;
            for (MethodNode m : methodList) {
                if (m.desc.contains("II[[IIII")) {
                    classes.myObjectDefinition.set(c);
                    break;
                }
            }
        }
    }
}
