package com.rent.rentmanagement.renttest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by imazjav0017 on 02-03-2018.
 */

public class ProfileFragment extends Fragment {
    View v;
    Button addRoomsBtn;
    Context context;
    LinearLayout circle;
    public ProfileFragment() {

    }
    public ProfileFragment(Context context) {
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.profile_tab,container,false);
        addRoomsBtn=(Button)v.findViewById(R.id.addRoomsButton2);
        addRoomsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(v.getContext(),BuildActivity.class);
                v.getContext().startActivity(i);
            }
        });
        circle=(LinearLayout)v.findViewById(R.id.circleLayout);
        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(v.getContext(),totalstudentActivity.class);
                v.getContext().startActivity(i);
            }
        });

        return v;
    }
}
