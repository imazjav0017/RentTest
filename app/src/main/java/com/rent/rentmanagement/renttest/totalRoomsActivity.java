package com.rent.rentmanagement.renttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class totalRoomsActivity extends AppCompatActivity {
    static ArrayList<RoomModel> trooms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_rooms);
        trooms = new ArrayList<>();
        setStaticData(LoginActivity.sharedPreferences.getString("roomsDetails", null));
    }

    public void setStaticData(String s) {
        if(s!=null) {
            if (s.equals("0")) {
                Toast.makeText(this, "Fetching!", Toast.LENGTH_SHORT).show();

            } else {

                trooms.clear();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);

                    JSONArray array = jsonObject.getJSONArray("room");


                    Log.i("arrayStatic", array.toString());
                    if (array.length() == 0) {

                    } else {

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject detail = array.getJSONObject(i);


                            trooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                    detail.getString("roomRent"), detail.getString("_id")));

                        }
                    }

                }
                catch (Exception e) {
                    Log.i("err", "err");
                    e.printStackTrace();
                }
            }
        }


    }
}