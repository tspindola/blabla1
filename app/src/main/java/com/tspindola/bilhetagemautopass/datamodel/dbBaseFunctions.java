package com.tspindola.bilhetagemautopass.datamodel;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class dbBaseFunctions {

    private static DatabaseReference database;
    private static Company companyHolder;
    private static Card cardHolder;
    private static Vehicle vehicleHolder;
    private static Route routeHolder;
    private static Fee feeHolder;
    private static Person personHolder;
    private static List<String> listHolder;
    private static boolean isDataLoadingFinished = false;

    public static void start(Context context)
    {
        Log.i("Firebase","Starting firebase");
        FirebaseApp.initializeApp(context);
        Log.i("Firebase","Firebase initialized on context "+context.getPackageName());
    }

    public static void addCard(long id, String uid, String type)
    {
        database = FirebaseDatabase.getInstance().getReference();
        Log.i("Firebase","Adding card");
        if(null != database) {
            Card c = new Card(id, uid, type);
            database.child("Card").child(Long.toString(id)).setValue(c);
        }
        Log.i("Firebase","Card "+uid+" added");
    }

    public static void addCompany(long id,String name)
    {
        database = FirebaseDatabase.getInstance().getReference();
        Log.i("Firebase","Adding company");
        if(null != database){
            Company c = new Company(id,name);
            database.child("Company").child(Long.toString(id)).setValue(c);
        }
        Log.i("Firebase","Company "+name+" added");
    }

    public static void addFee(long id,double value)
    {
        database = FirebaseDatabase.getInstance().getReference();
        Log.i("Firebase","Adding fee");
        if(null != database){
            Fee f = new Fee(id,value);
            database.child("Fee").child(Long.toString(id)).setValue(f);
        }
        Log.i("Firebase","Fee "+id+" added");
    }

    public static void addPerson(long id,String name, String cpf, String phone, String address, String email)
    {
        database = FirebaseDatabase.getInstance().getReference();
        Log.i("Firebase","Adding person");
        if(null != database){
            Person p = new Person(id,name,cpf,phone,address,email);
            database.child("Person").child(Long.toString(id)).setValue(p);
        }
        Log.i("Firebase","Person "+name+" added");
    }

    public static void addRoute(long id, String description, double length, int capacity, long companyID,
                         long feeID)
    {
        database = FirebaseDatabase.getInstance().getReference();
        Log.i("Firebase","Adding Route");
        if(null != database){
            Route r = new Route(id,description,length,capacity,companyID,feeID);
            database.child("Route").child(Long.toString(id)).setValue(r);
        }
        Log.i("Firebase","Route "+description+" added");
    }

    public static void addVehicle(long id, String type, String companyObjectId, int manufactureYear,
                           long companyID)
    {
        database = FirebaseDatabase.getInstance().getReference();
        Log.i("Firebase","Adding Vehicle");
        if(null != database){
            Vehicle v = new Vehicle(id,type,companyObjectId,manufactureYear,companyID);
            database.child("Vehicle").child(Long.toString(id)).setValue(v);
        }
        Log.i("Firebase","Vehicle "+type+ ": "+ companyObjectId +" added");
    }


    //TODO: Agrupar todos os finds
    public static Company findCompanyById(long id) {
        database = FirebaseDatabase.getInstance().getReference();
        Log.i("Firebase","findCompanyById called");
        companyHolder = null;
        database.child("Company")
                .orderByChild("id")
                .equalTo(id)
                .limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        companyHolder = dataSnapshot.getValue(Company.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });
        return companyHolder;
    }

    public static Vehicle findVehicleById(long id) {
        database = FirebaseDatabase.getInstance().getReference();
        Log.i("Firebase","findVehicleById called");
        vehicleHolder = null;
        database.child("Vehicle")
                .orderByChild("id")
                .equalTo(id)
                .limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        vehicleHolder = dataSnapshot.getValue(Vehicle.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });
        return vehicleHolder;
    }

    public static Route findRouteById(long id) {
        database = FirebaseDatabase.getInstance().getReference();
        Log.i("Firebase","findRouteById called");
        routeHolder = null;
        database.child("Route")
                .orderByChild("id")
                .equalTo(id)
                .limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        routeHolder = dataSnapshot.getValue(Route.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });
        return routeHolder;
    }

    public static Fee findFeeById(long id) {
        database = FirebaseDatabase.getInstance().getReference();
        Log.i("Firebase","findFeeById called");
        feeHolder = null;
        database.child("Fee")
                .orderByChild("id")
                .equalTo(id)
                .limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        feeHolder = dataSnapshot.getValue(Fee.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });
        return feeHolder;
    }

    public static Person findPersonById(long id) {
        database = FirebaseDatabase.getInstance().getReference();
        Log.i("Firebase","findPersonById called");
        personHolder = null;
        database.child("Person")
                .orderByChild("id")
                .equalTo(id)
                .limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        personHolder = dataSnapshot.getValue(Person.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });
        return personHolder;
    }


    public static Card findCardByUid(String s) {
        database = FirebaseDatabase.getInstance().getReference();
        Log.i("Firebase","findCardByUid called");
        cardHolder = null;
        database.child("Card")
                .orderByChild("uid")
                .equalTo(s)
                .limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        cardHolder = dataSnapshot.getValue(Card.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });
        return cardHolder;
    }

    public static List<String> getListOfStringFromTable(final String tablename){
        database = FirebaseDatabase.getInstance().getReference();
        Log.i("Firebase","getListOfStringFromTable called for table "+tablename);
        DatabaseReference tmp= database.child(tablename);
        tmp.addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i("Firebase","onDataChange called with childrenCount = "
                                                                +dataSnapshot.getChildrenCount());
                    listHolder = new ArrayList<String>();
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
                        listHolder.add(s);
                    }
                    isDataLoadingFinished = true;
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            }
        );
        while(!isTaskFinished()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                }
            }, 100);
        }
        Log.i("Firebase","getListOfStringFromTable returned "+listHolder.size()+" objects.");
        return listHolder;
    }

    public static void updateCreditsFromCard(long id, double newValue) {
        database = FirebaseDatabase.getInstance().getReference();
        Log.i("Firebase","updateCreditsFromCard called");
        database.child("Card").child(Long.toString(id)).child("credits").setValue(newValue);
    }

    public static Company findCompanyByName(String name) {
        database = FirebaseDatabase.getInstance().getReference();
        Log.i("Firebase","findCompanyByName called");
        companyHolder = null;
        database.child("Company")
                .orderByChild("name")
                .equalTo(name)
                .limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        companyHolder = dataSnapshot.getValue(Company.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });
        return companyHolder;
    }

    public static Vehicle findVehicleByName(String name) {
        database = FirebaseDatabase.getInstance().getReference();
        Log.i("Firebase","findVehicleByName called");
        vehicleHolder = null;
        database.child("Vehicle")
                .orderByChild("companyObjectId")
                .equalTo(name)
                .limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        vehicleHolder = dataSnapshot.getValue(Vehicle.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });
        return vehicleHolder;
    }

    public static Route findRouteByName(String name) {
        database = FirebaseDatabase.getInstance().getReference();
        Log.i("Firebase","findRouteByName called");
        routeHolder = null;
        database.child("Route")
                .orderByChild("description")
                .equalTo(name)
                .limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        routeHolder = dataSnapshot.getValue(Route.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });
        return routeHolder;
    }

    public static boolean isTaskFinished(){
        if(isDataLoadingFinished)
        {
            isDataLoadingFinished = false;
            return true;
        }
        return false;
    }
}
