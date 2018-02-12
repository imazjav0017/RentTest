package com.rent.rentmanagement.renttest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by imazjav0017 on 12-02-2018.
 */

public class OccupiedRoomsAdapter extends ArrayAdapter {
    List<RoomModel> roomList;
    Context context;
    int resource;
    public OccupiedRoomsAdapter(Context context,int resource,List<RoomModel>roomList)
    {
        super(context,resource,roomList);
        this.context = context;
        this.resource = resource;
        this.roomList = roomList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(resource,null,false);
        TextView roomNo=(TextView)view.findViewById(R.id.roomNoOccupiedop);
        TextView rent=(TextView)view.findViewById(R.id.rentToBeCollected);
        RoomModel model=roomList.get(position);
        roomNo.setText("Room "+model.getRoomNo());
        rent.setText(model.getRoomRent());
        return view;
    }
}
