package com.tspindola.bilhetagemautopass.datamodel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class dbBaseFunctions {

    private DatabaseReference database;
    private String token;

    public void start()
    {
        database = FirebaseDatabase.getInstance().getReference();
        token = FirebaseInstanceId.getInstance().getToken();
    }

    public void addCard(long id, String uid, String type)
    {
        Card c = new Card(id,uid,type);
        database.child("Cards").child(Long.toString(c.getId())).setValue(c);
    }
}
