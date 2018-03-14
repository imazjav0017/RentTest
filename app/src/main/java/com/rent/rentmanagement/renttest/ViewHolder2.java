package com.rent.rentmanagement.renttest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by imazjav0017 on 28-02-2018.
 */

public class ViewHolder2 extends RecyclerView.ViewHolder {
    LinearLayout ll;
    TextView roomNo;
    TextView roomType;
    TextView amount;
    TextView date;
    Context context;
    Button reason;
    Button collect;
    public ViewHolder2(View itemView) {
        super(itemView);
        ll=(LinearLayout)itemView.findViewById(R.id.ocRoomLl);
        context=itemView.getContext();
        date=(TextView)itemView.findViewById(R.id.checkInDate);
        roomNo=(TextView)itemView.findViewById(R.id.roomNoOccupiedop);
        roomType=(TextView)itemView.findViewById(R.id.roomTypeOcc);
        amount=(TextView)itemView.findViewById(R.id.rentToBeCollected);
        reason=(Button)itemView.findViewById(R.id.reason);
        collect=(Button)itemView.findViewById(R.id.collectingButton);
    }
}
