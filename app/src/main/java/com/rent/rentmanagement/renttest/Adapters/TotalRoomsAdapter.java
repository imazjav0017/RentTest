package com.rent.rentmanagement.renttest.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.rent.rentmanagement.renttest.AsyncTasks.CheckoutTask;
import com.rent.rentmanagement.renttest.Fragments.RoomsFragment;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.AsyncTasks.PaymentTask;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.DataModels.RoomModel;
import com.rent.rentmanagement.renttest.StudentActivity;
import com.rent.rentmanagement.renttest.roomActivity;
import com.rent.rentmanagement.renttest.roomDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by imazjav0017 on 18-03-2018.
 */

public class TotalRoomsAdapter extends RecyclerView.Adapter<TotalRoomsAdapter.TotalRoomsHolder> {
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
            holder.roomType.setText(", "+model.getRoomType());
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
                        final Button collectedButton=(Button)view.findViewById(R.id.collectedbutton);
                        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
                        Date dateObj=new Date();
                        String date=dateFormat.format(dateObj).toString();
                        setStaticData(LoginActivity.sharedPreferences.getString("roomsDetails",null),payee,model.get_id());
                        TextView dateCollected=(TextView)view.findViewById(R.id.datecollectedinput);
                        dateCollected.setText(date);
                        builder.setView(view);
                        final AlertDialog dialog=builder.create();
                        dialog.show();
                        collectedButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                makeJson(model.get_id(),payee,rentCollectedInput,"c",null);
                                collectedButton.setClickable(false);
                                PaymentTask task=new PaymentTask();
                                try {
                                    String response=task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/paymentDetail",rentdetails.toString()).get();
                                    if(response!=null)
                                    {
                                        Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
                                        if(response.equals("Some Error,check if fields are missings!"))
                                            enable(collectedButton);
                                        else {
                                            dialog.dismiss();
                                            goBack(holder.context);
                                        }

                                    }
                                    else
                                    {
                                        enable(collectedButton);
                                        Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        break;
                    case "CheckOut":
                        new AlertDialog.Builder(context)
                                .setTitle("Delete!").setMessage("Are You Sure You Wish To Checkout from Room No "+model.getRoomNo()+"?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        setTokenJson("ch",model.get_id());
                                    }
                                })
                                .setNegativeButton("No",null).show();
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
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        rentdetails=new JSONObject();
        try {

            if(LoginActivity.sharedPreferences.getString("token",null)==null)
            {
                throw new Exception("invalid token");
            }
            else {
                rentdetails.put("roomId",_id);
                rentdetails.put("auth", LoginActivity.sharedPreferences.getString("token", null));
                dateFormat.format(new Date()).toString();
                if(mode.equals("c")) {
                    rentdetails.put("payee", payee.getText().toString());
                    rentdetails.put("amount", Integer.parseInt(rentCollectedInput.getText().toString()));
                    dateFormat.format(new Date()).toString();
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
    public void setFilter(List<RoomModel> filteredList)
    {
        roomList=new ArrayList<>();
        roomList.addAll(filteredList);
        notifyDataSetChanged();
    }
    void goBack(Context context)
    {
       // RoomsFragment ob=new RoomsFragment();
        //ob.refresh();
        new RoomsFragment(context).onResume();
    }
    void enable(Button btn)
    {
        btn.setClickable(true);
    }
    public void setTokenJson(String mode,String _id)
    {
        try {
            if(LoginActivity.sharedPreferences.getString("token",null)!=null) {
                JSONObject token = new JSONObject();
                token.put("auth",LoginActivity.sharedPreferences.getString("token", null));
                token.put("roomId",_id);
               /* if(mode.equals("delete")) {
                    roomDetailActivity.DeleteRoomsTask task = new roomDetailActivity.DeleteRoomsTask();
                    task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/deleteRooms", token.toString());
                }*/
                if(mode.equals("ch"))
                {
                    CheckoutTask task = new CheckoutTask();
                    String s=task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/vacateRooms", token.toString()).get();
                    if (s != null) {
                        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
                        if(s.equals("checked out from Room"))
                        {
                           goBack(context);
                        }
                    }
                    else
                    {
                        Toast.makeText(context, "Please Check Your Internet Connection and try later!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Created by imazjav0017 on 18-03-2018.
     */

    public static class TotalRoomsHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;
        LinearLayout statusBar;
        TextView roomNo;
        TextView roomType;
        TextView amount;
        TextView date;
        TextView status;
        Context context;
        Button checkIn;
        public TotalRoomsHolder(View itemView) {
            super(itemView);
            ll=(LinearLayout)itemView.findViewById(R.id.TotalLl);
            statusBar=(LinearLayout)itemView.findViewById(R.id.totalStatusBar);
            context=itemView.getContext();
            date=(TextView)itemView.findViewById(R.id.totalCheckInDate);
            roomNo=(TextView)itemView.findViewById(R.id.totalRoomNo);
            status=(TextView)itemView.findViewById(R.id.TotalStatus);
            roomType=(TextView)itemView.findViewById(R.id.totalRoomType);
            amount=(TextView)itemView.findViewById(R.id.TotalRentToBeCollected);
            checkIn=(Button)itemView.findViewById(R.id.totalCheckinButton);
        }
    }
}
