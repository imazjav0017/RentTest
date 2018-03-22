package com.rent.rentmanagement.renttest;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    List<ProfileDetailsModel>pList;
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
        roomActivity.mode=2;
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
        totalRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(v.getContext(),AllRoomsActivity.class);
                v.getContext().startActivity(i);
            }
        });
        totalStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(v.getContext(),TotalTenantsctivity.class);
                v.getContext().startActivity(i);
            }
        });
            setData();
            name.setText(oName);
            noOfRooms.setText(rooms);
        noOfTenants.setText(tenants);
        pList.add(new ProfileDetailsModel("Total Occupied Rooms","375"));
        pList.add(new ProfileDetailsModel("Total Empty Rooms","300"));
        pList.add(new ProfileDetailsModel("Total Income","Rs 3,75,000"));
        pList.add(new ProfileDetailsModel("Today's Income","Rs 30000"));
        pList.add(new ProfileDetailsModel("Total Rent Due","3750"));
        pList.add(new ProfileDetailsModel("Total Rent Collected","3300"));
        adapter.notifyDataSetChanged();
        return v;
    }
    public static void setData(){
        String s=LoginActivity.sharedPreferences.getString("ownerDetails",null);
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

    }
}
