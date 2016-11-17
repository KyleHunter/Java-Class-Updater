package eUpdater.frame;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 7/25/2015.
 */
public class classFrame extends ClassNode {

    public List<FieldNode> getFields(String desc) {
        List<FieldNode> fields = this.fields;
        List<FieldNode> temp = new ArrayList();
        for (FieldNode fN : fields) {
            if (fN.desc.equals(desc)) {
                temp.add(fN);
            }
        }
        return temp;
    }

    public List<FieldNode> getFields(Integer nonAccess) {
        List<FieldNode> fields = this.fields;
        List<FieldNode> temp = new ArrayList();
        for (FieldNode fN : fields)
            if ((fN.access & nonAccess) == 0)
                temp.add(fN);
        return temp;
    }

    public List<FieldNode> getFields(Integer nonAccess1, Integer nonAccess2) {
        List<FieldNode> fields = this.fields;
        List<FieldNode> temp = new ArrayList();
        for (FieldNode fN : fields)
            if ((fN.access & nonAccess1) == 0 && (fN.access & nonAccess2) == 0)
                temp.add(fN);
        return temp;
    }

    public List<FieldNode> getFields(Integer none, Integer Access, Integer nonAccess2) {
        List<FieldNode> fields = this.fields;
        List<FieldNode> temp = new ArrayList();
        for (FieldNode fN : fields)
            if ((fN.access & Access) != 0 && (fN.access & nonAccess2) == 0)
                temp.add(fN);
        return temp;
    }

    public void removeFields(String nonDesc) {
        List<FieldNode> fields = this.fields;
        List<FieldNode> temp = new ArrayList();
        for (FieldNode fN : fields) {
            if (!fN.desc.equals(nonDesc)) {
                temp.remove(fN);
            }
        }
    }

    public MethodNode getMethod(boolean exact, String desc) {
        List<MethodNode> methods = new ArrayList<>();
        List<MethodNode> methodList = this.methods;
        for (MethodNode m : methodList) {
            if (exact) {
                if (m.desc.equals(desc))
                    methods.add(m);
            } else if (m.desc.contains(desc))
                methods.add(m);
        }
        if (methods.size() > 1)
            System.out.println(methods.get(231312312));
        if (methods.size() == 0)
            return null;
        return methods.get(0);
    }

    public List<MethodNode> getMethods(boolean exact, String desc) {
        List<MethodNode> methods = new ArrayList<>();
        List<MethodNode> methodList = this.methods;
        for (MethodNode m : methodList) {
            if (exact) {
                if (m.desc.equals(desc))
                    methods.add(m);
            } else if (m.desc.contains(desc))
                methods.add(m);
        }
        return methods;
    }

    public List<MethodNode> getMethodsName(boolean exact, String desc) {
        List<MethodNode> methods = new ArrayList<>();
        List<MethodNode> methodList = this.methods;
        for (MethodNode m : methodList) {
            if (exact) {
                if (m.name.equals(desc))
                    methods.add(m);
            } else if (m.name.contains(desc))
                methods.add(m);
        }
        return methods;
    }

}
