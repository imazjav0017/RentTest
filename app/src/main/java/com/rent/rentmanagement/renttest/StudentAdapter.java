package com.rent.rentmanagement.renttest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by imazjav0017 on 12-03-2018.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentViewHolder> {
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
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }
}
