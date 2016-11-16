package eUpdater.analysers.methods;

import eUpdater.frame.hook;
import eUpdater.misc.classes;
import java.util.Arrays;

/**
 * Created by Kyle on 11/16/2015.
 */
public class sceneTile extends methodAnalyserFrame {

    public void identify() {
        this.setParent("SceneTile", classes.mySceneTile);
        this.setNeededHooks(Arrays.asList("GameObject", "BoundaryObject", "WallDecoration",
                "GroundDecoration"));
        addHook(new hook("GameObject", parentClass.getFields("[L" + classes.myGameObject.getName() + ";").get(0)));
        addHook(new hook("BoundaryObject", parentClass.getFields("L" + classes.myBoundaryObject.getName() + ";").get(0)));
        addHook(new hook("WallDecoration", parentClass.getFields("L" + classes.myWallDecoration.getName() + ";").get(0)));
        addHook(new hook("GroundDecoration", parentClass.getFields("L" + classes.myFloorDecoration.getName() + ";").get(0)));

    }
}
