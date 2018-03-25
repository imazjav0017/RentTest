package com.rent.rentmanagement.renttest.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rent.rentmanagement.renttest.Adapters.RecyclerAdapter;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.roomActivity;

/**
 * Created by imazjav0017 on 01-03-2018.
 */

public class EmptyRoomsFragment extends Fragment {
    View v;
    Context context;
    RecyclerView emptyRoomsListView;
    static TextView emptyList;



    public EmptyRoomsFragment() {
    }

    public EmptyRoomsFragment(Context context) {
               this.context=context;
    }

    @Override
    public void onResume() {
        super.onResume();
        RoomsFragment.adapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.empty_rooms_tab,container,false);
        emptyRoomsListView=(RecyclerView)v. findViewById(R.id.emptyRoomsList);
        emptyList=(TextView)v.findViewById(R.id.addBuildingText);
        LinearLayoutManager lm=new LinearLayoutManager(context);
        emptyRoomsListView.setLayoutManager(lm);
        emptyRoomsListView.setHasFixedSize(true);
        emptyRoomsListView.setAdapter(RoomsFragment.adapter);
        return v;
    }
}
