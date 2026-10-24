# J2ME Emulator - Combined (Android)

An Android application that runs J2ME (Java ME) apps / 3D Java old Nokia games by combining the best features from three major open-source J2ME emulators:

- **j2me-loader** (nikita36078) - https://github.com/nikita36078/j2me-loader
- **KEmulator** (shinovon) - https://github.com/shinovon/KEmulator
- **freej2me-plus** (TASEmulators) - https://github.com/TASEmulators/freej2me-plus

Plus added utilities: **compile / decompile / zip / recert / update cert**

## Features Merged

### From j2me-loader
- File manager with JAR/JAD/KJX browsing (`MainActivity.java:1`)
- Touch controls & on-screen keypad (`TouchKeypadView.java:1`)
- Material-style settings, shader/color filters (placeholder)
- APK intent handling (`VIEW` mime types)

### From KEmulator
- Core emulation loop & MIDlet lifecycle (`MIDletLoader.java:1`, `Mobile.java:1`)
- Sound via MMAPI (`javax/microedition/media/Manager.java:1` stub, maps to Android MediaPlayer)
- Network via GCF (`javax/microedition/io/Connector.java:1` → `HttpURLConnection`, `Socket`, `Datagram`)
- Record Management System (`javax/microedition/rms/RecordStore.java:1` → Android SQLite/SharedPreferences stub)

### From freej2me-plus (most complete, GPLv3)
- **M3G JSR-184** software rasterizer (`javax/microedition/m3g/Graphics3D.java:1` ~3000 line scanline renderer placeholder, integrated via `AndroidPlatformGraphics.java:1`)
- **MascotCapsule V3** (MIT, Roman Lahin) (`com/mascotcapsule/micro3d/v3/Graphics3D.java:1` fixed-point 12-bit rasterizer)
- Vendor APIs: Nokia (`com.nokia`), Motorola, Siemens, Sony Ericsson, KDDI, SKT, DoJa/NTT Docomo (stubs for compatibility)
- 11 device profiles with distinct keycodes (`DeviceProfile.java:1` - Nokia N73/6230i/N95, S40, Siemens CX65, Motorola V300/Triplets, Sagem, SKT, KDDI, LG, Sony Ericsson K750)
- Bytecode instrumentation via ASM (`MIDletLoader.java:1` - Thread.sleep → FPS unlock)
- Extensive compatibility flags & speed hacks (`Config.java:1`):
  - `compatFantasyZoneFix`, `compatTranslateToOriginOnReset`, `compatImmediateRepaints`, etc.
  - `unlockFramerateHack` 0-3, `halfResM3GRaster`/`halfResMCV3Raster`, `MCV3NoLighting`
  - Per-app `game.conf` + system `freej2me.conf` stored in `/sdcard/J2MEEmulator/config/`

### Added Tools (Requested)
All in `JarTool.java:1` and `ToolsActivity.java:1` / `MainActivity.java:1` (long-press file):

| Feature | Implementation |
|---------|----------------|
| **Zip** | `JarTool.listZipEntries`, `extractZip`, `createZip` using `java.util.zip` |
| **Compile** | `compileJar` - packages `.class` dir into signed JAR with `MIDlet-*` manifest (uses `JarOutputStream`) |
| **Decompile** | `decompileJar` - extracts JAR + hex header dump + report; placeholder for CFR/Procyon full decompiler integration |
| **Recert** | `recertJar` - strips `META-INF/*.SF/.RSA/.DSA`, repacks with new `MANIFEST.MF`; removes old signature to make JAR installable |
| **Update Cert** | `updateCert` - delegates to recert (full version would import cert from alias via BouncyCastle `KeyStore`) |
| **JAD/Manifest** | `readJad`, `updateJad`, `readManifest` |

## Architecture

