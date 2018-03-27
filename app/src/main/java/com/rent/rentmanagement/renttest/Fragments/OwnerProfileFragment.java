package com.rent.rentmanagement.renttest.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nitish on 27-03-2018.
 */

public class OwnerProfileFragment extends Fragment {
    EditText name,email,phNo;
    Context context;
    public OwnerProfileFragment() {
    }

    public OwnerProfileFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.owner_profile_fragment,container,false);
        name=(EditText)v.findViewById(R.id.ownerNameTabText);
        email=(EditText)v.findViewById(R.id.ownerEmailText);
        phNo=(EditText)v.findViewById(R.id.ownerNumbertext);
        setDetails();
        return v;
    }
    void setDetails()
    {
        String s= LoginActivity.sharedPreferences.getString("ownerDetails",null);
        if(s!=null)
        {
            try {
                JSONObject jsonObject=new JSONObject(s);
                String oName=jsonObject.getString("name");
                String oEmail=jsonObject.getString("email");
                String oNo=jsonObject.getString("mobileNo");
                if(oName!=null)
                {
                    name.setText(oName);
                    name.setSelection(oName.length());
                }
                if(oEmail!=null)
                {
                    email.setText(oEmail);
                    email.setSelection(oEmail.length());
                }
                if(oNo!=null)
                {
                    phNo.setText(oNo);
                    phNo.setSelection(oNo.length());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
