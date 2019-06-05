package com.example.cliqv1;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ChangeBackgroundActivity extends AppCompatActivity {

    ImageButton btn1;
    ImageButton btn2;
    ImageButton btn3;
    ImageButton btn4;
    Button btn;
    RelativeLayout r;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_background);

        btn1 = findViewById(R.id.bg1);
        btn2 = findViewById(R.id.bg2);
        btn3 = findViewById(R.id.bg3);
        btn4 = findViewById(R.id.bg4);
        btn = findViewById(R.id.btn);
        r = findViewById(R.id.activity_change_background);
    }

    public void bg1(View v){
        r.setBackgroundResource(R.drawable.background1);
    }

    public void bg2(View v) {
        r.setBackgroundResource(R.drawable.background2);
    }

    public void bg3 (View v) {
        r.setBackgroundResource(R.drawable.background3);
    }

    public void bg4 (View v) {
        r.setBackgroundResource(R.drawable.background4);
    }

    public void safe(View view) {
        Toast.makeText(ChangeBackgroundActivity.this, "Background safed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shortcut_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.back_to_profile) {

            finish();
            return true;

        }

        if (id == R.id.set_logout) {

            PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("id", 0).apply();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            Toast.makeText(ChangeBackgroundActivity.this, "Logout successful",  Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}

