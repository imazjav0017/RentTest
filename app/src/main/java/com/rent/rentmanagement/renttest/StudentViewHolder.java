package com.rent.rentmanagement.renttest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by imazjav0017 on 12-03-2018.
 */

public class StudentViewHolder extends RecyclerView.ViewHolder {
   public TextView studentName;
    public StudentViewHolder(View itemView) {
        super(itemView);
        studentName=(TextView)itemView.findViewById(R.id.studentNameTextView);
    }
}
