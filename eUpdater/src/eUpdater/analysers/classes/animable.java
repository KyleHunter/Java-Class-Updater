package eUpdater.analysers.classes;

import eUpdater.frame.classFrame;
import eUpdater.misc.classes;
import org.objectweb.asm.tree.MethodNode;

/**
 * Created by Kyle on 7/21/2015.
 */
public class animable extends classAnalyserFrame {

    public void identify(classFrame c) {
        classes.myAnimable.setId("Animable");
        if (c.superName.equals(classes.myRenderable.getName())) {
            for (int I = 0; I < c.methods.size(); ++I) {
                MethodNode Method = (MethodNode) c.methods.get(I);
                if (Method.desc.equals("(IIIIIIIZL" + classes.myRenderable.getName() + ";)V"))
                    classes.myAnimable.set(c);
            }
        }
    }
}


