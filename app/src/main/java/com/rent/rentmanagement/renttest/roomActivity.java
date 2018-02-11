package com.rent.rentmanagement.renttest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class roomActivity extends AppCompatActivity {


    public void logout(View v)
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
        setContentView(R.layout.activity_room);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       Button logout=(Button)findViewById(R.id.logout);
        logout.setClickable(true);
        logout.setVisibility(View.VISIBLE);
        setTitle("Building Name");
    }
}
