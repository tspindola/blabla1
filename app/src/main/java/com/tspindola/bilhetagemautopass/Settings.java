package com.tspindola.bilhetagemautopass;

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
        setContentView(R.layout.activity_settings);

        Spinner spCompany = findViewById(R.id.spinCompany);
        Spinner spRoute = findViewById(R.id.spinRoute);
        Spinner spVehicle = findViewById(R.id.spinVehicle);

        spCompany.setOnItemSelectedListener(this);
        spRoute.setOnItemSelectedListener(this);
        spVehicle.setOnItemSelectedListener(this);

        BoxStore boxStore = ((App) getApplication()).getBoxStore();
        Box<Company> companyBox = boxStore.boxFor(Company.class);
        Box<Route> routeBox = boxStore.boxFor(Route.class);
        Box<Vehicle> vehicleBox = boxStore.boxFor(Vehicle.class);

        configureSpinner(spCompany,companyBox,Company_.name);
        configureSpinner(spRoute,routeBox, Route_.description);
        configureSpinner(spVehicle,vehicleBox, Vehicle_.companyObjectId);
    }

    private void configureSpinner(Spinner s, Box b, Property p)
    {
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
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int s = ((Spinner) parent).getId();
        Log.d("Info Debug","Spinner selected: "+s);
        if(s == R.id.spinCompany) {
            String label = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(), "You selected: " + label, Toast.LENGTH_LONG).show();
        }
        else if(s == R.id.spinRoute) {
            String label = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(), "You selected: " + label, Toast.LENGTH_LONG).show();
        }
        else if(s == R.id.spinVehicle) {
            String label = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(), "You selected: " + label, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}
