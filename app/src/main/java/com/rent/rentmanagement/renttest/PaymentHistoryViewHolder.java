package com.rent.rentmanagement.renttest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by imazjav0017 on 14-03-2018.
 */

public class PaymentHistoryViewHolder extends RecyclerView.ViewHolder {
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
