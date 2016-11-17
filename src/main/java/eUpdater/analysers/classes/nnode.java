package eUpdater.analysers.classes;

import eUpdater.analysers.methods.methods;
import eUpdater.frame.classFrame;
import eUpdater.misc.classes;
import eUpdater.searchers.FieldSearcher;

/**
 * Created by Kyle on 7/21/2015.
 */
public class nnode extends classAnalyserFrame {

    public void identify(classFrame c) {
        int nodeCount;
        int idCount;
        setMethodAnalyser(methods.myNode);
        if (c.superName.equals("java/lang/Object")) {
            classes.myNode.setId("Node");
            FieldSearcher fieldSearch = new FieldSearcher(c);
            idCount = fieldSearch.countDesc("J");
            nodeCount = fieldSearch.countDesc(String.format("L%s;", c.name));
            if ((idCount >= 1) & (nodeCount == 2)) {
                classes.myNode.set(c);
            }
        }
    }
}
