package com.tspindola.bilhetagemautopass.datamodel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class dbBaseFunctions {

    private static DatabaseReference database;

    public static void start()
    {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public void addCard(long id, String uid, String type)
    {
        if(null != database) {
            Card c = new Card(id, uid, type);
            database.child("Card").child(Long.toString(id)).setValue(c);
        }
    }

    public void addCompany(long id,String name)
    {
        if(null != database){
            Company c = new Company(id,name);
            database.child("Company").child(Long.toString(id)).setValue(c);
        }
    }

    public void addFee(long id,double value)
    {
        if(null != database){
            Fee f = new Fee(id,value);
            database.child("Fee").child(Long.toString(id)).setValue(f);
        }
    }

    public void addPerson(long id,String name, String cpf, String phone, String address, String email)
    {
        if(null != database){
            Person p = new Person(id,name,cpf,phone,address,email);
            database.child("Person").child(Long.toString(id)).setValue(p);
        }
    }

    public void addRoute(long id, String description, double length, int capacity, long companyID,
                         long feeID)
    {
        if(null != database){
            Route r = new Route(id,description,length,capacity,companyID,feeID);
            database.child("Route").child(Long.toString(id)).setValue(r);
        }
    }

    public void addVehicle(long id, String type, String companyObjectId, int manufactureYear,
                           long companyID)
    {
        if(null != database){
            Vehicle v = new Vehicle(id,type,companyObjectId,manufactureYear,companyID);
            database.child("Vehicle").child(Long.toString(id)).setValue(v);
        }
    }
}
