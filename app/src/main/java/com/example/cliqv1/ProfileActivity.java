package com.example.cliqv1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

        ImageButton chat_nav = (ImageButton) findViewById(R.id.chat_nav);
        ImageButton profile_nav = (ImageButton) findViewById(R.id.profile_nav);

        chat_nav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, UserListActivity.class));
            }
        });

        profile_nav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "You are already on Profile", Toast.LENGTH_SHORT).show();
            }
        });

        userId = PreferenceManager.getDefaultSharedPreferences(this).getInt("id", -1);
        username = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "-1");
        userImage = PreferenceManager.getDefaultSharedPreferences(this).getString("image", "-1");

        imageView = findViewById(R.id.groupchat_imageView);
        usernameTV = findViewById(R.id.groupchat_name);
        // passwordET = findViewById(R.id.profile_password);

        queue = Volley.newRequestQueue(this);

        if(userImage != null && userImage.length() > 0) {

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

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);

        }

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

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

                        if(success == 1){

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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

            if (id == R.id.set_avatar) {
                PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("id", 0).apply();
                Intent ia = new Intent(getApplicationContext(), ChangeAvatarActivity.class);
                startActivity(ia);
                return true;
            }

            if (id == R.id.set_background) {
                PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("id", 0).apply();
                Intent bg = new Intent(getApplicationContext(), ChangeBackgroundActivity.class);
                startActivity(bg);
            return true;
            }

            if (id == R.id.set_password) {
                PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("id", 0).apply();
                Intent ip = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(ip);
                return true;
            }

            if (id == R.id.set_email) {
                PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("id", 0).apply();
                Intent ie = new Intent(getApplicationContext(), ChangeEmailActivity.class);
                startActivity(ie);
                return true;
            }

            if (id == R.id.set_consent_form) {
                PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("id", 0).apply();
                Intent ie = new Intent(getApplicationContext(), AcceptedConsentFormActivity.class);
                startActivity(ie);
                return true;
            }

            if (id == R.id.set_logout) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("id", 0).apply();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            Toast.makeText(ProfileActivity.this, "Logout successful", Toast.LENGTH_SHORT).show();
            return true;
            }

        if (id == R.id.createGroup) {

            RequestNewGroup();

        }

        return super.onOptionsItemSelected(item);
    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        builder.setTitle("Enter Group Name :");

        final EditText groupNameField = new EditText(this);
        groupNameField.setHint("Klasse 1A");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String groupName = groupNameField.getText().toString();

                if (TextUtils.isEmpty(groupName)) {

                    Toast.makeText(ProfileActivity.this, "Please choose a group name",  Toast.LENGTH_SHORT).show();
                }
                else {
                    CreateNewGroup(groupName);
                }


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();

            }
        });

        builder.show();
    }

    private void CreateNewGroup(String groupName) {

        Toast.makeText(ProfileActivity.this, groupName + " is created successfully", Toast.LENGTH_SHORT).show();
    }
}
