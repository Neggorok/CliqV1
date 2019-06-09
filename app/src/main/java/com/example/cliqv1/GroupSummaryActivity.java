package com.example.cliqv1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class GroupSummaryActivity extends AppCompatActivity {

    ImageButton btnEditGroupImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_summary);
        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        btnEditGroupImg = findViewById(R.id.btnEditGroupImg);

        btnEditGroupImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent iProfileSettings = new Intent(GroupSummaryActivity.this, ChangeGroupPicActivity.class);
                startActivity(iProfileSettings);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_groupchat_shortcut, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.back_to_chat) {

            Intent i = new Intent(getApplicationContext(), GroupChatViewActivity.class);
            Toast.makeText(GroupSummaryActivity.this, "Group overview selected", Toast.LENGTH_SHORT).show();
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }

}
