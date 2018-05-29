package com.tspindola.bilhetagemautopass.datamodel;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Route {
    private long id;
    private String description;
    private double length;
    private int capacity;
    private long companyID;
    private long feeID;

    public Route(){}

    public Route(long id, String description, double length, int capacity, long companyID, long feeID)
    {
        this.id = id;
        this.description = description;
        this.length = length;
        this.capacity = capacity;
        this.companyID = companyID;
        this.feeID = feeID;
    }

    public void setCompany(long companyID)
    {
        this.companyID = companyID;
    }

    public void setFee(long feeID)
    {
        this.feeID = feeID;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getLength() {
        return length;
    }

    public int getCapacity() {
        return capacity;
    }

    public long getCompanyID() {
        return companyID;
    }

    public long getFeeID() {
        return feeID;
    }
}
