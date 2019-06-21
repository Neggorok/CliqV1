package com.example.cliqv1;

import android.graphics.Bitmap;

public class Gruppen {

    String groupName;
    Bitmap groupImage;

    int groupId;

    public String getName() {
        return groupName;
    }

    public void setName(String name) {
        this.groupName = name;
    }

    public Bitmap getImage() {
        return groupImage;
    }

    public void setImage(Bitmap image) {
        this.groupImage = image;
    }

//    public int getGroupId() { return groupId; }
//
//    public void setGroupId(int groupId) { this.groupId = groupId; }

    // sorgt nur dafür, dass unsere Gruppen immer mit den richtigen Werten erstellt werden, um so Probleme beim erstellen vermieden werden
    public Gruppen(String name, Bitmap image) {
        this.groupName = name;
        this.groupImage = image;





    }

}
