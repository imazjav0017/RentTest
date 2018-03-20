package com.rent.rentmanagement.renttest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class rent_collectedActivity extends AppCompatActivity {
EditText rentCollectedInput,payee;
    Button collectedButton;
    String _id,date;
    JSONObject rentdetails;
    boolean fromTotal;
    public class RentCollectionTask extends AsyncTask<String,Void,String>
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
               // Log.i("data", params[1]);
                int resp = connection.getResponseCode();
                Log.i("rentCollectedResp",String.valueOf(resp));
                if(resp==200)
                {
                    return "success";
                }
                else
                    return "try again later";

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
            if(s!=null)
            {
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
                if(s.equals("success"))
                {
                    onBackPressed();
                }
            }
        }
    }
    public void makeJson()
    {
        rentdetails=new JSONObject();
        try {
            if(LoginActivity.sharedPreferences.getString("token",null)==null)
            {
                throw new Exception("invalid token");
            }
            else {
                rentdetails.put("roomId",_id);
                rentdetails.put("payee",payee.getText().toString());
                rentdetails.put("amount",Integer.parseInt(rentCollectedInput.getText().toString()));
                rentdetails.put("auth", LoginActivity.sharedPreferences.getString("token", null));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_collected);
        String rent=getIntent().getStringExtra("rentAmount");
        _id=getIntent().getStringExtra("roomId");
        fromTotal=getIntent().getBooleanExtra("fromTotal",false);
        rentCollectedInput=(EditText)findViewById(R.id.rentcollectedinput);
        payee=(EditText)findViewById(R.id.payee);
        rentCollectedInput.setText(rent);
        rentCollectedInput.setSelection(rentCollectedInput.getText().toString().length());
        collectedButton=(Button)findViewById(R.id.collectedbutton);
        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        Date dateObj=new Date();
        date=dateFormat.format(dateObj).toString();
        TextView dateCollected=(TextView)findViewById(R.id.datecollectedinput);
        dateCollected.setText(date);
        collectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeJson();
                RentCollectionTask task=new RentCollectionTask();
                task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/paymentDetail",rentdetails.toString());

            }
        });
        setStaticData(LoginActivity.sharedPreferences.getString("roomsDetails",null));
    }
    public void setStaticData(String s) {
        if(s!=null) {
            if (s.equals("0")) {
                Toast.makeText(this, "Fetching!", Toast.LENGTH_SHORT).show();

            } else {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("room");
                   
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject detail = array.getJSONObject(i);
                            if(detail.getBoolean("isEmpty")==false && detail.getString("_id").equals(_id))
                            {
                                JSONArray students=detail.getJSONArray("students");
                                if(students.length()>0)
                                {
                                    JSONObject studentDetails=students.getJSONObject(0);
                                    payee.setText(studentDetails.getString("name"));
                                }
                            }

                        }
                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            }
        }

    @Override
    public void onBackPressed() {
        if(fromTotal)
        {
            Intent i = new Intent(getApplicationContext(), AllRoomsActivity.class);
            startActivity(i);
            finish();
        }
        else {
        Intent i = new Intent(getApplicationContext(), roomActivity.class);
        roomActivity.mode = 1;
        startActivity(i);
        finish();
        }

    }
}

