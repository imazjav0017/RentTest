package com.rent.rentmanagement.renttest;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class roomActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    static ArrayList<RoomModel>erooms,oRooms;
    String response="";
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    static RelativeLayout reasonPage;
    static boolean isVisible=false;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item=menu.findItem(R.id.searchMenu);
        SearchView searchView=(SearchView)MenuItemCompat.getActionView(item);
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
        newText=newText.toLowerCase();
        ArrayList<RoomModel>filteredList=new ArrayList<>();
        ArrayList<RoomModel>filteredOcList=new ArrayList<>();
        filteredList.clear();
        filteredOcList.clear();
        for(RoomModel model : erooms)
        {
            if(model.getRoomNo().toLowerCase().contains(newText))
            {
                filteredList.add(model);
            }
        }
        for(RoomModel model : oRooms)
        {
            if(model.getRoomNo().toLowerCase().contains(newText))
            {
                filteredOcList.add(model);
            }
        }

        EmptyRoomsFragment.adapter.setFilter(filteredList);
        RentDueFragment.adapter2.setFilter(filteredOcList);
        return true;
    }
    public void setTokenJson()
    {
        try {
            if(LoginActivity.sharedPreferences.getString("token",null)!=null) {
            JSONObject token = new JSONObject();
                token.put("auth",LoginActivity.sharedPreferences.getString("token", null));
            GetRoomsTask task = new GetRoomsTask();
            task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/getRooms", token.toString());
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String  getResponse(HttpURLConnection connection)
    {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            StringBuffer sb = new StringBuffer("");
            String line = "";

            while ((line = in.readLine()) != null) {

                sb.append(line);
                break;
            }

            in.close();
            return sb.toString();
        }catch(Exception e)
        {
            return e.getMessage();
        }
    }




    public class GetRoomsTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("Accept", "application/json");
                connection.addRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.connect();
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(params[1]);
                Log.i("data", params[1]);
                int resp = connection.getResponseCode();
                Log.i("getRoomsResp",String.valueOf(resp));
                if(resp==200)
                {
                    response=getResponse(connection);
                    return response;
                }
                else
                {
                    return null;
                }

            }catch(MalformedURLException e)
            {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                Log.i("getRooms", s);
                try {
                    setData(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(roomActivity.this, "Please Check Your Internet Connection and try later!", Toast.LENGTH_SHORT).show();
                super.onPostExecute(s);
            }
        }
    }
    public void setData(String s) throws JSONException {
        erooms.clear();
        oRooms.clear();
        JSONObject jsonObject=new JSONObject(s);
        JSONArray array=jsonObject.getJSONArray("room");
        Log.i("array",array.toString());
        LoginActivity.sharedPreferences.edit().putString("roomsDetails",s).apply();
        if(array.length()==0)
        {

        }
        else {
            Toast.makeText(this, "Refreshed!", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < array.length(); i++) {
                JSONObject detail = array.getJSONObject(i);
                if(detail.getBoolean("isEmpty")==true) {
                    //empty rooms
                    erooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                            detail.getString("roomRent"), detail.getString("_id")));


                }
                else
                {
                    oRooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                            detail.getString("roomRent"), detail.getString("_id")));


                }
            }
            EmptyRoomsFragment.adapter.notifyDataSetChanged();
            RentDueFragment.adapter2.notifyDataSetChanged();
        }



    }


    public void logout(View v)
    {
        LoginActivity.sharedPreferences.edit().putBoolean("isLoggedIn",false).apply();
        Log.i("status","Logging out");
        LoginActivity.sharedPreferences.edit().putString("token",null).apply();
        Intent i=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);

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
        erooms=new ArrayList<>();
        oRooms=new ArrayList<>();
        setStaticData(LoginActivity.sharedPreferences.getString("roomsDetails",null));
        setTokenJson();
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),getApplicationContext());
        viewPagerAdapter.addFragment(new EmptyRoomsFragment(getApplicationContext()),"Empty Rooms");
        viewPagerAdapter.addFragment(new RentDueFragment(getApplicationContext()),"Rent Due");
        viewPagerAdapter.addFragment(new ProfileFragment(),"My Profile");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
       setNavigation();
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.open,R.string.closed);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
       Button logout=(Button)findViewById(R.id.logout);
        logout.setClickable(true);
        logout.setVisibility(View.VISIBLE);
        setTitle("Rooms");
        reasonPage=(RelativeLayout)findViewById(R.id.reasonPage);

    }
public void setStaticData(String s) {
    if(s!=null)
    {

        erooms.clear();
        oRooms.clear();
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(s);

            JSONArray array = jsonObject.getJSONArray("room");


            Log.i("arrayStatic", array.toString());
            if (array.length() == 0) {

            } else {

                for (int i = 0; i < array.length(); i++) {
                    JSONObject detail = array.getJSONObject(i);
                    if (detail.getBoolean("isEmpty") == true) {
                        //empty rooms
                        erooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                detail.getString("roomRent"), detail.getString("_id")));


                    }
                    else {
                        oRooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                detail.getString("roomRent"), detail.getString("_id")));


                    }
                }
                EmptyRoomsFragment.adapter.notifyDataSetChanged();
                RentDueFragment.adapter2.notifyDataSetChanged();
            }
        }
        catch (Exception e) {
            Log.i("err","err");
        e.printStackTrace();
    }
    }
}
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START) || isVisible) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            if (isVisible) {
                reasonPage.setVisibility(View.INVISIBLE);
                isVisible = false;
            }
        }
        else {
            moveTaskToBack(true);
        }

    }
   public void submit(View v)
    {
        reasonPage.setVisibility(View.INVISIBLE);
        isVisible=false;
    }
}
