package com.example.cliqv1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileSettingsActivity extends AppCompatActivity {

    Button changeAvatar;
    Button changeBackground;
    Button seeConsentForm;
    Button changeEmail;
    Button changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        changeAvatar = findViewById(R.id.changeAvatar);
        changeBackground = findViewById(R.id.changeBackground);
        seeConsentForm = findViewById(R.id.see_consent_form);
        changeEmail = findViewById(R.id.set_email);
        changePassword = findViewById(R.id.set_password);

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

        seeConsentForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent iProfileSettings = new Intent(ProfileSettingsActivity.this, ChangeEmailActivity.class);
                startActivity(iProfileSettings);

            }
        });

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent iProfileSettings = new Intent(ProfileSettingsActivity.this, ChangeEmailActivity.class);
                startActivity(iProfileSettings);

            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent iProfileSettings = new Intent(ProfileSettingsActivity.this, ChangePasswordActivity.class);
                startActivity(iProfileSettings);

            }
        });
    }
}
