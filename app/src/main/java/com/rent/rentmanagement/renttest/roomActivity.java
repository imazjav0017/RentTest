package com.rent.rentmanagement.renttest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.AsyncTasks.GetRoomsTask;
import com.rent.rentmanagement.renttest.DataModels.RoomModel;
import com.rent.rentmanagement.renttest.Fragments.EmptyRoomsFragment;
import com.rent.rentmanagement.renttest.Fragments.ProfileFragment;
import com.rent.rentmanagement.renttest.Fragments.RentDueFragment;


public class roomActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    public static ArrayList<RoomModel> erooms;
    public static ArrayList<RoomModel> oRooms;
    String response = "", buildingName;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    static RelativeLayout reasonPage;
    static boolean isVisible = false;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    public static int mode = 2;

    public void setBuildingName() {
        String s = LoginActivity.sharedPreferences.getString("ownerDetails", null);
        if (s != null) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                buildingName = jsonObject.getString("buildingName").toUpperCase();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            buildingName = "Rooms";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
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
        filteredList.clear();
        filteredOcList.clear();
        for (RoomModel model : erooms) {
            if (model.getRoomNo().toLowerCase().contains(newText)) {
                filteredList.add(model);
            }
        }
        for (RoomModel model : oRooms) {
            if (model.getRoomNo().toLowerCase().contains(newText)) {
                filteredOcList.add(model);
            }
        }

        //EmptyRoomsFragment.adapter.setFilter(filteredList);
        //RentDueFragment.adapter2.setFilter(filteredOcList);
        return true;
    }

    public void setTokenJson() {
        try {
            if (LoginActivity.sharedPreferences.getString("token", null) != null) {
                JSONObject token = new JSONObject();
                token.put("auth", LoginActivity.sharedPreferences.getString("token", null));
                GetRoomsTask task = new GetRoomsTask(getApplicationContext());
                String s = task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/getRooms", token.toString()).get();
                if (s != null) {
                    Log.i("getRooms", s);
                    try {
                        setData(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(roomActivity.this, "Please Check Your Internet Connection and try later!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public void setData(String s) throws JSONException {
        erooms.clear();
        oRooms.clear();
        JSONObject jsonObject = new JSONObject(s);
        LoginActivity.sharedPreferences.edit().putInt("totalTenants", jsonObject.getInt("totalStudents")).apply();
        JSONArray array = jsonObject.getJSONArray("room");
        Log.i("array", array.toString());
        LoginActivity.sharedPreferences.edit().putInt("totalRooms", array.length()).apply();
        LoginActivity.sharedPreferences.edit().putString("totalIncome", String.valueOf(jsonObject.getInt("totalIncome"))).apply();
        LoginActivity.sharedPreferences.edit().putString("todayIncome", String.valueOf(jsonObject.getInt("todayIncome"))).apply();
        LoginActivity.sharedPreferences.edit().putString("collected", String.valueOf(jsonObject.getInt("collected"))).apply();
        //  ProfileFragment.setData();
        LoginActivity.sharedPreferences.edit().putString("roomsDetails", s).apply();
        if (array.length() == 0) {

        } else {
            Toast.makeText(this, "Refreshed!", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < array.length(); i++) {
                JSONObject detail = array.getJSONObject(i);
                if (detail.getBoolean("isEmpty") == true) {
                    //empty rooms
                    erooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                            detail.getString("roomRent"), detail.getString("_id"), detail.getString("checkOutDate"), detail.getBoolean("isEmpty"), detail.getString("emptyDays")));


                } else if (detail.getBoolean("isRentDue") == true) {
                    oRooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                            detail.getString("roomRent"), detail.getString("dueAmount"),
                            detail.getString("_id"), detail.getString("dueDate"), detail.getBoolean("isEmpty"), detail.getBoolean("isRentDue")
                            , detail.getString("dueDays")));


                }
            }
            //EmptyRoomsFragment.adapter.notifyDataSetChanged();
            // RentDueFragment.adapter2.notifyDataSetChanged();
           /* if(erooms.size()==0)
            {
                EmptyRoomsFragment.emptyList.setVisibility(View.VISIBLE);
                EmptyRoomsFragment.emptyList.setClickable(true);

            }
            else {
                EmptyRoomsFragment.emptyList.setVisibility(View.INVISIBLE);
                EmptyRoomsFragment.emptyList.setClickable(false);
            }*/
        }


    }

    void setNavigation() {
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.i("item", "selected");
                switch (item.getItemId()) {
                    case R.id.addRoomsNavigationMenu:
                        Intent i = new Intent(getApplicationContext(), BuildActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.emptyRoomsNavigationMenu:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.rentDueNavigationMenu:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.profileMenu:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.totalTenantsMenu:
                        Intent x = new Intent(getApplicationContext(), TotalTenantsctivity.class);
                        startActivity(x);
                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            }

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rooms_activity);
        erooms = new ArrayList<>();
        oRooms = new ArrayList<>();
        setStaticData(LoginActivity.sharedPreferences.getString("roomsDetails", null));
        setTokenJson();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        viewPagerAdapter.addFragment(new EmptyRoomsFragment(getApplicationContext()), "Empty Rooms");
        viewPagerAdapter.addFragment(new RentDueFragment(roomActivity.this), "Rent Due");
        viewPagerAdapter.addFragment(new ProfileFragment(getApplicationContext()), "My Profile");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        setNavigation();
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.closed);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        setBuildingName();
        setTitle(buildingName);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mode = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(mode);
            }
        }, 0);

    }

    public void setStaticData(String s) {
        if (s != null) {
            if (s.equals("0")) {
                Toast.makeText(this, "Fetching!", Toast.LENGTH_SHORT).show();

            } else {

                erooms.clear();
                oRooms.clear();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);

                    JSONArray array = jsonObject.getJSONArray("room");


                    //Log.i("arrayStatic", array.toString());
                    if (array.length() == 0) {

                    } else {

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject detail = array.getJSONObject(i);
                            if (detail.getBoolean("isEmpty") == true) {
                                //empty rooms
                                erooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                        detail.getString("roomRent"), detail.getString("_id"),
                                        detail.getString("checkOutDate"), detail.getBoolean("isEmpty"), detail.getString("emptyDays")));


                            } else if (detail.getBoolean("isRentDue") == true) {
                                JSONArray a = detail.getJSONArray("students");
                                Log.i("students", a.toString());
                                oRooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                        detail.getString("roomRent"), detail.getString("dueAmount"), detail.getString("_id"), detail.getString("dueDate")
                                        , detail.getBoolean("isEmpty"), detail.getBoolean("isRentDue"), detail.getString("dueDays")));


                            }
                        }
                        //EmptyRoomsFragment.adapter.notifyDataSetChanged();
                        // RentDueFragment.adapter2.notifyDataSetChanged();
                    /*if(erooms.size()==0)
                    {
                        EmptyRoomsFragment.emptyList.setVisibility(View.VISIBLE);
                        EmptyRoomsFragment.emptyList.setClickable(true);

                    }
                    else {
                        EmptyRoomsFragment.emptyList.setVisibility(View.INVISIBLE);
                        EmptyRoomsFragment.emptyList.setClickable(false);
                    }*/
                    }
                } catch (Exception e) {
                    Log.i("err", "err");
                    e.printStackTrace();
                }
            }
        } /*else {
        EmptyRoomsFragment.emptyList.setVisibility(View.VISIBLE);
        EmptyRoomsFragment.emptyList.setClickable(true);
    }*/
    }

    @Override
    public void onBackPressed() {


    }
}
