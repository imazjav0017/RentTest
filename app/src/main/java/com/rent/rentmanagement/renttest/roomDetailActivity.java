package com.rent.rentmanagement.renttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class roomDetailActivity extends AppCompatActivity {
    TextView rn,rt,rr,studentsExpandingView,paymentsExpandLayout;
    RecyclerView studentsRV,paymentsHistoryList;
    StudentAdapter adapter;
    List<StudentModel> studentsList;
    List<PaymentHistoryModel>paymentList;
    PaymentHistoryAdapter pAdapter;
    ExpandableRelativeLayout expandableRelativeLayout,expandablePayments;
    String roomNo,roomType,roomRent,_id;
    public void setPaymentHistory(String s) {
        paymentList.clear();
        if(s!=null) {
            if (s.equals("0")) {
                Toast.makeText(this, "Fetching!", Toast.LENGTH_SHORT).show();

            } else {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("room");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject detail = array.getJSONObject(i);
                        if(detail.getString("_id").equals(_id))
                        {
                            JSONObject paymentobj=detail.getJSONObject("paymentDetail");
                            Log.i("payments",paymentobj.toString());
                            JSONArray payments=paymentobj.getJSONArray("payment");
                            if(payments.length()>0)
                            {

                                for(int k=0;k<payments.length();k++) {
                                    JSONObject paymentDetails = payments.getJSONObject(k);
                                    paymentList.add(new PaymentHistoryModel(paymentDetails.getString("payee"),
                                            paymentDetails.getString("amount"),paymentDetails.getString("date")));

                                }
                                pAdapter.notifyDataSetChanged();

                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void setStudentsData(String s) {
        studentsList.clear();
        if(s!=null) {
            if (s.equals("0")) {
                Toast.makeText(this, "Fetching!", Toast.LENGTH_SHORT).show();

            } else {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("room");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject detail = array.getJSONObject(i);
                        if(detail.getBoolean("isEmpty")==false && detail.getString("_id").equals(_id))
                        {
                            JSONArray students=detail.getJSONArray("students");
                            if(students.length()>0)
                            {

                                for(int k=0;k<students.length();k++) {
                                    JSONObject studentDetails = students.getJSONObject(k);
                                     studentsList.add(new StudentModel(studentDetails.getString("name"),studentDetails.getString("mobileNo")));
                                }
                                adapter.notifyDataSetChanged();

                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i=getIntent();
        _id=i.getStringExtra("id");
        roomNo=i.getStringExtra("roomNo");
        roomType=i.getStringExtra("roomType");
        roomRent=i.getStringExtra("roomRent");
        setTitle("RoomNo: "+i.getStringExtra("roomNo"));
        rn = (TextView) findViewById(R.id.roomno);
        rt = (TextView) findViewById(R.id.roomtype);
        rr = (TextView) findViewById(R.id.roomrent);
        studentsExpandingView = (TextView) findViewById(R.id.studentsExpandingView);
        paymentsExpandLayout = (TextView) findViewById(R.id.paymentsExpandLayout);
        expandableRelativeLayout=(ExpandableRelativeLayout)findViewById(R.id.studentsLayout);
        expandablePayments=(ExpandableRelativeLayout)findViewById(R.id.paymentsLayout);
        rn.setText(roomNo);
        rt.setText(roomType);
        rr.setText("\u20B9"+roomRent);
        studentsRV=(RecyclerView)findViewById(R.id.studentsRecyclerView);
        studentsList=new ArrayList<>();
        adapter=new StudentAdapter(studentsList,getApplicationContext());
        LinearLayoutManager lm=new LinearLayoutManager(getApplicationContext());
        studentsRV.setLayoutManager(lm);
        studentsRV.setHasFixedSize(true);
        studentsRV.setAdapter(adapter);
        paymentList=new ArrayList<>();
        paymentsHistoryList=(RecyclerView)findViewById(R.id.paymentsHistoryList);
        pAdapter=new PaymentHistoryAdapter(paymentList);
        LinearLayoutManager lm2=new LinearLayoutManager(getApplicationContext());
        paymentsHistoryList.setLayoutManager(lm2);
        paymentsHistoryList.setHasFixedSize(true);
        paymentsHistoryList.setAdapter(pAdapter);
        setPaymentHistory(LoginActivity.sharedPreferences.getString("roomsDetails",null));
        setStudentsData(LoginActivity.sharedPreferences.getString("roomsDetails",null));
    }
    public void expandStudents(View v)
    {
        if(expandableRelativeLayout.isExpanded())
        {
            studentsExpandingView.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_down_arrow,0);
            expandableRelativeLayout.collapse();
        }
        else {
            studentsExpandingView.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_up_arrow,0);
            expandableRelativeLayout.toggle();
        }
    }
    public void expandPayments(View v)
    {
        if(expandablePayments.isExpanded())
        {
            paymentsExpandLayout.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_down_arrow,0);
            expandablePayments.collapse();
        }
        else {
            paymentsExpandLayout.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_up_arrow,0);
            expandablePayments.toggle();
        }
    }
}
