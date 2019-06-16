package com.example.cliqv1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText userName;
    EditText userMail;
    TextInputEditText userPassword;
    TextInputEditText userPassword2;
    Button button;
    private Toolbar toolbar;
    private RadioButton radioS, radioL;
//test
    RequestQueue queue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userName = findViewById(R.id.username);
        userMail = findViewById(R.id.mail);
        userPassword =  findViewById(R.id.pass);
        userPassword2 = findViewById(R.id.pass2);
        radioS = findViewById(R.id.accS);
        radioL = findViewById(R.id.accL);
        button = findViewById(R.id.signup);

        queue = Volley.newRequestQueue(this);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = userName.getText().toString();
                String mail = userMail.getText().toString();
                String pw = userPassword.getText().toString();
                String pw2 = userPassword2.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Bitte gebe einen Benutzernamen an.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(mail)) {
                    Toast.makeText(getApplicationContext(), "Bitte gebe deine Email-Adresse an.!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(pw)) {
                    Toast.makeText(getApplicationContext(), "Bitte gebe dein Passwort an.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(pw2)) {
                    Toast.makeText(getApplicationContext(), "Bitte wiederhole dein Passwort.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pw.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Dein Passwort muss mindestens 6 Zeichen lang sein.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!pw.equals(pw2)) {
                    Toast.makeText(getApplicationContext(), "Dein Passwort stimmt nicht überein.", Toast.LENGTH_SHORT).show();
                    return;
                }
                //guckt, ob ein Lehrer-Accout oder Schüler-Account erstellt werden soll
                //checkt, ob ein Radiobutton angeklickt ist, wenn nicht, gibt er den Toast aus
               /* if (!radioL.isChecked() && !radioS.isChecked()) {
                    Toast.makeText(SignUpActivity.this, "Bitte wähle eine Account-Art aus.", Toast.LENGTH_SHORT).show();
                }*/
                else {
                    getConsent();
                }
            }
        });
    }

    //Erstellt das Pop-Up für die Einverständniserklärung mit 2 Buttons: akzeptieren und abbrechen
    public void getConsent(){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_pop_up_consent_form, null);
        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        Button abbrechen = popupView.findViewById(R.id.btnCancel);
        Button akzeptieren = popupView.findViewById(R.id.btnAccept);
        ImageView consent = popupView.findViewById(R.id.consentText);
        Picasso.get().load("https://cliqstudent.000webhostapp.com/cliq/datenschutz.jpg")
        .into(consent);
        // boolean focusable = true;  lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);


        abbrechen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        akzeptieren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    private void signup() {

            // sorgt dafür, das ein StringRequest, also eine Anfrage an den Server gestellt wird
            String create_user_url = getString(R.string.cliq) + "/registrierung_amir.php";

            // der Request prüft ob die Daten des Scripts "/registrierung_cliq.php" correct sind
            StringRequest postRequest = new StringRequest(Request.Method.POST, create_user_url,

                    // stellt die Antwort des Serves dar
                    response -> {

                        // gibt die Antwort des Serves in der AS Console aus
                        // dient nur zur Kontrolle
                        Log.i("response", response);

                        // der Try - catch bereich funktioniert ähnlich wie eine If Abfrage
                        // es wird im Try Bereich versucht auf die Antwort zu reagieren
                        // und sollte die Antwort des Servers die Erwartete sein, reagiert der Catch Bereich dementsprechend
                        try {

                            // wandelt die Antwort des Servers in JSON um
                            JSONObject jsonResponse = new JSONObject(response);

                            // der Toast nimmt die Antwort des Servers und gibt diese für den Nutzer in der App sichtbar aus
                            Toast.makeText(SignUpActivity.this, jsonResponse.get("message").toString(), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }, error -> {

            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    // Hier werden die Eingabenr aus den EditTexten userName und userPassword ausgelesen
                    // und in die entsprechenden Variablen geladen
                    params.put("username", userName.getText().toString());
                    params.put("password", userPassword.getText().toString());
                    params.put("useremail", userMail.getText().toString());


                    return params;
                }
            };

// Add the request to the RequestQueue.
            queue.add(postRequest);
        Intent iLog = new Intent(this, MainActivity.class);
        startActivity(iLog);
        }
    }

