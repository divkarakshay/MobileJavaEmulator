package com.mobileemulator.j2me.emulation;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import java.io.File;
import java.util.*;

import com.mobileemulator.j2me.utils.JarTool;

public class MainActivity extends Activity {
    private LinearLayout root;
    private ListView listView;
    private EditText pathField;
    private ArrayAdapter<String> adapter;
    private List<File> currentFiles = new ArrayList<>();
    private File currentDir;

    @Override protected void onCreate(Bundle b){
        super.onCreate(b);
        setTitle("J2ME Loader - Combined");
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        setContentView(root);

        TextView title = new TextView(this);
        title.setText("J2ME Emulator (j2me-loader + KEmulator + freej2me-plus)");
        title.setPadding(16,16,16,8);
        root.addView(title);

        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setOrientation(LinearLayout.HORIZONTAL);
        Button btnUp = new Button(this); btnUp.setText("↑");
        Button btnTools = new Button(this); btnTools.setText("Tools");
        Button btnSettings = new Button(this); btnSettings.setText("Settings / Profiles");
        Button btnAbout = new Button(this); btnAbout.setText("About");
        toolbar.addView(btnUp);
        toolbar.addView(btnTools);
        toolbar.addView(btnSettings);
        toolbar.addView(btnAbout);
        root.addView(toolbar);

        pathField = new EditText(this);
        pathField.setHint("/sdcard - tap to edit path");
        root.addView(pathField, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        listView = new ListView(this);
        root.addView(listView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        btnUp.setOnClickListener(v->{
            if(currentDir!=null && currentDir.getParentFile()!=null) loadDir(currentDir.getParentFile());
        });
        btnTools.setOnClickListener(v-> startActivity(new Intent(this, ToolsActivity.class)));
        btnSettings.setOnClickListener(v-> showProfilesDialog());
        btnAbout.setOnClickListener(v-> showAbout());

        listView.setOnItemClickListener((p,view,pos,id)->{
            File f = currentFiles.get(pos);
            if(f.isDirectory()) loadDir(f);
            else if(f.getName().endsWith(".jar")||f.getName().endsWith(".jad")||f.getName().endsWith(".kjx")){
                Intent i = new Intent(this, EmulatorActivity.class);
                i.setData(Uri.fromFile(f));
                // Pass path via extra as well
                i.putExtra("jarPath", f.getAbsolutePath());
                startActivity(i);
            } else {
                Toast.makeText(this, "File: "+f.getName()+"\nSize: "+f.length()+"\nUse Tools for zip/cert", Toast.LENGTH_LONG).show();
            }
        });
        listView.setOnItemLongClickListener((p,view,pos,id)->{
            File f = currentFiles.get(pos);
            showFileOps(f);
            return true;
        });

        // permission check for Android 11+
        if (android.os.Build.VERSION.SDK_INT >= 30 && !Environment.isExternalStorageManager()) {
            try {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                Toast.makeText(this, "Grant All files access for file browser, then restart", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        }
        // request legacy perms for <30
        if (android.os.Build.VERSION.SDK_INT < 30) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        // default dir: try external, else private
        File ext = Environment.getExternalStorageDirectory();
        // also try app private external files as fallback
        File privateExt = getExternalFilesDir(null);
        if (ext.exists() && ext.listFiles() != null) currentDir = ext;
        else if (privateExt != null && privateExt.exists()) currentDir = privateExt;
        else currentDir = new File("/");

        // ensure private Download exists for demo
        if (privateExt != null) new File(privateExt, "Download").mkdirs();

        pathField.setText(currentDir.getAbsolutePath());
        pathField.setOnEditorActionListener((v,a,e)->{ loadDir(new File(pathField.getText().toString())); return true; });
        loadDir(currentDir);
    }

    private void loadDir(File dir){
        if(!dir.exists() || !dir.isDirectory()){ Toast.makeText(this,"Not a dir: "+dir,Toast.LENGTH_SHORT).show(); return; }
        currentDir = dir;
        pathField.setText(dir.getAbsolutePath());
        File[] files = dir.listFiles();
        if (files == null) {
            // Scoped storage fallback - try private equivalent or private root
            Toast.makeText(this,"No permission for "+dir+" - Grant All files access in Settings. Showing private dir", Toast.LENGTH_LONG).show();
            File fallback = null;
            try {
                File extRoot = Environment.getExternalStorageDirectory();
                File privateRoot = getExternalFilesDir(null);
                if (privateRoot != null && dir.getAbsolutePath().startsWith(extRoot.getAbsolutePath())) {
                    String rel = dir.getAbsolutePath().substring(extRoot.getAbsolutePath().length());
                    fallback = new File(privateRoot, rel);
                    if (!fallback.exists()) fallback = privateRoot;
                    files = fallback.listFiles();
                }
                if (files == null && privateRoot != null) {
                    files = privateRoot.listFiles();
                    fallback = privateRoot;
                }
                if (fallback != null) {
                    currentDir = fallback;
                    pathField.setText(fallback.getAbsolutePath());
                }
            } catch (Exception e) { e.printStackTrace(); }
            if (files == null) files = new File[0];
        }
        currentFiles.clear();
        List<String> names = new ArrayList<>();
        if(files!=null){
            Arrays.sort(files, (a,b)->{
                if(a.isDirectory() && !b.isDirectory()) return -1;
                if(!a.isDirectory() && b.isDirectory()) return 1;
                return a.getName().compareToIgnoreCase(b.getName());
            });
            for(File f: files){
                // filter hidden
                if(f.getName().startsWith(".")) continue;
                currentFiles.add(f);
                String prefix = f.isDirectory()? "[DIR] " : "";
                String suffix = "";
                if(f.getName().endsWith(".jar")) suffix=" (J2ME)";
                else if(f.getName().endsWith(".zip")) suffix=" (ZIP)";
                names.add(prefix+f.getName()+suffix);
            }
        }
        if(adapter==null){
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
            listView.setAdapter(adapter);
        } else { adapter.clear(); adapter.addAll(names); adapter.notifyDataSetChanged(); }
    }

    private void showFileOps(File f){
        String[] ops = {"Info","Decompile / Extract","Zip / Compile","Recert / Update Cert","Delete","Share"};
        new android.app.AlertDialog.Builder(this).setTitle(f.getName()).setItems(ops,(d,which)->{
            try{
                switch(which){
                    case 0: Toast.makeText(this, JarTool.getJarInfo(f), Toast.LENGTH_LONG).show(); break;
                    case 1: {
                        File out = new File(f.getParentFile(), f.getName()+"_decompiled");
                        out.mkdirs();
                        File report = JarTool.decompileJar(f,out);
                        Toast.makeText(this,"Decompiled to "+out+" report "+report,Toast.LENGTH_LONG).show();
                        break;
                    }
                    case 2: {
                        File outZip = new File(f.getParentFile(), f.getName()+".zip");
                        JarTool.createZip(f.isDirectory()?f:f.getParentFile(), outZip);
                        Toast.makeText(this,"Zipped to "+outZip,Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 3: {
                        File out = new File(f.getParentFile(), f.getName().replace(".jar","_recert.jar"));
                        JarTool.recertJar(f,out);
                        Toast.makeText(this,"Recerted to "+out,Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 4: if(f.delete()){ loadDir(currentDir); Toast.makeText(this,"Deleted",Toast.LENGTH_SHORT).show(); } break;
                    case 5: {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("*/*");
                        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
                        startActivity(Intent.createChooser(share,"Share "+f.getName()));
                        break;
                    }
                }
            } catch(Exception e){ Toast.makeText(this,"Error: "+e.getMessage(),Toast.LENGTH_LONG).show(); e.printStackTrace(); }
        }).show();
    }

    private void showProfilesDialog(){
        Map<String, DeviceProfile.Profile> map = DeviceProfile.getAll();
        String[] names = map.values().stream().map(p->p.displayName+" ("+p.width+"x"+p.height+")").toArray(String[]::new);
        String[] keys = map.keySet().toArray(new String[0]);
        new android.app.AlertDialog.Builder(this).setTitle("Device Profiles (from freej2me-plus) ").setItems(names,(d,i)->{
            DeviceProfile.Profile p = DeviceProfile.get(keys[i]);
            Mobile.getInstance().setDeviceProfile(p.name);
            Toast.makeText(this,"Selected: "+p.displayName+"\nPlatform: "+p.platformString,Toast.LENGTH_LONG).show();
        }).show();
    }

    private void showAbout(){
        String msg = "J2ME Emulator - Combined\n\nFeatures merged:\n• j2me-loader: File manager, touch controls, shader, settings\n• KEmulator: Core emulation, sound, network\n• freej2me-plus: M3G (JSR-184) + MascotCapsule V3 3D, vendor APIs (Nokia/Motorola/Siemens/SE/KDDI/SKT/DoJa), FPS unlock, compatibility hacks\n\nAdded tools: compile, decompile (zip), recert/update cert\n\n3D: M3G software rasterizer + MascotCapsule V3 (MIT)\n";
        new android.app.AlertDialog.Builder(this).setTitle("About").setMessage(msg).setPositiveButton("OK",null).show();
    }
}