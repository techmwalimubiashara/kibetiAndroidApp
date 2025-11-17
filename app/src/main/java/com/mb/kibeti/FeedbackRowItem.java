package com.mb.kibeti;

public class FeedbackRowItem {
    String strAlpha, strBeta, strGamma, strDelta, strEpsilon, strZeta;

    public FeedbackRowItem(String strAlpha, String strBeta, String strGamma, String strDelta, String strEpsilon, String strZeta) {
        this.strAlpha = strAlpha;
        this.strBeta = strBeta;
        this.strGamma = strGamma;
        this.strDelta = strDelta;
        this.strEpsilon = strEpsilon;
        this.strZeta = strZeta;
    }

    public String getStrAlpha() {
        return strAlpha;
    }

    public String getStrBeta() {
        return strBeta;
    }

    public String getStrGamma() {
        return strGamma;
    }

    public String getStrDelta() {
        return strDelta;
    }

    public String getStrEpsilon() {
        return strEpsilon;
    }

    public String getStrZeta() {
        return strZeta;
    }

    @Override
    public String toString() {
        return strAlpha + strBeta + strGamma;
    }
}