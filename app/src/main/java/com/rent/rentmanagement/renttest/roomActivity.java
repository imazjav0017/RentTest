package com.rent.rentmanagement.renttest;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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


public class roomActivity extends AppCompatActivity {
    static ArrayList<RoomModel>erooms,oRooms;
    String response="";
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    static RelativeLayout reasonPage;
    static boolean isVisible=false;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return false;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
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
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
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
        if(isVisible)
        {
            reasonPage.setVisibility(View.INVISIBLE);
            isVisible=false;
        }
        else {
            Intent i = new Intent(getApplicationContext(), BuildActivity.class);
            startActivity(i);
            finish();
        }
    }
    void submit(View v)
    {
        reasonPage.setVisibility(View.INVISIBLE);
        isVisible=false;
    }
}
