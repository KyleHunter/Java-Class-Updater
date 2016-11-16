package eUpdater.analysers.classes;

import eUpdater.analysers.methods.methods;
import eUpdater.frame.classFrame;
import eUpdater.misc.classes;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;

/**
 * Created by Kyle on 7/24/2015.
 */
public class item extends classAnalyserFrame {

    public void identify(classFrame c) {
        classes.myItem.setId("Item");
        this.setMethodAnalyser(methods.myItem);
        if (c.superName.equals(classes.myRenderable.getName()) && c.access == 49) {
            List<FieldNode> fields = c.fields;
            int count = 0;
            for (FieldNode fN : fields) {
                if (fN.desc.equals("I") && (fN.access & Opcodes.ACC_PUBLIC) == 0 && (fN.access & Opcodes.ACC_STATIC) == 0)
                    ++count;
            }
            if (count == 2)
                classes.myItem.set(c);
        }
    }
}
