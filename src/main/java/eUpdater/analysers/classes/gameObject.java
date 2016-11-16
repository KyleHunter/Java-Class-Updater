package eUpdater.analysers.classes;

import eUpdater.analysers.methods.methods;
import eUpdater.frame.classFrame;
import eUpdater.misc.classes;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldNode;
import java.util.List;

/**
 * Created by Kyle on 7/23/2015.
 */
public class gameObject extends classAnalyserFrame {

    public void identify(classFrame c) {
        classes.myGameObject.setId("GameObject");
        this.setMethodAnalyser(methods.myGameObject);

        classFrame removed = c;
        removed.removeFields("I");
        int count = 0, count2= 0;
        List<FieldNode> fields = removed.fields;
        for (FieldNode fN : fields) {
            if (fN.desc.equals("I")) {if ((fN.access & Opcodes.ACC_PUBLIC) == 0
                    && (fN.access & Opcodes.ACC_STATIC) == 0)
                ++count;
                if ((fN.access & Opcodes.ACC_PUBLIC) != 0 && (fN.access & Opcodes.ACC_STATIC) == 0)
                    ++count2;
            }
        }
        if ((count == 12) && count2 == 1)
            classes.myGameObject.set(c);
    }
}
