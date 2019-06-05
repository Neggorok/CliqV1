package com.example.cliqv1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileSettingsActivity extends AppCompatActivity {

    Button changeAvatar;
    Button changeBackground;
    Button see_consent_form;
    Button set_email;
    Button set_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        changeAvatar = findViewById(R.id.changeAvatar);
        changeBackground = findViewById(R.id.changeBackground);
        see_consent_form = findViewById(R.id.see_consent_form);
        set_email = findViewById(R.id.set_email);
        set_password = findViewById(R.id.set_password);

        changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent iProfileSettings = new Intent(ProfileSettingsActivity.this, ChangeAvatarActivity.class);
                startActivity(iProfileSettings);

            }
        });

        changeBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent iProfileSettings = new Intent(ProfileSettingsActivity.this, ChangeBackgroundActivity.class);
                startActivity(iProfileSettings);

            }
        });
    }
}
