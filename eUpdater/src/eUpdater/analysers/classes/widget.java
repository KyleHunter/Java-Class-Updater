package eUpdater.analysers.classes;

import eUpdater.frame.classFrame;
import eUpdater.analysers.methods.methods;
import eUpdater.searchers.FieldSearcher;
import eUpdater.misc.classes;


/**
 * Created by Kyle on 7/22/2015.
 */
public class widget extends classAnalyserFrame{

    public void identify(classFrame c){
        this.setId("Widget");
        this.setMethodAnalyser(methods.myWidget);
        classes.myWidget.setId("Widget");
        if (c.superName.equals(classes.myNode.getName())) {
            FieldSearcher search = new FieldSearcher(c);
            int L = search.countContainsDesc("[[L" + c.name);
            if (L == 1)
                this.set(c);
        }
    }
}
