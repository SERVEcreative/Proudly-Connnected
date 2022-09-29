package com.servecreative.myapplication;

public class Userdata {

    String name;
    String phone;
    String uId;

    public Userdata(){}

    public Userdata(String name, String phone, String uId) {
        this.name = name;
        this.phone = phone;
        this.uId = uId;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
