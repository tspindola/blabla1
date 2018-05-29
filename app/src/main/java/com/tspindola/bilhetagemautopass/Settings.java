package com.tspindola.bilhetagemautopass;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.tspindola.bilhetagemautopass.datamodel.dbBaseFunctions;

import java.util.List;

public class Settings extends AppCompatActivity implements OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Log Debug","Settings onCreate called");

        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onResume(){
        super.onResume();

        Log.d("Log Debug","Settings onResume called");

        Spinner spCompany = findViewById(R.id.spinCompany);
        Spinner spRoute = findViewById(R.id.spinRoute);
        Spinner spVehicle = findViewById(R.id.spinVehicle);

        configureSpinner(spCompany,"Company");
        configureSpinner(spRoute,"Route");
        configureSpinner(spVehicle,"Vehicle");
    }

    private void configureSpinner(Spinner s, String id)
    {
        Log.d("Log Debug","Configuring Spinner");
        List<String> list = dbBaseFunctions.getListOfStringFromTable(id);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        //Inicializar com o valor salvo
        SharedPreferences sp = ((App) getApplication()).getSharedPreferences();
        long index = sp.getLong(id,0);
        Log.d("Log Debug","Setting spinner selection = " + index);
        s.setSelection((int)index-1, false);

        s.setOnItemSelectedListener(Settings.this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Log.d("Log Debug","Spinner item selected");

        SharedPreferences.Editor spEditor = ((App) getApplication()).getSharedPreferencesEditor();
        int s = ((Spinner) parent).getId();

        String label;

        if(s == R.id.spinCompany) {
            Log.d("Log Debug","Spinner = Company");
            label = parent.getItemAtPosition(position).toString();
            long aux = dbBaseFunctions.findCompanyByName(label).getId();
            Log.d("Log Debug","ID Salvo: "+aux);
            spEditor.putLong("Company",aux);
            spEditor.commit();
        }
        else if(s == R.id.spinRoute) {
            Log.d("Log Debug","Spinner = Route");
            label = parent.getItemAtPosition(position).toString();
            long aux = dbBaseFunctions.findRouteByName(label).getId();
            Log.d("Log Debug","ID Salvo: "+aux);
            spEditor.putLong("Route",aux);
            spEditor.commit();
        }
        else if(s == R.id.spinVehicle) {
            Log.d("Log Debug","Spinner = Vehicle");
            label = parent.getItemAtPosition(position).toString();
            long aux = dbBaseFunctions.findVehicleByName(label).getId();
            Log.d("Log Debug","ID Salvo: "+aux);
            spEditor.putLong("Vehicle",aux);
            spEditor.commit();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}
