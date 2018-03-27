package com.rent.rentmanagement.renttest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rent.rentmanagement.renttest.DataModels.RoomModel;
import com.rent.rentmanagement.renttest.Fragments.OwnerProfileFragment;
import com.rent.rentmanagement.renttest.Fragments.ProfileFragment;
import com.rent.rentmanagement.renttest.Fragments.RoomsFragment;
import com.rent.rentmanagement.renttest.Fragments.TenantsFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
   public static BottomNavigationView bottomNavigationView;
    SearchView searchView;
    FloatingActionButton fab;
    public static String roomInfo;
    boolean showSv=false;
    public static boolean completedTasks=true;
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
                            if(completedTasks) {
                                LoginActivity.sharedPreferences.edit().putBoolean("isLoggedIn", false).apply();
                                Log.i("status", "Logging out");
                                LoginActivity.sharedPreferences.edit().putString("token", null).apply();
                                LoginActivity.sharedPreferences.edit().putString("roomsDetails", "0").apply();
                                LoginActivity.sharedPreferences.edit().putInt("totalTenants", 0).apply();
                                LoginActivity.sharedPreferences.edit().putInt("totalRooms", 0).apply();
                                LoginActivity.sharedPreferences.edit().putString("totalIncome", null).apply();
                                LoginActivity.sharedPreferences.edit().putString("todayIncome", null).apply();
                                LoginActivity.sharedPreferences.edit().putString("collected", null).apply();

                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(i);
                            }
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
        item.setVisible(true);
            searchView = (SearchView) MenuItemCompat.getActionView(item);
            searchView.setOnQueryTextListener(this);
            searchView.setMaxWidth(Integer.MAX_VALUE);

        return true;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<RoomModel> filteredList = new ArrayList<>();
        ArrayList<RoomModel> filteredOcList = new ArrayList<>();
        ArrayList<RoomModel> filteredTotalList = new ArrayList<>();
        filteredList.clear();
        filteredOcList.clear();
        filteredTotalList.clear();
        if(RoomsFragment.erooms!=null) {
            for (RoomModel model : RoomsFragment.erooms) {
                if (model.getRoomNo().toLowerCase().contains(newText)) {
                    filteredList.add(model);
                }
            }
            for (RoomModel model : RoomsFragment.oRooms) {
                if (model.getRoomNo().toLowerCase().contains(newText)) {
                    filteredOcList.add(model);
                }
            }
            for (RoomModel model : RoomsFragment.tRooms) {
                if (model.getRoomNo().toLowerCase().contains(newText)) {
                    filteredTotalList.add(model);
                }
            }
        }
        if(RoomsFragment.adapter3!=null) {

            RoomsFragment.adapter.setFilter(filteredList);
            RoomsFragment.adapter2.setFilter(filteredOcList);
            RoomsFragment.adapter3.setFilter(filteredTotalList);
        }
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
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);

        fab = (FloatingActionButton) findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,automanualActivity.class);
                startActivity(intent);

            }
        });
        fab.setVisibility(View.INVISIBLE);
        loadFragment(new ProfileFragment(MainActivity.this));
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        int id=item.getItemId();

        switch (id)
        {
            case R.id.profileViewItem:
                fab.setVisibility(View.INVISIBLE);
                fragment=new ProfileFragment(MainActivity.this);
                break;
            case R.id.roomViewItem:
                fragment=new RoomsFragment(MainActivity.this);
                fab.setVisibility(View.VISIBLE);
                break;
            case R.id.tenantsViewiTem:
                fab.setVisibility(View.INVISIBLE);

                fragment=new TenantsFragment(MainActivity.this);
                Log.i("current","tenants");
                break;
            case R.id.myProfileViewTab:
                fab.setVisibility(View.INVISIBLE);

                fragment=new OwnerProfileFragment(MainActivity.this);
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


    @Override
    public void onBackPressed() {

        if(!(searchView.isIconified()))
        {
           searchView.setIconified(true);
        }
        else
        {
            new AlertDialog.Builder(this).setTitle("Exit!").setMessage("Are You Sure You Wish To Exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.super.onBackPressed();
                        }
                    }).setNegativeButton("No",null).show();
        }
    }

}

