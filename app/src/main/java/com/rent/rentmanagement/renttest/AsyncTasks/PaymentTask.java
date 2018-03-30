package com.rent.rentmanagement.renttest.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.Adapters.OccupiedRoomsAdapter;
import com.rent.rentmanagement.renttest.Fragments.RoomsFragment;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by imazjav0017 on 21-03-2018.
 */
 public class PaymentTask extends AsyncTask<String, Void, String> {
Context context;
android.app.AlertDialog dialog;
Button btn;

    public PaymentTask(Context context, android.app.AlertDialog dialog, Button btn) {
        this.context = context;
        this.dialog = dialog;
        this.btn = btn;
    }

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
                Log.i("rentCollectedResp", String.valueOf(resp));
                if (resp == 200) {
                    return "success";
                } else
                    return "Some Error,check if fields are missings!";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (response != null) {
            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
            if(response.equals("Some Error,check if fields are missings!"))
                enable(btn);
            else {
                dialog.dismiss();
                goBack(context);
            }
        }
        else
        {
            enable(btn);
            Toast.makeText(context, "No Internet!", Toast.LENGTH_SHORT).show();
        }
    }
    public static void goBack(Context context)
    {
        new RoomsFragment(context).onResume();

    }
    public static void enable(Button btn)
    {
        btn.setClickable(true);
    }
}

