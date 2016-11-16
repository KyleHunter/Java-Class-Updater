package eUpdater.analysers.classes;

import eUpdater.analysers.methods.methods;
import eUpdater.frame.classFrame;
import eUpdater.misc.classes;
import eUpdater.searchers.Searcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

/**
 * Created by Kyle on 7/21/2015.
 */
public class renderable extends classAnalyserFrame {

    public void identify(classFrame c) {
        this.setId("Renderable");
        setMethodAnalyser(methods.myRenderable);

        if (c.superName.equals(classes.myCacheable.getName())) {
            for (int I = 0; I < c.methods.size(); ++I) {
                List<MethodNode> methodList = c.methods;
                for (MethodNode m : methodList) {
                    if (m.desc.contains("(IIIIIIII")) {
                        Searcher search = new Searcher(m);
                        int L = search.find(new int[]{Opcodes.ALOAD, Opcodes.ILOAD, Opcodes.ILOAD, Opcodes.ILOAD, Opcodes.ILOAD, Opcodes.ILOAD}, 0);
                        if (L > 0) {
                            this.set(c);
                        }
                    }
                }
            }
        }
    }
}
