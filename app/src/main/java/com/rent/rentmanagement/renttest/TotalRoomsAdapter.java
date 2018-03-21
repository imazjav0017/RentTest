package com.rent.rentmanagement.renttest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by imazjav0017 on 18-03-2018.
 */

public class TotalRoomsAdapter extends RecyclerView.Adapter<TotalRoomsHolder> {
    List<RoomModel> roomList;
    JSONObject rentdetails;
    Context context;

    public TotalRoomsAdapter(List<RoomModel> roomList, Context context) {
        this.roomList = roomList;
        this.context = context;
    }

    @Override
    public TotalRoomsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.total_rooms_list,parent,false);
        return new TotalRoomsHolder(v);
    }

    @Override
    public void onBindViewHolder(final TotalRoomsHolder holder, int position) {
        final RoomModel model=roomList.get(position);

        if(model.isEmpty==false)
        {
            holder.roomNo.setText("Room No. "+model.getRoomNo());
            holder.amount.setText("Due Amount: \u20B9"+model.getDueAmount());
            holder.date.setText(model.getCheckInDate());
            holder.roomType.setText(", "+model.getRoomType()+" ,");
            if(model.isRentDue==false)
            {
                holder.checkIn.setText("CheckOut");
                holder.status.setText("Paid");
                holder.statusBar.setBackgroundColor(Color.parseColor("#0ed747"));
            }
            else
            {

                holder.checkIn.setText("Collect");
                holder.status.setText("Rent Due");
                if(model.getDueAmount().equals(model.getRoomRent()))
                holder.statusBar.setBackgroundColor(Color.parseColor("#D32F2F"));
                else
                    holder.statusBar.setBackgroundColor(Color.parseColor("#b2df3e"));
            }
        }
        else
        {
            holder.date.setText(model.getCheckInDate());
            holder.roomNo.setText("Room No. "+model.getRoomNo());
            holder.roomType.setText(", "+model.getRoomType()+" ,1st floor,");
            holder.amount.setText(" \u20B9"+model.getRoomRent());
            holder.checkIn.setText("CheckIn");
            holder.status.setText("Vacant");
           holder.statusBar.setBackgroundColor(Color.parseColor("#000000"));
        }
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(holder.context,roomDetailActivity.class);
                i.putExtra("id",model.get_id());
                i.putExtra("roomNo",model.getRoomNo());
                i.putExtra("roomType",model.getRoomType());
                i.putExtra("roomRent",model.getRoomRent());
                i.putExtra("fromTotal",true);
                holder.context.startActivity(i);
            }
        });
        holder.checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(holder.checkIn.getText().toString())
                {
                    case "CheckIn":
                        Intent i=new Intent(holder.context,StudentActivity.class);
                        i.putExtra("id",model.get_id());
                        i.putExtra("roomNo",model.getRoomNo());
                        i.putExtra("fromTotal",true);
                        holder.context.startActivity(i);
                        break;
                    case "Collect":
                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        View view=LayoutInflater.from(context).inflate(R.layout.collect_dialog,null,false);
                        final EditText rentCollectedInput=(EditText)view.findViewById(R.id.rentcollectedinput);
                        final EditText payee=(EditText)view.findViewById(R.id.payee);
                        rentCollectedInput.setText(model.getDueAmount());
                        rentCollectedInput.setSelection(rentCollectedInput.getText().toString().length());
                        Button collectedButton=(Button)view.findViewById(R.id.collectedbutton);
                        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
                        Date dateObj=new Date();
                        String date=dateFormat.format(dateObj).toString();
                        collectedButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                makeJson(model.get_id(),payee,rentCollectedInput,"c",null);
                                PaymentTask task=new PaymentTask();
                                try {
                                    String response=task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/paymentDetail",rentdetails.toString()).get();
                                    if(response!=null)
                                    {
                                        Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
                                        goBack(holder.context);
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        setStaticData(LoginActivity.sharedPreferences.getString("roomsDetails",null),payee,model.get_id());
                        TextView dateCollected=(TextView)view.findViewById(R.id.datecollectedinput);
                        dateCollected.setText(date);
                        builder.setView(view);
                        AlertDialog dialog=builder.create();
                        dialog.show();
                        break;
                    
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }
    public void setStaticData(String s, EditText payee, String _id) {
        if(s!=null) {
            if (s.equals("0")) {
                Toast.makeText(context, "Fetching!", Toast.LENGTH_SHORT).show();

            } else {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("room");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject detail = array.getJSONObject(i);
                        if(detail.getBoolean("isEmpty")==false && detail.getString("_id").equals(_id))
                        {
                            JSONArray students=detail.getJSONArray("students");
                            if(students.length()>0)
                            {
                                JSONObject studentDetails=students.getJSONObject(0);
                                payee.setText(studentDetails.getString("name"));
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void makeJson(String _id,EditText payee,EditText rentCollectedInput,String mode,String reason)
    {
        rentdetails=new JSONObject();
        try {

            if(LoginActivity.sharedPreferences.getString("token",null)==null)
            {
                throw new Exception("invalid token");
            }
            else {
                rentdetails.put("roomId",_id);
                rentdetails.put("auth", LoginActivity.sharedPreferences.getString("token", null));
                if(mode.equals("c")) {
                    rentdetails.put("payee", payee.getText().toString());
                    rentdetails.put("amount", Integer.parseInt(rentCollectedInput.getText().toString()));
                }else if(mode.equals("r"))
                {
                    Log.i("reason",reason);
                    rentdetails.put("reason",reason);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    void goBack(Context context)
    {
        Intent i=new Intent(context,AllRoomsActivity.class);
        roomActivity.mode=1;
        context.startActivity(i);
    }
}
