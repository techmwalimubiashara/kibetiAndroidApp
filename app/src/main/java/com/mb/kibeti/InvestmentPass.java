package com.mb.kibeti;

import java.io.Serializable;

public class InvestmentPass implements Serializable {
    private String invest_name;
    private  String amount;
    private String freq;

    public InvestmentPass(String invest_name, String amount, String freq) {
        this.invest_name = invest_name;
        this.amount = amount;
        this.freq = freq;
    }

    public String getInvest_name() {
        return invest_name;
    }

    public void setInvest_name(String invest_name) {
        this.invest_name = invest_name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }
}
