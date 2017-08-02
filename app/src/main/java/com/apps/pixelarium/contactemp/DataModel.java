package com.apps.pixelarium.contactemp;

import java.io.Serializable;

/**
 * Created by smarti42 on 01/08/2017.
 */

public class DataModel implements Comparable<DataModel>, Serializable{

    private String name;
    private String number;

    public DataModel(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public int compareTo(DataModel d) {
        return this.name.compareTo(d.getName());
    }
 }
