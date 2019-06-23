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

//Mit dieser Klasse sollte dem User das Einsehen der Gruppeninformationen ermöglicht werden,
//leider hat die Gruppe, aus zeitlich Gründen, es nicht mehr geschafft die Funkitonen zu erweiten.
//Aus diesem Grung ist diese Klasse auf einem, für die App, unbrauchbaren Stand.
//Daher haben wir uns dafür entschieden, dass wir die Verknüpfung mit der App entfernen (auskommentiert lassen).

public class GroupSummaryActivity extends AppCompatActivity {

    ImageButton btnEditGroupImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_summary);
        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        //Das Gurppenbild kann mit dem ImageButton btnEditGroupImg geändert werden

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
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    //Mit dem menu-back gelangt der Nutzer von den Gruppeneinstellungen zurück zum Gruppenchat

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.back) {

            finish();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
// -