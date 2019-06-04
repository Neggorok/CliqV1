package com.example.cliqv1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;

public class AttachFileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_file);

        ImageButton btn_camera = (ImageButton) findViewById(R.id.ibtn_camera);
        //     ImageButton btn_gallery = (ImageButton) findViewById(R.id.ibtn_gallery);
        //     ImageButton btn_document = (ImageButton) findViewById(R.id.ibtn_document);
        ImageButton btn_audio = (ImageButton) findViewById(R.id.ibtn_audio);

        btn_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttachFileActivity.this, CameraActivity.class));
            }
        });

        btn_audio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttachFileActivity.this, AudioActivity.class));
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .5), (int) (height * .3));
        getWindow().setBackgroundDrawable(null);

    }
}
