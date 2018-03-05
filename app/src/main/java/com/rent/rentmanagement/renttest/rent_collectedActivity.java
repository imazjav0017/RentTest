package com.rent.rentmanagement.renttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class rent_collectedActivity extends AppCompatActivity {
EditText rentCollectedInput,payee;
    Button collectedButton;
    String _id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_collected);
        String rent=getIntent().getStringExtra("rentAmount");
        _id=getIntent().getStringExtra("roomId");
        rentCollectedInput=(EditText)findViewById(R.id.rentcollectedinput);
        payee=(EditText)findViewById(R.id.payee);
        rentCollectedInput.setText("\u20B9"+rent);
        collectedButton=(Button)findViewById(R.id.collectedbutton);
        collectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
    }

