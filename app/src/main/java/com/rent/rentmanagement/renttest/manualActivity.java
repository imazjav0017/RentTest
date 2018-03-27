package com.rent.rentmanagement.renttest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class manualActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    EditText rentInput,roomNo;
    Button addRoomsbutton;
    String accessToken,rooms=null,rentAmount=null,roomType=null;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return false;
    }

    public void enable()
    {

        addRoomsbutton.setClickable(true);
    }
    public void saveRooms(View v) {
        addRoomsbutton.setClickable(false);
        rooms=roomNo.getText().toString();
        rentAmount=rentInput.getText().toString();
        if (accessToken == null || rentAmount.equals("") || rooms.equals("")||roomType==null)
        {
            Toast.makeText(this, "Missing Fields", Toast.LENGTH_SHORT).show();
            addRoomsbutton.setClickable(true);
        }
        else {
            new AlertDialog.Builder(this)
                    .setTitle("Add a room").setMessage("Are You Sure You Wish To Add a room "+roomType+" rooms?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "Processing!", Toast.LENGTH_SHORT).show();
                            Log.i("sending","sending");
                            Log.i("amo",rentAmount);
                            manualActivity.SendToken task = new manualActivity.SendToken();
                            task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/addRooms");
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    enable();
                }
            }).show();


        }
    }
    public class SendToken extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... params) {
            try {
                JSONObject roomsData=new JSONObject();
                URL url = new URL(params[0]);
                roomsData.put("roomType",roomType);
                roomsData.put("roomRent",Integer.parseInt(rentAmount));
                roomsData.put("roomNo",rooms);
                if(accessToken!=null)
                    roomsData.put("auth",accessToken);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.addRequestProperty("Accept","application/json");
                connection.addRequestProperty("Content-Type", "application/json");

                connection.setDoOutput(true);
                connection.connect();
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(roomsData.toString());
                Log.i("data", roomsData.toString());
                int tokenRecieved = connection.getResponseCode();
                Log.i("tokenResp", String.valueOf(tokenRecieved));
                return String.valueOf(tokenRecieved);


            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            response(s);



        }
    }
    public void response(String s) {
        if (s != null)
            if (s.equals("200")) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
              onBackPressed();
            } else {
                enable();
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        else {
            enable();
            Toast.makeText(this, "Please Check Your Internet Connection And Try Later!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        addRoomsbutton= (Button) findViewById(R.id.addroomsButton);
        roomNo=(EditText) findViewById(R.id.roomdetailInput);
        rentInput=(EditText)findViewById(R.id.rentInput);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Add Rooms");
        accessToken=LoginActivity.sharedPreferences.getString("token",null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final String[] items={"Room Type","Single","Double","Triple"};
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.single)
                {
                    roomType=items[1];
                }
                else if(checkedId==R.id.doubleBtn)
                {
                    roomType=items[2];
                }
                else if(checkedId==R.id.triple)
                {
                    roomType=items[3];
                }

            }
        });
    }
}