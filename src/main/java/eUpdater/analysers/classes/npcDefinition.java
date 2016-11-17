package eUpdater.analysers.classes;

import eUpdater.analysers.methods.methods;
import eUpdater.frame.classFrame;
import eUpdater.misc.classes;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

/**
 * Created by Kyle on 7/22/2015.
 */
public class npcDefinition extends classAnalyserFrame {

    public void identify(classFrame c) {
        classes.myNpcDefinition.setId("NpcDefinition");
        setMethodAnalyser(methods.myNpcDefinition);

        if (c.superName.equals(classes.myCacheable.getName()) && c.access == 33) {
            List<MethodNode> methodList = c.methods;
            for (MethodNode m : methodList) {
                if (m.desc.contains(classes.myAnimationSequence.getName()
                        + ";IL" + classes.myAnimationSequence.getName()))
                    classes.myNpcDefinition.set(c);

            }
        }
    }
}
