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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.tspindola.bilhetagemautopass.datamodel.Company;
import com.tspindola.bilhetagemautopass.datamodel.Route;
import com.tspindola.bilhetagemautopass.datamodel.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity implements OnItemSelectedListener {
    private static DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance().getReference();

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

        startConfiguringSpinner(spCompany,"Company");
        startConfiguringSpinner(spRoute,"Route");
        startConfiguringSpinner(spVehicle,"Vehicle");
    }

    public void startConfiguringSpinner(final Spinner spinner,final String tablename){
        Log.i("Firebase","getListOfStringFromTable called for table "+tablename);
        DatabaseReference tmp= database.child(tablename);
        tmp.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot){
                        List<String> datalist = new ArrayList();
                        for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                            String s = null;
                            switch (tablename){
                                case "Company":
                                    s = areaSnapshot.getValue(Company.class).getName();
                                    break;
                                case "Route":
                                    s = areaSnapshot.getValue(Route.class).getDescription();
                                    break;
                                case "Vehicle":
                                    s = areaSnapshot.getValue(Vehicle.class).getCompanyObjectId();
                                    break;
                            }
                            datalist.add(s);
                        }
                        configureSpinner(spinner,tablename,datalist);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                }
        );
    }

    public void configureSpinner(Spinner s, String id,List<String> list){
        Log.d("Log Debug","Configuring Spinner for "+id);
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

    public void findByIdAndUpdateSharedPref(final String tablename, String id,
                                            final SharedPreferences.Editor spEditor){
        Log.i("Firebase","findById called for tablename = "+tablename);
        database.child(tablename)
                .orderByChild("id")
                .equalTo(id)
                .limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long id = 0;
                        switch (tablename){
                            case "Company":
                                Company c = dataSnapshot.getValue(Company.class);
                                if(null != c)
                                    id = c.getId();
                                break;
                            case "Vehicle":
                                Vehicle v = dataSnapshot.getValue(Vehicle.class);
                                if(null != v)
                                    id = v.getId();
                                break;
                            case "Route":
                                Route r = dataSnapshot.getValue(Route.class);
                                if(null != r)
                                    id = r.getId();
                                break;
                        }
                        updateSharedPreferences(spEditor,tablename,id);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });
    }

    public long getIdFromObject(Object o, String tablename){
        long id=0;
        switch(tablename){
            case "Company":
                Company c = (Company)o;
                id = c.getId();
                break;
            case "Vehicle":
                Vehicle v = (Vehicle)o;
                id = v.getId();
                break;
            case "Route":
                Route r = (Route)o;
                id = r.getId();
                break;
        }
        return id;
    }

    public void updateSharedPreferences(SharedPreferences.Editor spEditor,String tablename,long id){
        Log.d("Log Debug","ID Salvo: "+id);
        spEditor.putLong(tablename,id);
        spEditor.commit();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Log.d("Log Debug","Spinner item selected");

        SharedPreferences.Editor spEditor = ((App) getApplication()).getSharedPreferencesEditor();
        int s = parent.getId();

        String label;

        if(s == R.id.spinCompany) {
            Log.d("Log Debug","Spinner = Company");
            label = parent.getItemAtPosition(position).toString();
            findByIdAndUpdateSharedPref("Company", Integer.toString(position),spEditor);
        }
        else if(s == R.id.spinRoute) {
            Log.d("Log Debug","Spinner = Route");
            label = parent.getItemAtPosition(position).toString();
            findByIdAndUpdateSharedPref("Route",Integer.toString(position),spEditor);
        }
        else if(s == R.id.spinVehicle) {
            Log.d("Log Debug","Spinner = Vehicle");
            label = parent.getItemAtPosition(position).toString();
            findByIdAndUpdateSharedPref("Vehicle",Integer.toString(position),spEditor);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}
