package com.example.cliqv1;

import android.graphics.Bitmap;

public class GroupMessage {

    private String username;
    private String message;
    private String timestamp;
    private Bitmap userImage;


    public GroupMessage(String username, String message, String timestamp, Bitmap userImage) {
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
        this.userImage = userImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Bitmap getUserImage() {
        return userImage;
    }

    public void setUserImage(Bitmap userImage) {
        this.userImage = userImage;
    }
}
