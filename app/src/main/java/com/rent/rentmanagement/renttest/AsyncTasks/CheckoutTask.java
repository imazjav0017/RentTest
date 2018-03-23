package com.rent.rentmanagement.renttest.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by imazjav0017 on 22-03-2018.
 */

public class CheckoutTask extends AsyncTask<String,Void,String>
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
            Log.i("checkoutResp",String.valueOf(resp));
            if(resp==422)
            {
                return "First clear Dues!";
            }
            else if(resp==200)
            {
                return "checked out from Room";
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

   /* @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            Toast.makeText(roomDetailActivity.this,s,Toast.LENGTH_SHORT).show();
            if(s.equals("checked out from Room"))
            {
                onBackPressed();
            }
        }
        else
        {

            Toast.makeText(roomDetailActivity.this, "Please Check Your Internet Connection and try later!", Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
        }
    }*/
}
