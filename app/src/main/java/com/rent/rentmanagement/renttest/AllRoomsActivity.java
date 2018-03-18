package com.rent.rentmanagement.renttest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllRoomsActivity extends AppCompatActivity {
    ArrayList<RoomModel> trooms;
    RecyclerView totalRoomsList;
    TotalRoomsAdapter adapter;
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        if(item.getItemId()==R.id.logoutMenuOption)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Logout!").setMessage("Are You Sure You Wish To Logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LoginActivity.sharedPreferences.edit().putBoolean("isLoggedIn",false).apply();
                            Log.i("status","Logging out");
                            LoginActivity.sharedPreferences.edit().putString("token",null).apply();
                            LoginActivity.sharedPreferences.edit().putString("roomsDetails","0").apply();
                            Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(i);
                        }
                    }).setNegativeButton("No",null).show();
            return true;

        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_rooms);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
        trooms = new ArrayList<>();
        totalRoomsList=(RecyclerView)findViewById(R.id.totalRoomsList);
        adapter=new TotalRoomsAdapter(trooms);
        LinearLayoutManager lm=new LinearLayoutManager(getApplicationContext());
        totalRoomsList.setLayoutManager(lm);
        totalRoomsList.setHasFixedSize(true);
        totalRoomsList.setAdapter(adapter);
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
                    Log.i("tRooms",array.toString());
                    if (array.length() == 0) {

                    } else {

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject detail = array.getJSONObject(i);
                            if (detail.getBoolean("isEmpty") == true) {
                                trooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                        detail.getString("roomRent"), detail.getString("_id"),null,detail.getBoolean("isEmpty")));

                            } else {
                                JSONArray a=detail.getJSONArray("students");
                                trooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                        detail.getString("roomRent"),detail.getString("dueAmount"), detail.getString("_id"),detail.getString("checkInDate")
                                        ,detail.getBoolean("isEmpty"),detail.getBoolean("isRentDue")));


                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Log.i("err", "err");
                    e.printStackTrace();
                }
            }
        }
    }

}
