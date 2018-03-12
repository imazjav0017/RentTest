package com.rent.rentmanagement.renttest;

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
    List<String> studentNames;

    public StudentAdapter(List<String> studentNames) {
        this.studentNames = studentNames;
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.students_list_item,parent,false);
        return new StudentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StudentViewHolder holder, int position) {
        String name=studentNames.get(position);
        Log.i(String.valueOf(position),name);
        holder.studentName.setText(name);
    }

    @Override
    public int getItemCount() {
        return studentNames.size();
    }
}
