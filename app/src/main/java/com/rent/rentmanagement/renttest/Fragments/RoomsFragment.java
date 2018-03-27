package com.rent.rentmanagement.renttest.Fragments;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.Adapters.OccupiedRoomsAdapter;
import com.rent.rentmanagement.renttest.Adapters.RecyclerAdapter;
import com.rent.rentmanagement.renttest.Adapters.TotalRoomsAdapter;
import com.rent.rentmanagement.renttest.AsyncTasks.GetRoomsTask;
import com.rent.rentmanagement.renttest.DataModels.RoomModel;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.ViewPagerAdapter;
import com.rent.rentmanagement.renttest.roomActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by imazjav0017 on 24-03-2018.
 */

public class RoomsFragment extends Fragment {
    Context context;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    public static RecyclerAdapter adapter;
    public static TotalRoomsAdapter adapter3;
    public static OccupiedRoomsAdapter adapter2;
    public static ArrayList<RoomModel> erooms;
    public static ArrayList<RoomModel> oRooms;
    public static ArrayList<RoomModel> tRooms;
    //android.support.v4.app.FragmentManager fragmentManager;
    public RoomsFragment() {
    }

    public RoomsFragment(Context context) {
        this.context = context;
    }
    public void refresh()
    {
        onResume();
    }
    public void setTokenJson()
    {
        try {
            if(LoginActivity.sharedPreferences.getString("token",null)!=null) {
                JSONObject token = new JSONObject();
                token.put("auth",LoginActivity.sharedPreferences.getString("token", null));
                GetRoomsTask task = new GetRoomsTask();
                String s=task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/getRooms", token.toString()).get();
                if (s != null) {
                    Log.i("getRooms", s);
                    try {
                        setData(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(context, "Please Check Your Internet Connection and try later!", Toast.LENGTH_SHORT).show();
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
    public void setData(String s) throws JSONException {
        erooms.clear();
        oRooms.clear();
        tRooms.clear();
        JSONObject jsonObject=new JSONObject(s);
        LoginActivity.sharedPreferences.edit().putInt("totalTenants",jsonObject.getInt("totalStudents")).apply();
        JSONArray array=jsonObject.getJSONArray("room");
        Log.i("array",array.toString());
        LoginActivity.sharedPreferences.edit().putInt("totalRooms",array.length()).apply();
        LoginActivity.sharedPreferences.edit().putString("totalIncome",String.valueOf(jsonObject.getInt("totalIncome"))).apply();
        LoginActivity.sharedPreferences.edit().putString("todayIncome",String.valueOf(jsonObject.getInt("todayIncome"))).apply();
        LoginActivity.sharedPreferences.edit().putString("collected",String.valueOf(jsonObject.getInt("collected"))).apply();
        ProfileFragment.setData();
        LoginActivity.sharedPreferences.edit().putString("roomsDetails",s).apply();
        if(array.length()==0)
        {

        }
        else {
            Toast.makeText(context, "Refreshed!", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < array.length(); i++) {
                JSONObject detail = array.getJSONObject(i);
                if(detail.getBoolean("isEmpty")==true) {
                    //empty rooms
                    erooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                            detail.getString("roomRent"), detail.getString("_id"),detail.getString("checkOutDate"),detail.getBoolean("isEmpty"),detail.getString("emptyDays")));
                    tRooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                            detail.getString("roomRent"), detail.getString("_id"),
                            detail.getString("checkOutDate"),detail.getBoolean("isEmpty"),detail.getString("emptyDays")));

                }
                else
                {
                    tRooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                            detail.getString("roomRent"),detail.getString("dueAmount"), detail.getString("_id"),detail.getString("dueDate")
                            ,detail.getBoolean("isEmpty"),detail.getBoolean("isRentDue"),detail.getString("emptyDays")));
                    if(detail.getBoolean("isRentDue")==true) {
                        oRooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                detail.getString("roomRent"), detail.getString("dueAmount"),
                                detail.getString("_id"), detail.getString("dueDate"), detail.getBoolean("isEmpty"), detail.getBoolean("isRentDue")
                                , detail.getString("dueDays")));
                    }



                }
            }
            adapter.notifyDataSetChanged();
            adapter2.notifyDataSetChanged();
            adapter3.notifyDataSetChanged();
           /* if(erooms.size()==0)
            {
                EmptyRoomsFragment.emptyList.setVisibility(View.VISIBLE);
                EmptyRoomsFragment.emptyList.setClickable(true);

            }
            else {
                EmptyRoomsFragment.emptyList.setVisibility(View.INVISIBLE);
                EmptyRoomsFragment.emptyList.setClickable(false);
            }*/
        }



    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View  v=inflater.inflate(R.layout.activity_room,container,false);
        erooms=new ArrayList<>();
        oRooms=new ArrayList<>();
        tRooms=new ArrayList<>();
        adapter=new RecyclerAdapter(erooms,context);
        adapter2=new OccupiedRoomsAdapter(oRooms,context);
        adapter3=new TotalRoomsAdapter(tRooms,context);
        tabLayout=(TabLayout)v.findViewById(R.id.tabLayout);
        viewPager=(ViewPager)v.findViewById(R.id.viewPager);
        viewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager(),context);
        viewPagerAdapter.addFragment(new TotalRoomsFragment(context),"All Rooms");
        viewPagerAdapter.addFragment(new EmptyRoomsFragment(context),"Empty Rooms");
        viewPagerAdapter.addFragment(new RentDueFragment(context),"Rent Due");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        setStaticData(LoginActivity.sharedPreferences.getString("roomsDetails",null));
        new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {
        setTokenJson();
    }
}, 0);
        
       // viewPagerAdapter.notifyDataSetChanged();
       // adapter.notifyDataSetChanged();
    }

    public void setStaticData(String s) {
        erooms.clear();
        oRooms.clear();
        tRooms.clear();
        if(s!=null) {
            if (s.equals("0")) {
                Toast.makeText(context, "Fetching!", Toast.LENGTH_SHORT).show();

            } else {


                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);

                    JSONArray array = jsonObject.getJSONArray("room");


                    //Log.i("arrayStatic", array.toString());
                    if (array.length() == 0) {

                    } else {

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject detail = array.getJSONObject(i);
                            if (detail.getBoolean("isEmpty") == true) {
                                //empty rooms
                                erooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                        detail.getString("roomRent"), detail.getString("_id"),
                                        detail.getString("checkOutDate"),detail.getBoolean("isEmpty"),detail.getString("emptyDays")));
                                tRooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                        detail.getString("roomRent"), detail.getString("_id"),
                                        detail.getString("checkOutDate"),detail.getBoolean("isEmpty"),detail.getString("emptyDays")));

                            }   else
                            {
                                tRooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                        detail.getString("roomRent"),detail.getString("dueAmount"), detail.getString("_id"),detail.getString("dueDate")
                                        ,detail.getBoolean("isEmpty"),detail.getBoolean("isRentDue"),detail.getString("emptyDays")));
                                if(detail.getBoolean("isRentDue")==true) {
                                    oRooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                            detail.getString("roomRent"), detail.getString("dueAmount"),
                                            detail.getString("_id"), detail.getString("dueDate"), detail.getBoolean("isEmpty"), detail.getBoolean("isRentDue")
                                            , detail.getString("dueDays")));
                                }



                            }
                        }
                        adapter.notifyDataSetChanged();
                        adapter2.notifyDataSetChanged();
                        adapter3.notifyDataSetChanged();
                    /*if(erooms.size()==0)
                    {
                        EmptyRoomsFragment.emptyList.setVisibility(View.VISIBLE);
                        EmptyRoomsFragment.emptyList.setClickable(true);

                    }
                    else {
                        EmptyRoomsFragment.emptyList.setVisibility(View.INVISIBLE);
                        EmptyRoomsFragment.emptyList.setClickable(false);
                    }*/
                    }
                } catch (Exception e) {
                    Log.i("err", "err");
                    e.printStackTrace();
                }
            }
        }

        /*else {
        EmptyRoomsFragment.emptyList.setVisibility(View.VISIBLE);
        EmptyRoomsFragment.emptyList.setClickable(true);
    }*/
    }
}
