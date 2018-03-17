package com.rent.rentmanagement.renttest;

/**
 * Created by imazjav0017 on 11-02-2018.
 */

public class RoomModel {
    String roomType,roomNo,roomRent,_id,checkInDate,dueAmount;
    boolean isEmpty,isRentDue;

    public RoomModel(String roomType, String roomNo, String roomRent, String _id,String checkInDate,boolean isEmpty) {
        this.roomType = roomType;
        this.roomNo = roomNo;
        this.roomRent = roomRent;
        this._id = _id;
        this.isEmpty=isEmpty;
        this.checkInDate=checkInDate;
    }

    public RoomModel(String roomType, String roomNo, String roomRent, String dueAmount, String _id, String checkInDate,boolean isEmpty,boolean isRentDue) {
        this.roomType = roomType;
        this.roomNo = roomNo;
        this.roomRent = roomRent;
        this._id = _id;
        this.checkInDate = checkInDate;
        this.dueAmount = dueAmount;
        this.isEmpty=isEmpty;
        this.isRentDue=isRentDue;
    }

    public String getDueAmount() {
        return dueAmount;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public String getRoomRent() {
        return roomRent;
    }

    public String get_id() {
        return _id;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public boolean isRentDue() {
        return isRentDue;
    }
}
