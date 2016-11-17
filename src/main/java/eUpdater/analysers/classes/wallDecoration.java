package eUpdater.analysers.classes;

import eUpdater.analysers.methods.methods;
import eUpdater.frame.classFrame;
import eUpdater.misc.classes;
import org.objectweb.asm.Opcodes;

/**
 * Created by Kyle on 7/23/2015.
 */
public class wallDecoration extends classAnalyserFrame {

    public void identify(classFrame c) {
        this.setId("WallDecoration");
        this.setMethodAnalyser(methods.myWallDecoration);
        if ((c.getFields("L" + classes.myRenderable.getName() + ";").size() == 2)
                && (c.getFields(Opcodes.ACC_PUBLIC, Opcodes.ACC_STATIC).size() == 8))
            this.set(c);
    }
}
