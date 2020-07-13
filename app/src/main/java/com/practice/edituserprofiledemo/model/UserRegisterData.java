package com.practice.edituserprofiledemo.model;

public class UserRegisterData {
    private String email;
    private String password;
    private String nickname;

//    public UserRegisterData(String email, String nickname,String password){
//        this.email = email;
//        this.password = password;
//        this.nickname = nickname;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickname;
    }

    public void setNickName(String nickName) {
        this.nickname = nickname;
    }
}
