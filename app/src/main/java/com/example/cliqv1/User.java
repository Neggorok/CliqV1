package com.example.cliqv1;

import android.graphics.Bitmap;

import java.util.Date;

public class User {

    String name;
    Bitmap image;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public User(String name, Bitmap image) {
        this.name = name;
        this.image = image;




    }

}
