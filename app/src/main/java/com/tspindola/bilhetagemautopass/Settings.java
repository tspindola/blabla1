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
import android.widget.Toast;

import com.tspindola.bilhetagemautopass.datamodel.Company;
import com.tspindola.bilhetagemautopass.datamodel.Company_;
import com.tspindola.bilhetagemautopass.datamodel.Route;
import com.tspindola.bilhetagemautopass.datamodel.Route_;
import com.tspindola.bilhetagemautopass.datamodel.Vehicle;
import com.tspindola.bilhetagemautopass.datamodel.Vehicle_;

import java.util.Arrays;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.Property;

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

        BoxStore boxStore = ((App) getApplication()).getBoxStore();
        Box<Company> companyBox = boxStore.boxFor(Company.class);
        Box<Route> routeBox = boxStore.boxFor(Route.class);
        Box<Vehicle> vehicleBox = boxStore.boxFor(Vehicle.class);

        configureSpinner(spCompany,companyBox,Company_.name,"Company");
        configureSpinner(spRoute,routeBox, Route_.description,"Route");
        configureSpinner(spVehicle,vehicleBox, Vehicle_.companyObjectId,"Vehicle");
    }

    private void configureSpinner(Spinner s, Box b, Property p, String id)
    {
        Log.d("Log Debug","Configuring Spinner");
        if(b.count()==0)
        {
            Toast.makeText(this,R.string.emptyDb,Toast.LENGTH_SHORT);
        }
        else
        {
            List<String> list = Arrays.asList(b.query().build()
                    .property(p).nullValue("unknown").findStrings());
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
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Log.d("Log Debug","Spinner item selected");

        SharedPreferences.Editor spEditor = ((App) getApplication()).getSharedPreferencesEditor();
        int s = ((Spinner) parent).getId();

        BoxStore boxStore = ((App) getApplication()).getBoxStore();
        Box<Company> companyBox = boxStore.boxFor(Company.class);
        Box<Route> routeBox = boxStore.boxFor(Route.class);
        Box<Vehicle> vehicleBox = boxStore.boxFor(Vehicle.class);

        String label;

        if(s == R.id.spinCompany) {
            Log.d("Log Debug","Spinner = Company");
            label = parent.getItemAtPosition(position).toString();
            long aux = getObjectBoxID(companyBox, Company_.name,label);
            Log.d("Log Debug","ID Salvo: "+aux);
            spEditor.putLong("Company",aux);
            spEditor.commit();
        }
        else if(s == R.id.spinRoute) {
            Log.d("Log Debug","Spinner = Route");
            label = parent.getItemAtPosition(position).toString();
            long aux = getObjectBoxID(routeBox, Route_.description,label);
            Log.d("Log Debug","ID Salvo: "+aux);
            spEditor.putLong("Route",aux);
            spEditor.commit();
        }
        else if(s == R.id.spinVehicle) {
            Log.d("Log Debug","Spinner = Vehicle");
            label = parent.getItemAtPosition(position).toString();
            long aux = getObjectBoxID(vehicleBox, Vehicle_.companyObjectId,label);
            Log.d("Log Debug","ID Salvo: "+aux);
            spEditor.putLong("Vehicle",aux);
            spEditor.commit();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public long getObjectBoxID(Box b, Property p, String name){
        return b.query().equal(p,name).build().findIds()[0];
    }
}
