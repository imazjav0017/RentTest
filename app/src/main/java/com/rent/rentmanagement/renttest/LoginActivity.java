package com.rent.rentmanagement.renttest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    EditText emailInput,passwordInput;
    Button loginButton;
    public static SharedPreferences sharedPreferences;
    public void gotoHome()
    {

        Intent i=new Intent(getApplicationContext(),HomePageActivity.class);
        startActivity(i);
        finish();
    }
    public class LoginTask extends AsyncTask<String,Void,String>
    {
        public void enableButton()
        {
            loginButton.setClickable(true);
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
                Log.i("data",params[1]);
                int resp=connection.getResponseCode();
                if(resp!=200) {
                    enableButton();

                }
                else
                {
                    sharedPreferences.edit().putBoolean("isLoggedIn",true).apply();
                    gotoHome();
                }
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
    public void login(View v)  {
        loginButton.setClickable(false);
        JSONObject loginDetails=new JSONObject();
        try {
            loginDetails.put("email", emailInput.getText().toString());
            loginDetails.put("password", passwordInput.getText().toString());
        }catch(Exception e)
        {
            loginButton.setClickable(true);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        LoginTask task=new LoginTask();
        task.execute("https://sleepy-atoll-65823.herokuapp.com/users/signin",loginDetails.toString());
    }
    public void Register(View v)
    {
        Intent i=new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        Log.i("isLogedIn",String.valueOf(sharedPreferences.getBoolean("isLoggedIn",false)));
        if(sharedPreferences.getBoolean("isLoggedIn",false)==true)
        {
            gotoHome();
        }
        setContentView(R.layout.activity_login);
        emailInput=(EditText)findViewById(R.id.emailInput);
        passwordInput=(EditText)findViewById(R.id.passwordInput);
        loginButton=(Button)findViewById(R.id.loginButton);


    }
}
