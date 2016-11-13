package eUpdater.analysers.methods;

import eUpdater.frame.hook;
import eUpdater.misc.classes;

import java.util.Arrays;

/**
 * Created by Kyle on 11/16/2015.
 */
public class region extends methodAnalyserFrame {

    public void identify() {
        this.setParent("Region", classes.myRegion);
        this.setNeededHooks(Arrays.asList("SceneTiles"));

        addHook(new hook("SceneTiles", this.parentClass.getFields("[[[L" + classes.mySceneTile.getName() + ";").get(0)));

    }
}
