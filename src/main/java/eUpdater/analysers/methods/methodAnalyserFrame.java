package eUpdater.analysers.methods;

import eUpdater.analysers.classes.classAnalyserFrame;
import eUpdater.frame.classFrame;
import eUpdater.frame.hook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 7/26/2015.
 */
public abstract class methodAnalyserFrame {
    classFrame parentClass;
    private ArrayList<hook> fields = new ArrayList<>();
    private List<String> neededFields;

    public abstract void identify();

    public void setParent(String s, classAnalyserFrame _parentClass) {

        this.parentClass = _parentClass.getNodes().get(0);
    }

    public classFrame getParent() {
        return parentClass;
    }

    public void setNeededHooks(List<String> s) {
        neededFields = s;
    }

    public ArrayList<hook> getHooks() {
        return this.fields;
    }

    public hook getHook(String id) {
        ArrayList<hook> fs = this.getHooks();
        for (hook f : fs) {
            if (f.getId().equals(id))
                return f;
        }
        return null;
    }

    public boolean containsHook(String s) {
        ArrayList<hook> ours = this.fields;
        for (hook ourField : ours)
            if (ourField.getId().equals(s)) {
                return true;
            }
        return false;
    }

    private boolean duplicateHook(hook h) {
        ArrayList<hook> ours = this.fields;
        for (hook ourField : ours)
            if (ourField.getId().equals(h.getId()) && !ourField.getName().equals(h.getName())) {
                ourField.setDuplicate(true);
                return true;
            }
        return false;
    }

    public List<String> getNeededHooks() {
        return neededFields;
    }

    public void addHook(hook f) {
        if (!this.containsHook(f.getId())) {
            this.fields.add(f);
            duplicateHook(f);
        }

    }

    public ArrayList<hook> getBrokenHooks() {
        ArrayList<hook> temp = new ArrayList<>();
        for (String f : this.getNeededHooks()) {
            if (!this.containsHook(f)) {
                temp.add(this.getHook(f));
            }
        }
        for (hook f : this.getHooks()) {
            if (f.getName().equals("NULL"))
                temp.add(f);
        }
        return temp;
    }

    public ArrayList<hook> getDuplicateHooks() {
        ArrayList<hook> temp = new ArrayList<>();
        for (String f : this.getNeededHooks()) {
            if (!this.containsHook(f)) {
                temp.add(this.getHook(f));
            }
        }
        for (hook f : this.getHooks()) {
            if (f.getName().equals("NULL"))
                temp.add(f);
        }
        return temp;
    }

}
