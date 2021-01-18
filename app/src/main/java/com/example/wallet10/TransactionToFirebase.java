package com.example.wallet10;

import java.util.Date;

public class TransactionToFirebase {
    private String SenderPhone,ReceiverPhone,Amount,date;
    Date t;

    public TransactionToFirebase(String SenderPhone,String ReceiverPhone,String Amount,String date){
        this.Amount = Amount;
        this.ReceiverPhone = ReceiverPhone;
        this.SenderPhone = SenderPhone;
        this.date = date ;
    }
    public TransactionToFirebase(String ReceiverPhone,String Amount,String date,int r){
        this.Amount = Amount;
        this.ReceiverPhone = ReceiverPhone;
        this.date = date ;
    }
    public TransactionToFirebase(String SenderPhone,String Amount,String date){
        this.Amount = Amount;
        this.SenderPhone = SenderPhone;
        this.date = date ;
    }

    public String getSenderPhone() {
        return SenderPhone;
    }

    public String getReceiverPhone() {
        return ReceiverPhone;
    }

    public String getAmount() {
        return Amount;
    }

    public String getDate() {
        return date;
    }

    public Date getT() {
        return t;
    }
}
