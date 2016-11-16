package eUpdater.misc;

public class Printer {

/*
    public static void Log() {
        for (Map.Entry<Clazz, Field[]> entry : myFields.entrySet()) {
            Clazz key = entry.getKey();
            for (Map.Entry<Clazz, Field[]> _entry : myFields.entrySet()) {
                Clazz _key = _entry.getKey();
                if (key.superName.equals(_key.name))
                    key.superName = _key.ID;

            }
        }


        int theLength = 0;
        int secondLength = 0;
        int Broken = 0;
        int goodFields = 0;
        int goodClasses = 0;
        ArrayList<String[]> brokenFields = new ArrayList<>();
        for (Map.Entry<Clazz, Field[]> entry : myFields.entrySet()) {
            Clazz key = entry.getKey();
            System.out.println("|>" + key.ID + " = " + key.name + " -> Extends " + key.superName);
             ++ goodClasses;
            int tempBroken = 0;
            for (int I = 0; I < entry.getValue().length; ++I) {
                Field f = entry.getValue()[I];
                if (f.name == null) {
                    brokenFields.add(new String[] {key.ID, f.ID});
                    ++ Broken;
                    ++ tempBroken;
                    continue;
                }
                ++ goodFields;
                theLength = f.ID.length();
                System.out.print("      ^ " + f.ID);
                for (int C = 0; C < 19 - theLength; ++C)
                    System.out.print(".");
                if (key.name.equals("client"))
                    System.out.print(f.owner + "." + f.name);
                else
                    System.out.print(key.name + "." + f.name);
                if (f.multi != 0) {
                    System.out.print(" * " + f.multi);
                    secondLength = String.valueOf(f.multi).length();
                    if (!key.name.equals("client")) {
                        for (int C = 0; C < 21 - (secondLength + f.name.length() + key.name.length()); ++C)
                            System.out.print(" ");
                        System.out.println(" -> " + f.desc);
                    } else {
                        for (int C = 0; C < 21 - (secondLength + f.name.length() + f.owner.length()); ++C)
                            System.out.print(" ");
                        System.out.println(" -> " + f.desc);
                    }
                } else {
                    if (!key.name.equals("client")) {
                        for (int C = 0; C < 24 - (f.name.length() + key.name.length()); ++C)
                            System.out.print(" ");
                        System.out.println(" -> " + f.desc);
                    } else {
                        for (int C = 0; C < 24 - (f.name.length() + f.owner.length()); ++C)
                            System.out.print(" ");
                        System.out.println(" -> " + f.desc);
                    }
                }
            }
            if (entry.getValue().length != 0)
                System.out.println("  Identified " + (entry.getValue().length - tempBroken) +  " / " + entry.getValue().length + " Fields \n");
            else
                System.out.println("");
        }
        System.out.println("Identified " + goodClasses + " / " + goodClasses + " Classes.. \nIdentified " + goodFields + " / " + (Broken + goodFields) + " Fields..\n");
        if (Broken > 0) {
            System.out.println("Broken Fields: ");
            for (int I = 0; I < Broken; ++ I)
                System.out.println("  -> " + brokenFields.get(I)[0] + "." + brokenFields.get(I)[1]);
        }
    }

    public static void simbaLape() {
        int theLength = 0;
        int miss = 0;
        try {
            for (Map.Entry<Clazz, Field[]> entry : myFields.entrySet()) {
                Clazz key = entry.getKey();
                System.out.println("{" + key.ID + " = " + key.name + "}");
                for (int I = 0; I < entry.getValue().length; ++I) {
                    try {
                        Field f = entry.getValue()[I];
                        theLength = f.ID.length() + key.ID.length();
                        System.out.print(" " + key.ID + "_" + f.ID + ": THook = ");
                        for (int C = 0; C < 25 - theLength; ++C) {
                            System.out.print(" ");
                        }
                        if (key.name.equals("client"))
                            System.out.print(" ['" + f.owner + "." + f.name + "', ");
                        else
                            System.out.print(" ['" + f.name + "', ");
                        if (f.multi != 0) {
                            System.out.println(f.multi + "];");
                        } else {
                            System.out.println("1];");
                        }
                    } catch (Exception e){
                        ++miss;
                        System.out.println("//Broken");
                    }
                }
                System.out.println("");
            }
        } catch (Exception e) { e.printStackTrace(); }
        if (miss > 0)
            System.out.println("Lost " + miss + " Fields");
    }

    public static void simbaPascal() {
        int miss = 0;
        try {
            for (Map.Entry<Clazz, Field[]> entry : myFields.entrySet()) {
                Clazz key = entry.getKey();
                System.out.println("{" + key.ID + " = " + key.name + "}");
                for (int I = 0; I < entry.getValue().length; ++I) {
                    try {
                        Field f = entry.getValue()[I];
                        System.out.print(" " + key.ID + "_" + f.ID + " = ");
                        if (key.name.equals("client")) {
                            System.out.println(" '" + f.owner + "." + f.name + "'" + ";");
                            if (f.multi != 0)
                                System.out.println(" " + "Client" + "_" + f.ID + "_Multiplier" + " = " + f.multi + ";");
                        }
                        else {
                            System.out.println(" '" + f.name + "'" + ";");
                            if (f.multi != 0)
                                System.out.println(" " + key.ID + "_" + f.ID + "_Multiplier" + " = " + f.multi + ";");
                        }

                    } catch (Exception e){
                        ++miss;
                        System.out.println("//Broken");
                    }
                }
                System.out.println("");
            }
        } catch (Exception e) { e.printStackTrace(); }
        if (miss > 0)
            System.out.println("Lost " + miss + " Fields");
    }
*/
}
