package com.rent.rentmanagement.renttest;

<<<<<<< HEAD
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
=======
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
>>>>>>> 948f4df92e57249e5b30c00d2755264c467e5dab

public class roomActivity extends AppCompatActivity {
    ArrayList<String>rooms;
    ArrayAdapter adapter;
    String response="";
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
                super.onPostExecute(s);
            }
        }
    }
    public void setData(String s) throws JSONException {
        JSONObject jsonObject=new JSONObject(s);
        JSONArray array=jsonObject.getJSONArray("room");
        for(int i=0;i<array.length();i++)
        {
            JSONObject detail=array.getJSONObject(i);
           rooms.add(detail.getString("roomType")+" "+detail.getString("roomNo")+ " "+detail.getString("roomRent"));
            adapter.notifyDataSetChanged();
        }
        Log.i("size",String.valueOf(rooms.size()));


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
<<<<<<< HEAD
        ListView listView=(ListView)findViewById(R.id.roomdetailsList);
        rooms=new ArrayList<>();
        adapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,rooms);
        listView.setAdapter(adapter);
        setTokenJson();


=======
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       Button logout=(Button)findViewById(R.id.logout);
        logout.setClickable(true);
        logout.setVisibility(View.VISIBLE);
        setTitle("Building Name");
>>>>>>> 948f4df92e57249e5b30c00d2755264c467e5dab
    }
}
