package eUpdater.analysers.classes;

import eUpdater.frame.classFrame;
import org.objectweb.asm.tree.MethodNode;
import eUpdater.misc.classes;
import java.util.List;

/**
 * Created by Kyle on 7/22/2015.
 */
public class buffer extends classAnalyserFrame{

    public void identify(classFrame c) {
        classes.myBuffer.setId("Buffer");
        if (c.superName.equals(classes.myNode.getName())) {
            int Count = 0;
            List<MethodNode> methodList = c.methods;
            for (MethodNode m : methodList) {
                if (m.desc.contains("(Ljava/lang/CharSequence;"))
                    ++Count;
                if (m.desc.contains("(Ljava/math/BigInteger;Ljava/math/BigInteger;"))
                    ++Count;
                if (Count == 2) {
                    classes.myBuffer.set(c);
                    break;
                }
            }
        }
    }
}
