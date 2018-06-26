package com.rent.rentmanagement.renttest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class studentProfile extends AppCompatActivity {
String _id,name,phNo,roomNo,adhaarNo;
    boolean from;
    EditText sRoomNo,sPhNo,sAadharNo,sName;
    Button edit,delete;
    String response;
    public class EditStudentsTask extends AsyncTask<String,Void,String>
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
                Log.i("editStudentsResp",String.valueOf(resp));
                if(resp==200)
                {
                    response=getResponse(connection);
                    return response;
                }
                else
                {
                    return null;
                }

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
            if (s != null) {
                Toast.makeText(getApplicationContext(),"Saved Changes",Toast.LENGTH_SHORT).show();
            }
            else
            {
                enable(edit);
                Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection and try later!", Toast.LENGTH_SHORT).show();
                super.onPostExecute(s);
            }
        }
    }
    public String  getResponse(HttpURLConnection connection)
    {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            StringBuffer sb = new StringBuffer("");
            String line = "";

            while ((line = in.readLine()) != null) {

                sb.append(line);
                break;
            }

            in.close();
            return sb.toString();
        }catch(Exception e)
        {
            return e.getMessage();
        }
    }
    void setTokenJson(String mode)
    {
        try {
        JSONObject token = new JSONObject();
        token.put("auth", LoginActivity.sharedPreferences.getString("token", null));
        token.put("studentId", _id);

        if(mode.equals("e"))
        {
            if(LoginActivity.sharedPreferences.getString("token",null)!=null) {
                    if(roomNo.equals("")||name.equals("")||adhaarNo.equals("")||phNo.equals(""))
                    {
                        enable(edit);
                        Toast.makeText(getApplicationContext(), "Missing Fields!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        token.put("name",name);
                        token.put("adharNo",adhaarNo);
                        token.put("mobileNo",phNo);
                        EditStudentsTask task=new EditStudentsTask();
                        task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/editStudents", token.toString());
                    }
                }

        }
        else
        {

        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void enable(Button b)
    {
        b.setClickable(true);
    }
    public void editStudent(View v)
    {
        edit.setClickable(false);
        name=sName.getText().toString();
        phNo=sPhNo.getText().toString();
        adhaarNo=sAadharNo.getText().toString();
        setTokenJson("e");
    }
    public void deleteStudent(View v)
    {
        setTokenJson("d");
    }
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
        adhaarNo=i.getStringExtra("aadharNo");
        Log.i("aadharNo",adhaarNo);
        sName=(EditText) findViewById(R.id.studentNameField);
        sRoomNo=(EditText)findViewById(R.id.studentRoomNoField);
        sPhNo=(EditText)findViewById(R.id.studentPhNoField);
        sAadharNo=(EditText)findViewById(R.id.studentAadharNoField);
        edit=(Button)findViewById(R.id.editStudent);
        delete=(Button)findViewById(R.id.deleteStudent);
        sName.setText(name);
        sRoomNo.setText(roomNo);
        sPhNo.setText(phNo);
        sAadharNo.setText(adhaarNo);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
