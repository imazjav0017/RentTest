package com.rent.rentmanagement.renttest.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.roomActivity;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by imazjav0017 on 24-03-2018.
 */

public class GetRoomsTask extends AsyncTask<String,Void,String>
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
            Log.i("getRoomsResp",String.valueOf(resp));
            if(resp==200)
            {
               String response=getResponse(connection);
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


}