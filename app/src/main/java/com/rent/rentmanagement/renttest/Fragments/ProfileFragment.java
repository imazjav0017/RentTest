package com.rent.rentmanagement.renttest.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rent.rentmanagement.renttest.Adapters.ProfileDetailsAdapter;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.DataModels.ProfileDetailsModel;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.TotalTenantsctivity;
import com.rent.rentmanagement.renttest.roomActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imazjav0017 on 02-03-2018.
 */

public class ProfileFragment extends Fragment {
    View v;
    Context context;
    LinearLayout totalRooms;
    LinearLayout totalStudents;
    TextView name;
    TextView noOfRooms;
    TextView noOfTenants;
    static String oName,rooms,tenants;
    RecyclerView detailsRv;
    ProfileDetailsAdapter adapter;
    static List<ProfileDetailsModel>pList;
    public ProfileFragment() {

    }
    public ProfileFragment(Context context) {
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.activity_newprofile,container,false);
        name=(TextView)v.findViewById(R.id.ownerNameTextView);
        //roomActivity.mode=2;
        noOfRooms=(TextView)v.findViewById(R.id.totalRoomsTextView);
        noOfTenants=(TextView)v.findViewById(R.id.totalTenantsTextView);
        totalRooms=(LinearLayout)v.findViewById(R.id.totalRoomsButton);
        totalStudents=(LinearLayout)v.findViewById(R.id.totalTenantsButton);
        detailsRv=(RecyclerView)v.findViewById(R.id.profileDetailsRv);
        pList=new ArrayList<>();
        adapter=new ProfileDetailsAdapter(pList);
        LinearLayoutManager lm1=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        detailsRv.setLayoutManager(lm1);
        detailsRv.setHasFixedSize(true);
        detailsRv.setAdapter(adapter);
       /* totalRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                v.getContext().startActivity(i);
            }
        });*/
            setData();
            name.setText(oName);
            noOfRooms.setText(rooms);
        noOfTenants.setText(tenants);
        adapter.notifyDataSetChanged();
        return v;
    }
    public static void setData(){
        pList.clear();
        String s= LoginActivity.sharedPreferences.getString("ownerDetails",null);
        if(s!=null)
        {
            try {
            JSONObject jsonObject=new JSONObject(s);
                oName=jsonObject.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        rooms=String.valueOf(LoginActivity.sharedPreferences.getInt("totalRooms",0));
        tenants=String.valueOf(LoginActivity.sharedPreferences.getInt("totalTenants",0));
   //       String emptySize= String.valueOf(RoomsFragment.erooms.size());
      //  String occRoomsSize= String.valueOf(LoginActivity.sharedPreferences.getInt("totalRooms",0)-(RoomsFragment.erooms.size()));
        String ti=LoginActivity.sharedPreferences.getString("totalIncome",null);
        String todI=LoginActivity.sharedPreferences.getString("todayIncome",null);
        String col=LoginActivity.sharedPreferences.getString("collected",null);
        //pList.add(new ProfileDetailsModel("Total Occupied Rooms",occRoomsSize));
       // pList.add(new ProfileDetailsModel("Total Empty Rooms",emptySize));
        if(ti!=null)
        pList.add(new ProfileDetailsModel("Total Income",ti));
        if(todI!=null)
        pList.add(new ProfileDetailsModel("Today's Income",todI));
        pList.add(new ProfileDetailsModel("Total Rent Due","3750"));
        if(col!=null)
        pList.add(new ProfileDetailsModel("Total Rent Collected",col));


    }
}
