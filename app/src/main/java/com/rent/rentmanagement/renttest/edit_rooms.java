package com.rent.rentmanagement.renttest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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

public class edit_rooms extends AppCompatActivity {
String roomNo,roomType,roomRent,_id,response,type;
    EditText roomNoInput,roomRentInput;
    RadioGroup newRoomType;
    boolean isChanged=false,from;
    Button edit;

    void enableButton(Button btn,boolean val)
    {
        btn.setClickable(val);
    }
    @Override
    public void onBackPressed() {
        if(isChanged) {
                Intent i = new Intent(getApplicationContext(), roomDetailActivity.class);
                i.putExtra("id", _id);
                i.putExtra("roomNo", roomNo);
                i.putExtra("roomType", roomType);
                i.putExtra("roomRent", roomRent);
            if(from)
                i.putExtra("fromTotal",from);
                startActivity(i);
                finish();

        }
        else
            super.onBackPressed();
    }

    public void editDetails(View v)
    {
        enableButton(edit,false);
        roomNo=roomNoInput.getText().toString();
        roomRent=roomRentInput.getText().toString();
        roomType=type;
        try {
            if(LoginActivity.sharedPreferences.getString("token",null)!=null) {
                if(roomNo.equals("")||roomRent.equals(""))
                {
                    enableButton(edit,true);
                    Toast.makeText(getApplicationContext(), "Missing Fields!", Toast.LENGTH_SHORT).show();
                }
                else {
                    JSONObject token = new JSONObject();
                    token.put("auth", LoginActivity.sharedPreferences.getString("token", null));
                    token.put("roomId", _id);
                    token.put("roomNo",roomNo);
                    token.put("roomType",roomType);
                    token.put("roomRent",roomRent);
                    EditTask task = new EditTask();
                    task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/editRooms", token.toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public class EditTask extends AsyncTask<String,Void,String>
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
                Log.i("editResp",String.valueOf(resp));
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
                Toast.makeText(edit_rooms.this,"Saved Changes",Toast.LENGTH_SHORT).show();
                isChanged=true;
                onBackPressed();
            }
            else
            {
                enableButton(edit,true);
                Toast.makeText(edit_rooms.this, "Please Check Your Internet Connection and try later!", Toast.LENGTH_SHORT).show();
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rooms);
        Intent i=getIntent();
        _id=i.getStringExtra("id");
        roomNo=i.getStringExtra("roomNo");
        roomType=i.getStringExtra("roomType");
        roomRent=i.getStringExtra("roomRent");
        from=i.getBooleanExtra("fromTotal",false);
        edit=(Button)findViewById(R.id.editButtonRoom);
        roomNoInput=(EditText)findViewById(R.id.newRoomNoInput);
        roomRentInput=(EditText)findViewById(R.id.newRoomRentInput);
        newRoomType=(RadioGroup)findViewById(R.id.newRoomTypeGroup);
        roomNoInput.setText(roomNo);
        roomRentInput.setText(roomRent);
        roomNoInput.setSelection(roomNo.length());
        roomRentInput.setSelection(roomRent.length());
        switch (roomType)
        {
            case "Single":
                newRoomType.check(R.id.sin);
                type="Single";
                break;
            case "Double":
                newRoomType.check(R.id.doub);
                type="Double";
                break;
            case "Triple":
                newRoomType.check(R.id.trip);
                type="Triple";
                break;

        }
        newRoomType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId)
                {
                    case R.id.sin:
                        type="Single";
                        break;
                    case R.id.doub:
                        type="Double";
                        break;
                    case R.id.trip:
                        type="Triple";
                        break;
                }
            }
        });

    }
}
