package com.rent.rentmanagement.renttest.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.DataModels.StudentModel;
import com.rent.rentmanagement.renttest.studentProfile;

import java.util.List;

/**
 * Created by imazjav0017 on 17-03-2018.
 */

public class TotalTenantsAdapter extends RecyclerView.Adapter<TotalTenantsAdapter.TotalTenantsHolder> {
    List<StudentModel> studentModels;

    public TotalTenantsAdapter(List<StudentModel> studentModels) {
        this.studentModels = studentModels;
    }

    @Override
    public TotalTenantsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.total_students,parent,false);
        return new TotalTenantsHolder(v);
    }

    @Override
    public void onBindViewHolder(final TotalTenantsHolder holder, int position) {
        final StudentModel model=studentModels.get(position);
        holder.studentName.setText(model.getName());
        holder.phNo.setText("Room No "+model.getRoomNo());
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:"+model.getPhNo()));
                holder.context.startActivity(i);

            }
        });
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(holder.context,studentProfile.class);
                i.putExtra("name",model.getName());
                i.putExtra("id",model.get_id());
                i.putExtra("roomNo",model.getRoomNo());
                i.putExtra("phNo",model.getPhNo());
                i.putExtra("total",true);
                holder.context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return studentModels.size();
    }

    /**
     * Created by imazjav0017 on 17-03-2018.
     */

    public static class TotalTenantsHolder extends RecyclerView.ViewHolder {
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
}
