package com.rent.rentmanagement.renttest.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rent.rentmanagement.renttest.DataModels.ProfileDetailsModel;
import com.rent.rentmanagement.renttest.R;

import java.util.List;

/**
 * Created by imazjav0017 on 22-03-2018.
 */

public class ProfileDetailsAdapter extends RecyclerView.Adapter<ProfileDetailsAdapter.ProfileDetailsHolder> {
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

    /**
     * Created by imazjav0017 on 22-03-2018.
     */

    public static class ProfileDetailsHolder extends RecyclerView.ViewHolder {
        TextView title,value;
        Context context;
        public ProfileDetailsHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            title=(TextView)itemView.findViewById(R.id.titleText);
            value=(TextView)itemView.findViewById(R.id.valueText);

        }
    }
}
