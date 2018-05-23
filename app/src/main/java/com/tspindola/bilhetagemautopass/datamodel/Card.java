package com.tspindola.bilhetagemautopass.datamodel;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Card {
    private long id;
    private String uid;
    private String type;
    private double credits;
    private long personID;

    public Card(){}

    public long getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getType() {
        return type;
    }

    public double getCredits() {
        return credits;
    }

    public long getPersonID() {
        return personID;
    }

    public Card(long id, String uid, String type)
    {

        this.id = id;
        this.uid = uid;
        this.type = type;
        this.credits = 0.0;
        this.personID = 0;
    }

    public void setCredits(double amount)
    {
        this.credits = amount;
    }

    public void assignOwner(long personID)
    {
        this.personID = personID;
    }
}
