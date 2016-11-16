package eUpdater.analysers.methods;

import eUpdater.frame.classFrame;
import eUpdater.frame.hook;
import eUpdater.misc.classes;
import eUpdater.searchers.Searcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Collections;

import static eUpdater.misc.JarHandler.CLASSES;

/**
 * Created by Kyle on 11/9/2015.
 */
public class npc extends methodAnalyserFrame {

    public void identify() {
        this.setParent("Npc", classes.myNpc);
        this.setNeededHooks(Collections.singletonList("Definition"));

        int L = 0;
        for (classFrame c : CLASSES.values()) {
            MethodNode method = c.getMethod(true, "(IIIILjava/lang/String;Ljava/lang/String;II)V");
            if (method != null) {
                AbstractInsnNode[] Instructions = method.instructions.toArray();
                Searcher search = new Searcher(method);
                for (int I = 0; L != -1; ++I) {
                    L = search.find(new int[]{Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.ASTORE}, I);
                    if (L != -1)
                        if ((((FieldInsnNode) Instructions[L + 1]).desc.equals("L" + classes.myNpcDefinition.getName() + ";")))
                            addHook(new hook("Definition", Instructions, L + 1));
                }
            }
        }
    }

}

