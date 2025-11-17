package com.mb.kibeti;

public class DataClass {
    String id,name,age,gender;

    String cat;
    String line;
    String amount;
    String frequency;
    String inflowId;
    String status;
    String type;
    String trans_code;
    String recipient;
    String date_time;

    public DataClass(){

    };
    public DataClass(String id,String cat,String line,String amount,String frequency,String status){
        this.inflowId = id;
        this.cat = cat;
        this.line = line;
        this.amount = amount;
        this.frequency = frequency;
        this.status = status;
        this.type = status;
    }
    public DataClass(String id,String cat,String line,String amount,String frequency,String status,String type,String recipient){
        this.inflowId = id;
        this.cat = cat;
        this.line = line;
        this.amount = amount;
        this.frequency = frequency;
        this.status = status;
        this.type = type;
        this.recipient = recipient;
    }

    public DataClass(String id,String cat,String line,String amount,String frequency,String status,String type){
        this.inflowId = id;
        this.cat = cat;
        this.line = line;
        this.amount = amount;
        this.frequency = frequency;
        this.status = status;
        this.type = type;
    }
    public String getStatus() {
        return status;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getTrans_code() {
        return trans_code;
    }
    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setTrans_code(String trans_code) {
        this.trans_code = trans_code;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getType() {
        return type;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getInflowId() {
        return inflowId;
    }

    public void setInflowId(String inflowId) {
        this.inflowId = inflowId;
    }

    public String getId() {
        return inflowId;
    }

    public void setId(String id) {
        this.inflowId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
