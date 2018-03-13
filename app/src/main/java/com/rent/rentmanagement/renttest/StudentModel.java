package com.rent.rentmanagement.renttest;

/**
 * Created by imazjav0017 on 13-03-2018.
 */

public class StudentModel {
    String name,phNo;

    public StudentModel(String name, String phNo) {
        this.name = name;
        this.phNo = phNo;
    }

    public String getName() {
        return name;
    }

    public String getPhNo() {
        return phNo;
    }
}
