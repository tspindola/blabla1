package com.tspindola.bilhetagemautopass.datamodel;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Vehicle {
    private long id;
    private String type;
    private String companyObjectId;
    private int manufactureYear;
    private long companyID;

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getCompanyObjectId() {
        return companyObjectId;
    }

    public int getManufactureYear() {
        return manufactureYear;
    }

    public long getCompanyID() {
        return companyID;
    }

    public Vehicle(){}

    public Vehicle(long id, String type, String companyObjectId, int manufactureYear, long companyID)
    {
        this.id = id;
        this.type = type;
        this.companyObjectId = companyObjectId;
        this.manufactureYear = manufactureYear;
        this.companyID = companyID;
    }
}
