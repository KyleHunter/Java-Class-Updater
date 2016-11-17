package eUpdater.analysers.classes;

import eUpdater.analysers.methods.methods;
import eUpdater.frame.classFrame;
import eUpdater.misc.classes;
import org.objectweb.asm.tree.FieldNode;

import java.lang.reflect.Modifier;

/**
 * Created by Kyle on 7/22/2015.
 */
public class widgetNode extends classAnalyserFrame {

    public void identify(classFrame c) {
        this.setId("WidgetNode");
        this.setMethodAnalyser(methods.myWidgetNode);
        int intCount = 0, boolCount = 0;
        if (c.superName.equals(classes.myNode.getName())) {
            for (int I = 0; I < c.fields.size(); ++I) {
                FieldNode Field = (FieldNode) c.fields.get(I);
                if (Modifier.isStatic(Field.access))
                    continue;
                if (Field.desc.equals("I"))
                    ++intCount;
                if (Field.desc.equals("Z"))
                    ++boolCount;
            }
            if (boolCount == 1 && intCount == 2)
                this.set(c);
        }
    }

}
