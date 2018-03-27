package com.rent.rentmanagement.renttest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    EditText emailInput,passwordInput;
    Button loginButton;
    String accessToken=null;
    int resp;
    JSONObject tokenJson;
    public static SharedPreferences sharedPreferences;
    public String getResponse(HttpURLConnection connection)
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
    public void setToken(String token)
    {
        try {
            tokenJson=new JSONObject(token);
            accessToken=tokenJson.getString("token");
            sharedPreferences.edit().putString("token",accessToken).apply();
            JSONObject jsonObject=tokenJson.getJSONObject("user");
            Log.i("respone111",jsonObject.toString());
            sharedPreferences.edit().putString("ownerDetails",jsonObject.toString()).apply();
            LoginActivity.sharedPreferences.edit().putInt("totalTenants",tokenJson.getInt("totalStudents")).apply();
            LoginActivity.sharedPreferences.edit().putString("totalIncome", tokenJson.getString("totalIncome")).apply();
            LoginActivity.sharedPreferences.edit().putString("todayIncome", tokenJson.getString("todayIncome")).apply();
            LoginActivity.sharedPreferences.edit().putString("collected",tokenJson.getString("collected")).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void gotoHome()
    {

        Intent i=new Intent(getApplicationContext(),MainActivity.class);
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
                 resp=connection.getResponseCode();
                if(resp!=200) {
                    enableButton();
                    String response=getResponse(connection);

                    Log.i("resp",String.valueOf(resp));
                    return String.valueOf(resp);
                }
                else
                {
                    sharedPreferences.edit().putBoolean("isLoggedIn",true).apply();
                   String response=getResponse(connection);
                    Log.i("resp",String.valueOf(resp));
                    setToken(response);
                    Log.i("ressss",response);
                    outputStream.flush();
                    outputStream.close();
                    gotoHome();
                    return String.valueOf(resp);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                if (response.equals("200")) {
                    Toast.makeText(getApplicationContext(), "Logged In!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Try Again!", Toast.LENGTH_SHORT).show();
                }


            }
            else
            {
                enableButton();
                Toast.makeText(LoginActivity.this, "Please Check Your Internet Connection and Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void login(View v)  {
        loginButton.setClickable(false);
        JSONObject loginDetails=new JSONObject();
        try {
            if(!(emailInput.getText().toString().equals("")||passwordInput.getText().toString().equals(""))) {
                loginDetails.put("email", emailInput.getText().toString());
                loginDetails.put("password", passwordInput.getText().toString());
                Toast.makeText(this, "Logging In..!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Please Enter something!", Toast.LENGTH_SHORT).show();
            }
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
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Login");
        emailInput=(EditText)findViewById(R.id.emailInput);
        passwordInput=(EditText)findViewById(R.id.passwordInput);
        loginButton=(Button)findViewById(R.id.loginButton);


    }
}
