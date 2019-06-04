package com.example.cliqv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainPageActivity extends AppCompatActivity {

    private DatabaseReference rootRef;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabsAdapter tabsAdapter;
    private Toolbar toolbar;
    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;
    private String currentUserID, user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        rootRef = FirebaseDatabase.getInstance().getReference();

        viewPager = findViewById(R.id.mainpager);
        tabsAdapter = new TabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsAdapter);

        tabLayout = findViewById(R.id.maintab);
        tabLayout.setupWithViewPager(viewPager);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        currentUserID = firebaseAuth.getCurrentUser().getUid();

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cliq");

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mainpage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.newGroup){
            rootRef.child("Users").child("Teachers").child(currentUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        requestNewGroup();
                    }
                    else {
                        Toast.makeText(MainPageActivity.this, "Du musst einen Lehrer-Account besitzen, um eine Gruppe zu erstellen.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        if (id == R.id.logout) {
            firebaseAuth.signOut();
            sendUserToLogin();
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestNewGroup() {
        if (rootRef != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainPageActivity.this, R.style.AlertDialog);
            builder.setTitle("Gruppenname: ");

            final EditText groupNameField = new EditText(MainPageActivity.this);
            groupNameField.setHint("Bsp. Klasse 4A");
            builder.setView(groupNameField);

            builder.setPositiveButton("Erstellen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String groupName = groupNameField.getText().toString();

                    if (TextUtils.isEmpty(groupName)) {
                        Toast.makeText(MainPageActivity.this, "Bitte benenne deine Gruppe!", Toast.LENGTH_SHORT).show();
                    } else {
                        createNewGroup(groupName);
                    }
                }
            });
            builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        } else {
            Toast.makeText(this, "Du musst einen Admin-Account besitzen, um Gruppen zu erstellen.", Toast.LENGTH_SHORT).show();
        }
    }
    private void createNewGroup(String groupName) {
        rootRef.child("Users").child("Teachers").child(currentUserID).child("Groups").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainPageActivity.this, groupName+" wurde erstellt!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void sendUserToLogin() {
        finish();
        Intent iLog = new Intent(MainPageActivity.this, LoginActivity.class);
        startActivity(iLog);
    }

}
