package com.theikdi.shwethike.APIResponse;

public class LoginResponse {
    String message;
    String username;
    int shopId;
    int userId;

    public LoginResponse(String message, String username, int shopId, int userId){
        this.message = message;
        this.username = username;
        this.shopId = shopId;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
