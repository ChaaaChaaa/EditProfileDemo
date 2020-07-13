package com.practice.edituserprofiledemo.model.entity;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ApiResultDto {
    @SerializedName("resultCode")
    private int resultCode;
    @SerializedName("resultData")
    private JsonObject resultData; //json으로 받기에
    @SerializedName("message")
    private String message;

    public int getResultCode(){
        return resultCode;
    }

    public void setResultCode(int resultCode){
        this.resultCode = resultCode;
    }

    public JsonObject getResultData(){
        return resultData;
    }

    public void setResultData(JsonObject resultData){
        this.resultData = resultData;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
