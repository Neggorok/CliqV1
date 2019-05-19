package com.example.cliqv1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ChangeAvatarActivity extends AppCompatActivity {

    ImageView imageView;
    ImageButton imageButton;
    Button chooseImage;

    //private static final int IMAGE_PICK_CODE = 1000;
    //private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avatar);

        imageView = findViewById(R.id.profileImageView);
        imageButton = findViewById(R.id.imageButton1);
        imageButton = findViewById(R.id.imageButton2);
        imageButton = findViewById(R.id.imageButton3);
        chooseImage = findViewById(R.id.choose_image_btn);

        //       chooseImage.setOnClickListener(new View.OnClickListener() {
        //           public void onClick(View v) {
        //               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        //                   if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        //                           == PackageManager.PERMISSION_DENIED) {
        //                       String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
        //                      requestPermissions(permissions, PERMISSION_CODE);
        //                } else {
        //                    pickFromGallery();
        //                 }
        //          } else {
        //              pickFromGallery();
        //          }

        //       }
        // }
        // private void pickFromGallery() {
        //    Intent intent = new Intent(Intent.ACTION_PICK);
        //      intent.setType("image/*");
        //   startActivity(intent, IMAGE_PICK_CODE);
        //}

        // public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int grantResults) {
        //      switch (requestCode) {
        //         case PERMISSION_CODE: {
        //             if (grantResults.lenght >0 && grantResults[0] ==
        //             PackageManager.PERMISSION_GRANTED) {
        //                pickFromGallery();
        //            }
        //            else {
        //               Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
        //           }
        //      }
        //   }
        // }

        // public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
        //     ImageView.setImageURI(data.getData());
        //  }
        // }
}
}