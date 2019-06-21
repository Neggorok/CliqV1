package com.example.cliqv1;

import android.graphics.Bitmap;

public class Message {

    private String username;
    private String message;
    private String timestamp;
    private Bitmap userImage;
    private String messageID;



    // sorgt nur dafür, dass die einzelnen Messages immer mit den richtigen Werten erstellt werden, um so Probleme beim Erzeugen zu vermeiden
    public Message(String username, String message, String timestamp, Bitmap userImage, String messageid) {
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
        this.userImage = userImage;
        this.messageID = messageid;
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

    public String getMessageID() { return messageID; }

    public void setMessageID(String messageID) { this.messageID = messageID; }
}
