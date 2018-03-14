package com.rent.rentmanagement.renttest;

/**
 * Created by imazjav0017 on 14-03-2018.
 */

public class PaymentHistoryModel {
    String payee,amount,date;

    public PaymentHistoryModel(String payee, String amount, String date) {
        this.payee = payee;
        this.amount = amount;
        this.date = date;
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
