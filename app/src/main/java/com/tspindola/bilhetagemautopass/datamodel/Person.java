package com.tspindola.bilhetagemautopass.datamodel;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Person {
    private long id;
    private String name;
    private String cpf;
    private String phone;
    private String address;
    private String email;

    public Person(){}

    public Person(long id, String name, String cpf, String phone, String address, String email)
    {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.phone = phone;
        this.address = address;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }
}