package com.tspindola.bilhetagemautopass.datamodel;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Fee {
    @Id(assignable = true)
    private long id;
    private double value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
