package com.example.cliqv1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText userPassword;
    EditText userEmail;
    private Button btn, btn2;
    private Toolbar toolbar;
    SharedPreferences pref;
    Intent intent;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lädt die TextViews der Eingabezeilen, um sie in der Methode nutzen zu können
        // Eine Id wird auf dem Server automatisch per Script angelegt
        userEmail = findViewById(R.id.edittext_email);
        userPassword = (EditText) findViewById(R.id.edittext_password);
        btn = findViewById(R.id.signBtn);
        btn2 = findViewById(R.id.LogInButton);

        queue = Volley.newRequestQueue(this);

        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        intent = new Intent(MainActivity.this,GroupChatViewActivity.class);
        if(pref.contains("username") && pref.contains("password")){
            startActivity(intent);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iSign = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(iSign);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        // sorgt dafür, das ein StringRequest, also eine Anfrage an den Server gestellt wird
        String create_user_url = getString(R.string.cliq) + "/login_cliq.php";

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
                        Toast.makeText(MainActivity.this, jsonResponse.get("message").toString(), Toast.LENGTH_SHORT).show();

                        int success = Integer.parseInt(jsonResponse.get("success").toString());

                        if(success == 1){
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("username", String.valueOf(userEmail));
                            editor.putString("password", String.valueOf(userPassword));
                            editor.commit();

                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putInt("id", jsonResponse.getInt("user_id")).apply();
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putInt("admin", jsonResponse.getInt("admin")).apply();
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putInt("moderator", jsonResponse.getInt("moderator")).apply();
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("username", jsonResponse.getString("username")).apply();
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("useremail", userEmail.getText().toString()).apply();
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("created_at", jsonResponse.getString("acc_created_at")).apply();
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("image", jsonResponse.getString("user_image")).apply();


                            Intent intent = new Intent(getApplicationContext(), GroupChatViewActivity.class);
                            startActivity(intent);


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {


        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Hier werden die Eingaben aus den EditTexten userName und userPassword ausgelesen
                // und in die entsprechenden Variablen geladen
                params.put("useremail", userEmail.getText().toString());
                params.put("password", userPassword.getText().toString());
                verschluesselung(userPassword.getText().toString(),userEmail.getText().toString());
                return params;
            }
        };

// Add the request to the RequestQueue.
        queue.add(postRequest);
    }

    public static byte[] enkryt(byte[] password) throws Exception{
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(password);
        return md5.digest();
    }

    public void verschluesselung(String password, String user)
    {

        byte[] md5key = userPassword.getText().toString().getBytes();
        byte[] md5keya = userEmail.getText().toString().getBytes();
        BigInteger md5data = null;

        try {
            md5data = new BigInteger(1, enkryt(md5key));
            md5data = new BigInteger(1, enkryt(md5keya));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        String md5str = md5data.toString(16);



    }
}

