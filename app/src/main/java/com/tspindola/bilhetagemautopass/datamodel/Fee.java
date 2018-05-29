package com.tspindola.bilhetagemautopass.datamodel;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Fee {
    private long id;
    private double value;

    public Fee(){}

    public Fee(long id, double value)
    {
        this.id = id;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public double getValue() {
        return value;
    }
}
