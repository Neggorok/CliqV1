package com.example.cliqv1;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ChangeAvatarActivity extends AppCompatActivity {

    ImageView imageView;
    ImageButton imageButton1;
    ImageButton imageButton2;
    ImageButton imageButton3;
    ImageButton imageButton4;
    ImageButton imageButton5;
    ImageButton imageButton6;
    ImageButton imageButton7;
    ImageButton imageButton8;

    Button button;

    //private static final int IMAGE_PICK_CODE = 1000;
    //private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avatar);

        imageView = findViewById(R.id.profileUnknown);
        imageButton1 = findViewById(R.id.imageButton1);
        imageButton2 = findViewById(R.id.imageButton2);
        imageButton3 = findViewById(R.id.imageButton3);
        imageButton4 = findViewById(R.id.imageButton4);
        imageButton5 = findViewById(R.id.imageButton5);
        imageButton6 = findViewById(R.id.imageButton6);
        imageButton7 = findViewById(R.id.imageButton7);
        imageButton8 = findViewById(R.id.imageButton8);
        button = findViewById(R.id.safe);



            imageButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = findViewById(R.id.profileUnknown);
                    imageView.setImageResource(R.drawable.cliq_avatar1);

                }
            });

            imageButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = findViewById(R.id.profileUnknown);
                    imageView.setImageResource(R.drawable.cliq_avatar2);

                }
            });
            imageButton3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = findViewById(R.id.profileUnknown);
                    imageView.setImageResource(R.drawable.cliq_avatar3);

                }
            });
            imageButton4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = findViewById(R.id.profileUnknown);
                    imageView.setImageResource(R.drawable.cliq_avatar4);

                }
            });
            imageButton5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = findViewById(R.id.profileUnknown);
                    imageView.setImageResource(R.drawable.cliq_avatar7);

                }
            });
            imageButton6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = findViewById(R.id.profileUnknown);
                    imageView.setImageResource(R.drawable.cliq_avatar6);

                }
            });
            imageButton7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = findViewById(R.id.profileUnknown);
                    imageView.setImageResource(R.drawable.cliq_avatar5);

                }
            });
            imageButton8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = findViewById(R.id.profileUnknown);
                    imageView.setImageResource(R.drawable.cliq_avatar8);

                }
            });
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_previous, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.back_to_previous) {

            finish();
            return true;

        }

        if (id == R.id.set_logout) {

            PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("id", 0).apply();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            Toast.makeText(ChangeAvatarActivity.this, "Logout successful",  Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}