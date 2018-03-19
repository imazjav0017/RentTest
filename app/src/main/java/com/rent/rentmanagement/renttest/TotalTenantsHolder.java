package com.rent.rentmanagement.renttest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by imazjav0017 on 17-03-2018.
 */

public class TotalTenantsHolder extends RecyclerView.ViewHolder {
    TextView studentName,phNo;
    Button call;
    Context context;
    RelativeLayout rl;
    public TotalTenantsHolder(View itemView) {
        super(itemView);
        studentName=(TextView)itemView.findViewById(R.id.studentNameTextView2);
        phNo=(TextView)itemView.findViewById(R.id.studentPhoneNumber2);
        call=(Button)itemView.findViewById(R.id.callButton1);
        rl=(RelativeLayout)itemView.findViewById(R.id.viewDetailsRl);
        context=itemView.getContext();

    }
}
