package eUpdater.analysers.methods;

import eUpdater.frame.classFrame;
import eUpdater.frame.hook;
import eUpdater.misc.classes;
import eUpdater.searchers.FieldSearcher;
import eUpdater.searchers.Searcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;

import static eUpdater.misc.JarHandler.CLASSES;

/**
 * Created by Kyle on 10/16/2015.
 */
public class npcDefinition extends methodAnalyserFrame {

    public void identify() {
        this.setParent("NpcDefinition", classes.myNpcDefinition);
        this.setNeededHooks(Arrays.asList("Actions", "Name", "ID", "CombatLevel"));

        FieldSearcher fs = new FieldSearcher(parentClass);
        addHook(new hook("Actions", fs.findDesc("[Ljava/lang/String;")));
        addHook(new hook("Name", fs.findDesc("Ljava/lang/String;")));

        MethodNode method = parentClass.getMethod(false, classes.myAnimationSequence.getName() + ";IL" +
                classes.myAnimationSequence.getName());

        Searcher search = new Searcher(method);
        AbstractInsnNode[] Instructions = method.instructions.toArray();
        int L = search.find(new int[]{Opcodes.GETFIELD, Opcodes.LDC, Opcodes.IMUL, Opcodes.I2L}, 0);
        if (L != -1)
            addHook(new hook("ID", Instructions, L));

        for (classFrame c : CLASSES.values()) {
            method = c.getMethod(false, "L" + classes.myNpcDefinition.getName() + ";III");
            if (method == null)
                continue;
            search = new Searcher(method);
            L = search.find(new int[]{Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.LDC, Opcodes.IMUL,
                    Opcodes.GETSTATIC, Opcodes.GETFIELD}, 0);
            Instructions = method.instructions.toArray();
            if (L != -1)
                addHook(new hook("CombatLevel", Instructions, L + 1));
        }
    }

}
