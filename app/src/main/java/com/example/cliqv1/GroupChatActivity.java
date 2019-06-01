package com.example.cliqv1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class GroupChatActivity extends AppCompatActivity {
    private EditText messagesInput;
    private ImageButton sendMessagesbtn;
    private ScrollView scrollView;
    private TextView displayMessages;
    private String currentGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        sendMessagesbtn = findViewById(R.id.sendBtn);
        messagesInput = findViewById(R.id.groupmessages);
        scrollView = findViewById(R.id.scroller);
        displayMessages = findViewById(R.id.groupchat_textdisplay);

        currentGroupName = getIntent().getExtras().get("groupname").toString();
        Toast.makeText(this, currentGroupName, Toast.LENGTH_SHORT).show();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mainpage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.newMember){

        }

        if (id == R.id.back) {

        }

        return super.onOptionsItemSelected(item);
    }

}
