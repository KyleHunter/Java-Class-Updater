package eUpdater.analysers.classes;

import eUpdater.analysers.methods.methods;
import eUpdater.frame.classFrame;
import eUpdater.misc.classes;
import eUpdater.searchers.FieldSearcher;

/**
 * Created by Kyle on 7/22/2015.
 */
public class region extends classAnalyserFrame {

    public void identify(classFrame c) {
        classes.myRegion.setId("Region");
        this.setMethodAnalyser(methods.myRegion);
        if (c.superName.equals("java/lang/Object")) {
            FieldSearcher s = new FieldSearcher(c);
            int L = s.countDesc("[[[[Z");
            if (L == 1)
                classes.myRegion.set(c);
        }
    }
}
