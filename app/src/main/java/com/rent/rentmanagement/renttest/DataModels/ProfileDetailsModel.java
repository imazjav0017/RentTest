package com.rent.rentmanagement.renttest.DataModels;

/**
 * Created by imazjav0017 on 22-03-2018.
 */

public class ProfileDetailsModel {
    String title,value;

    public ProfileDetailsModel(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }
}
