package eUpdater.analysers.classes;

import eUpdater.analysers.methods.methods;
import eUpdater.frame.classFrame;
import eUpdater.misc.classes;
import eUpdater.searchers.FieldSearcher;

/**
 * Created by Kyle on 7/22/2015.
 */
public class hashTable extends classAnalyserFrame {

    public void identify(classFrame c) {
        classes.myHashTable.setId("HashTable");
        this.setMethodAnalyser(methods.myHashTable);

        if (c.superName.equals("java/lang/Object")) {
            FieldSearcher FSearch = new FieldSearcher(c);
            int nodeArray = FSearch.countDesc("[L" + classes.myNode.getName() + ";");
            int nodeCount = FSearch.countDesc("L" + classes.myNode.getName() + ";");
            if (nodeArray == 1 && nodeCount == 2) {
                classes.myHashTable.set(c);
            }
        }
    }
}
