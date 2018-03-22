package com.rent.rentmanagement.renttest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by imazjav0017 on 22-03-2018.
 */

public class ProfileDetailsHolder extends RecyclerView.ViewHolder {
    TextView title,value;
    Context context;
    public ProfileDetailsHolder(View itemView) {
        super(itemView);
        context=itemView.getContext();
        title=(TextView)itemView.findViewById(R.id.titleText);
        value=(TextView)itemView.findViewById(R.id.valueText);

    }
}
