package com.tspindola.bilhetagemautopass.datamodel;

import android.widget.ArrayAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

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

    public static void start()
    {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public static void addCard(long id, String uid, String type)
    {
        if(null != database) {
            Card c = new Card(id, uid, type);
            database.child("Card").child(Long.toString(id)).setValue(c);
        }
    }

    public static void addCompany(long id,String name)
    {
        if(null != database){
            Company c = new Company(id,name);
            database.child("Company").child(Long.toString(id)).setValue(c);
        }
    }

    public static void addFee(long id,double value)
    {
        if(null != database){
            Fee f = new Fee(id,value);
            database.child("Fee").child(Long.toString(id)).setValue(f);
        }
    }

    public static void addPerson(long id,String name, String cpf, String phone, String address, String email)
    {
        if(null != database){
            Person p = new Person(id,name,cpf,phone,address,email);
            database.child("Person").child(Long.toString(id)).setValue(p);
        }
    }

    public static void addRoute(long id, String description, double length, int capacity, long companyID,
                         long feeID)
    {
        if(null != database){
            Route r = new Route(id,description,length,capacity,companyID,feeID);
            database.child("Route").child(Long.toString(id)).setValue(r);
        }
    }

    public static void addVehicle(long id, String type, String companyObjectId, int manufactureYear,
                           long companyID)
    {
        if(null != database){
            Vehicle v = new Vehicle(id,type,companyObjectId,manufactureYear,companyID);
            database.child("Vehicle").child(Long.toString(id)).setValue(v);
        }
    }


    //TODO: Agrupar todos os finds
    public static Company findCompanyById(long id) {
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
        database.child(tablename).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return listHolder;
    }

    public static void updateCreditsFromCard(long id, double newValue) {
        database.child("Card").child(Long.toString(id)).child("credits").setValue(newValue);
    }

    public static Company findCompanyByName(String name) {
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
}
