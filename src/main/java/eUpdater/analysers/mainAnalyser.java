package eUpdater.analysers;

import eUpdater.analysers.classes.classAnalyserFrame;
import eUpdater.frame.classFrame;
import eUpdater.frame.hook;
import eUpdater.main.eUpdater;
import eUpdater.misc.Misc;
import eUpdater.misc.classes;
import eUpdater.misc.timer;
import eUpdater.refactor.refactor;
import eUpdater.searchers.mutltiplierSearcher;
import org.objectweb.asm.tree.FieldNode;

import javax.json.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static eUpdater.misc.JarHandler.CLASSES;
import static javax.json.Json.createArrayBuilder;

/**
 * Created by Kyle on 7/21/2015.
 */
public class mainAnalyser {
    public static mainAnalyser access = null;

    private ArrayList<classAnalyserFrame> classAnalysers = new ArrayList();
    private List<String> brokenFields = new ArrayList<>();
    private int brokenFieldsInt = 0, totalFieldsInt = 0;
    private List<String> duplicateFields = new ArrayList<>();
    private int duplicateFieldsInt = 0;

    public ArrayList<classAnalyserFrame> getClassAnalysers() {
        return classAnalysers;
    }

    public void setClassAnalysers(ArrayList<classAnalyserFrame> classAnalysers) {
        this.classAnalysers = classAnalysers;
    }

    private void loadClasses() {

        this.classAnalysers.add(classes.myNode);
        this.classAnalysers.add(classes.myCacheable);
        this.classAnalysers.add(classes.myRenderable);
        this.classAnalysers.add(classes.myAnimable);
        this.classAnalysers.add(classes.myModel);
        this.classAnalysers.add(classes.myAnimationSequence);
        this.classAnalysers.add(classes.myNpcDefinition);
        this.classAnalysers.add(classes.myLinkedList);
        this.classAnalysers.add(classes.myActor);
        this.classAnalysers.add(classes.myNpc);
        this.classAnalysers.add(classes.myObjectDefinition);
        this.classAnalysers.add(classes.myBuffer);
        this.classAnalysers.add(classes.myWidget);
        this.classAnalysers.add(classes.myWidgetNode);
        this.classAnalysers.add(classes.myHashTable);
        this.classAnalysers.add(classes.myGameShell);
        this.classAnalysers.add(classes.myPlayer);
        this.classAnalysers.add(classes.myClient);
        this.classAnalysers.add(classes.myRegion);
        this.classAnalysers.add(classes.myBoundaryObject);
        this.classAnalysers.add(classes.myGameObject);
        this.classAnalysers.add(classes.myFloorDecoration);
        this.classAnalysers.add(classes.myWallDecoration);
        this.classAnalysers.add(classes.mySceneTile);
        this.classAnalysers.add(classes.myItem);
    }

    private void runClassAnalysers() {
        timer t = new timer(true);
        System.out.println("%% Identifying Classes..");
        for (classAnalyserFrame a : this.classAnalysers) {
            for (classFrame c : CLASSES.values()) {
                a.identify(c);
            }
            if (a.getName() == null) {
                Scanner inputClass = new Scanner(System.in);
                System.out.println(a.getId() + " broke :/");
                System.out.print(a.getId() + " = ");
                if (!a.getClass(inputClass.next())) {
                    System.out.println("Class not found, terminating..");
                    System.exit(1);
                }
            }
        }
        System.out.print("    (Identified " + this.classAnalysers.size() + " out of " + this.classAnalysers.size() + " Classes)");
        System.out.println(" ~ Took " + t.getElapsedTime() + " ms\n");
    }

    private void runMethodAnalysers() {
        timer t = new timer(true);
        System.out.println("%% Identifying Fields..");
        for (classAnalyserFrame a : this.classAnalysers) {
            if (a.hasMethodAnalyser) {
                a.getMethodAnalyser().identify();
                for (hook f : a.getMethodAnalyser().getHooks()) {
                    if (f.isDuplicate()) {
                        duplicateFieldsInt += 1;
                        duplicateFields.add(f.getId());
                    }
                    if (f.broken)
                        brokenFieldsInt += 1;
                }
                brokenFieldsInt += (a.getMethodAnalyser().getNeededHooks().size() - a.getMethodAnalyser().getHooks().size());
                totalFieldsInt += a.getMethodAnalyser().getNeededHooks().size();
            }
        }
        System.out.print("    (Identified " + (totalFieldsInt - brokenFieldsInt - duplicateFieldsInt) + " out of " + totalFieldsInt + " Fields)");
        System.out.println(" ~ Took " + t.getElapsedTime() + " ms\n");
    }