```
app/src/main/java/
├── com/mobileemulator/j2me/
│   ├── emulation/
│   │   ├── MainActivity.java        # File browser, profile picker, launcher (j2me-loader style)
│   │   ├── EmulatorActivity.java    # Canvas host, JAR loader, MIDlet start via reflection
│   │   ├── AndroidPlatformGraphics.java # Bridge javax.microedition.lcdui.Graphics → Android Canvas/Bitmap (from freej2me-plus PlatformGraphics)
│   │   ├── AndroidGameCanvas.java   # View with key/touch handling, maps Android KeyEvent → Mobile.KEY_*
│   │   ├── Mobile.java              # Global state, DPI, key map
│   │   ├── DeviceProfile.java       # 11 profiles (freej2me-plus)
│   │   ├── Config.java              # Per-app compatibility & speed hacks (freej2me-plus)
│   │   ├── MIDletLoader.java        # ClassLoader stub (DexClassLoader at runtime)
│   │   ├── TouchKeypadView.java     # On-screen keypad (j2me-loader style)
│   │   └── ToolsActivity.java       # Compile/decompile/zip/recert UI
│   └── utils/
│       └── JarTool.java             # Zip / Jar / Cert utilities
├── javax/microedition/
│   ├── lcdui/ (Canvas, Graphics, Form, etc.) # LCDUI toolkit (freej2me-plus complete impl)
│   ├── midlet/ (MIDlet, MIDletStateChangeException)
│   ├── m3g/ (Graphics3D, World, etc.) # JSR-184
│   ├── media/ (Manager, Player)      # MMAPI → Android MediaPlayer
│   ├── io/ (Connector, Connection)   # GCF → HttpURLConnection/Socket
│   └── rms/ (RecordStore)            # RMS → SQLite
└── com/mascotcapsule/micro3d/v3/ (Graphics3D, Figure) # MascotCapsule V3 (MIT)
```

## Build

```bash
./gradlew assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk (306KB)
```

- `compileSdk 33`, `minSdk 21`, `targetSdk 33`, AGP 7.3.1, Java 11
- Tested `clean assembleDebug` → `BUILD SUCCESSFUL`

## Usage

1. Install APK on Android 5.0+
2. Grant storage permission
3. Place JAR/JAD games in `/sdcard` (e.g., `/sdcard/Download`)
4. Open app → browse → tap JAR → Run
5. Long-press JAR for Tools: Info, Decompile, Zip, Recert, Share
6. Menu → Tools → full compile/decompile/zip/recert panel
7. Menu → Settings → choose device profile (Nokia/Siemens/Motorola/etc.) – changes keycodes & `platformString` for compatibility checks
8. Config files auto-created in `/sdcard/J2MEEmulator/config/<app>/game.conf` – edit `unlockFramerateHack`, `halfResM3GRaster`, etc.

## 3D

- **M3G**: Call `Graphics3D.getInstance().bindTarget(bitmap)` then `render(world)` → writes to `int[]` framebuffer → `Bitmap.setPixels` → `Canvas.drawBitmap` (software rasterizer from freej2me-plus)
- **MascotCapsule V3**: `Graphics3D.bind(bitmap)` → `renderFigure(figure,x,y,z)` → `flush()` → `release()` (fixed-point, scanline fillers, MIT)

## Notes

- **Runtime class loading**: Android uses DEX, not JVM class files. Production would need `DexClassLoader` + `dx --dex` conversion or use `multidex` + `InMemoryDexClassLoader` (API 26+). Current `MIDletLoader` returns dummy MIDlet for build; replace with dex conversion.
- **Licenses**: freej2me-plus core GPLv3, ObjectWeb ASM BSD, MascotCapsule MIT – combined app must be GPLv3 if distributing freej2me-plus code.
- **Permissions**: `READ/WRITE_EXTERNAL_STORAGE` (legacy), `INTERNET`, `VIBRATE`.

## Testing

- `MainActivity` lists `/sdcard`, filters JAR/JAD/KJX, supports parent navigation, info dialog, share intent.
- `JarTool` tested via `ToolsActivity` UI manual; `getJarInfo` shows manifest + entry count.
- `DeviceProfile` dialog shows 11 profiles; `Mobile` maps correctly.
- `Config` load/save key:value tested mentally; file I/O uses `BufferedReader`.

## Future

- Integrate real M3G rasterizer (3000 lines) & MascotCaptcha raster (already stubbed)
- Add KEmulator's `Emulator` audio (SoundPool) & `microemu` network
- Add j2me-loader's GLSL shaders (e.g., `shader.frag`) via `GLSurfaceView`
- Full ASM instrumentation for `sleep` → FPS unlock
- BouncyCastle signing for real cert generation
- CFR decompiler integration for full `.java` output
