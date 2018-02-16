package com.rent.rentmanagement.renttest;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.TextView;


public class roomActivity extends AppCompatActivity {
    ArrayList<RoomModel>erooms,oRooms;
    CustomAdapter adapter1;
    OccupiedRoomsAdapter adapter2;
    String response="";
    ListView emptyRoomsListView,occupiedRoomsListView;
    boolean isToggled;
    public void toggle(View v)
    {
        if(!isToggled) {
            emptyRoomsListView.setVisibility(View.INVISIBLE);
            occupiedRoomsListView.setVisibility(View.VISIBLE);
            isToggled=true;
        }
        else
        {
            emptyRoomsListView.setVisibility(View.VISIBLE);
            occupiedRoomsListView.setVisibility(View.INVISIBLE);
            isToggled=false;
        }
    }
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
                super.onPostExecute(s);
            }
        }
    }
    public void setData(String s) throws JSONException {
        erooms.clear();
        JSONObject jsonObject=new JSONObject(s);
        JSONArray array=jsonObject.getJSONArray("room");
        Log.i("array",array.toString());
        if(array.length()==0)
        {

        }
        else {

            for (int i = 0; i < array.length(); i++) {
                JSONObject detail = array.getJSONObject(i);
                if(detail.getBoolean("isEmpty")==true) {
                    //empty rooms
                    erooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                            detail.getString("roomRent"), detail.getString("_id")));
                    adapter1.notifyDataSetChanged();
                }
                else
                {
                    oRooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                            detail.getString("roomRent"), detail.getString("_id")));
                    adapter2.notifyDataSetChanged();
                }
            }
            emptyRoomsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    RoomModel model=erooms.get(position);
                   Intent i=new Intent(getApplicationContext(),roomDetailActivity.class);
                    i.putExtra("id",model.get_id());
                    i.putExtra("roomType",model.getRoomType());
                    i.putExtra("roomRent",model.getRoomRent());
                    i.putExtra("roomNo",model.getRoomNo());
                    startActivity(i);
                }
            });
            occupiedRoomsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    RoomModel model=oRooms.get(position);
                    Intent i=new Intent(getApplicationContext(),roomDetailActivity.class);
                    i.putExtra("id",model.get_id());
                    i.putExtra("roomNo",model.getRoomNo());
                    i.putExtra("roomType",model.getRoomType());
                    i.putExtra("roomRent",model.getRoomRent());
                    startActivity(i);
                }
            });
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
        isToggled=false;
        emptyRoomsListView=(ListView)findViewById(R.id.emptyRoomsList);
        erooms=new ArrayList<>();
        adapter1=new CustomAdapter(getApplicationContext(),R.layout.customlist_item,erooms);
        emptyRoomsListView.setAdapter(adapter1);



        occupiedRoomsListView=(ListView)findViewById(R.id.occupiedRoomsList);
        oRooms=new ArrayList<>();
        adapter2=new OccupiedRoomsAdapter(getApplicationContext(),R.layout.roomdisplayafeature,oRooms);
        occupiedRoomsListView.setAdapter(adapter2);

        setTokenJson();
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       Button logout=(Button)findViewById(R.id.logout);
        logout.setClickable(true);
        logout.setVisibility(View.VISIBLE);
        setTitle("Rooms");

    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),BuildActivity.class);
        startActivity(i);
        finish();
    }
}