    private void checkFoundFields() {
        for (classAnalyserFrame a : this.classAnalysers) {
            if (!a.hasMethodAnalyser)
                continue;
            if (a.getMethodAnalyser().getNeededHooks().size() != a.getMethodAnalyser().getHooks().size()) {
                for (String f : a.getMethodAnalyser().getNeededHooks()) {
                    if (!a.getMethodAnalyser().containsHook(f)) {
                        Scanner inputField = new Scanner(System.in);
                        System.out.println(a.getId() + "." + f + " broke :/");
                        System.out.print(f + " = ");
                        String id = inputField.next();
                        System.out.println(id);
                        FieldNode newField = Misc.getField(id, a.getNode());
                        if (newField == null)
                            System.out.println("Field" + id + " Not Found");
                        a.getMethodAnalyser().addHook(new hook(f, newField));
                    }
                }
            }
        }
    }

    private void addMultis() {
        int multis = 0;
        boolean staticc;
        System.out.println("%% Populating Multipliers..");
        timer t = new timer(true);
        for (classAnalyserFrame a : this.classAnalysers) {
            if (a.getId().contains("Client"))
                staticc = true;
            else
                staticc = false;
            if (!a.hasMethodAnalyser)
                continue;
            for (hook f : a.getMethodAnalyser().getHooks()) {
                if (f.getDesc().equals("I")) {
                    if (staticc)
                        f.setMultiplier(mutltiplierSearcher.get(f.getName(), f.getOwner(), true));
                    else
                        f.setMultiplier(mutltiplierSearcher.get(f.getName(), a.getName(), false));
                    if (f.getMultiplier() != 0)
                        ++multis;
                }
            }
        }
        System.out.print("    (Populated " + multis + " Multipliers)");
        System.out.println(" ~ Took " + t.getElapsedTime() + " ms\n\n");

    }

 /*   private void jsonPrint() {

        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder build = factory.createObjectBuilder();
        JsonObjectBuilder build2 = factory.createObjectBuilder();

        for (classAnalyserFrame a : this.classAnalysers) {
            if (!a.hasMethodAnalyser)
                continue;
            for (hook f : a.getMethodAnalyser().getHooks()) {

                if (!a.getId().equals("Client")) {
                    build2.add(f.getId(), factory.createObjectBuilder()
                            .add("hook", f.getName())
                            .add("multi", f.getMultiplier()));
                    build2.add("name", a.getName());
                } else {
                    build2.add(f.getId(), factory.createObjectBuilder()
                            .add("hook", f.getOwner() + "." + f.getName())
                            .add("multi", f.getMultiplier()));
                    build2.add("name", a.getName());
                }
            }
            build.add(a.getId(), build2);
        }
        System.out.println(build.build().toString());
    }
*/
    private void logPrint() {
        for (classAnalyserFrame a : this.classAnalysers) {
            System.out.print(" # " + a.getId() + ": " + a.getNodes().get(0).name + ", " + a.duplicates() + " duplicates");
            if (a.duplicates() > 0) {
                System.out.print(": ");
                for (int i = 1; i < a.getNodes().size(); ++i)
                    System.out.print(a.getNodes().get(i).name + ", ");
            }
            System.out.println("");
            if (a.hasMethodAnalyser) {
                for (hook f : a.getMethodAnalyser().getHooks()) {
                    if (f.broken) {
                        brokenFields.add(f.getId());
                        System.out.println("     ~> " + f.getId() + " : Broken");
                    } else {
                        if (f.getMultiplier() != 0) {
                            if (a.getId().equals("Client")) {
                                System.out.println("     ~> " + f.getId() + " : " + f.getOwner() + "." + f.getName() + " * " + f.getMultiplier());
                            } else {
                                System.out.println("     ~> " + f.getId() + " : " + a.getNodes().get(0).name + "." + f.getName() + " * " + f.getMultiplier());
                            }
                        } else {
                            if (a.getId().equals("Client")) {
                                System.out.println("     ~> " + f.getId() + " : " + f.getOwner() + "." + f.getName());
                            } else {
                                System.out.println("     ~> " + f.getId() + " : " + a.getNodes().get(0).name + "." + f.getName());
                            }

                        }
                    }
                }
                int brokenFields = a.getMethodAnalyser().getBrokenHooks().size();
                int totalFields = a.getMethodAnalyser().getNeededHooks().size();
                System.out.println(" **Identified (" + (totalFields - brokenFields) + " / " + totalFields + ") Fields**");
            }
            System.out.println("");
        }
        if (brokenFields.size() > 0) {
            System.out.println("Broken Fields:");
            for (String s : brokenFields)
                System.out.println(" ~> " + s);
            System.out.println("");
        } else
            System.out.println("");

        if (duplicateFieldsInt > 0) {
            System.out.println("Duplicate Fields:");
            for (String s : duplicateFields)
                System.out.println(" ~> " + s);
            System.out.println("");
        } else
            System.out.println("");
    }

