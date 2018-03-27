package com.rent.rentmanagement.renttest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.Fragments.RoomsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class StudentActivity extends AppCompatActivity {
    EditText studentName,contactNo,aadharNo;
    String sName,sNo,sAad;
    String _id;
    JSONObject studentDetails;
    boolean fromTotal,added=false;
    Button checkin,finish;
    void enable()
    {
        checkin.setClickable(true);
    }
    public class AddStudentsTask extends AsyncTask<String,Void,String>
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
                Log.i("addStudentsResp",String.valueOf(resp));
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
            enable();

            if(s!=null)
            {
                added=true;
                finish.setClickable(true);
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
                if(s.equals("success"))
                {
                    studentName.setText("");
                    contactNo.setText("");
                    aadharNo.setText("");
                }
            }else {
                Toast.makeText(StudentActivity.this, "No Internet!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void back(View v)
    {
        onBackPressed();
    }
    public void makeJson()
    {
        studentDetails=new JSONObject();
        try {
            if(LoginActivity.sharedPreferences.getString("token",null)==null)
            {
                throw new Exception("invalid token");
            }
            else {
                studentDetails.put("name",sName);
                studentDetails.put("mobileNo",sNo);
                studentDetails.put("adharNo",sAad);
                studentDetails.put("roomId",_id);
                studentDetails.put("auth", LoginActivity.sharedPreferences.getString("token", null));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void checkIn(View v)
    {
        checkin.setClickable(false);
        sName=studentName.getText().toString();
        sNo=contactNo.getText().toString();
        sAad=aadharNo.getText().toString();
        if(sName.equals("")||sNo.equals("")||sAad.equals(""))
        {
            enable();
            Toast.makeText(this, "Missing Fields", Toast.LENGTH_SHORT).show();
        }
        else
        {
            makeJson();
            AddStudentsTask task=new AddStudentsTask();
            task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/addStudents",studentDetails.toString());
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
        setContentView(R.layout.activity_student); Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkin=(Button)findViewById(R.id.checkInButton);
        finish=(Button)findViewById(R.id.addExtraStudentButton);
        finish.setClickable(false);
        Intent i=getIntent();
        _id=i.getStringExtra("id");
        fromTotal=i.getBooleanExtra("fromTotal",false);
        setTitle("Room No: "+i.getStringExtra("roomNo"));
        studentName=(EditText)findViewById(R.id.studentNameInput);
        contactNo=(EditText)findViewById(R.id.studentContactNoInput);
        aadharNo=(EditText)findViewById(R.id.studentAadharNoInput);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        RoomsFragment.currentTab=2;
    }
}


