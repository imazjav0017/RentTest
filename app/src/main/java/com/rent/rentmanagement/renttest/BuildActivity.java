package com.rent.rentmanagement.renttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BuildActivity extends AppCompatActivity {
  TextView text1,text2,text3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build);
        Button button = (Button) findViewById(R.id.addrooms);
        text1=(TextView)findViewById(R.id.buildingname);
        text2=(TextView)findViewById(R.id.ownername);
        text2=(TextView)findViewById(R.id.roomdetail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
