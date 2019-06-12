package com.example.cliqv1;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import org.apache.http.HttpClientConnection;
import org.apache.http.HttpConnection;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;


public class CameraActivity extends AppCompatActivity {

    Button btnTakePicture;
    Button btnRecordVideo;
    Button btnCropImage;
    Button btnFilter;
    Button btnSend;
    Button btnOpenGallery;


    private String DESCRIPTION = "CameraDemo";
    private String TITLE = "Demo";
    private static final int IMAGE_CAPTURE = 1;
    private static final int PERMISSION_REQUEST = 99;
    private static final int GALLERY_Pick = 2;
    private static final int IMAGE_TO_CROP = 3;
    private static final int IMAGE_FROM_CROP = 4;
    private static final String Adresse ="https://cliqstudent.000webhostapp.com/cliq";
    private Uri imageUri;
    private Bitmap actualBitmap;
    private ImageView imageView;
    private boolean permissionGranted = false, cropImage = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnRecordVideo = findViewById(R.id.btnRecordVideo);
        btnOpenGallery = findViewById(R.id.btnOpenGallery);
        btnCropImage = findViewById(R.id.btnCropImage);
        btnFilter = findViewById(R.id.btnFilter);
        btnSend = findViewById(R.id.btnSend);


        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentC = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
                startActivity(intentC);
            }
        });

        btnRecordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                Intent intentV = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
                startActivity(intentV);
            }
        });

        btnOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intentG = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivity(intentG);
            }
        });

        btnCropImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

            }
        });

    }

    private void startCamera(boolean cropImage) {
        if (this.permissionGranted) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.TITLE, TITLE);
            contentValues.put(MediaStore.Images.Media.DESCRIPTION, DESCRIPTION);
            contentValues.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");

            imageUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
            );
            Intent intentC = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentC.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            if(!cropImage) {
                startActivityForResult(intentC, IMAGE_CAPTURE);
            }
            else {
                startActivityForResult(intentC, IMAGE_TO_CROP);
            }

        }
    }



    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        if (requestcode == IMAGE_CAPTURE) {
            if (resultcode == RESULT_OK) {
                updateBitmap(getandScaleBitmap(this.imageUri,-1,300));

            } else {
                int rowsDeleted = getContentResolver().delete(imageUri, null, null);
                Log.d(CameraActivity.class.getSimpleName(), rowsDeleted + "rows deleted");
            }
        }

        if (requestcode == GALLERY_Pick)
        {
            if (requestcode == RESULT_OK)
            {
                if(data != null)
                {
                    Uri uri = data.getData();
                    updateBitmap(getandScaleBitmap(uri,-1,300));
                }
            }
            else
            {
                Log.d(CameraActivity.class.getSimpleName(),"Kein Bild auasgewählt");
            }
        }


        if(requestcode == IMAGE_TO_CROP)
        {
            if(requestcode == RESULT_OK)
            {
                cropImage(this.imageUri);
            }
        }

        if(requestcode == IMAGE_FROM_CROP)
        {
            if(requestcode == RESULT_OK)
            {
                updateBitmap(getandScaleBitmap(this.imageUri,-1,300));
            }
        }


    }

    public void updateBitmap(Bitmap bitmap)
    {
        this.actualBitmap = bitmap;
        this.imageView.setImageBitmap(bitmap);
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
            Log.e(CameraActivity.class.getSimpleName(), "setBitmap", e);
        }
        return null;
    }

    private void shareImage(Uri uri)
    {
        List<Intent> intentList = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("Image/?");
        List<ResolveInfo> activities = packageManager.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY
        );

        for(ResolveInfo info : activities)  {
            String packageName = info.activityInfo.packageName;
            if ("de.hshl.myapp6".equals(packageName)){
                continue;
            }
            Intent intentSend = new Intent(Intent.ACTION_SEND,uri);
            intentSend.setType("Image/?");
            intentSend.setPackage(packageName);
            intentList.add(intentSend);
        }

        int size = intentList.size();
        if(size > 0)
        {
            Intent intentChooser = Intent.createChooser(intentList.remove(size -1 ),
                    getString(R.string.send));
            Parcelable[] parcelables = new Parcelable[size -1 ];
            intentChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,intentList.toArray(parcelables));
            startActivity(intentChooser);
        }
    }

    private Bitmap changeToGreyscale(Bitmap src)
    {
        int width = src.getWidth(),
                height = src.getHeight();

        Bitmap dst = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(dst);
        Paint paint = new Paint();

        ColorMatrix colormatrix = new ColorMatrix();
        colormatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colormatrix);
        paint.setColorFilter(filter);


        canvas.drawBitmap(src,0,0,paint);
        return  dst;
    }


    private void cropImage(Uri uri)
    {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.Crop");
            cropIntent.setDataAndType(uri,"Image/*");
            cropIntent.putExtra("crop","true");
            cropIntent.putExtra("aspectX",1);
            cropIntent.putExtra("aspectY",1);
            cropIntent.putExtra("outputX",256);
            cropIntent.putExtra("outputY",256);
            cropIntent.putExtra("return-data",true);
            startActivityForResult(cropIntent,IMAGE_FROM_CROP);
        }
        catch (Exception e)
        {
            Log.e(CameraActivity.class.getSimpleName(),"cropImage()",e);
        }
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
                    startCamera(true);
                }
                else {this.permissionGranted = false;}
        }
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.back) {

            finish();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }


   private   class UploadImage extends AsyncTask<Void, Void, Void>
    {
        private static final String Adresse ="https://cliqstudent.000webhostapp.com/cliq";
        Bitmap image;
        String name;
        public UploadImage(Bitmap image , String name)
        {
            this.image = image;
            this.name = name;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100 ,
                    byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),
                    Base64.DEFAULT);
            ArrayList<NameValuePair> dataToSend= new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("image", encodedImage));
            dataToSend.add(new BasicNameValuePair("name", name));

            HttpParams httpRequestParams = gethttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Adresse + "Bildspeichern.php");

            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e)
            {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(new CameraActivity(), "Bild hochgeladen", Toast.LENGTH_SHORT).show();
        }

        private HttpParams gethttpRequestParams()
        {
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, 100*30);
            HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
            return httpRequestParams;
        }
    }
}

