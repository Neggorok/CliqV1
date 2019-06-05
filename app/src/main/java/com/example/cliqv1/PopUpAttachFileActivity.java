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

public class PopUpAttachFileActivity extends Activity {

    ImageButton btn_camera;
    ImageButton btn_audio;
    ImageButton btn_gallery;
    ImageButton btn_document;
    Button btn_close;

    private static final int galleryPick = 1;
    private static final int DocumentPick = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_attach_file);

        btn_camera = (ImageButton) findViewById(R.id.ibtn_camera);
        btn_gallery = (ImageButton) findViewById(R.id.ibtn_gallery);
        btn_document = (ImageButton) findViewById(R.id.ibtn_document);
        btn_audio = (ImageButton) findViewById(R.id.ibtn_audio);
        btn_close = (Button) findViewById(R.id.btn_close);




        btn_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(PopUpAttachFileActivity.this, CameraActivity.class));
            }
        });

        btn_gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent();
                iGallery.setAction(Intent.ACTION_GET_CONTENT);
                iGallery.setType("image/*");
                startActivityForResult(iGallery, galleryPick );
            }
        });


        btn_audio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(PopUpAttachFileActivity.this, AudioActivity.class));
            }
        });

        btn_document.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent iDocument = new Intent();
                iDocument.setAction(Intent.ACTION_GET_CONTENT);
                iDocument.setType("documents/*");
                startActivityForResult(iDocument, DocumentPick );
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
