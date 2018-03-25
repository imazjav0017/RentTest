package com.rent.rentmanagement.renttest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.rent.rentmanagement.renttest.Fragments.ProfileFragment;
import com.rent.rentmanagement.renttest.Fragments.RoomsFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    SearchView searchView;

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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
                            LoginActivity.sharedPreferences.edit().putInt("totalTenants",0).apply();
                            LoginActivity.sharedPreferences.edit().putInt("totalRooms",0).apply();
                            LoginActivity.sharedPreferences.edit().putString("totalIncome",null).apply();
                            LoginActivity.sharedPreferences.edit().putString("todayIncome",null).apply();
                            LoginActivity.sharedPreferences.edit().putString("collected",null).apply();

                            Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(i);
                        }
                    }).setNegativeButton("No",null).show();
            return true;

        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item=menu.findItem(R.id.searchMenu);
        searchView=(SearchView) MenuItemCompat.getActionView(item);
        //searchView.setOnQueryTextListener(this);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        loadFragment(new ProfileFragment(MainActivity.this));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        int id=item.getItemId();
        switch (id)
        {
            case R.id.profileViewItem:
                fragment=new ProfileFragment(MainActivity.this);
                break;
            case R.id.roomViewItem:
                fragment=new RoomsFragment(MainActivity.this);
                break;
            case R.id.tenantsViewiTem:
                Log.i("current","tenants");
                break;
        }
        loadFragment(fragment);
        return false;
    }
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentsContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}

