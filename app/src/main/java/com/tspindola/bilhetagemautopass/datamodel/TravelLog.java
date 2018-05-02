package com.tspindola.bilhetagemautopass.datamodel;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class TravelLog {
    @Id(assignable = true)
    private long id;
    //TODO: Adicionar horário e localização

    @Backlink
    public ToOne<Card> card;
    public ToOne<Route> route;
    public ToOne<Vehicle> vehicle;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
