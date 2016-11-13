package eUpdater.deob;
/**
 * Created by Kyle on 1/12/2015.
 */

import eUpdater.misc.Misc;

import java.util.ArrayList;

public class Deobsfucate {
    private ArrayList<DeobFrame> Deobs = new ArrayList();

    private void loadDeobs() {
        this.Deobs.add(new Method());
        this.Deobs.add(new Multipliers());
        this.Deobs.add(new EqualSwap());
        this.Deobs.add(new RemoveExceptions());
        this.Deobs.add(new OpaquePredicates());
        this.Deobs.add(new MethodName());
    }


    private void runAnalysers() {
        double totalTime = 0;
        for (DeobFrame tempDeob: this.Deobs) {
            long startTime = System.nanoTime();
            tempDeob.Deob();
            long endTime = System.nanoTime();
            double tempTime = (endTime - startTime) / 1e6;
            System.out.println(" (" + Misc.round(tempTime, 2) + " ms)");
            totalTime = totalTime + tempTime;
        }
        System.out.println("Total Deob took " + Misc.round(totalTime, 2) + " ms");
    }

    public void Run() {
        System.out.println("Beginning Deob..");
        this.loadDeobs();
        this.runAnalysers();
        System.out.println("Deob Finished..\n");
    }
}
