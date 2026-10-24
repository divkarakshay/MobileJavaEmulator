package com.mobileemulator.j2me.emulation;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;
import android.view.ViewGroup;
import java.io.File;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import javax.microedition.midlet.MIDlet;

public class EmulatorActivity extends Activity {
    public static EmulatorActivity instance;
    private AndroidGameCanvas gameCanvas;
    private J2MEView j2meView;
    private LinearLayout contentLayout;
    private EditText filePathField;
    private Button browseButton;
    private Button runButton;
    private MIDlet currentMIDlet;
    private String currentJARPath;
    private String currentJADPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setTitle("J2ME Emulator");

        contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(contentLayout);

        filePathField = new EditText(this);
        filePathField.setHint("Enter JAR file path");
        contentLayout.addView(filePathField, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout btnRow = new LinearLayout(this);
        btnRow.setOrientation(LinearLayout.HORIZONTAL);
        browseButton = new Button(this);
        browseButton.setText("Browse");
        runButton = new Button(this);
        runButton.setText("Run");
        btnRow.addView(browseButton);
        btnRow.addView(runButton);
        contentLayout.addView(btnRow);

        browseButton.setOnClickListener(v -> browseJAR());
        runButton.setOnClickListener(v -> runMIDlet());

        int canvasWidth = 240;
        int canvasHeight = 320;
        // Dummy MIDlet for canvas creation; will be replaced when loading real MIDlet
        gameCanvas = new AndroidGameCanvas(this, null, canvasWidth, canvasHeight);
        contentLayout.addView(gameCanvas, new LinearLayout.LayoutParams(canvasWidth, canvasHeight));
        // J2ME Display view for actual game Canvas (from Display.setCurrent)
        j2meView = new J2MEView(this);
        contentLayout.addView(j2meView, new LinearLayout.LayoutParams(canvasWidth, canvasHeight));

        Intent intent = getIntent();
        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();
            String scheme = data.getScheme();
            if (scheme != null && (scheme.equals("content") || scheme.equals("file"))) {
                String path = data.getPath();
                if (path != null && (path.endsWith(".jar") || path.endsWith(".jad"))) {
                    filePathField.setText(Uri.decode(path));
                    runMIDlet();
                }
            }
        }
    }

    private void browseJAR() {
        String path = filePathField.getText().toString();
        if (path.isEmpty()) {
            Toast.makeText(this, "Enter a file path", Toast.LENGTH_SHORT).show();
            return;
        }
        currentJARPath = path;
        currentJADPath = path.replace(".jar", ".jad");
        runMIDlet();
    }

    private byte[] readFileBytes(File f) throws Exception {
        FileInputStream fis = new FileInputStream(f);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int n;
        while ((n = fis.read(buf)) != -1) bos.write(buf, 0, n);
        fis.close();
        return bos.toByteArray();
    }

    private void runMIDlet() {
        String path = filePathField.getText().toString();
        if (path.isEmpty()) {
            Toast.makeText(this, "Enter a JAR file path", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            byte[] jadBytes = null;
            if (currentJADPath != null) {
                File jadFile = new File(currentJADPath);
                if (jadFile.exists()) {
                    jadBytes = readFileBytes(jadFile);
                }
            }
            File jarFile = new File(path);
            if (!jarFile.exists()) {
                Toast.makeText(this, "JAR not found: " + path, Toast.LENGTH_SHORT).show();
                return;
            }
            byte[] jarBytes = readFileBytes(jarFile);
            MIDletLoader loader = new MIDletLoader(getClassLoader(), this);
            currentMIDlet = loader.loadMIDlet(path, jarBytes, jadBytes);
            // Use reflection to call protected startApp
            try {
                java.lang.reflect.Method m = MIDlet.class.getDeclaredMethod("startApp");
                m.setAccessible(true);
                m.invoke(currentMIDlet);
                android.util.Log.i("EmulatorActivity","startApp succeeded for "+currentMIDlet.getClass().getName());
                // Try to get Display current
                try {
                    javax.microedition.lcdui.Display d = javax.microedition.lcdui.Display.getDisplay(currentMIDlet);
                    android.util.Log.i("EmulatorActivity","Display current after startApp: "+d.getCurrent());
                } catch (Exception e2) {}
            } catch (Exception e) {
                android.util.Log.e("EmulatorActivity","startApp failed",e);
                Toast.makeText(this, "startApp failed: " + e.getCause(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            setCurrentMIDlet(currentMIDlet);
            Toast.makeText(this, "Loaded: " + path+" class "+currentMIDlet.getClass().getName(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public AndroidGameCanvas getGameCanvas() { return gameCanvas; }
    public void setCurrentMIDlet(MIDlet midi) { this.currentMIDlet = midi; }
    public MIDlet getCurrentMIDlet() { return currentMIDlet; }

    public void showDisplayable(javax.microedition.lcdui.Displayable d) {
        if (j2meView != null) {
            j2meView.setDisplayable(d);
            j2meView.invalidate();
            // Make J2ME view visible, hide dummy
            j2meView.setVisibility(android.view.View.VISIBLE);
            gameCanvas.setVisibility(android.view.View.GONE);
            android.util.Log.i("EmulatorActivity","showDisplayable: "+d.getClass().getName());
        }
    }
}