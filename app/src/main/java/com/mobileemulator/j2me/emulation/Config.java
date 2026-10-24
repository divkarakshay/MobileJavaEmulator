package com.mobileemulator.j2me.emulation;

import java.io.*;
import java.util.*;

// Per-app config system from freej2me-plus (GPLv3) adapted for Android
// Stored as key:value in /sdcard/J2MEEmulator/config/<appname>/game.conf
public class Config {
    private Map<String,String> props = new LinkedHashMap<>();
    private File file;

    // Compatibility flags (freej2me-plus)
    public boolean compatFantasyZoneFix = false;
    public boolean compatTranslateToOriginOnReset = false;
    public boolean compatImmediateRepaints = false;
    public boolean compatOverridePlatformChecks = false;
    public boolean compatSiemensFriendlyDrawing = false;
    public boolean compatIgnoreVolumeChanges = false;
    public boolean compatMCV3HorizontalFovFix = false;

    // Speed hacks
    public int unlockFramerateHack = 0; // 0=disabled 1=safe 2=extended 3=aggressive
    public boolean halfResM3GRaster = false;
    public boolean halfResMCV3Raster = false;
    public boolean MCV3NoLighting = false;

    // Rendering overrides (M3G)
    public String m3gAntiAliasingMode = "app";
    public String m3gBilinearMode = "app";
    public String m3gDitheringMode = "app";
    public String m3gPerspectiveCorrectionMode = "app";
    public int m3gPerspCorrSubFactor = 15;

    public Config(File file){ this.file=file; if(file.exists()) load(); }

    public void load(){
        try(BufferedReader br=new BufferedReader(new FileReader(file))){
            String line;
            while((line=br.readLine())!=null){
                line=line.trim();
                if(line.isEmpty()||line.startsWith("#")) continue;
                int idx=line.indexOf(':');
                if(idx<0) idx=line.indexOf('=');
                if(idx<0) continue;
                String k=line.substring(0,idx).trim();
                String v=line.substring(idx+1).trim();
                props.put(k,v);
                apply(k,v);
            }
        } catch(Exception e){ e.printStackTrace(); }
    }

    private void apply(String k,String v){
        switch(k){
            case "compatFantasyZoneFix": compatFantasyZoneFix=parseBool(v); break;
            case "compatTranslateToOriginOnReset": compatTranslateToOriginOnReset=parseBool(v); break;
            case "compatImmediateRepaints": compatImmediateRepaints=parseBool(v); break;
            case "compatOverridePlatformChecks": compatOverridePlatformChecks=parseBool(v); break;
            case "compatSiemensFriendlyDrawing": compatSiemensFriendlyDrawing=parseBool(v); break;
            case "compatIgnoreVolumeChanges": compatIgnoreVolumeChanges=parseBool(v); break;
            case "compatMCV3HorizontalFovFix": compatMCV3HorizontalFovFix=parseBool(v); break;
            case "unlockFramerateHack": unlockFramerateHack=parseInt(v,0); break;
            case "halfResM3GRaster": halfResM3GRaster=parseBool(v); break;
            case "halfResMCV3Raster": halfResMCV3Raster=parseBool(v); break;
            case "MCV3NoLighting": MCV3NoLighting=parseBool(v); break;
            case "m3gAntiAliasingMode": m3gAntiAliasingMode=v; break;
            case "m3gBilinearMode": m3gBilinearMode=v; break;
            case "m3gDitheringMode": m3gDitheringMode=v; break;
            case "m3gPerspectiveCorrectionMode": m3gPerspectiveCorrectionMode=v; break;
            case "m3gPerspCorrSubFactor": m3gPerspCorrSubFactor=parseInt(v,15); break;
        }
    }

    public void save(){
        try{ file.getParentFile().mkdirs();
            try(PrintWriter pw=new PrintWriter(file)){
                for(Map.Entry<String,String> e: props.entrySet()) pw.println(e.getKey()+": "+e.getValue());
            }
        } catch(Exception e){ e.printStackTrace(); }
    }

    public void set(String k,String v){ props.put(k,v); apply(k,v); }

    private boolean parseBool(String v){ return v.equalsIgnoreCase("true")||v.equals("1")||v.equalsIgnoreCase("on"); }
    private int parseInt(String v,int d){ try{ return Integer.parseInt(v);} catch(Exception e){ return d; } }

    public static Config forApp(String appName){
        File f = new File(android.os.Environment.getExternalStorageDirectory(), "J2MEEmulator/config/"+appName+"/game.conf");
        return new Config(f);
    }

    public static Config systemConfig(){
        File f = new File(android.os.Environment.getExternalStorageDirectory(), "J2MEEmulator/freej2me_system/freej2me.conf");
        return new Config(f);
    }
}