    private void banzaiPrint() {
        int length = 0;
        try {
            FileWriter write = new FileWriter("" +
                    "C:/Users/Kyle/Banzai/Internal/hooks.py", false);
            PrintWriter printer = new PrintWriter(write);
            printer.println("ReflectionRevision = '" + eUpdater.Revision + "'\n");
            for (classAnalyserFrame a : this.classAnalysers) {
                printer.print("#  " + a.getId() + ": " + a.getNodes().get(0).name);
                printer.println("");
                if (a.hasMethodAnalyser) {
                    for (hook f : a.getMethodAnalyser().getHooks()) {
                        length = (a.getId().length() + f.getId().length());
                        if (f.getMultiplier() != 0) {
                            printer.print(a.getId() + "_" + f.getId() + " = ");
                            for (int I = 0; I < 25 - length; ++I)
                                printer.print(" ");
                            if (a.getId().equals("Client"))
                                printer.println("['" + f.getOwner() + "." + f.getName() + "', " + f.getMultiplier() + "]");
                            else
                                printer.println("['" + f.getName() + "', " + f.getMultiplier() + "]");

                        } else {
                            printer.print(a.getId() + "_" + f.getId() + " = ");
                            for (int I = 0; I < 25 - length; ++I)
                                printer.print(" ");
                            if (a.getId().equals("Client"))
                                printer.println("['" + f.getOwner() + "." + f.getName() + "', 1]");
                            else
                                printer.println("['" + f.getName() + "', 1]");


                        }
                    }
                }
                printer.println("");
            }
            printer.close();
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }

    private void simbaPrint() {
        int length = 0;
        try {
            FileWriter write = new FileWriter("" +
                    "C:/Simba/Includes/Reflection/lib/internal/Hooks.simba", false);
            PrintWriter printer = new PrintWriter(write);
            printer.println("const");
            printer.println("    ReflectionRevision = '" + eUpdater.Revision + "';");
            for (classAnalyserFrame a : this.classAnalysers) {
                printer.print("{" + a.getId() + ": " + a.getNodes().get(0).name + "}");
                printer.println("");
                if (a.hasMethodAnalyser) {
                    for (hook f : a.getMethodAnalyser().getHooks()) {
                        length = (a.getId().length() + f.getId().length());
                        if (f.getMultiplier() != 0) {
                            printer.print(" " + a.getId() + "_" + f.getId() + ": THook = ");
                            for (int I = 0; I < 25 - length; ++I)
                                printer.print(" ");
                            if (a.getId().equals("Client"))
                                printer.println("['" + f.getOwner() + "." + f.getName() + "', " + f.getMultiplier() + "];");
                            else
                                printer.println("['" + f.getName() + "', " + f.getMultiplier() + "];");

                        } else {
                            printer.print(" " + a.getId() + "_" + f.getId() + ": THook = ");
                            for (int I = 0; I < 25 - length; ++I)
                                printer.print(" ");
                            if (a.getId().equals("Client"))
                                printer.println("['" + f.getOwner() + "." + f.getName() + "', 1];");
                            else
                                printer.println("['" + f.getName() + "', 1];");


                        }
                    }
                }
                printer.println("");
            }
            printer.close();
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }

    public void Run() {
        loadClasses();
        runClassAnalysers();
        runMethodAnalysers();
        //checkFoundFields();
        if (eUpdater.findMultis)
            addMultis();
        if (eUpdater.simbaPrint)
            simbaPrint();
        if (eUpdater.logPrint)
            logPrint();
        banzaiPrint();
        access = this;
        if (eUpdater.doRefactor)
            refactor.run();
    }
}
