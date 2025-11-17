package com.mb.kibeti;

public class InvestmentItem {

    private String invest_name;
    private String invest_desc;
    public InvestmentItem(){

    };
    public InvestmentItem(String invest_name, String invest_desc){
        this.invest_name = invest_name;
        this.invest_desc = invest_desc;
    };



    public String getInvest_name() {
        return invest_name;
    }

    public void setInvest_name(String invest_name) {
        this.invest_name = invest_name;
    }

    public String getInvest_desc() {
        return invest_desc;
    }

    public void setInvest_desc(String invest_desc) {
        this.invest_desc = invest_desc;
    }
}
