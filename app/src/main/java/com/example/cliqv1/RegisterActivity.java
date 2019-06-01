package com.example.cliqv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText eMail, ePass, ePass2, user;
    private Button btn;
    private String currentUserID;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databasePupil, databaseTeacher;
    private RadioButton radioS, radioL;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        eMail = findViewById(R.id.mail);
        ePass = findViewById(R.id.pass);
        ePass2 = findViewById(R.id.pass2);
        btn = findViewById(R.id.signup);
        user = findViewById(R.id.username);
        radioS = findViewById(R.id.accS);
        radioL = findViewById(R.id.accL);
        firebaseAuth = FirebaseAuth.getInstance();

        databasePupil = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseTeacher = FirebaseDatabase.getInstance().getReference().child("Admin-Users");



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    public void register() {
        String mail = eMail.getText().toString().trim();
        String pass = ePass.getText().toString().trim();
        String pass2 = ePass2.getText().toString().trim();
        String username = user.getText().toString().trim();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(getApplicationContext(), "Bitte gebe einen Benutzernamen an.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(mail)){
            Toast.makeText(getApplicationContext(), "Bitte gebe deine Email-Adresse an.!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pass)){
            Toast.makeText(getApplicationContext(), "Bitte gebe dein Passwort an.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pass2)){
            Toast.makeText(getApplicationContext(), "Bitte wiederhole dein Passwort.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(pass.length() < 6){
            Toast.makeText(getApplicationContext(), "Dein Passwort muss mindestens 6 Zeichen lang sein.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!pass.equals(pass2)){
            Toast.makeText(getApplicationContext(), "Dein Passwort stimmt nicht überein.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(mail)&&!TextUtils.isEmpty(pass)&&!TextUtils.isEmpty(pass2)) {

            if(radioS.isChecked()) {
                HashMap<String, String> userMap = new HashMap<>();
                userMap.put("uid", firebaseAuth.getCurrentUser().getUid());
                userMap.put("name", username);
                databasePupil.child(firebaseAuth.getCurrentUser().getUid()).setValue(userMap);
            }
            if(radioL.isChecked()) {
                HashMap<String, String> userMap = new HashMap<>();
                userMap.put("uid", firebaseAuth.getCurrentUser().getUid());
                userMap.put("name", username);
                databaseTeacher.child(firebaseAuth.getCurrentUser().getUid()).setValue(userMap);
            }

            firebaseAuth.createUserWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Benutzer registriert.", Toast.LENGTH_SHORT).show();
                                sendUserToMainPage();

                            } else {
                                Toast.makeText(getApplicationContext(), "Oops, ein Fehler ist unterlaufen... versuche es später nochmal!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    private void sendUserToMainPage() {
        finish();
        Intent iMain = new Intent(RegisterActivity.this, MainPageActivity.class);
        startActivity(iMain);
    }
}
