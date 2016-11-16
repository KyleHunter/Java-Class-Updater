package eUpdater.analysers.classes;

import eUpdater.frame.classFrame;
import eUpdater.misc.classes;

/**
 * Created by Kyle on 7/22/2015.
 */
public class gameShell extends classAnalyserFrame{

    public void identify(classFrame c){
        classes.myGameShell.setId("GameShell");
        if (c.superName.equals("java/applet/Applet")) {
            if (c.interfaces.size() == 3)
                classes.myGameShell.set(c);
        }
    }
}
