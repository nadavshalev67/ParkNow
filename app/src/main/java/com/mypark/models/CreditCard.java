package com.mypark.models;

import com.google.gson.Gson;

public class CreditCard {

    public int fourLastDigits;
    public boolean isChecked;


    public CreditCard(int fourLastDigits, boolean isChecked) {
        this.fourLastDigits = fourLastDigits;
        this.isChecked = isChecked;
    }
}
