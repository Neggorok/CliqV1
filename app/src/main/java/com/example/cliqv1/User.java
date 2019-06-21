package com.example.cliqv1;

import android.graphics.Bitmap;

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
    // sorgt nur dafür, dass unsere User immer mit den richtigen Werten erstellt werden, um so Probleme beim Registrieren und Einloggen zu vermeiden
    // und um die User besser verwalten zu können
    public User(String name, Bitmap image) {
        this.name = name;
        this.image = image;




    }

}
