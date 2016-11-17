package eUpdater.misc;

import eUpdater.frame.classFrame;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;


public final class JarHandler {

    public static HashMap<String, classFrame> CLASSES = new HashMap();

    public JarHandler(String theFile) {
        try {
            JarFile jar = new JarFile(theFile);

            CLASSES = this.parseJar(jar);
        } catch (IOException e) {
        }
    }

    public static void Parse(String File) {
        new JarHandler(File);
    }

    public HashMap<String, classFrame> parseJar(JarFile jar) {
        HashMap<String, classFrame> classes = new HashMap<>();
        try {
            Enumeration<?> enumeration = jar.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry entry = (JarEntry) enumeration.nextElement();
                if (entry.getName().endsWith(".class")) {
                    ClassReader classReader = new ClassReader(jar.getInputStream(entry));
                    classFrame classNode = new classFrame();
                    classReader.accept(classNode, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                    classes.put(classNode.name, classNode);
                }
            }
            jar.close();
            return classes;
        } catch (Exception e) {
            return null;
        }
    }

    public static void downloadJar(String Address, String UserAgent, String Location) {
        try {
            URLConnection Connection = new URL(Address).openConnection();
            Connection.addRequestProperty("Protocol", "HTTP/1.1");
            Connection.addRequestProperty("Connection", "keep-alive");
            Connection.addRequestProperty("Keep-Alive", "300");
            if (UserAgent != null) {
                Connection.addRequestProperty("User-Agent", UserAgent);
            } else {
                Connection.addRequestProperty("User-Agent", "Mozilla/5.0 (" + System.getProperty("os.name") + " " + System.getProperty("os.version") + ") Java/" + System.getProperty("java.version"));
            }
            byte[] Buffer = new byte[Connection.getContentLength()];
            try (DataInputStream Stream = new DataInputStream(Connection.getInputStream())) {
                Stream.readFully(Buffer);
            }
            try (FileOutputStream fos = new FileOutputStream(Location + "GamePack.jar")) {
                fos.write(Buffer);
            }
        } catch (Exception Ex) {
            Ex.printStackTrace();
        }
    }

    public static void save(final String name) {
        final File file = new File(name);
        try (final JarOutputStream out = new JarOutputStream(new FileOutputStream(file))) {
            for (final ClassNode c : JarHandler.CLASSES.values()) {
                out.putNextEntry(new JarEntry(c.name + ".class"));
                final ClassWriter cw = new ClassWriter(0);
                c.accept(cw);
                //System.out.println(c.name);
                //c.accept(new CheckClassAdapter(cw, true));
                out.write(cw.toByteArray());
                out.closeEntry();
            }
            out.flush();
            out.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

}
