package eUpdater.analysers.classes;

import eUpdater.analysers.methods.methods;
import eUpdater.frame.classFrame;
import eUpdater.misc.classes;
import org.objectweb.asm.tree.FieldNode;

/**
 * Created by Kyle on 7/22/2015.
 */
public class player extends classAnalyserFrame {

    public void identify(classFrame c) {
        classes.myPlayer.setId("Player");
        this.setMethodAnalyser(methods.myPlayer);

        if (c.superName.equals(classes.myActor.getName())) {
            for (int I = 0; I < c.fields.size(); ++I) {
                FieldNode field = (FieldNode) c.fields.get(I);
                if (field.desc.equals("Ljava/lang/String;")) {
                    if (c.fields.size() > 10)
                        classes.myPlayer.set(c);
                }
            }
        }
    }
}
