package com.example.cliqv1;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

public class ConsentFormActivity extends AppCompatActivity {

    ImageView consent;
    TextView time;
    String signTime;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent_form);
        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        consent = findViewById(R.id.consentText);

        Picasso.get().load("https://cliqstudent.000webhostapp.com/cliq/datenschutz.jpg")
                .into(consent);
        signTime = PreferenceManager.getDefaultSharedPreferences(this).getString("created_at", "-1");
        time = findViewById(R.id.signtime);
        queue = Volley.newRequestQueue(this);
        time.setText(signTime);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

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