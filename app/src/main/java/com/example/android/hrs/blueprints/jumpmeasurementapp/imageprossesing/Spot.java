package com.example.android.hrs.blueprints.jumpmeasurementapp.imageprossesing;

import java.util.UUID;

public class Spot {

    // privateでいいのか？？
    private String mId;      //
    private String mName;  //
    private String mCity;   //
    private String mUrl;


    public Spot(String name, String city, String url) {
        // 自動作成のIDみたいなのと考えています。
        mId = UUID.randomUUID().toString();
        mName = name;
        mCity = city;
        mUrl =url;
    }

    public String getName(){
        return  mName;
    }

    public String getCity(){
        return  mCity;
    }

    public String getURL(){
        return  mUrl;
    }






}
