package eUpdater.deob;
/**
 * Created by Kyle on 1/12/2015.
 */

import org.objectweb.asm.Opcodes;
        import org.objectweb.asm.tree.AbstractInsnNode;
        import org.objectweb.asm.tree.ClassNode;
        import org.objectweb.asm.tree.MethodInsnNode;
        import org.objectweb.asm.tree.MethodNode;

        import java.util.ArrayList;
        import java.util.Collection;
        import java.util.List;

public class MethodDeob {
    private Collection<ClassNode> classes;
    private ArrayList<Info> usedMethods;
    private int method_count = 0;

    public MethodDeob(Collection<ClassNode> classes) {
        this.classes = classes;
        this.usedMethods = new ArrayList<>();
    }

    public MethodDeob analyse() {
        for (ClassNode n : classes) {
            List<MethodNode> Methods = n.methods;
            for (MethodNode m : Methods) {
                if (m.name.equals("<init>") || m.name.equals("<clinit>") || ((m.access & Opcodes.ACC_ABSTRACT) != 0)) {
                    addMethod(n, m);
                }
                ++method_count;
            }
        }

        for (ClassNode n : classes) {
            if (n.name.equals("client")) {
                List<MethodNode> Methods = n.methods;
                for (MethodNode m : Methods) {
                    if ((m.name.equals("<init>") || m.name.equals("<clinit>")) && m.desc.equals("()V")) {

                        AbstractInsnNode arr[] = m.instructions.toArray();
                        for (AbstractInsnNode a : arr) {
                            if (a instanceof MethodInsnNode) {
                                addMethod(((MethodInsnNode) a).owner, (MethodInsnNode) a);
                            }
                        }
                    }
                }
            }
        }

        follow();

        return this;
    }

    public void remove() {
        System.out.println("Kept Methods: " + usedMethods.size() + " of " + method_count);
    }


    private void follow() {
        for (int i = 0; i < usedMethods.size(); ++i) {
            MethodNode m = getMethod(usedMethods.get(i));
            if (m != null) {
                AbstractInsnNode arr[] = m.instructions.toArray();
                for (AbstractInsnNode a : arr) {
                    if (a instanceof MethodInsnNode) {
                        MethodInsnNode mi = (MethodInsnNode)a;
                        if (addMethod(mi.owner, mi)) {
                            i = -1;
                        }
                    }
                }
            }
        }
    }


    private MethodNode getMethod(Info u) {
        ClassNode n = findClass(u.getNode());
        if (n != null) {
            List<MethodNode> Methods = n.methods;
            for (MethodNode m : Methods) {
                if (m.name.equals(u.getName()) && m.desc.equals(u.desc)) {
                    return m;
                }
            }
        }
        return null;
    }

    private ClassNode findClass(String name) {
        for (ClassNode n : classes) {
            if (n.name.equals(name)) {
                return n;
            }
        }
        return null;
    }

    private boolean addMethod(String node, MethodInsnNode method) {
        ClassNode n = findClass(node);
        if (n != null) {
            return addMethod(n, method);
        }
        return false;
    }

    private boolean addMethod(ClassNode node, MethodInsnNode method) {
        MethodNode custom_method = new MethodNode(Opcodes.ASM4, Opcodes.ACC_PUBLIC, method.name, method.desc, null, null);
        return addMethod(node, custom_method);
    }

    private boolean addMethod(ClassNode node, MethodNode method) {
        Info info = new Info(node, method);
        if (!node.name.contains("java") && !usedMethods.contains(info)) {
            usedMethods.add(info);
            return true;
        }
        return false;
    }

    private class Info {
        private String node, name, desc;

        public Info(ClassNode node, MethodNode method) {
            this.node = node.name;
            this.name = method.name;
            this.desc = method.desc;
        }

        public String getNode() {
            return node;
        }

        public void setNode(String node) {
            this.node = node;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Info) {
                Info info = (Info) o;
                return node.equals(info.node) && name.equals(info.name) && desc.equals(info.desc);
            }
            return false;
        }

        @Override
        public String toString() {
            return node + "." + name + "   " + desc;
        }
    }
}