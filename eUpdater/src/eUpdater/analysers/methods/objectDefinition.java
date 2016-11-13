package eUpdater.analysers.methods;

import eUpdater.frame.hook;
import eUpdater.misc.classes;

import java.util.Arrays;


/**
 * Created by Kyle on 11/9/2015.
 */
public class objectDefinition extends methodAnalyserFrame {

    public void identify() {
        this.setParent("ObjectDefinition", classes.myObjectDefinition);
        this.setNeededHooks(Arrays.asList("Actions", "Name"));

        addHook(new hook("Actions", parentClass.getFields("[Ljava/lang/String;").get(0)));
        addHook(new hook("Name", parentClass.getFields("Ljava/lang/String;").get(0)));
    }
}
