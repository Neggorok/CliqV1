package com.example.cliqv1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class ProfileActivity extends AppCompatActivity {

    int userId;
    String username;
    String userImage;

    ImageView imageView;
    TextView usernameTV;
    // EditText passwordET;

    Bitmap currentBitmap;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.NavBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.nav_contacts:
                        Intent c = new Intent(getApplicationContext(), UserListActivity.class);
                        startActivity(c);
                        break;

                    case R.id.nav_groups:
                        Intent g = new Intent(getApplicationContext(), GroupChatViewActivity.class);
                        startActivity(g);
                        break;

                    case R.id.nav_profile:
                        Toast.makeText(ProfileActivity.this, "You are already on profile",  Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        userId = PreferenceManager.getDefaultSharedPreferences(this).getInt("id", -1);
        username = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "-1");
        userImage = PreferenceManager.getDefaultSharedPreferences(this).getString("image", "-1");

        imageView = findViewById(R.id.groupchat_imageView);
        usernameTV = findViewById(R.id.groupchat_name);
        // passwordET = findViewById(R.id.profile_password);

        queue = Volley.newRequestQueue(this);

        if (userImage != null && userImage.length() > 0) {

            imageView.setImageBitmap(Util.getBitmapFromBase64String(userImage));
            currentBitmap = Util.getBitmapFromBase64String(userImage);

        } else {

            imageView.setImageResource(R.drawable.standard_user_image);
            currentBitmap = Util.getBitmapFromDrawable(this, R.drawable.standard_user_image);

        }

        usernameTV.setText(username);

        setTitle(username);
    }

    public void chooseImage(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);

        }

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                try {
                    Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                    imageView.setImageBitmap(bitmapImage);
                    currentBitmap = bitmapImage;

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);

            }

        }
    }

    public void saveUserImage(View view) {

        final Bitmap smallerBitmap = currentBitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

        final String bitmapString = Util.getBase64StringFromBitmap(smallerBitmap);

        String create_user_url = getString(R.string.cliq) + "/insertBitmapIntoDB_cliq.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, create_user_url,

                response -> {

                    Log.i("response", response);

                    try {

                        JSONObject jsonResponse = new JSONObject(response);
                        String message = jsonResponse.getString("message");


                        int success = Integer.parseInt(jsonResponse.get("success").toString());

                        if (success == 1) {

                            Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                            PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this).edit().putString("image", Util.getBase64StringFromBitmap(smallerBitmap)).apply();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {


        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userId));
                params.put("bitmapString", bitmapString);

                return params;
            }
        };


        queue.add(postRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.set_logout) {

            PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("id", 0).apply();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            Toast.makeText(ProfileActivity.this, "Logout successful",  Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
