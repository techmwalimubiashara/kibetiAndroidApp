package com.mb.kibeti;

public class SettingGoalRowItem  {
    private String strGoal, strAmount,strDate,strDaily,strMonthly,strWeekly,strQuarterly,strSemiAnnually,strAnnually;

    public SettingGoalRowItem(String strGoal, String strAmount, String strDate, String strDaily, String strWeekly, String strMonthly,String strQuarterly,String strSemiAnnually,String strAnnually) {
        this.strGoal = strGoal;
        this.strAmount = strAmount;
        this.strDate = strDate;
        this.strDaily = strDaily;
        this.strMonthly = strMonthly;
        this.strWeekly = strWeekly;
        this.strSemiAnnually = strSemiAnnually;
        this.strAnnually = strAnnually;
        this.strQuarterly = strQuarterly;

    }

    public String getStrGoal() {
        return strGoal;
    }

    public void setStrGoal(String strGoal) {
        this.strGoal = strGoal;
    }

    public String getStrAmount() {
        return strAmount;
    }

    public void setStrAmount(String strAmount) {
        this.strAmount = strAmount;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getStrDaily() {
        return strDaily;
    }

    public void setStrDaily(String strDaily) {
        this.strDaily = strDaily;
    }

    public String getStrMonthly() {
        return strMonthly;
    }

    public String getStrQuarterly() {
        return strQuarterly;
    }

    public void setStrMonthly(String strMonthly) {
        this.strMonthly = strMonthly;
    }

    public void setStrQuarterly(String strQuarterly) {
        this.strQuarterly = strQuarterly;
    }

    public String getStrWeekly() {
        return strWeekly;
    }

    public void setStrWeekly(String strWeekly) {
        this.strWeekly = strWeekly;
    }

    public String getStrSemiAnnually() {
        return strSemiAnnually;
    }

    public void setStrSemiAnnually(String strSemiAnnually) {
        this.strSemiAnnually = strSemiAnnually;
    }

    public String getStrAnnually() {
        return strAnnually;
    }

    public void setStrAnnually(String strAnnually) {
        this.strAnnually = strAnnually;
    }
}