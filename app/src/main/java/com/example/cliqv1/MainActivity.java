package com.example.cliqv1;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText eMail, ePass;
    private Button btn;
    private TextView text;
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eMail = findViewById(R.id.editMail);
        ePass = findViewById(R.id.editPass);
        btn = findViewById(R.id.btnLog);
        text = findViewById(R.id.textSign);
        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cliq");


    }

    private void sendUserToMainPage() {
        finish();
        Intent iMain = new Intent(MainActivity.this, MainPageActivity.class);
        startActivity(iMain);
    }

    public void sendUserToRegister(View view) {
        finish();
        Intent iR = new Intent(MainActivity.this, RegisterActivity.class);
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

        if (!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pass)) {

            firebaseAuth.signInWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Eingeloggt", Toast.LENGTH_SHORT).show();
                                finish();
                                sendUserToMainPage();
                            } else {
                                Toast.makeText(getApplicationContext(), "Oops, ein Fehler ist unterlaufen... versuche es später erneut!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}