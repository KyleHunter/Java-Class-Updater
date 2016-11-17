package eUpdater.analysers.classes;

import eUpdater.analysers.methods.methods;
import eUpdater.frame.classFrame;
import eUpdater.misc.classes;

/**
 * Created by Kyle on 7/22/2015.
 */
public class npc extends classAnalyserFrame {

    public void identify(classFrame c) {
        classes.myNpc.setId("Npc");
        classes.myNpc.setMethodAnalyser(methods.myNpc);
        if (c.superName.equals(classes.myActor.getName()) && c.access == 49
                && c.fields.size() < 10)
            classes.myNpc.set(c);
    }
}
