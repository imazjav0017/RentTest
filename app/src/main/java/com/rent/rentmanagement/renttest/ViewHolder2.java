package com.rent.rentmanagement.renttest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by imazjav0017 on 28-02-2018.
 */

public class ViewHolder2 extends RecyclerView.ViewHolder {
    LinearLayout ll;
    TextView roomNo;
    TextView amount;
    Context context;
    public ViewHolder2(View itemView) {
        super(itemView);
        ll=(LinearLayout)itemView.findViewById(R.id.ocRoomLl);
        context=itemView.getContext();
        roomNo=(TextView)itemView.findViewById(R.id.roomNoOccupiedop);
        amount=(TextView)itemView.findViewById(R.id.rentToBeCollected);
    }
}
