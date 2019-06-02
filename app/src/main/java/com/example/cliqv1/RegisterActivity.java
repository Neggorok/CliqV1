package com.example.cliqv1;

import android.app.ProgressDialog;
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
    private DatabaseReference rootRef, databasePupil, databaseTeacher;
    private RadioButton radioS, radioL;
    private ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        firebaseAuth = FirebaseAuth.getInstance();
        databasePupil = FirebaseDatabase.getInstance().getReference().child("Users");
        rootRef = FirebaseDatabase.getInstance().getReference();
       // databaseTeacher = FirebaseDatabase.getInstance().getReference().child("Admin-Users");

        initializeFields();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void initializeFields() {
        eMail = findViewById(R.id.mail);
        ePass = findViewById(R.id.pass);
        ePass2 = findViewById(R.id.pass2);
        btn = findViewById(R.id.signup);
        user = findViewById(R.id.username);
        radioS = findViewById(R.id.accS);
        radioL = findViewById(R.id.accL);
        progressDialog = new ProgressDialog(this);
    }

    public void register() {
        String mail = eMail.getText().toString();
        String pass = ePass.getText().toString();
        String pass2 = ePass2.getText().toString();
        String username = user.getText().toString();

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
        if(!radioL.isChecked()&&!radioS.isChecked()) {
            Toast.makeText(this, "Bitte wähle eine Account-Art aus.", Toast.LENGTH_SHORT).show();
        }
            else {
                progressDialog.setTitle("Erstelle Account.");
                progressDialog.setMessage("Bitte warten Sie, bis ihr Account erstellt wurde...");
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(mail, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String currentUserID = firebaseAuth.getCurrentUser().getUid();
                                    rootRef.child("Users").child(currentUserID).setValue("");

                                    Toast.makeText(getApplicationContext(), "Benutzer registriert.", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    sendUserToMainPage();

                                } else {
                                    String message = task.getException().toString();
                                    Toast.makeText(RegisterActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
            }
        }

       /* if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(mail)&&!TextUtils.isEmpty(pass)&&!TextUtils.isEmpty(pass2)&&radioS.isChecked()) {

                HashMap<String, String> userMap = new HashMap<>();
                userMap.put("uid", firebaseAuth.getCurrentUser().getUid());
                userMap.put("name", username);
                databasePupil.child(firebaseAuth.getCurrentUser().getUid()).setValue(userMap);
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
        if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(mail)&&!TextUtils.isEmpty(pass)&&!TextUtils.isEmpty(pass2)&&radioL.isChecked()) {


                HashMap<String, String> userMap = new HashMap<>();
                userMap.put("uid", firebaseAuth.getCurrentUser().getUid());
                userMap.put("name", username);
                databaseTeacher.child(firebaseAuth.getCurrentUser().getUid()).setValue(userMap);
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
        } */
    private void sendUserToMainPage() {
        finish();
        Intent iMain = new Intent(RegisterActivity.this, MainPageActivity.class);
        startActivity(iMain);
    }
}
