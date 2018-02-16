package com.rent.rentmanagement.renttest;

/**
 * Created by imazjav0017 on 11-02-2018.
 */

public class RoomModel {
    String roomType,roomNo,roomRent,_id;

    public RoomModel(String roomType, String roomNo, String roomRent, String _id) {
        this.roomType = roomType;
        this.roomNo = roomNo;
        this.roomRent = roomRent;
        this._id = _id;
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
