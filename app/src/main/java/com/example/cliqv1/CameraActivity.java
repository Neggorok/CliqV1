package com.example.cliqv1;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.widget.Button;
;
import java.io.IOException;


public class CameraActivity extends AppCompatActivity {



    private static final String TAG = "CameraActivity";
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        case R.id.buttonMakePicture:
            Intent intentc = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            startActivity(intentc);
    }

    private void startCamera() {
        if (this.permissionGranted) {
            ContentValues contentValues = new ContentValues();
            ContentValues.put(MediaStore.Images.Media.TITLE, TITLE);
            ContentValues.put(MediaStore.Images.Media.DESCRIPTION, DESCRIPTION);

            imageUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
            );
            Intent intentC = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentC.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intentC, IMAGE_CAPTURE);
        }
    }



    private void verifyPermission()
    {
        Log.d(TAG,"verifyPermissions: asking user for permissions");
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                        permissions[1]) == PackageManager.PERMISSION_GRANTED
                                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                                permissions[2]) == PackageManager.PERMISSION_GRANTED)
        {

        }



    }


    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        if (requestcode == IMAGE_CAPTURE) {
            if (resultcode == RESULT_OK) {
                try {
                    //show Image
                } catch (IOException e) {
                    Log.e(MainActivity.class.getSimpleName(), "setBitmap()", e);
                }

            } else {
                int rowsDeleted = getContentResolver().delete(imageUri, null, null);
                Log.d(MainActivity.class.getSimpleName(), rowsDeleted + "rows deleted");
            }
        }
    }

    private Bitmap getandScaleBitmap(Uri uri, int dstWidth, int dstheight) {
        try {
            Bitmap src = MediaStore.Images.Media.getBitmap(
                    getContentResolver(), uri
            );
            float srcWidth = src.getWidth(), srcHeight = src.getHeight();

            if (dstWidth < 1) {
                dstWidth = (int) (srcWidth / srcHeight * dstheight);
            }
            Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstheight, false);
            return dst;
        } catch (IOException e) {
            Log.e(MainActivity.class.getSimpleName(), "setBitmap", e);
        }
        return null;
    }


    protected void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //show hint
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, this.PERMISSION_REQUEST);
            }
        }
        else{
            this.permissionGranted  = true;
        }



    }

public void onRequestPermissionsResult(int requestCode,String permissions[],
                                       int[] grantResults)
{
    switch(requestCode)
    {
        case PERMISSION_REQUEST:
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                this.permissionGranted = true;
                startCamera();
            }
            else {this.permissionGranted = false;}
    }
    return;
}

*/
}


