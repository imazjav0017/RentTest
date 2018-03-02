package com.rent.rentmanagement.renttest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imazjav0017 on 28-02-2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    List<RoomModel> roomModels;
    Context context;
    public RecyclerAdapter(List<RoomModel> roomModels,Context context)
    {
        this.context=context;
        this.roomModels=roomModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.customlist_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final RoomModel model=roomModels.get(position);
        holder.roomNo.setText(model.getRoomNo());
        holder.roomType.setText(model.getRoomType());
        holder.roomRent.setText(model.getRoomRent());
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
        holder.checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(holder.context,StudentActivity.class);
                i.putExtra("id",model.get_id());
                i.putExtra("roomNo",model.getRoomNo());
                holder.context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomModels.size();
    }
    public void setFilter(List<RoomModel> filteredList)
    {
        roomModels=new ArrayList<>();
        roomModels.addAll(filteredList);
        notifyDataSetChanged();
    }
}
