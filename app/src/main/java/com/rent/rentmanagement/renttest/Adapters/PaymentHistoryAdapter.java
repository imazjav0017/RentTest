package com.rent.rentmanagement.renttest.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rent.rentmanagement.renttest.DataModels.PaymentHistoryModel;
import com.rent.rentmanagement.renttest.R;

import java.util.List;

/**
 * Created by imazjav0017 on 14-03-2018.
 */

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.PaymentHistoryViewHolder> {
    List<PaymentHistoryModel> paymentList;

    public PaymentHistoryAdapter(List<PaymentHistoryModel> paymentList) {
        this.paymentList = paymentList;
    }

    @Override
    public PaymentHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_history_list_item,parent,false);
        return new PaymentHistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PaymentHistoryViewHolder holder, int position) {
        PaymentHistoryModel model=paymentList.get(position);

        holder.date.setText(model.getDate());
        if(model.status==true)
        {
            holder.paymentHistory.setText("₹ "+model.getAmount()+"-"+model.getPayee());
            holder.status.setText("Paid");
            holder.status.setTextColor(Color.parseColor("#3230ff"));
        }
        else {
            holder.paymentHistory.setText("Not paid due to : "+model.getPayee());
            holder.status.setTextColor(Color.parseColor("#FFC20720"));
            holder.status.setText("Not Paid!");
        }
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    /**
     * Created by imazjav0017 on 14-03-2018.
     */

    public static class PaymentHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView paymentHistory;
        TextView date;
        TextView status;

        public PaymentHistoryViewHolder(View itemView) {
            super(itemView);
            paymentHistory=(TextView)itemView.findViewById(R.id.paymentHistory);
            date=(TextView)itemView.findViewById(R.id.paymentDateView);
            status=(TextView)itemView.findViewById(R.id.paymentStatus);
        }
    }
}
