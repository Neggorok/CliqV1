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
        // Eine Id wird auf dem Server automatisch per sql angelegt
        userEmail = findViewById(R.id.edittext_email);
        userPassword = (EditText) findViewById(R.id.edittext_password);
        btn = findViewById(R.id.signBtn);
        btn2 = findViewById(R.id.LogInButton);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        queue = Volley.newRequestQueue(this);

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
        // die Anfrage löst das php Skript login_cliq aus
        String create_user_url = getString(R.string.cliq) + "/login_cliq.php";

        // erzeugt einen neuen Request an den Server, der das oben angesprochene PhP file ausführt
        StringRequest postRequest = new StringRequest(Request.Method.POST, create_user_url,

                // stellt die Antwort des Serves dar
                response -> {

                    // gibt die Antwort des Serves in der AS Console aus
                    // dient nur zur Kontrolle
                    Log.i("response", response);

                    // der Try - catch bereich funktioniert ähnlich wie eine If Abfrage
                    // es wird im Try Bereich versucht auf den Response des Servers zu reagieren
                    // und sollte die Antwort des Servers die Erwartete sein, reagiert der Catch Bereich dementsprechend
                    try {

                        // wandelt die Antwort des Servers in JSON um
                        JSONObject jsonResponse = new JSONObject(response);

                        // der Toast nimmt die Antwort des Servers und gibt diese für den Nutzer in der App als Popup sichtbar aus
                        Toast.makeText(MainActivity.this, jsonResponse.get("message").toString(), Toast.LENGTH_SHORT).show();

                        // holt die success Ausgabe des php skriptes und legt es in die "string-variable" success ab, um sie später leichter aufrufen zu können
                        int success = Integer.parseInt(jsonResponse.get("success").toString());
                        // prüft ob die success Ausgabe in der Antwort des Servers 1 ist
                        // wenn ja, werden die folgenden Serverausgaben in die einzelnen "string-Variablen" geladen um leichter weiter verarbeitet werden zu können
                        if(success == 1){
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("username", String.valueOf(userEmail));
                            editor.putString("password", String.valueOf(userPassword));
                            editor.commit();
                            // nimmt aus dem response des Servers die einzelnen Elemente des angelegten Arrays, und legt die einträge in die entsprechenden string variablen
                            // zum Beispiel wird die user_id des servers in der App leichter aufrufbar gemacht, indem sie in den Chatpreferences unter "id" einfach
                            // mit einer Zeile aufgerufen werden kann
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putInt("id", jsonResponse.getInt("user_id")).apply();
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putInt("admin", jsonResponse.getInt("admin")).apply();
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putInt("moderator", jsonResponse.getInt("moderator")).apply();
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("username", jsonResponse.getString("username")).apply();
                            //PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("familyname", jsonResponse.getString("familyname")).apply();
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("useremail", userEmail.getText().toString()).apply();
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("created_at", jsonResponse.getString("acc_created_at")).apply();
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("image", jsonResponse.getString("user_image")).apply();

                            // im anschluss wird die nächste Activity gestartet
                            Intent intent = new Intent(getApplicationContext(), GroupChatViewActivity.class);
                            startActivity(intent);


                        }
                    // sorgt dafür, dass alle Informationen die die App produziert in der Konsole ausgegeben werden
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // sorgt dafür, dass wenn fehler im Try bereich auftreten, die Fehlermeldung in der Konsole ausgegeben wird
                }, error -> {


        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Hier werden die Eingaben aus den EditTexten ausgelesen
                // und in die entsprechenden Variablen geladen um diese dann an den Server weiter zu geben
                params.put("useremail", userEmail.getText().toString());
                params.put("password", userPassword.getText().toString());
                verschluesselung(userPassword.getText().toString(),userEmail.getText().toString());
                return params;
            }
        };

        // addet den request zur Request Queue
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

