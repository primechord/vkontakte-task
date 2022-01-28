package com.atdroid.atyurin.futuremoney.serialization;

import java.io.Serializable;

/**
 * Created by atdroid on 11.10.2015.
 */
public class Account implements Serializable {

    private long id;
    private String name = "";
    private double value = 0;

    public Account() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(final double value) {
        this.value = value;
    }



    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
