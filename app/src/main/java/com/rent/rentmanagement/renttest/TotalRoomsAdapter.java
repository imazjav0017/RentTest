package com.rent.rentmanagement.renttest;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by imazjav0017 on 18-03-2018.
 */

public class TotalRoomsAdapter extends RecyclerView.Adapter<TotalRoomsHolder> {
    List<RoomModel> roomList;

    public TotalRoomsAdapter(List<RoomModel> roomList) {
        this.roomList = roomList;
    }

    @Override
    public TotalRoomsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.total_rooms_list,parent,false);
        return new TotalRoomsHolder(v);
    }

    @Override
    public void onBindViewHolder(final TotalRoomsHolder holder, int position) {
        final RoomModel model=roomList.get(position);

        if(model.isEmpty==false)
        {
            holder.roomNo.setText("Room No. "+model.getRoomNo());
            holder.amount.setText("Due Amount: \u20B9"+model.getDueAmount());
            holder.date.setText(model.getCheckInDate());
            holder.roomType.setText(", "+model.getRoomType()+" ,");
            if(model.isRentDue==false)
            {
                holder.checkIn.setText("CheckOut");
                holder.status.setText("Paid");
                holder.statusBar.setBackgroundColor(Color.parseColor("#0ed747"));
            }
            else
            {

                holder.checkIn.setText("Collect");
                holder.status.setText("Rent Due");
                if(model.getDueAmount().equals(model.getRoomRent()))
                holder.statusBar.setBackgroundColor(Color.parseColor("#D32F2F"));
                else
                    holder.statusBar.setBackgroundColor(Color.parseColor("#b2df3e"));
            }
        }
        else
        {
            holder.date.setText(model.getCheckInDate());
            holder.roomNo.setText("Room No. "+model.getRoomNo());
            holder.roomType.setText(", "+model.getRoomType()+" ,1st floor,");
            holder.amount.setText(" \u20B9"+model.getRoomRent());
            holder.checkIn.setText("CheckIn");
            holder.status.setText("Vacant");
           holder.statusBar.setBackgroundColor(Color.parseColor("#000000"));
        }
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(holder.context,roomDetailActivity.class);
                i.putExtra("id",model.get_id());
                i.putExtra("roomNo",model.getRoomNo());
                i.putExtra("roomType",model.getRoomType());
                i.putExtra("roomRent",model.getRoomRent());
                i.putExtra("fromTotal",true);
                holder.context.startActivity(i);
            }
        });
        holder.checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(holder.checkIn.getText().toString())
                {
                    case "CheckIn":
                        Intent i=new Intent(holder.context,StudentActivity.class);
                        i.putExtra("id",model.get_id());
                        i.putExtra("roomNo",model.getRoomNo());
                        i.putExtra("fromTotal",true);
                        holder.context.startActivity(i);
                        break;
                    case "Collect":
                        Intent x=new Intent(holder.context,rent_collectedActivity.class);
                        x.putExtra("roomId",model.get_id());
                        x.putExtra("rentAmount",model.getDueAmount());
                        x.putExtra("fromTotal",true);
                        holder.context.startActivity(x);
                        break;

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }
}
