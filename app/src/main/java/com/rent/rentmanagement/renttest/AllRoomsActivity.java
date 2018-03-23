package com.rent.rentmanagement.renttest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.Adapters.TotalRoomsAdapter;
import com.rent.rentmanagement.renttest.DataModels.RoomModel;

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

public class AllRoomsActivity extends AppCompatActivity {
    ArrayList<RoomModel> trooms;
    RecyclerView totalRoomsList;
    TotalRoomsAdapter adapter;
    String response;
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
                Log.i("getAllRoomsResp",String.valueOf(resp));
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
                setData(s);
            }
            else
            {
                Toast.makeText(AllRoomsActivity.this, "Please Check Your Internet Connection and try later!", Toast.LENGTH_SHORT).show();
                super.onPostExecute(s);
            }
        }
    }
    void setData(String s)
    {
        if(s!=null) {
            if (s.equals("0")) {
                Toast.makeText(this, "Fetching!", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Refreshed!", Toast.LENGTH_SHORT).show();
                trooms.clear();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);

                    JSONArray array = jsonObject.getJSONArray("room");
                    LoginActivity.sharedPreferences.edit().putString("roomsDetails",s).apply();
                    if (array.length() == 0) {

                    } else {

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject detail = array.getJSONObject(i);
                            if (detail.getBoolean("isEmpty") == true) {
                                trooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                        detail.getString("roomRent"), detail.getString("_id"),
                                        detail.getString("checkOutDate"),detail.getBoolean("isEmpty"),detail.getString("emptyDays")));

                            } else {
                                JSONArray a=detail.getJSONArray("students");
                                trooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                        detail.getString("roomRent"),detail.getString("dueAmount"), detail.getString("_id"),detail.getString("dueDate")
                                        ,detail.getBoolean("isEmpty"),detail.getBoolean("isRentDue"),detail.getString("emptyDays")));


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
    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),roomActivity.class);
        roomActivity.mode=2;
        startActivity(i);
    }

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
        trooms = new ArrayList<>();
        totalRoomsList=(RecyclerView)findViewById(R.id.totalRoomsList);
        adapter=new TotalRoomsAdapter(trooms,AllRoomsActivity.this);
        LinearLayoutManager lm=new LinearLayoutManager(getApplicationContext());
        totalRoomsList.setLayoutManager(lm);
        totalRoomsList.setHasFixedSize(true);
        totalRoomsList.setAdapter(adapter);
        setStaticData(LoginActivity.sharedPreferences.getString("roomsDetails", null));
        setTokenJson();
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
                                        detail.getString("roomRent"), detail.getString("_id"),
                                        detail.getString("checkOutDate"),detail.getBoolean("isEmpty"),detail.getString("emptyDays")));

                            } else {
                                JSONArray a=detail.getJSONArray("students");
                                trooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                        detail.getString("roomRent"),detail.getString("dueAmount"), detail.getString("_id"),detail.getString("dueDate")
                                        ,detail.getBoolean("isEmpty"),detail.getBoolean("isRentDue"),detail.getString("emptyDays")));


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
