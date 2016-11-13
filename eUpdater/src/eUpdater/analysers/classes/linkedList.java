package eUpdater.analysers.classes;

import eUpdater.frame.classFrame;
import eUpdater.analysers.methods.methods;
import eUpdater.searchers.FieldSearcher;
import eUpdater.misc.classes;


/**
 * Created by Kyle on 7/22/2015.
 */
public class linkedList extends classAnalyserFrame{

    public void identify(classFrame c) {
        classes.myLinkedList.setId("linkedList");
        setMethodAnalyser(methods.myLinkedList);
        if (c.superName.equals("java/lang/Object") && c.fields.size() == 2) {
            FieldSearcher FSearch = new FieldSearcher(c);
            int L = FSearch.countDesc("L" + classes.myNode.getName() + ";");
            if (L == 2 && c.methods.size() > 9)
                classes.myLinkedList.set(c);
        }
    }
}
