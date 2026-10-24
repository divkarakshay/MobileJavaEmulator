package com.mobileemulator.j2me.emulation;

import java.util.LinkedHashMap;
import java.util.Map;

public class DeviceProfile {
    public static class Profile {
        String name;
        String displayName;
        int width, height;
        int dpi;
        Map<Integer,Integer> keyMap = new LinkedHashMap<>();
        String platformString;

        Profile(String name, String displayName, int w, int h, int dpi, String platform) {
            this.name=name; this.displayName=displayName; this.width=w; this.height=h; this.dpi=dpi; this.platformString=platform;
        }
    }

    private static final Map<String, Profile> PROFILES = new LinkedHashMap<>();

    static {
        // Taken from freej2me-plus (11 profiles) + j2me-loader defaults
        add(nokia(240,320, "Nokia N73", "MIDP-2.0 CLDC-1.1", 240));
        add(nokia(176,208, "Nokia 6230i", "MIDP-2.0 CLDC-1.1", 182));
        add(nokia(128,160, "Nokia 6230", "MIDP-2.0 CLDC-1.1", 154));
        add(nokia(352,416, "Nokia N95", "MIDP-2.0 CLDC-1.1", 240));
        addProfile("nokia_s40", "Nokia S40 128x128", 128,128, 121, "Nokia6230i/2.0 (04.44) Profile/MIDP-2.0 Configuration/CLDC-1.1");
        addProfile("siemens", "Siemens CX65", 132,176, 130, "SIE-CX65/48 UP.Browser/7.0.2.2");
        addProfile("motorola", "Motorola V300", 176,220, 141, "MOT-V300/0B.09.2FR MIB/2.2.1 Profile/MIDP-2.0 Configuration/CLDC-1.0");
        addProfile("motorola_triplets", "Motorola Triplets", 176,220, 141, "MOT-C650/0B.09.2FR MIB/2.2.1 Profile/MIDP-2.0");
        addProfile("sagem", "Sagem myX-7", 128,160, 130, "SAGEM-myX-7/1.0");
        addProfile("skt", "SKT", 240,320, 240, "SKT-IM-8300");
        addProfile("kddi", "KDDI W21H", 240,320, 240, "KDDI-W21H");
        addProfile("lg", "LG U880", 176,220, 141, "LG-U880");
        addProfile("sony_ericsson", "Sony Ericsson K750", 176,220, 189, "SonyEricssonK750i/R1A");
    }

    private static Profile nokia(int w, int h, String display, String platform, int dpi) {
        Profile p = new Profile("nokia_"+w+"x"+h, display, w,h,dpi, platform);
        // Nokia keycodes (standard)
        p.keyMap.put(Mobile.KEY_UP, -1);
        p.keyMap.put(Mobile.KEY_DOWN, -2);
        p.keyMap.put(Mobile.KEY_LEFT, -3);
        p.keyMap.put(Mobile.KEY_RIGHT, -4);
        p.keyMap.put(Mobile.KEY_FIRE, -5);
        p.keyMap.put(Mobile.KEY_SOFT_LEFT, -6);
        p.keyMap.put(Mobile.KEY_SOFT_RIGHT, -7);
        p.keyMap.put(Mobile.KEY_STAR, 42);
        p.keyMap.put(Mobile.KEY_POUND, 35);
        for (int i=0;i<=9;i++) p.keyMap.put(Mobile.KEY_0+i, 48+i);
        return p;
    }

    private static void add(Profile p){ PROFILES.put(p.name, p); }
    private static void addProfile(String id, String display, int w,int h,int dpi,String platform){
        Profile p = new Profile(id, display, w,h,dpi,platform);
        // generic mapping
        p.keyMap.put(Mobile.KEY_UP, -1);
        p.keyMap.put(Mobile.KEY_DOWN, -2);
        p.keyMap.put(Mobile.KEY_LEFT, -3);
        p.keyMap.put(Mobile.KEY_RIGHT, -4);
        p.keyMap.put(Mobile.KEY_FIRE, -5);
        p.keyMap.put(Mobile.KEY_SOFT_LEFT, -6);
        p.keyMap.put(Mobile.KEY_SOFT_RIGHT, -7);
        PROFILES.put(id, p);
    }

    public static Map<String,Profile> getAll(){ return PROFILES; }
    public static Profile get(String id){ return PROFILES.get(id); }
    public static Profile getDefault(){ return PROFILES.get("nokia_240x320"); }
}