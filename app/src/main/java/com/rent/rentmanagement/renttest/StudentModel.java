package com.rent.rentmanagement.renttest;

/**
 * Created by imazjav0017 on 13-03-2018.
 */

public class StudentModel {
    String name,phNo,roomNo;

    public StudentModel(String name, String phNo, String roomNo) {
        this.name = name;
        this.phNo = phNo;
        this.roomNo = roomNo;
    }

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

    public String getRoomNo() {
        return roomNo;
    }
}
