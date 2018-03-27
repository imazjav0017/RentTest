package com.rent.rentmanagement.renttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

public class studentProfile extends AppCompatActivity {
String _id,name,phNo,roomNo;
    boolean from;
    EditText sRoomNo,sPhNo,sAadharNo,sAdress;
    TextView sName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        Intent i=getIntent();
        name=i.getStringExtra("name");
        _id=i.getStringExtra("id");
        phNo=i.getStringExtra("phNo");
        roomNo=i.getStringExtra("roomNo");
        from=i.getBooleanExtra("total",false);
        sName=(TextView)findViewById(R.id.studentNameField);
        sRoomNo=(EditText)findViewById(R.id.studentRoomNoField);
        sPhNo=(EditText)findViewById(R.id.studentPhNoField);
        sAadharNo=(EditText)findViewById(R.id.studentAadharNoField);
        sAdress=(EditText)findViewById(R.id.studentAddressField);
        sName.setText(name);
        sRoomNo.setText(roomNo);
        sPhNo.setText(phNo);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
