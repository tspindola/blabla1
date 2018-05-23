package com.tspindola.bilhetagemautopass.datamodel;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class TravelLog {
    private long id;
    private String datetime;
    private long cardID;
    private long routeID;
    private long vehicleID;

    public TravelLog(){}

    public TravelLog(long id, String datetime, long cardID, long routeID, long vehicleID)
    {
        this.id = id;
        this.datetime = datetime;
        this.cardID = cardID;
        this.routeID = routeID;
        this.vehicleID = vehicleID;
    }
}
