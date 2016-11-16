package eUpdater.analysers.classes;

import eUpdater.analysers.methods.methods;
import eUpdater.frame.classFrame;
import eUpdater.misc.classes;
import org.objectweb.asm.Opcodes;

/**
 * Created by Kyle on 7/23/2015.
 */
public class floorDecoration extends classAnalyserFrame {

    public void identify(classFrame c) {
        classes.myFloorDecoration.setId("FloorDecoration");
        this.setMethodAnalyser(methods.myFloorDecoration);
        if ((c.getFields("L" + classes.myRenderable.getName() + ";").size() == 1)
                && (c.getFields(Opcodes.ACC_PUBLIC, Opcodes.ACC_STATIC)).size() == 4)
            classes.myFloorDecoration.set(c);
    }
}
