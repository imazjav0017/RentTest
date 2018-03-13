package com.rent.rentmanagement.renttest;

/**
 * Created by imazjav0017 on 11-02-2018.
 */

public class RoomModel {
    String roomType,roomNo,roomRent,_id,checkInDate,dueAmount;

    public RoomModel(String roomType, String roomNo, String roomRent, String _id,String checkInDate) {
        this.roomType = roomType;
        this.roomNo = roomNo;
        this.roomRent = roomRent;
        this._id = _id;
        this.checkInDate=checkInDate;
    }

    public RoomModel(String roomType, String roomNo, String roomRent, String dueAmount, String _id, String checkInDate) {
        this.roomType = roomType;
        this.roomNo = roomNo;
        this.roomRent = roomRent;
        this._id = _id;
        this.checkInDate = checkInDate;
        this.dueAmount = dueAmount;
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
}
