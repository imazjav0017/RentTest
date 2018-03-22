package com.rent.rentmanagement.renttest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by imazjav0017 on 22-03-2018.
 */

public class ProfileDetailsAdapter extends RecyclerView.Adapter<ProfileDetailsHolder> {
    List<ProfileDetailsModel> modelList;

    public ProfileDetailsAdapter(List<ProfileDetailsModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public ProfileDetailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.display_onprofile,parent,false);
        return new ProfileDetailsHolder(v);
    }

    @Override
    public void onBindViewHolder(ProfileDetailsHolder holder, int position) {
        ProfileDetailsModel model=modelList.get(position);
        holder.title.setText(model.getTitle());
        holder.value.setText(model.getValue());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
