package eUpdater.analysers.classes;

import eUpdater.analysers.methods.methodAnalyserFrame;
import eUpdater.analysers.methods.methods;
import eUpdater.frame.classFrame;
import eUpdater.misc.classes;
import eUpdater.searchers.FieldSearcher;

/**
 * Created by Kyle on 7/21/2015.
 */
public class cacheable extends classAnalyserFrame {

    public void identify(classFrame c) {
        classes.myCacheable.setId("Cacheable");
        setMethodAnalyser(methods.myCacheable);

        int nodeCount;
        if (c.superName.equals(classes.myNode.getName()) && c.fields.size() == 2) {
            FieldSearcher fieldSearch = new FieldSearcher(c);
            nodeCount = fieldSearch.countDesc(String.format("L%s;", c.name));
            if (nodeCount == 2)
                classes.myCacheable.set(c);
        }
    }

    public void setMethodAnalyser(methodAnalyserFrame m) {

    }

}
