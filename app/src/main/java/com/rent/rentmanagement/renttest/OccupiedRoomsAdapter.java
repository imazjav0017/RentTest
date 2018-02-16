package com.rent.rentmanagement.renttest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    public View getView(int position, View convertView,final ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(resource,null,false);
        TextView roomNo=(TextView)view.findViewById(R.id.roomNoOccupiedop);
        TextView rent=(TextView)view.findViewById(R.id.rentToBeCollected);
        final RoomModel model=roomList.get(position);
        roomNo.setText("Room "+model.getRoomNo());
        rent.setText(model.getRoomRent());
         Button checkIn=(Button)view.findViewById(R.id.collectingButton);
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(parent.getContext(),rent_collectedActivity.class);
                i.putExtra("id",model.get_id());
                i.putExtra("roomNo",model.getRoomNo());
                parent.getContext().startActivity(i);
            }
        });
        return view;
    }
}
