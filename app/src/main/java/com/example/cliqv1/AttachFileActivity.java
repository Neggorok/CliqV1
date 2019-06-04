package com.example.cliqv1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class AttachFileActivity extends Activity {

    ImageButton btn_camera;
    ImageButton btn_audio;
    Button btn_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_file);

        btn_camera = (ImageButton) findViewById(R.id.ibtn_camera);
        //   btn_gallery = (ImageButton) findViewById(R.id.ibtn_gallery);
        //   btn_document = (ImageButton) findViewById(R.id.ibtn_document);
        btn_audio = (ImageButton) findViewById(R.id.ibtn_audio);
        btn_close = (Button) findViewById(R.id.btn_close);

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

        btn_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .6), (int) (height * .5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

    }
}
