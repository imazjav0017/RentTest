package com.rent.rentmanagement.renttest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.Adapters.TotalTenantsAdapter;
import com.rent.rentmanagement.renttest.DataModels.StudentModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TotalTenantsctivity extends AppCompatActivity {
    RecyclerView totalTenants;
    List<StudentModel> studentModelList;
    TotalTenantsAdapter adapter;
    String response;
    public void setTokenJson()
    {
        try {
            if(LoginActivity.sharedPreferences.getString("token",null)!=null) {
                JSONObject token = new JSONObject();
                token.put("auth",LoginActivity.sharedPreferences.getString("token", null));
                GetTentantsTask task = new GetTentantsTask();
                task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/getAllStudents", token.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String  getResponse(HttpURLConnection connection)
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
    public class GetTentantsTask extends AsyncTask<String,Void,String>
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
                Log.i("getAllSTudentsResp",String.valueOf(resp));
                if(resp==200)
                {
                    response=getResponse(connection);
                    return response;
                }
                else
                {
                    return null;
                }

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
            if (s != null) {

                try {
                    setData(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(TotalTenantsctivity.this, "Please Check Your Internet Connection and try later!", Toast.LENGTH_SHORT).show();
                super.onPostExecute(s);
            }
        }
    }
    public void setData(String s) throws JSONException {
        Log.i("getAllStudents", s);
        JSONObject jsonObject=new JSONObject(s);
        JSONArray array=jsonObject.getJSONArray("data");
        JSONArray roomNo=jsonObject.getJSONArray("roomNo");
        for(int k=0;k<array.length();k++)
        {
            String rNo=roomNo.getString(k);
            JSONArray array1=array.getJSONArray(k);
            for (int i = 0; i < array1.length(); i++) {
                JSONObject detail = array1.getJSONObject(i);
                studentModelList.add(new StudentModel(detail.getString("name"),detail.getString("mobileNo"),rNo
                ,detail.getString("_id")));
            }
        }
        adapter.notifyDataSetChanged();

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_tenantsctivity);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("All Tentants");
        setTokenJson();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        totalTenants=(RecyclerView)findViewById(R.id.totalStudentsList);
        studentModelList=new ArrayList<>();
        adapter=new TotalTenantsAdapter(studentModelList);
        LinearLayoutManager lm=new LinearLayoutManager(getApplicationContext());
        totalTenants.setLayoutManager(lm);
        totalTenants.setHasFixedSize(true);
        totalTenants.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),roomActivity.class);
        roomActivity.mode=2;
        startActivity(i);
        finish();
    }
}
