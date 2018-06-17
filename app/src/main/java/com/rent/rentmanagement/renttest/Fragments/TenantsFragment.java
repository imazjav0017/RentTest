package com.rent.rentmanagement.renttest.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.Adapters.TotalTenantsAdapter;
import com.rent.rentmanagement.renttest.DataModels.RoomModel;
import com.rent.rentmanagement.renttest.DataModels.StudentModel;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.MainActivity;
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

public class TenantsFragment extends Fragment implements SearchView.OnQueryTextListener {
    Context context;
    public static SearchView searchView;
    static TextView empty;
    CheckBox onlyRooms;
    boolean rooms;
    String checkRooms;
    public TenantsFragment() {
    }

    public TenantsFragment(Context context) {
        this.context = context;
    }
    RecyclerView totalTenants;
    public static List<StudentModel> studentModelList;
  public static TotalTenantsAdapter adapter;
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
    void setStaticData(String s) throws JSONException
    {
        if(s!=null) {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray array = jsonObject.getJSONArray("data");
            JSONArray roomNo = jsonObject.getJSONArray("roomNo");
            studentModelList.clear();
            for (int k = 0; k < array.length(); k++) {
                String rNo = roomNo.getString(k);
                JSONArray array1 = array.getJSONArray(k);
                for (int i = 0; i < array1.length(); i++) {
                    JSONObject detail = array1.getJSONObject(i);
                    studentModelList.add(new StudentModel(detail.getString("name"), detail.getString("mobileNo"), rNo
                            , detail.getString("_id")));
                }
            }
            adapter.notifyDataSetChanged();
        }

    }
    public void setData(String s) throws JSONException {
        Log.i("getAllStudents", s);
        LoginActivity.sharedPreferences.edit().putString("allTenantsinfo",s).apply();
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
        if(studentModelList.size()==0)
        {
            empty.setVisibility(View.VISIBLE);
        }
        else
            empty.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            setStaticData(LoginActivity.sharedPreferences.getString("allTenantsinfo",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setTokenJson();

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    //to check if string has only numbers
    boolean isNumber(String s)
    {
        int x=1;
        for(int i=0;i<s.length();i++)
        {
            if(Character.isDigit(s.charAt(i)))
            {
                x=0;
                continue;
            }
            else {
                x=1;
            }
        }
        if(x==0)
        return true;
        else
            return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item=menu.findItem(R.id.searchMenu);
        item.setVisible(true);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Enter Tenant name or RoomNo...");
        onlyRooms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    rooms = true;
                    onQueryTextChange(checkRooms);
                }
                else
                {
                    rooms=false;
                    onQueryTextChange(checkRooms);

                }


            }
        });
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<StudentModel>filteredTenants=new ArrayList<>();
        filteredTenants.clear();
        if(newText.isEmpty())
        {
            onlyRooms.setChecked(false);
        }

        if(TenantsFragment.studentModelList!=null)
        {
            if(isNumber(newText))
            {
                checkRooms=newText;
                onlyRooms.setVisibility(View.VISIBLE);
            }
            else
            {
                onlyRooms.setVisibility(View.INVISIBLE);
            }
            for(StudentModel model:TenantsFragment.studentModelList)
            {
                if(rooms)
                {
                    if(model.getRoomNo().equals(newText))
                    {
                        filteredTenants.add(model);
                    }
                }
                else {
                    if (model.getName().toLowerCase().contains(newText)) {
                        filteredTenants.add(model);
                    }
                    if(model.getRoomNo().equals(newText))
                    {
                        filteredTenants.add(model);
                    }
                }

            }

            if(TenantsFragment.adapter!=null)
            {
                TenantsFragment.adapter.setFilter(filteredTenants);
            }
        }
        return true;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_total_tenantsctivity,container,false);
        totalTenants=(RecyclerView)v.findViewById(R.id.totalStudentsList);
        empty=(TextView)v.findViewById(R.id.noTenantsText);
        onlyRooms=(CheckBox)v.findViewById(R.id.roomsOnlySearchFilter);
        studentModelList=new ArrayList<>();
        adapter=new TotalTenantsAdapter(studentModelList);
        LinearLayoutManager lm=new LinearLayoutManager(context);
        totalTenants.setLayoutManager(lm);
        totalTenants.setHasFixedSize(true);
        totalTenants.setAdapter(adapter);
        return v;
    }
}
