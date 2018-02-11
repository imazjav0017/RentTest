package com.rent.rentmanagement.renttest;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HomePageActivity extends AppCompatActivity {
Button logout;
public void logout(View v)
{
    LoginActivity.sharedPreferences.edit().putBoolean("isLoggedIn",false).apply();
    Log.i("status","Logging out");
    LoginActivity.sharedPreferences.edit().putString("token",null).apply();
    Intent i=new Intent(getApplicationContext(),LoginActivity.class);
    startActivity(i);

}
    public void addRoom(View v)
    {
        Intent i=new Intent(getApplicationContext(),BuildActivity.class);
        startActivity(i);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        logout=(Button)findViewById(R.id.logout);
        logout.setClickable(true);
        logout.setVisibility(View.VISIBLE);
        setTitle("Building Name");
        Log.i("isLoggedIn",String.valueOf(LoginActivity.sharedPreferences.getBoolean("isLoggedIn",false)));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
