package eUpdater.analysers.methods;

import eUpdater.frame.hook;
import eUpdater.misc.classes;
import eUpdater.searchers.FieldSearcher;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;


/**
 * Created by Kyle on 11/5/2015.
 */
public class linkedList extends methodAnalyserFrame {

    public void identify() {
        this.setParent("LinkedList", classes.myLinkedList);
        this.setNeededHooks(Arrays.asList("Head", "Current"));

        FieldSearcher FSearch = new FieldSearcher(parentClass);
        addHook(new hook("Head", FSearch.findAccess(Opcodes.ACC_PUBLIC)));
        addHook(new hook("Current", FSearch.findAccess(0)));
    }
}
