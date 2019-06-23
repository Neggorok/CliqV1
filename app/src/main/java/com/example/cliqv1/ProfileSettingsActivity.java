package com.example.cliqv1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

//Diese Klasse ermöglicht das Verknüpfen von weiteren Profilfunktionen

public class ProfileSettingsActivity extends AppCompatActivity {

    //Buttons erstellt um OnClickListener zu übergeben
    Button changeAvatar;
    Button seeConsentForm;
    Button changeEmail;
    Button changePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        changeAvatar = findViewById(R.id.changeAvatar);
        seeConsentForm = findViewById(R.id.see_consent_form);
        changeEmail = findViewById(R.id.set_email);
        changePassword = findViewById(R.id.set_password);

        //Buttons OnClick zugewiesen, um Wechsel zu anderen Activities zu ermöglichen

        //Zugriff auf Änderung seines Avatars

        changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent iProfileSettings = new Intent(ProfileSettingsActivity.this, ChangeAvatarActivity.class);
                startActivity(iProfileSettings);

            }
        });

        //Zugriff auf die bestätigte Einverständniserklärung

        seeConsentForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent iProfileSettings = new Intent(ProfileSettingsActivity.this, ConsentFormActivity.class);
                startActivity(iProfileSettings);

            }
        });

        //Zugirff auf das Ändern der Persönlichen Daten: Passwort & E-Mail

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

    //menu_back back Icon leitet User zu verherigen Activity zurück
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    //menu_back um zurück zum Profil zu gelangen

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.back) {

            finish();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}

// -