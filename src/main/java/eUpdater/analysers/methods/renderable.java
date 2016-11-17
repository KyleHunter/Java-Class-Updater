package eUpdater.analysers.methods;

import eUpdater.frame.hook;
import eUpdater.misc.classes;
import eUpdater.searchers.FieldSearcher;
import eUpdater.searchers.Searcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Kyle on 10/16/2015.
 */
public class renderable extends methodAnalyserFrame {

    public void identify() {
        this.setParent("Renderable", classes.myRenderable);
        this.setNeededHooks(Arrays.asList("ModelHeight"));

        List<MethodNode> methodList = parentClass.methods;
        for (MethodNode method : methodList) {
            if (method.desc.contains("(IIIIIIII")) {
                Searcher Search = new Searcher(method);
                int L = Search.find(new int[]{Opcodes.ALOAD, Opcodes.ILOAD, Opcodes.ILOAD, Opcodes.ILOAD, Opcodes.ILOAD, Opcodes.ILOAD}, 0);
                if (L > 0) {
                    FieldSearcher fs = new FieldSearcher(parentClass);
                    Searcher search = new Searcher(method);
                    addHook(new hook("ModelHeight", fs.findAccess(1)));
                }
            }
        }
    }

}
