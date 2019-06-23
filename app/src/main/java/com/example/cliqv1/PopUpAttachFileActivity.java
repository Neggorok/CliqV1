package com.example.cliqv1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

//Diese Klasse ermöglicht dem User das Aufrufen der CameraActivity, Galerie, Dokumente und der AudioActivity
//Verknüpfung findet im Einzel- und Gruppenchat statt

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


        //OnClickListener für Kamera-ImageButton
        btn_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(PopUpAttachFileActivity.this, CameraActivity.class));
            }
        });

        //OnClickListener mit Galerie-Zugriff für Galerie-ImageButton
        btn_gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent();
                iGallery.setAction(Intent.ACTION_GET_CONTENT);
                iGallery.setType("image/*");
                startActivityForResult(iGallery, galleryPick);

            }
        });

        //OnClickListener für Audio-ImageButton
        btn_audio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(PopUpAttachFileActivity.this, AudioActivity.class));
            }
        });

        //OnClickListener mit Dokumenten-Zugriff für Dokumente-ImageButton
        btn_document.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent iDocument = new Intent();
                iDocument.setAction(Intent.ACTION_GET_CONTENT);
                iDocument.setType("documents/*");
                startActivityForResult(iDocument, DocumentPick);
            }
        });

        //OnClickListener für Abbrechen-Button, schließt Dropdown
        btn_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Erstellungs-Daten des Pop ups (Breite, Höhe)
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

// -