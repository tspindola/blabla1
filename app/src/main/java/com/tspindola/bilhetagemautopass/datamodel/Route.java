package com.tspindola.bilhetagemautopass.datamodel;

import com.google.firebase.database.IgnoreExtraProperties;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

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
}
