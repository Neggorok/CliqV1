package com.example.cliqv1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText eMail, ePass;
    private Button btn;
    private TextView text;
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeFields();

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();


    }

    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser != null) {
            sendUserToMainPage();
        }
    }

    private void initializeFields() {
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cliq");

        eMail = findViewById(R.id.editMail);
        ePass = findViewById(R.id.editPass);
        btn = findViewById(R.id.btnLog);
        text = findViewById(R.id.textSign);
        progressDialog = new ProgressDialog(this);
    }

    private void sendUserToMainPage() {
        finish();
        Intent iMain = new Intent(LoginActivity.this, MainPageActivity.class);
        startActivity(iMain);
    }

    public void sendUserToRegister(View view) {
        finish();
        Intent iR = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(iR);
    }

    public void login(View view) {
        String mail = eMail.getText().toString().trim();
        String pass = ePass.getText().toString().trim();


        if (TextUtils.isEmpty(mail)) {
            Toast.makeText(getApplicationContext(), "Bitte gebe deine Email-Adresse an!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Bitte gebe dein Passwort an!", Toast.LENGTH_SHORT).show();
            return;
        }

       else {
           progressDialog.setTitle("Logge ein.");
           progressDialog.setMessage("Bitte warten.");
           progressDialog.setCanceledOnTouchOutside(true);
           progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Eingeloggt", Toast.LENGTH_SHORT).show();
                                finish();
                                progressDialog.dismiss();
                                sendUserToMainPage();
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
    }
}