package com.rent.rentmanagement.renttest.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.Adapters.TotalTenantsAdapter;
import com.rent.rentmanagement.renttest.DataModels.StudentModel;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.TotalTenantsctivity;

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

/**
 * Created by nitish on 27-03-2018.
 */

public class TenantsFragment extends Fragment {
    Context context;
    public TenantsFragment() {
    }

    public TenantsFragment(Context context) {
        this.context = context;
    }
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
                Toast.makeText(context, "Please Check Your Internet Connection and try later!", Toast.LENGTH_SHORT).show();
                super.onPostExecute(s);
            }
        }
    }
    public void setData(String s) throws JSONException {
        Log.i("getAllStudents", s);
        JSONObject jsonObject=new JSONObject(s);
        JSONArray array=jsonObject.getJSONArray("data");
        JSONArray roomNo=jsonObject.getJSONArray("roomNo");
        studentModelList.clear();
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
    public void onStart() {
        super.onStart();
        setTokenJson();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_total_tenantsctivity,container,false);
        totalTenants=(RecyclerView)v.findViewById(R.id.totalStudentsList);
        studentModelList=new ArrayList<>();
        adapter=new TotalTenantsAdapter(studentModelList);
        LinearLayoutManager lm=new LinearLayoutManager(context);
        totalTenants.setLayoutManager(lm);
        totalTenants.setHasFixedSize(true);
        totalTenants.setAdapter(adapter);
        return v;
    }
}
