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

import com.rent.rentmanagement.renttest.Adapters.TotalRoomsAdapter;

import com.rent.rentmanagement.renttest.R;

/**
 * Created by imazjav0017 on 24-03-2018.
 */

public class TotalRoomsFragment extends Fragment {
    Context context;
    public TotalRoomsFragment() {
    }

    public TotalRoomsFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  v=inflater.inflate(R.layout.activity_all_rooms,container,false);
       RecyclerView totalRoomsList=(RecyclerView)v.findViewById(R.id.totalRoomsList);

        LinearLayoutManager lm=new LinearLayoutManager(context);
        totalRoomsList.setLayoutManager(lm);
        totalRoomsList.setHasFixedSize(true);
        totalRoomsList.setAdapter(RoomsFragment.adapter3);
        return v;
    }
}
