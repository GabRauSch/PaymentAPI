package com.example.models;

public class Payment {
    private String amount;
    private String beneficiary;
    private String sender;
    
    public String getAmount(){
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getSender(){
        return sender;
    }

    public void setSender(String sender){
        this.sender = sender;
    }
}
