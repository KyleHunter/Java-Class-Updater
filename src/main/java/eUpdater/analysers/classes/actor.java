package eUpdater.analysers.classes;

import eUpdater.analysers.methods.methods;
import eUpdater.frame.classFrame;
import eUpdater.misc.classes;

/**
 * Created by Kyle on 7/22/2015.
 */
public class actor extends classAnalyserFrame {

    public void identify(classFrame c) {
        classes.myActor.setId("Actor");
        this.setMethodAnalyser(methods.myActor);
        if (c.superName.equals(classes.myRenderable.getName()) && c.access == 1057 && c.fields.size() > 40)
            classes.myActor.set(c);
    }
}
