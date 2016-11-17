package eUpdater.analysers.classes;

import eUpdater.analysers.methods.methodAnalyserFrame;
import eUpdater.frame.classFrame;

import java.util.ArrayList;

import static eUpdater.misc.JarHandler.CLASSES;

/**
 * Created by Kyle on 7/21/2015.
 */
public abstract class classAnalyserFrame {

    int count;
    private String id, name;
    private ArrayList<classFrame> nodes = new ArrayList<>();
    public boolean hasMethodAnalyser = false;
    private methodAnalyserFrame methodAnalyser;

    public abstract void identify(classFrame c);

    public int duplicates() {

        return count - 1;
    }

    public void setId(String s) {

        this.id = s;
    }

    public String getId() {

        return this.id;
    }

    public void setNode(classFrame c) {
        nodes.add(c);
        if (nodes.size() > 1) {
            if (!c.name.equals(nodes.get(nodes.size() - 2).name))
                ++count;
        } else {
            ++count;
        }
    }

    public ArrayList<classFrame> getNodes() {

        return this.nodes;
    }

    public classFrame getNode() {
        return getNodes().get(0);
    }

    public void setName(classFrame c) {
        this.name = c.name;
    }

    public String getName() {
        return this.name;
    }

    public void set(String id, classFrame c) {
        this.setId(id);
        this.setName(c);
        this.setNode(c);
    }

    public void set(classFrame c) {
        this.setName(c);
        this.setNode(c);
    }

    public boolean getClass(String name) {
        for (classFrame c : CLASSES.values()) {
            if (name.equals(c.name)) {
                this.set(c);
                return true;
            }
        }
        return false;
    }

    public void setMethodAnalyser(methodAnalyserFrame m) {
        this.methodAnalyser = m;
        this.hasMethodAnalyser = true;
    }

    public methodAnalyserFrame getMethodAnalyser() {
        return this.methodAnalyser;
    }

}
