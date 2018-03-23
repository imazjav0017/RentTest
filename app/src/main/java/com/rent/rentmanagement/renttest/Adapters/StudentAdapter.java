package com.rent.rentmanagement.renttest.Adapters;

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
 * Created by imazjav0017 on 12-03-2018.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    List<StudentModel> studentsList;
    Context context1;

    public StudentAdapter(List<StudentModel> studentsList,Context context) {
        this.studentsList = studentsList;
        this.context1=context;
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.students_list_item,parent,false);
        return new StudentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final StudentViewHolder holder, int position) {
        final StudentModel model=studentsList.get(position);
        holder.studentName.setText(model.getName());
        holder.phNo.setText(model.getPhNo());
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
                i.putExtra("total",false);
                holder.context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    /**
     * Created by imazjav0017 on 12-03-2018.
     */

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
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
}
