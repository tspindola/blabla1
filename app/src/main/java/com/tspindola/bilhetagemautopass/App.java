package com.tspindola.bilhetagemautopass;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.tspindola.bilhetagemautopass.datamodel.*;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

public class App extends Application {

    public static final String TAG = "ObjectBoxExample";
    public static final boolean EXTERNAL_DIR = false;

    private BoxStore boxStore;

    private SharedPreferences sharedPrefAP;
    private SharedPreferences.Editor editorSP;

    @Override
    public void onCreate() {
        super.onCreate();

        //Configuring ObjectBox
        boxStore = MyObjectBox.builder().androidContext(App.this).build();
        if (BuildConfig.DEBUG) {
            new AndroidObjectBrowser(boxStore).start(this);
        }
        Log.d("Log Debug", "Using ObjectBox " + BoxStore.getVersion() + " (" + BoxStore.getVersionNative() + ")");

        //Configuring SharedPreferences
        sharedPrefAP = getSharedPreferences("autopassSharedPref",0);
        editorSP = sharedPrefAP.edit();
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPrefAP;
    }

    public SharedPreferences.Editor getSharedPreferencesEditor() {return editorSP; }
}
