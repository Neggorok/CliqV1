package com.example.cliqv1;

import android.app.Activity;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class PopUpGroupSettingsActivity extends Activity {

    Button btnCancel;
    ImageButton addGroupMember;

    int loggedAdmin;
    int loggedModerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_group_settings);

        btnCancel = findViewById(R.id.btnCancel);
        addGroupMember = findViewById(R.id.addGroupMember);

        loggedAdmin = PreferenceManager.getDefaultSharedPreferences(this).getInt("admin", -1);
        loggedModerator = PreferenceManager.getDefaultSharedPreferences(this).getInt("moderator", -1);



        //Nur wer Moderator ist kann Gruppenmitglieder hinzufügen
        if (loggedModerator == 0)
        {
            addGroupMember.setEnabled(false);
        }
        else {
            addGroupMember.setEnabled(true);
        }

        addGroupMember.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                RequestNewMember();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                onBackPressed();

            }
        });

        //Erstellungs-Daten des Pop ups (Breite, Höhe)
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

    }

    //Pop up um Gruppenmitglieder hinzufügen zu können
    private void RequestNewMember() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        builder.setTitle("Enter Username:");

        final EditText newMemberField = new EditText(this);
        newMemberField.setHint("Max Mustermann");
        builder.setView(newMemberField);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String username = newMemberField.getText().toString();

                //Username wird benötigt um Gruppenmitglieder hinzufügen zu können
                if (TextUtils.isEmpty(username)) {

                    Toast.makeText(PopUpGroupSettingsActivity.this, "Please choose a username",  Toast.LENGTH_SHORT).show();
                }
                else {
                    CreateNewMember(username);
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

    private void CreateNewMember(String username) {

        Toast.makeText(PopUpGroupSettingsActivity.this, username + " was successfully added", Toast.LENGTH_SHORT).show();
    }
}
// -