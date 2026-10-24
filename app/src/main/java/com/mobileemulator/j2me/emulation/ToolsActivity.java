package com.mobileemulator.j2me.emulation;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.content.Intent;
import android.net.Uri;
import java.io.File;
import com.mobileemulator.j2me.utils.JarTool;

public class ToolsActivity extends Activity {
    private EditText inputPath, outputPath, mainClassField, midletNameField;
    private TextView logView;

    @Override protected void onCreate(Bundle b){
        super.onCreate(b);
        setTitle("Tools - Compile / Decompile / Zip / Recert");
        ScrollView sv = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(16,16,16,16);
        sv.addView(root);
        setContentView(sv);

        inputPath = new EditText(this); inputPath.setHint("Input JAR / ZIP / Dir path (e.g. /sdcard/game.jar)");
        outputPath = new EditText(this); outputPath.setHint("Output path (e.g. /sdcard/out.jar or /sdcard/outDir)");
        mainClassField = new EditText(this); mainClassField.setHint("Main class (for compile, e.g. com.example.Main)");
        midletNameField = new EditText(this); midletNameField.setHint("MIDlet name (for compile)");
        logView = new TextView(this); logView.setText("Log:\n");
        logView.setPadding(8,8,8,8);

        root.addView(inputPath);
        root.addView(outputPath);
        root.addView(mainClassField);
        root.addView(midletNameField);

        String[] labels = {"List ZIP entries","Extract ZIP","Create ZIP","Compile JAR (from classes dir)","Decompile JAR","Recert JAR (remove sig)","Update Cert","Get JAR Info"};
        for(String label: labels){
            Button btn = new Button(this); btn.setText(label);
            btn.setOnClickListener(v-> handle(label));
            root.addView(btn);
        }
        root.addView(logView);
    }

    private void handle(String op){
        String in = inputPath.getText().toString().trim();
        String out = outputPath.getText().toString().trim();
        if(in.isEmpty()){ log("Input path required"); return; }
        File inFile = new File(in);
        File outFile = out.isEmpty()? null : new File(out);
        try{
            switch(op){
                case "List ZIP entries":
                    log("Entries in "+inFile.getName()+":\n"+String.join("\n", JarTool.listZipEntries(inFile)));
                    break;
                case "Extract ZIP":
                    if(outFile==null) outFile = new File(inFile.getParentFile(), inFile.getName()+"_extracted");
                    outFile.mkdirs();
                    JarTool.extractZip(inFile, outFile);
                    log("Extracted to "+outFile);
                    break;
                case "Create ZIP":
                    if(outFile==null) outFile = new File(inFile.getParentFile(), inFile.getName()+".zip");
                    JarTool.createZip(inFile, outFile);
                    log("Zipped "+inFile+" -> "+outFile+" ("+outFile.length()+" bytes)");
                    break;
                case "Compile JAR (from classes dir)":
                    if(!inFile.isDirectory()){ log("Input must be directory with .class files"); break; }
                    if(outFile==null) outFile = new File(inFile.getParentFile(), "compiled.jar");
                    String main = mainClassField.getText().toString().trim();
                    String name = midletNameField.getText().toString().trim();
                    if(main.isEmpty()) main = "Main";
                    if(name.isEmpty()) name = "CompiledMidlet";
                    JarTool.compileJar(inFile, outFile, main, name);
                    log("Compiled JAR: "+outFile+" main="+main);
                    break;
                case "Decompile JAR":
                    if(outFile==null) outFile = new File(inFile.getParentFile(), inFile.getName()+"_decompiled");
                    outFile.mkdirs();
                    File report = JarTool.decompileJar(inFile, outFile);
                    log("Decompiled to "+outFile+"\nReport: "+report.getAbsolutePath()+"\n"+read(report));
                    break;
                case "Recert JAR (remove sig)":
                    if(outFile==null) outFile = new File(inFile.getParentFile(), inFile.getName().replace(".jar","_recert.jar"));
                    JarTool.recertJar(inFile, outFile);
                    log("Recert done: "+outFile+" (old sig removed, repacked)");
                    break;
                case "Update Cert":
                    if(outFile==null) outFile = new File(inFile.getParentFile(), inFile.getName().replace(".jar","_updated.jar"));
                    JarTool.updateCert(inFile, outFile, "newcert");
                    log("Update cert done: "+outFile);
                    break;
                case "Get JAR Info":
                    log(JarTool.getJarInfo(inFile));
                    break;
            }
        } catch(Exception e){ log("Error: "+e.getMessage()); e.printStackTrace(); }
    }

    private String read(File f){
        try{ byte[] b = new byte[(int)Math.min(f.length(), 4000)]; try(java.io.FileInputStream fis=new java.io.FileInputStream(f)){ fis.read(b);} return new String(b);} catch(Exception e){ return e.getMessage(); }
    }
    private void log(String s){ logView.append("\n"+s+"\n"); android.util.Log.d("Tools",s); }
}