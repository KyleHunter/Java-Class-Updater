package eUpdater.analysers.classes;

import eUpdater.frame.classFrame;
import eUpdater.misc.classes;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

/**
 * Created by Kyle on 7/22/2015.
 */
public class model extends classAnalyserFrame {

    public void identify(classFrame c) {
        classes.myModel.setId("Model");
        if (c.superName.equals(classes.myRenderable.getName()) && c.access == 33 && c.fields.size() > 50) {
            List<MethodNode> methodList = c.methods;
            for (MethodNode m : methodList) {
                if (m.desc.equals("([[IIIIZI)" + "L" + c.name + ";"))
                    classes.myModel.set(c);
            }
        }
    }
}
