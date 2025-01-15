package com.theikdi.shwethike.model;

public class User {
    String username;
    String password;
    String secret_code;

    public User(String username, String password, String secret_code){
        this.username = username;
        this.password = password;
        this.secret_code = secret_code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecret_code() {
        return secret_code;
    }

    public void setSecret_code(String secret_code) {
        this.secret_code = secret_code;
    }
}
