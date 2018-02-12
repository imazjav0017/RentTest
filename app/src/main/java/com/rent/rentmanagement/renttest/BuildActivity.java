package com.rent.rentmanagement.renttest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BuildActivity extends AppCompatActivity {
  TextView buildingName,OwnerName;
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

    public void logout(View v)
    {
        LoginActivity.sharedPreferences.edit().putBoolean("isLoggedIn",false).apply();
        Log.i("status","Logging out");
        LoginActivity.sharedPreferences.edit().putString("token",null).apply();
        Intent i=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);

    }
    public void enable()
    {

        addRoomsbutton.setClickable(true);
    }
    public void saveRooms(View v) {
        addRoomsbutton.setClickable(false);
        rooms=roomNo.getText().toString();
        rentAmount=rentInput.getText().toString();
        if (accessToken == null || rentInput.equals("") || rooms.equals("")||roomType==null)
        {
            Toast.makeText(this, "Missing Fields", Toast.LENGTH_SHORT).show();
            addRoomsbutton.setClickable(true);
        }
        else {
            Log.i("sending","sending");
            Log.i("amo",rentAmount);
            SendToken task = new SendToken();
            task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/addRooms");

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
                roomsData.put("roomNo",Integer.parseInt(rooms));
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
                enable();
                e.printStackTrace();
            } catch (IOException e) {
                enable();
                e.printStackTrace();
            } catch (JSONException e) {
                enable();
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            response(s);
            Log.i("response",s);
            enable();

        }
    }
    public void response(String s)
    {
        if(s.equals("200"))
        {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            TextView description= (TextView)findViewById(R.id.description);
            int income=Integer.parseInt(rooms)*Integer.parseInt(rentAmount);
            description.setText("No Of Rooms: "+rooms+"\nExpected Income: "+String.valueOf(income));

        }
        else
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),HomePageActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build);
         addRoomsbutton= (Button) findViewById(R.id.addroomsButton);
        buildingName=(TextView)findViewById(R.id.buildingname);
        OwnerName=(TextView)findViewById(R.id.ownername);
        roomNo=(EditText) findViewById(R.id.roomdetailInput);
        rentInput=(EditText)findViewById(R.id.rentInput);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button logout=(Button)findViewById(R.id.logout);
        logout.setClickable(true);
        logout.setVisibility(View.VISIBLE);
        setTitle("Add Rooms");
        accessToken=LoginActivity.sharedPreferences.getString("token",null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Spinner spinner =(Spinner)findViewById(R.id.spinner);
        final String[] items={"Room Type","Single","Double","Triple"};
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0)
                {

                    roomType=items[position];
                    enable();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

         Button finis =(Button)findViewById(R.id.finish);
        finis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(BuildActivity.this,roomActivity.class);
                startActivity(i);
            }
        });



    }
}
