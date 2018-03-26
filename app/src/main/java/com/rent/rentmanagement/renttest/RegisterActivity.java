package com.rent.rentmanagement.renttest;

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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    EditText username,email,password,mobileNo,buildingName;
    Button register;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return false;
    }

    public class RegisterTask extends AsyncTask<String,Void,String>
    {
        public void enableButton()
        {
            register.setClickable(true);
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url=new URL(params[0]);
                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                connection.addRequestProperty("Accept","application/json");
                connection.addRequestProperty("Content-Type","application/json");
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.connect();

                DataOutputStream outputStream=new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(params[1]);
                int resp=connection.getResponseCode();
                Log.i("resp",String.valueOf(resp));
                if(resp==200) {
                    onBackPressed();
                    return String.valueOf(resp);
                }
                else {
                    enableButton();
                    return String.valueOf(resp);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null)
            {
                if(s.equals("200"))
                {
                    Toast.makeText(getApplicationContext(), "Registered...", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Invalid", Toast.LENGTH_SHORT).show();

            }
            else Toast.makeText(RegisterActivity.this, "Please Check Your Internet and Try Again!", Toast.LENGTH_SHORT).show();

        }
    }
    public void Register(View v)  {
        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        Toast.makeText(this, "Registering..!", Toast.LENGTH_SHORT).show();
        register.setClickable(false);
        try {
            JSONObject userDetails = new JSONObject();
         userDetails.put("name", username.getText().toString());
            userDetails.put("email", email.getText().toString());
            userDetails.put("password", password.getText().toString());
            userDetails.put("mobileNo",mobileNo.getText().toString());
            userDetails.put("buildingName",buildingName.getText().toString());
            userDetails.put("date",dateFormat.format(new Date()).toString());

            RegisterTask task=new RegisterTask();
            Log.i("data:",userDetails.toString());
            task.execute("https://sleepy-atoll-65823.herokuapp.com/users/signup",userDetails.toString());
        }catch(Exception e)
        {
            Log.i("err",e.getMessage());
            register.setClickable(true);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        username=(EditText)findViewById(R.id.newUsernameInput);
        email=(EditText)findViewById(R.id.emailInput);
        password=(EditText)findViewById(R.id.newPasswordInput);
        register=(Button)findViewById(R.id.submitRegister);
        mobileNo=(EditText)findViewById(R.id.mobileNo);
        buildingName=(EditText)findViewById(R.id.buildingName);
    }
}
