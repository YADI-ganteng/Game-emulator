
package com.retroengine.app;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    
    static {
        try {
            System.loadLibrary("nestopia");
            System.loadLibrary("mgba");
            System.loadLibrary("gambatte");
            System.loadLibrary("snes9x");
            System.loadLibrary("genesis_plus_gx");
            System.loadLibrary("pcsx_rearmed");
        } catch (UnsatisfiedLinkError e) {
            // Core tidak ditemukan
        }
    }
    
    private native int retroLoadRom(String path, String core);
    private native int retroRun();
    private native void retroStop();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        String filePath = getIntent().getStringExtra("file_path");
        String fileExt = getIntent().getStringExtra("file_ext");
        
        String core = getCoreForExt(fileExt);
        
        if (core == null) {
            Toast.makeText(this, "Format tidak didukung: " + fileExt, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        Toast.makeText(this, "Loading: " + core + " core...", Toast.LENGTH_SHORT).show();
        
        int result = retroLoadRom(filePath, core);
        
        if (result == 0) {
            Toast.makeText(this, "ROM loaded!", Toast.LENGTH_SHORT).show();
            retroRun();
        } else {
            Toast.makeText(this, "Gagal load ROM", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    private String getCoreForExt(String ext) {
        switch (ext) {
            case "nes": return "nestopia";
            case "gba": return "mgba";
            case "gb": case "gbc": return "gambatte";
            case "snes": case "smc": return "snes9x";
            case "md": case "gen": return "genesis_plus_gx";
            case "psx": case "bin": return "pcsx_rearmed";
            default: return null;
        }
    }
    
    @Override
    protected void onDestroy() {
        retroStop();
        super.onDestroy();
    }
}
