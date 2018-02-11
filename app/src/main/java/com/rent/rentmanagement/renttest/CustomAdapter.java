package com.rent.rentmanagement.renttest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by imazjav0017 on 11-02-2018.
 */

public class CustomAdapter extends ArrayAdapter<RoomModel> {
    List<RoomModel> roomList;
    Context context;
    int resource;
    public CustomAdapter(Context context,int resource,List<RoomModel>roomList)
    {
        super(context,resource,roomList);
        this.context = context;
        this.resource = resource;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(resource,null,false);
        TextView roomNo=(TextView)view.findViewById(R.id.roomNoDisplay);
        TextView roomType=(TextView)view.findViewById(R.id.roomTypeDisplay);
        TextView rent=(TextView)view.findViewById(R.id.rentDisplay);
        RoomModel model=roomList.get(position);
        roomNo.setText(model.getRoomNo());
        roomType.setText(model.getRoomType());
        rent.setText(model.getRoomRent());


        return view;
    }
}
