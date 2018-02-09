package com.rent.rentmanagement.renttest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {
    EditText username,email,password;
    Button register;

    public class RegisterTask extends AsyncTask<String,Void,String>
    {
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
                outputStream.flush();
                outputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
    public void Register(View v)  {
        register.setClickable(false);
        try {
            JSONObject userDetails = new JSONObject();
            userDetails.put("name", username.getText().toString());
            userDetails.put("email", email.getText().toString());
            userDetails.put("password", password.getText().toString());
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
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username=(EditText)findViewById(R.id.newUsernameInput);
        email=(EditText)findViewById(R.id.emailInput);
        password=(EditText)findViewById(R.id.newPasswordInput);
        register=(Button)findViewById(R.id.submitRegister);
    }
}
