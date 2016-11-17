package eUpdater.searchers;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;

public class FieldSearcher {

    private final List<FieldNode> Fields;

    public FieldSearcher(ClassNode Node) {

        Fields = Node.fields;
    }

    public FieldNode findDesc(String desc) {

        for (FieldNode Field : Fields) {
            if (Field.desc.equals(desc)) {
                return Field;
            }
        }
        return null;
    }

    public int countDesc(String desc) {

        int Count = 0;
        for (FieldNode Field : Fields) {
            if (Field.desc.equals(desc)) {
                ++Count;
            }
        }
        return Count;
    }

    public FieldNode findContainsDesc(String desc) {

        for (FieldNode Field : Fields) {
            if (Field.desc.contains(desc)) {
                return Field;
            }
        }
        return null;
    }

    public FieldNode findAccess(int Acc) {

        for (FieldNode Field : Fields) {
            if (Field.access == Acc) {
                return Field;
            }
        }
        return null;
    }

    public int countContainsDesc(String desc) {

        int Count = 0;
        for (FieldNode Field : Fields) {
            if (Field.desc.contains(desc)) {
                ++Count;
            }
        }
        return Count;
    }

    public boolean findValue(int Acc) {

        for (FieldNode Field : Fields) {
            if (Field.value != null) {
                if (((int) Field.value) == Acc) {
                    return true;
                }
            }
        }
        return false;
    }

    public FieldNode findDescInstance(String desc, int Instance) {
        int Count = 0;
        for (FieldNode Field : Fields) {
            if (Field.desc.equals(desc)) {
                if (Count == Instance)
                    return Field;
                else
                    ++Count;
            }
        }
        return null;
    }
}
