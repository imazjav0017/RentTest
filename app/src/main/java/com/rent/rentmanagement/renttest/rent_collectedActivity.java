package com.rent.rentmanagement.renttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class rent_collectedActivity extends AppCompatActivity {
EditText rentCollectedInput;
    Button collectedButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_collected);
        String rent=getIntent().getStringExtra("rentAmount");
        rentCollectedInput=(EditText)findViewById(R.id.rentcollectedinput);
        rentCollectedInput.setText("\u20B9"+rent);
        collectedButton=(Button)findViewById(R.id.collectedbutton);
        collectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
