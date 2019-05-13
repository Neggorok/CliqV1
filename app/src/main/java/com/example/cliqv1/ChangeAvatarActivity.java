package com.example.cliqv1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ChangeAvatarActivity extends AppCompatActivity {

    ImageView imageView;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avatar);

        imageView = (ImageView)findViewById(R.id.profileImageView);
        imageButton = (ImageButton)findViewById(R.id.imageButton1);
        imageButton = (ImageButton)findViewById(R.id.imageButton2);
        imageButton = (ImageButton)findViewById(R.id.imageButton3);

    }
}
