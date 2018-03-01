package com.rent.rentmanagement.renttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class roomDetailActivity extends AppCompatActivity {
    TextView rn,rt,rr;
    String roomNo,roomType,roomRent;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return false;
    } public void logout(View v)
    {
        LoginActivity.sharedPreferences.edit().putBoolean("isLoggedIn",false).apply();
        Log.i("status","Logging out");
        LoginActivity.sharedPreferences.edit().putString("token",null).apply();
        Intent i=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button logout=(Button)findViewById(R.id.logout);
        logout.setClickable(true);
        logout.setVisibility(View.VISIBLE);
        Intent i=getIntent();
        roomNo=i.getStringExtra("roomNo");
        roomType=i.getStringExtra("roomType");
        roomRent=i.getStringExtra("roomRent");
        setTitle("RoomNo: "+i.getStringExtra("roomNo"));
        rn = (TextView) findViewById(R.id.roomno);
        rt = (TextView) findViewById(R.id.roomtype);
        rr = (TextView) findViewById(R.id.roomrent);
        rn.setText(roomNo);
        rt.setText(roomType);
        rr.setText(roomRent);
    }
}
