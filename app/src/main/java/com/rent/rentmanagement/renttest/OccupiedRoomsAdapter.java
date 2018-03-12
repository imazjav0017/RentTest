package com.rent.rentmanagement.renttest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imazjav0017 on 12-02-2018.
 */

public class OccupiedRoomsAdapter extends RecyclerView.Adapter<ViewHolder2> {
    List<RoomModel> roomList;
    Context context;

    public OccupiedRoomsAdapter(List<RoomModel> roomList, Context context) {
        this.roomList = roomList;
        this.context = context;
    }

    @Override
    public ViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.roomdisplayafeature,parent,false);
        return new ViewHolder2(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder2 holder, int position) {
        final RoomModel model=roomList.get(position);
        holder.roomNo.setText("Room No."+model.getRoomNo());
        holder.amount.setText("      \u20B9"+model.getRoomRent());
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(holder.context,roomDetailActivity.class);
                i.putExtra("id",model.get_id());
                i.putExtra("roomNo",model.getRoomNo());
                i.putExtra("roomType",model.getRoomType());
                i.putExtra("roomRent",model.getRoomRent());
                holder.context.startActivity(i);
            }
        });
        holder.reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomActivity.reasonPage.setVisibility(View.VISIBLE);
                roomActivity.isVisible=true;
            }
        });
        holder.collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(holder.context,rent_collectedActivity.class);
                i.putExtra("rentAmount",model.getRoomRent());
                i.putExtra("roomId",model.get_id());
                holder.context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }
    public void setFilter(List<RoomModel> filteredList)
    {
        roomList=new ArrayList<>();
        roomList.addAll(filteredList);
        notifyDataSetChanged();
    }
}
