package com.rent.rentmanagement.renttest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.DataModels.RoomModel;
import com.rent.rentmanagement.renttest.StudentActivity;
import com.rent.rentmanagement.renttest.roomDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imazjav0017 on 28-02-2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    List<RoomModel> roomModels;
    Context context;

    public RecyclerAdapter(List<RoomModel> roomModels, Context context) {
        this.roomModels = roomModels;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_rooms_list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final RoomModel model=roomModels.get(position);

        holder.roomNo.setText("Room No. "+model.getRoomNo());
        holder.roomType.setText(", "+model.getRoomType()+",");
        holder.roomRent.setText(" \u20B9"+model.getRoomRent());
        holder.emptyDays.setText("Empty From "+model.getDays());
        holder.date.setText(model.getCheckInDate());
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

    /**
     * Created by imazjav0017 on 28-02-2018.
     */

    public static class ViewHolder extends RecyclerView.ViewHolder {
       public TextView roomNo;
        public TextView roomType;
        public TextView roomRent;
        public Context context;
        LinearLayout ll;
        TextView emptyDays;
        TextView date;
        Button checkIn;
        public ViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
             roomNo=(TextView)itemView.findViewById(R.id.roomNoDisplay);
            date=(TextView)itemView.findViewById(R.id.checkInDate2);
            emptyDays=(TextView)itemView.findViewById(R.id.emptyDays);
            roomType=(TextView)itemView.findViewById(R.id.roomTypeDisplay);
            roomRent=(TextView)itemView.findViewById(R.id.rentDisplay);
            checkIn=(Button)itemView.findViewById(R.id.checkInOptionButton);
            ll=(LinearLayout)itemView.findViewById(R.id.emptyRoomsLinearLayout);

        }
    }
}
