package eUpdater.deob;

/**
 * @author Kyle
 */
public class MethodInfo {
    public final String owner;
    public final String name;
    public final String desc;

    public MethodInfo(String owner, String name, String desc) {
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MethodInfo) {
            MethodInfo info = (MethodInfo) obj;
            return owner.equals(info.owner) && name.equals(info.name) && desc.equals(info.desc);
        }
        return false;
    }

    @Override
    public String toString() {
        return owner + "." + name + desc;
    }
}
