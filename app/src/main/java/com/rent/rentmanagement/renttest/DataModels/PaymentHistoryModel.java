package com.rent.rentmanagement.renttest.DataModels;

/**
 * Created by imazjav0017 on 14-03-2018.
 */

public class PaymentHistoryModel {
    String payee,amount,date;
    public boolean status;


    public PaymentHistoryModel(String payee, String amount, String date,boolean status) {
        this.payee = payee;
        this.amount = amount;
        this.date = date;
        this.status=status;
    }

    public PaymentHistoryModel(String payee, String date, boolean status) {
        this.payee = payee;
        this.date = date;
        this.status = status;
    }

    public String getPayee() {
        return payee;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
