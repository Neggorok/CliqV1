package com.example.cliqv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class GroupChatSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_settings);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_chat_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //if (id == R.id.back_to_chat) {

        //}


        if (id == R.id.set_viewgroup) {

            Intent i = new Intent(getApplicationContext(), GroupChatViewActivity.class);
            Toast.makeText(GroupChatSettingsActivity.this, "Group-view selected", Toast.LENGTH_SHORT).show();
            startActivity(i);

        }

        if (id == R.id.set_logout) {

            PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("id", 0).apply();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            Toast.makeText(GroupChatSettingsActivity.this, "Logout successful",  Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.newMember) {

            NewGroupMember();

        }
        return super.onOptionsItemSelected(item);
    }

    private void NewGroupMember() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        builder.setTitle("Enter username:");

        final EditText usernameField = new EditText(this);
        usernameField.setHint("Max_Mustermann");
        builder.setView(usernameField);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String username = usernameField.getText().toString();

                if (TextUtils.isEmpty(username)) {

                    Toast.makeText(GroupChatSettingsActivity.this, "Please choose a new member",  Toast.LENGTH_SHORT).show();
                }
                else {
                    CreateNewGroup(username);
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

    private void CreateNewGroup(String username) {

        Toast.makeText(GroupChatSettingsActivity.this, username + " was added to the group", Toast.LENGTH_SHORT).show();
    }
}
