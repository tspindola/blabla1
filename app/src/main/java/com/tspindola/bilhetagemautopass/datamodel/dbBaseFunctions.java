package com.tspindola.bilhetagemautopass.datamodel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class dbBaseFunctions {

    private DatabaseReference database;
    private String token;

    public dbBaseFunctions()
    {
        database = FirebaseDatabase.getInstance().getReference();
        token = FirebaseInstanceId.getInstance().getToken();
    }
    
}
