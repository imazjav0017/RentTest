package com.rent.rentmanagement.renttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class roomDetailActivity extends AppCompatActivity {
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
        setTitle("RoomNo: "+i.getStringExtra("roomNo"));
    }
}
