package com.rent.rentmanagement.renttest;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/*
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(resource,null,false);
        TextView roomNo=(TextView)view.findViewById(R.id.roomNoDisplay);
        TextView roomType=(TextView)view.findViewById(R.id.roomTypeDisplay);
        TextView rent=(TextView)view.findViewById(R.id.rentDisplay);
       final RoomModel model=roomList.get(position);
        roomNo.setText("RoomNo "+model.getRoomNo());
        roomType.setText(model.getRoomType());
        rent.setText(model.getRoomRent());
        Button checkIn=(Button)view.findViewById(R.id.checkInOptionButton);
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(parent.getContext(),StudentActivity.class);
                i.putExtra("id",model.get_id());
                i.putExtra("roomNo",model.getRoomNo());
                parent.getContext().startActivity(i);
            }
        });


        return view;
    }
}
