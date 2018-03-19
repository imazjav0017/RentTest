package com.rent.rentmanagement.renttest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by imazjav0017 on 12-03-2018.
 */

public class StudentViewHolder extends RecyclerView.ViewHolder {
   public TextView studentName;
    TextView phNo;
    RelativeLayout rl;
    Button call;
    Context context;
    public StudentViewHolder(View itemView) {
        super(itemView);
        context=itemView.getContext();
        studentName=(TextView)itemView.findViewById(R.id.studentNameTextView);
        phNo=(TextView)itemView.findViewById(R.id.studentPhoneNumber);
        call=(Button)itemView.findViewById(R.id.callButton);
        rl=(RelativeLayout)itemView.findViewById(R.id.viewDetailsStudent);
    }
}
