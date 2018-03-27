package com.rent.rentmanagement.renttest.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.Fragments.RoomsFragment;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.AsyncTasks.PaymentTask;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.DataModels.RoomModel;
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
 * Created by imazjav0017 on 12-02-2018.
 */
class ViewHolder2 extends RecyclerView.ViewHolder {
    LinearLayout ll;
    LinearLayout status;
    TextView roomNo;
    TextView roomType;
    TextView amount;
    TextView date;
    Context context;
    Button reason;
    Button collect;
    TextView dueDays;
    public ViewHolder2(View itemView) {
        super(itemView);
        ll=(LinearLayout)itemView.findViewById(R.id.ocRoomLl);
        status=(LinearLayout)itemView.findViewById(R.id.statusRoom);
        context=itemView.getContext();
        date=(TextView)itemView.findViewById(R.id.checkInDate);
        dueDays=(TextView)itemView.findViewById(R.id.dueDays);
        roomNo=(TextView)itemView.findViewById(R.id.roomNoOccupiedop);
        roomType=(TextView)itemView.findViewById(R.id.roomTypeOcc);
        amount=(TextView)itemView.findViewById(R.id.rentToBeCollected);
        reason=(Button)itemView.findViewById(R.id.reason);
        collect=(Button)itemView.findViewById(R.id.collectingButton);
    }
}

public class OccupiedRoomsAdapter extends RecyclerView.Adapter<ViewHolder2> {
    List<RoomModel> roomList;
    Context context;
    JSONObject rentdetails;
    public OccupiedRoomsAdapter(List<RoomModel> roomList, Context context) {
        this.roomList = roomList;
        this.context = context;
    }

    @Override
    public ViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutway,parent,false);
        return new ViewHolder2(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder2 holder, int position) {
        final RoomModel model=roomList.get(position);
        holder.roomNo.setText("Room No. "+model.getRoomNo());
        holder.amount.setText("Due Amount: \u20B9"+model.getDueAmount());
        holder.date.setText(model.getCheckInDate());
        holder.dueDays.setText(model.getDays()+" left!");
        holder.roomType.setText(", "+model.getRoomType()+",");

        holder.roomType.setText(", "+model.getRoomType()+",");
        if(model.isEmpty==false)
        {
            if(model.getDueAmount().equals(model.getRoomRent()))
            {
                holder.status.setBackgroundColor(Color.parseColor("#D32F2F"));
            }
            else
            {
                holder.status.setBackgroundColor(Color.parseColor("#b2df3e"));
            }
        }
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(holder.context,roomDetailActivity.class);
                i.putExtra("id",model.get_id());
                i.putExtra("roomNo",model.getRoomNo());
                i.putExtra("roomType",model.getRoomType());
                i.putExtra("roomRent",model.getRoomRent());
                holder.context.startActivity(i);
            }
        });
        holder.reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // roomActivity.reasonPage.setVisibility(View.VISIBLE);
                //roomActivity.isVisible=true;
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                View view=LayoutInflater.from(context).inflate(R.layout.reason_dialog,null,false);
                final EditText input=(EditText)view.findViewById(R.id.reasonInputText);
                final Button btn=(Button)view.findViewById(R.id.reasonButtonDialog);
                builder.setView(view);
                final AlertDialog dialog=builder.create();
                dialog.show();
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btn.setClickable(false);
                        String reason=input.getText().toString();
                        if(reason.isEmpty()) {
                            enable(btn);
                            Toast.makeText(context, "Please Give A Reason..", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            makeJson(model.get_id(), null, null, "r", reason);
                            PaymentTask task = new PaymentTask();
                            try {
                                String response = task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/paymentDetail", rentdetails.toString()).get();
                                if (response != null) {
                                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                                    if(response.equals("Some Error,check if fields are missings!"))
                                        enable(btn);
                                    else {
                                        dialog.dismiss();
                                        goBack(holder.context);
                                    }
                                }
                                else
                                {
                                    enable(btn);
                                    Toast.makeText(context, "No Internet!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

            }
        });
        holder.collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        collectedButton.setClickable(false);
                        makeJson(model.get_id(),payee,rentCollectedInput,"c",null);
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

            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }
    public void setFilter(List<RoomModel> filteredList)
    {
        roomList=new ArrayList<>();
        roomList.addAll(filteredList);
        notifyDataSetChanged();
    }
    public void setStaticData(String s,EditText payee,String _id) {
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
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");

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
                   rentdetails.put("date",dateFormat.format(new Date()).toString());
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
        new RoomsFragment(context).onResume();

    }
    void enable(Button btn)
    {
        btn.setClickable(true);
    }
}
