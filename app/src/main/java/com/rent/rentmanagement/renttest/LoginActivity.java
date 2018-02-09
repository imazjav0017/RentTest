package com.rent.rentmanagement.renttest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    public class LoginTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... url) {
            StringBuilder result=new StringBuilder();
            try {
                URL api=new URL(url[0]);
                HttpURLConnection connection=(HttpURLConnection)api.openConnection();
                connection.connect();
                InputStreamReader reader=new InputStreamReader(connection.getInputStream());
                String line="";
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
    public void login(View v)
    {
        LoginTask task=new LoginTask();
        task.execute("api");
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
        setContentView(R.layout.activity_login);
    }
}
