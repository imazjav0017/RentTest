package com.rent.rentmanagement.renttest.DataModels;

/**
 * Created by imazjav0017 on 13-03-2018.
 */

public class StudentModel {
    String name,phNo,roomNo,_id;

    public StudentModel(String name, String phNo, String _id) {
        this.name = name;
        this.phNo = phNo;
        this._id = _id;
    }

    public StudentModel(String name, String phNo, String roomNo, String _id) {
        this.name = name;
        this.phNo = phNo;
        this.roomNo = roomNo;
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public String getPhNo() {
        return phNo;
    }

    public String get_id() {
        return _id;
    }

    public String getRoomNo() {
        return roomNo;
    }

}
