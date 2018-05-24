package com.tspindola.bilhetagemautopass;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.tspindola.bilhetagemautopass.datamodel.dbBaseFunctions;

public class App extends Application {

    private SharedPreferences sharedPrefAP;
    private SharedPreferences.Editor editorSP;

    @Override
    public void onCreate() {
        super.onCreate();

        //Configuring database
        dbBaseFunctions.start();

        //Configuring SharedPreferences
        sharedPrefAP = getSharedPreferences("autopassSharedPref",0);
        editorSP = sharedPrefAP.edit();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPrefAP;
    }

    public SharedPreferences.Editor getSharedPreferencesEditor() {return editorSP; }
}
