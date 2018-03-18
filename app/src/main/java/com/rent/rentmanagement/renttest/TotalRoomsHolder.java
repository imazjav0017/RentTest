package com.rent.rentmanagement.renttest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by imazjav0017 on 18-03-2018.
 */

public class TotalRoomsHolder extends RecyclerView.ViewHolder {
    LinearLayout ll;
    LinearLayout statusBar;
    TextView roomNo;
    TextView roomType;
    TextView amount;
    TextView date;
    TextView status;
    Context context;
    Button checkIn;
    public TotalRoomsHolder(View itemView) {
        super(itemView);
        ll=(LinearLayout)itemView.findViewById(R.id.TotalLl);
        statusBar=(LinearLayout)itemView.findViewById(R.id.totalStatusBar);
        context=itemView.getContext();
        date=(TextView)itemView.findViewById(R.id.totalCheckInDate);
        roomNo=(TextView)itemView.findViewById(R.id.totalRoomNo);
        status=(TextView)itemView.findViewById(R.id.TotalStatus);
        roomType=(TextView)itemView.findViewById(R.id.totalRoomType);
        amount=(TextView)itemView.findViewById(R.id.TotalRentToBeCollected);
        checkIn=(Button)itemView.findViewById(R.id.totalCheckinButton);
    }
}
