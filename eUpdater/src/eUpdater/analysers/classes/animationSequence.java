package eUpdater.analysers.classes;


import eUpdater.frame.classFrame;
import eUpdater.misc.classes;
import org.objectweb.asm.tree.MethodNode;
import java.util.List;

/**
 * Created by Kyle on 7/22/2015.
 */
public class animationSequence extends classAnalyserFrame{

    public void identify(classFrame c) {
        classes.myAnimationSequence.setId("AnimationSequence");
        if (c.superName.equals(classes.myCacheable.getName())) {
            List<MethodNode> methodList = c.methods;
            for (MethodNode m : methodList) {
                if (m.desc.equals("(L" + classes.myModel.getName() + ";IL" + c.name + ";I)L"
                        + classes.myModel.getName() + ";"))
                    classes.myAnimationSequence.set(c);
            }
        }
    }
}
