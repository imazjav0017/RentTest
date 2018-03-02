package com.rent.rentmanagement.renttest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by imazjav0017 on 02-03-2018.
 */

public class ProfileFragment extends Fragment {
    View v;
    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.profile_tab,container,false);
        return v;
    }
}
