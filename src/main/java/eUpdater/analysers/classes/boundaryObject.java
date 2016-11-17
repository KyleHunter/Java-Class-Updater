package eUpdater.analysers.classes;

import eUpdater.analysers.methods.methods;
import eUpdater.frame.classFrame;
import eUpdater.misc.classes;
import org.objectweb.asm.Opcodes;

/**
 * Created by Kyle on 7/23/2015.
 */
public class boundaryObject extends classAnalyserFrame {

    public void identify(classFrame c) {
        classes.myBoundaryObject.setId("BoundaryObject");
        this.setMethodAnalyser(methods.myBoundaryObject);
        if ((c.getFields("L" + classes.myRenderable.getName() + ";").size() == 2)
                && (c.getFields(Opcodes.ACC_PUBLIC, Opcodes.ACC_STATIC).size() == 6))
            classes.myBoundaryObject.set(c);
    }
}
