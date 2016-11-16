package eUpdater.analysers.classes;

import eUpdater.analysers.methods.methods;
import eUpdater.frame.classFrame;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldNode;
import java.util.List;

/**
 * Created by Kyle on 7/23/2015.
 */
public class sceneTile extends classAnalyserFrame {

    public void identify(classFrame c) {
        this.setId("SceneTile");
        this.setMethodAnalyser(methods.mySceneTile);
        List<FieldNode> fields = c.fields;
        int count = 0, count2 = 0;
        for (FieldNode fN : fields) {
            if (fN.desc.equals("I") && (fN.access & Opcodes.ACC_PUBLIC) == 0 && (fN.access & Opcodes.ACC_STATIC) == 0)
                ++count;
            if (fN.desc.equals("[I") && (fN.access & Opcodes.ACC_PUBLIC) == 0 && (fN.access & Opcodes.ACC_STATIC) == 0)
                ++count2;
        }
        if ((count == 11) && count2 == 1)
            this.set("SceneTile", c);
    }
}
