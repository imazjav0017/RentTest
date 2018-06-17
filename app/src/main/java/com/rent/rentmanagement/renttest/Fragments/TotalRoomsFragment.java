package com.rent.rentmanagement.renttest.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.rent.rentmanagement.renttest.Adapters.TotalRoomsAdapter;

import com.rent.rentmanagement.renttest.DataModels.RoomModel;
import com.rent.rentmanagement.renttest.MainActivity;
import com.rent.rentmanagement.renttest.R;

import java.util.ArrayList;

/**
 * Created by imazjav0017 on 24-03-2018.
 */

public class TotalRoomsFragment extends Fragment {
    Context context;
    public static TextView empty;
    RecyclerView totalRoomsList;
    Spinner spinner;
    public static boolean useSpinner=false;
    public TotalRoomsFragment() {
    }

    public TotalRoomsFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  v=inflater.inflate(R.layout.activity_all_rooms,container,false);
        totalRoomsList=(RecyclerView)v.findViewById(R.id.totalRoomsList);
       empty=(TextView)v.findViewById(R.id.noRoomsText);
        LinearLayoutManager lm=new LinearLayoutManager(context);
        totalRoomsList.setLayoutManager(lm);
        totalRoomsList.setHasFixedSize(true);
        totalRoomsList.setAdapter(RoomsFragment.adapter3);
        spinner=(Spinner)v.findViewById(R.id.filterAllRooms);
        String[]filterOptions={"All Rooms","Empty Rooms","Rent Due","Paid"};
        ArrayAdapter<String>filterAdapter=new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,filterOptions);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(filterAdapter);
      spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<RoomModel> filteredList = new ArrayList<>();
                filteredList.clear();
                if(RoomsFragment.tRooms!=null && RoomsFragment.tRooms.size()!=0) {
                    switch (i) {
                        case 0:

                            for(RoomModel model: RoomsFragment.tRooms)
                            {
                                filteredList.add(model);
                            }
                            if(RoomsFragment.adapter3!=null)
                            {
                                RoomsFragment.adapter3.setFilter(filteredList);
                            }
                            break;
                        case 1:
                            for(RoomModel model: RoomsFragment.tRooms)
                            {
                                if(model.isEmpty())
                                filteredList.add(model);
                            }
                            if(RoomsFragment.adapter3!=null)
                            {
                                RoomsFragment.adapter3.setFilter(filteredList);
                            }
                            break;
                        case 2:

                            for(RoomModel model: RoomsFragment.tRooms)
                            {
                                if(!model.isEmpty() && model.isRentDue())
                                filteredList.add(model);
                            }
                            if(RoomsFragment.adapter3!=null)
                            {
                                RoomsFragment.adapter3.setFilter(filteredList);
                            }
                            break;
                        case 3:
                            for(RoomModel model: RoomsFragment.tRooms)
                            {
                                if(!model.isEmpty() && !model.isRentDue())
                                filteredList.add(model);
                            }
                            if(RoomsFragment.adapter3!=null)
                            {
                                RoomsFragment.adapter3.setFilter(filteredList);
                            }

                        RoomsFragment.adapter3.notifyDataSetChanged();


                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

       /* if(RoomsFragment.tRooms.isEmpty())
        {
            if(empty!=null) {
                totalRoomsList.setVisibility(View.INVISIBLE);
                empty.setVisibility(View.VISIBLE);
            }
        }
        else {
            if(empty!=null) {
                totalRoomsList.setVisibility(View.VISIBLE);
                empty.setVisibility(View.INVISIBLE);
            }
        }*/
        return v;
    }
    public static void empty() {
        if (empty != null) {
            if (RoomsFragment.tRooms.isEmpty())
                RoomsFragment.adapter3.setEmptyView(empty);
            else
                empty.setVisibility(View.INVISIBLE);
        }
    }
}
