package com.rent.rentmanagement.renttest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by imazjav0017 on 28-02-2018.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
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
