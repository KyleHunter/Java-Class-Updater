package eUpdater.analysers.classes;

import eUpdater.analysers.methods.methods;
import eUpdater.frame.classFrame;
import eUpdater.misc.classes;

/**
 * Created by Kyle on 7/22/2015.
 */
public class client extends classAnalyserFrame {

    public void identify(classFrame c) {
        classes.myClient.setId("Client");
        this.setMethodAnalyser(methods.myClient);
        if (c.name.equals("client"))
            classes.myClient.set(c);
    }

}
