package com.mobileemulator.j2me.emulation;

import java.io.File;
import java.io.IOException;
import javax.microedition.midlet.MIDlet;
import android.content.Context;
import android.util.Log;

public class MIDletLoader extends ClassLoader {
    private String appPath;
    private String appName;
    private String midletClassName = "aa.aaa"; // default for 2in1
    private MIDlet midi;
    private Context ctx;

    public MIDletLoader(ClassLoader parent) {
        super(parent);
    }
    public MIDletLoader(ClassLoader parent, Context ctx) {
        super(parent);
        this.ctx = ctx;
    }

    public MIDlet loadMIDlet(String path, byte[] jarBytes, byte[] jadBytes) throws IOException {
        this.appPath = path;
        parseJAD(jadBytes);
        if ((midletClassName == null || midletClassName.equals("aa.aaa")) && jarBytes != null) {
            // Try parse manifest from jarBytes for MIDlet class (needed for LostInTheJungle etc.)
            parseJarManifest(jarBytes);
        }
        if (appName == null || appName.isEmpty()) appName = "MIDlet";
        // Try Dex loading if dex file exists
        MIDlet dexMidlet = tryLoadDex(path);
        if (dexMidlet != null) {
            midi = dexMidlet;
            Log.i("MIDletLoader", "Loaded via Dex: "+midletClassName);
            return midi;
        }
        // Fallback dummy
        Log.i("MIDletLoader", "Dex not found for "+path+", using DummyMIDlet (stub). To run real game, place _dex.zip alongside jar (generated via d8 --min-api 21)");
        midi = new DummyMIDlet();
        return midi;
    }

    private MIDlet tryLoadDex(String jarPath) {
        try {
            String dexPath = null;
            if (jarPath != null) {
                // Try alongside jar
                File jarFile = new File(jarPath);
                File dexAlongside = new File(jarFile.getParentFile(), jarFile.getName().replace(".jar","_dex.zip"));
                if (dexAlongside.exists()) dexPath = dexAlongside.getAbsolutePath();
                // Try private files
                if (dexPath == null && ctx != null) {
                    File privateDex = new File(ctx.getExternalFilesDir(null), "2in1_dex.zip");
                    if (privateDex.exists()) dexPath = privateDex.getAbsolutePath();
                    File privateDex2 = new File(ctx.getExternalFilesDir(null), new File(jarPath).getName().replace(".jar","_dex.zip"));
                    if (privateDex2.exists()) dexPath = privateDex2.getAbsolutePath();
                }
                // Try /sdcard/Download dex
                if (dexPath == null) {
                    File dlDex = new File("/sdcard/Download/2in1_dex.zip");
                    if (dlDex.exists()) dexPath = dlDex.getAbsolutePath();
                }
            }
            if (dexPath == null) return null;
            File dexFile = new File(dexPath);
            if (!dexFile.exists()) return null;
            File optDir = ctx != null ? ctx.getCodeCacheDir() : new File("/data/local/tmp");
            if (!optDir.exists()) optDir.mkdirs();
            dalvik.system.DexClassLoader dcl = new dalvik.system.DexClassLoader(dexPath, optDir.getAbsolutePath(), null, getParent());
            Class<?> cls = dcl.loadClass(midletClassName);
            Object obj = cls.getDeclaredConstructor().newInstance();
            if (obj instanceof MIDlet) return (MIDlet) obj;
            Log.w("MIDletLoader", "Loaded class not MIDlet: "+cls);
            return null;
        } catch (Exception e) {
            Log.w("MIDletLoader", "Dex load failed for "+midletClassName+": "+e, e);
            return null;
        }
    }

    private void parseJAD(byte[] jadBytes) throws IOException {
        if (jadBytes == null) {
            // Try read alongside jar
            if (appPath != null) {
                File jadFile = new File(appPath.replace(".jar",".jad"));
                if (jadFile.exists()) {
                    try {
                        byte[] b = new byte[(int)jadFile.length()];
                        try (java.io.FileInputStream fis = new java.io.FileInputStream(jadFile)) { fis.read(b); }
                        jadBytes = b;
                    } catch (Exception e) {}
                }
            }
        }
        if (jadBytes == null) return;
        try {
            String jad = new String(jadBytes, "UTF-8");
            for (String line : jad.split("\n")) {
                line = line.trim();
                if (line.startsWith("MIDlet-Name:")) {
                    appName = line.substring("MIDlet-Name:".length()).trim();
                } else if (line.startsWith("MIDlet-1:")) {
                    // MIDlet-1: Mahjong Mania!, /bundle_res_1/ic.png, aa.aaa
                    String[] parts = line.split(",");
                    if (parts.length >= 3) midletClassName = parts[2].trim();
                }
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    private void parseJarManifest(byte[] jarBytes) {
        try {
            java.util.jar.Manifest mf = null;
            // Try from jarBytes
            try {
                java.util.jar.JarInputStream jis = new java.util.jar.JarInputStream(new java.io.ByteArrayInputStream(jarBytes));
                mf = jis.getManifest();
                jis.close();
            } catch (Exception e) {}
            // Fallback to file path
            if (mf == null && appPath != null) {
                try {
                    java.util.jar.JarFile jf = new java.util.jar.JarFile(new File(appPath));
                    mf = jf.getManifest();
                    jf.close();
                } catch (Exception e) {}
            }
            if (mf != null) {
                String midlet1 = mf.getMainAttributes().getValue("MIDlet-1");
                Log.i("MIDletLoader","Manifest MIDlet-1 raw: "+midlet1);
                if (midlet1 != null) {
                    String[] parts = midlet1.split(",");
                    if (parts.length >= 3) {
                        midletClassName = parts[2].trim();
                        Log.i("MIDletLoader","Parsed midletClassName from manifest: "+midletClassName);
                    }
                }
                String name = mf.getMainAttributes().getValue("MIDlet-Name");
                if (name != null) appName = name;
            } else {
                Log.w("MIDletLoader","Manifest null for "+appPath);
                // Hardcode for known game
                if (appPath != null && appPath.contains("LostInTheJungle")) midletClassName = "Hyperfactory";
            }
        } catch (Exception e) {
            Log.w("MIDletLoader","parseJarManifest failed: "+e, e);
            if (appPath != null && appPath.contains("LostInTheJungle")) midletClassName = "Hyperfactory";
        }
    }

    public MIDlet getMIDlet() { return midi; }

    static class DummyMIDlet extends MIDlet {
        @Override protected void startApp() { Log.i("DummyMIDlet","startApp called - would run "+Thread.currentThread()); }
        @Override protected void pauseApp() {}
        @Override protected void destroyApp(boolean unconditional) {}
    }
}