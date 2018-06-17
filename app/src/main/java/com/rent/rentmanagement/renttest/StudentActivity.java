package com.rent.rentmanagement.renttest;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.Fragments.DatePickerFragment;
import com.rent.rentmanagement.renttest.Fragments.RoomsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class StudentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText studentName,contactNo,aadharNo;
    TextView datePickerText;
    String sName,sNo,sAad;
    ProgressBar progressBar;
    RelativeLayout bg;
    public static final int FROM_NOTIFICATION=1;
    String _id;
    ArrayList<String> tenantsNames;
    JSONObject studentDetails;
    boolean fromTotal,added=false;
    Button checkin,finish;
    void enable()
    {
        checkin.setClickable(true);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,i);
        c.set(Calendar.MONTH,i1);
        c.set(Calendar.DAY_OF_MONTH,i2);
        //String date=DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());
        String year= String.valueOf(c.get(Calendar.YEAR));
        String month= String.valueOf(c.get(Calendar.MONTH)+1);
        if(month.length()==1)
        {
            month="0"+month;
        }
        String day= String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        if(day.length()==1)
        {
            day="0"+day;
        }
        datePickerText.setText(year+"-"+month+"-"+day);
    }

    public class AddStudentsTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("Accept", "application/json");
                connection.addRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.connect();
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(params[1]);
                Log.i("data", params[1]);
                int resp = connection.getResponseCode();
                Log.i("addStudentsResp",String.valueOf(resp));
                if(resp==200)
                {
                    return "success";
                }
                else
                    return "try again later";

            }catch(MalformedURLException e)
            {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            enable();
            progressBar.setVisibility(View.GONE);
            bg.setClickable(true);
            if(s!=null)
            {
                tenantsNames.add(sName);
                added=true;
                finish.setClickable(true);
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
                if(s.equals("success"))
                {
                    studentName.setText("");
                    contactNo.setText("");
                    aadharNo.setText("");
                }
            }else {
                Toast.makeText(StudentActivity.this, "No Internet!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void back(View v)
    {
        onBackPressed();
        String names="";
        Iterator<String> it=tenantsNames.iterator();
        while(it.hasNext())
        {
            names+=it.next();
            if(it.hasNext())
            {
                names+=", ";
            }
            else
                names+=".";
        }
        /*NotificationCompat.Builder notf;
        final int not_id=100008800;
        notf=new NotificationCompat.Builder(getApplicationContext());
        notf.setAutoCancel(true);
        notf.setSmallIcon(R.drawable.logo);
        notf.setTicker("Students Added");
        notf.setWhen(System.currentTimeMillis());
        notf.setContentTitle("New Tenants!");
        notf.setContentText(names);
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        i.putExtra("fromN",FROM_NOTIFICATION);
        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(),0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        notf.setContentIntent(pi);
        NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.notify(not_id,notf.build());*/


    }
    public void makeJson()
    {
        studentDetails=new JSONObject();
        try {
            if(LoginActivity.sharedPreferences.getString("token",null)==null)
            {
                throw new Exception("invalid token");
            }
            else {
                Log.i("date",datePickerText.getText().toString());
                studentDetails.put("checkinDate",datePickerText.getText().toString());
                studentDetails.put("name",sName);
                studentDetails.put("mobileNo",sNo);
                studentDetails.put("adharNo",sAad);
                studentDetails.put("roomId",_id);
                studentDetails.put("auth", LoginActivity.sharedPreferences.getString("token", null));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void checkIn(View v)
    {
        checkin.setClickable(false);
        sName=studentName.getText().toString();
        sNo=contactNo.getText().toString();
        sAad=aadharNo.getText().toString();
        if(sName.equals("")||sNo.equals("")||sAad.equals(""))
        {
            enable();
            Toast.makeText(this, "Missing Fields", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            bg.setClickable(false);
            makeJson();
            AddStudentsTask task=new AddStudentsTask();
            task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/addStudents",studentDetails.toString());
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
        setContentView(R.layout.activity_student); Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkin=(Button)findViewById(R.id.checkInButton);
        finish=(Button)findViewById(R.id.addExtraStudentButton);
        finish.setClickable(false);
        Intent i=getIntent();
        _id=i.getStringExtra("id");
        bg=(RelativeLayout)findViewById(R.id.background);
        progressBar=(ProgressBar)findViewById(R.id.studentAddingProgress);
        datePickerText=(TextView)findViewById(R.id.datePicker);
        Calendar c=Calendar.getInstance();
        String year= String.valueOf(c.get(Calendar.YEAR));
        String month= String.valueOf(c.get(Calendar.MONTH)+1);
        if(month.length()==1)
        {
            month="0"+month;
        }
        String day= String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        if(day.length()==1)
        {
            day="0"+day;
        }
        datePickerText.setText(year+"-"+month+"-"+day);
       // datePickerText.setText(year+"-"+month+"-"+day);
        String date=DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());
        //datePickerText.setText(date);
        datePickerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment df=new DatePickerFragment();
                df.show(getSupportFragmentManager(),"Choose Date");

            }
        });
        fromTotal=i.getBooleanExtra("fromTotal",false);
        setTitle("Room No: "+i.getStringExtra("roomNo"));
        studentName=(EditText)findViewById(R.id.studentNameInput);
        contactNo=(EditText)findViewById(R.id.studentContactNoInput);
        aadharNo=(EditText)findViewById(R.id.studentAadharNoInput);
        tenantsNames=new ArrayList<>();
        tenantsNames.clear();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


