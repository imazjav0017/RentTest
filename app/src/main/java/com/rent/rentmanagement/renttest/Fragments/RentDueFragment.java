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

import com.rent.rentmanagement.renttest.Adapters.OccupiedRoomsAdapter;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.roomActivity;

/**
 * Created by imazjav0017 on 01-03-2018.
 */

public class RentDueFragment extends Fragment {
    View v;
    Context context;

    RecyclerView occupiedRoomsListView;

    public RentDueFragment() {
    }

    public RentDueFragment(Context context) {
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.rent_due_tab,container,false);
        occupiedRoomsListView=(RecyclerView)v.findViewById(R.id.occupiedRoomsList);
        LinearLayoutManager lm1=new LinearLayoutManager(context);
        occupiedRoomsListView.setLayoutManager(lm1);
        occupiedRoomsListView.setHasFixedSize(true);
        occupiedRoomsListView.setAdapter(RoomsFragment.adapter2);
        return v;
    }
}
