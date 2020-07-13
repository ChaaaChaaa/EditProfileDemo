package com.practice.edituserprofiledemo.model;

public class UserAccountData {
    private String email;
    private String nickname;
    private String user_token;

//    UserAccountData(String email, String nickname, String user_token){
//        this.email = email;
//        this.nickname = nickname;
//        this.user_token = user_token;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }
}
