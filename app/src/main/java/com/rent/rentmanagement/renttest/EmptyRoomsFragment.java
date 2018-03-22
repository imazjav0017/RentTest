package com.rent.rentmanagement.renttest;

import android.content.Context;
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

import java.util.ArrayList;

/**
 * Created by imazjav0017 on 01-03-2018.
 */

public class EmptyRoomsFragment extends Fragment {
    View v;
    Context context;
    RecyclerView emptyRoomsListView;
    static TextView emptyList;

   static RecyclerAdapter adapter;

    public EmptyRoomsFragment() {
    }

    public EmptyRoomsFragment(Context context) {
               this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.empty_rooms_tab,container,false);
        emptyRoomsListView=(RecyclerView)v. findViewById(R.id.emptyRoomsList);
        adapter=new RecyclerAdapter(roomActivity.erooms,context);
        emptyList=(TextView)v.findViewById(R.id.addBuildingText);
        LinearLayoutManager lm=new LinearLayoutManager(context);
        emptyRoomsListView.setLayoutManager(lm);
        emptyRoomsListView.setHasFixedSize(true);
        emptyRoomsListView.setAdapter(adapter);
        return v;
    }
}
