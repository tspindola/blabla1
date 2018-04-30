package com.tspindola.bilhetagemautopass.datamodel;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class Vehicle {
    @Id(assignable=true)
    private long id;
    private String type;
    private String companyObjectId;
    private int manufactureYear;
    @Backlink
    ToOne<Company> company;

    public String getCompanyObjectId() {
        return companyObjectId;
    }

    public void setCompanyObjectId(String companyObjectId) {
        this.companyObjectId = companyObjectId;
    }

    public int getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(int manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
