package com.example.cliqv1;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.view.Menu;
import android.view.MenuItem;


public class ProfileActivity extends AppCompatActivity {

    int userId;
    String username;
    //String userFamily;
    String userImage;

    ImageView imageView;
    TextView usernameTV;
    // EditText passwordET;

    Bitmap currentBitmap;
    SharedPreferences prf;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.NavBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.nav_contacts:
                        Intent c = new Intent(getApplicationContext(), UserListActivity.class);
                        startActivity(c);
                        break;

                    case R.id.nav_groups:
                        Intent g = new Intent(getApplicationContext(), GroupChatViewActivity.class);
                        startActivity(g);
                        break;

                    case R.id.nav_profile:
                        Toast.makeText(ProfileActivity.this, "You are already on profile",  Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        userId = PreferenceManager.getDefaultSharedPreferences(this).getInt("id", -1);
        username = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "-1");
        //userFamily = PreferenceManager.getDefaultSharedPreferences(this).getString("familyname", "-1");
        userImage = PreferenceManager.getDefaultSharedPreferences(this).getString("image", "-1");
        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        imageView = findViewById(R.id.groupchat_imageView);
        usernameTV = findViewById(R.id.groupchat_name);
        // passwordET = findViewById(R.id.profile_password);


        queue = Volley.newRequestQueue(this);

        // prüft ob das Userimage leer ist
        if (userImage != null) {
            // wenn es nicht leer ist wird das übergebene Userimage gespeichert und mit angezeigt
            imageView.setImageBitmap(Util.getBitmapFromBase64String(userImage));
            currentBitmap = Util.getBitmapFromBase64String(userImage);

        } else {
            // wenn es jedoch leer ist, bekommt wird ein Standartbild zugewiesen
            imageView.setImageResource(R.drawable.standard_user_image);
            currentBitmap = Util.getBitmapFromDrawable(this, R.drawable.standard_user_image);

        }

        usernameTV.setText(username/*+" "+userFamily*/);

        setTitle(username);
    }

    // Erste Schritte, das Bild zu ändern
    /* public void chooseImage(View view) {
        // checkt ob die Rechte vorhanden sind um auf den externen Speicher zu zu greifen
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // und wenn sie nicht gegeben sind, wird der Nutzer aufgefordert zuzustimmen, um die Funktion nutzen zu können
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);

        }
        // greift auf den Externen Speicher des Handy zu um ein Foto auszuwählen
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);

    }

    @Override
    // dies ist die Reaktion der App auf die Auswahl des Nutzers in der oberen Methode
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // prüft ob der Nutzer zugestimmt hat und ob das Bild das wir gewählt haben vorhanden ist
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            // hier werden die ausgewählten Daten, also in diesem Fall das Bild vom Speicher geholt
            // und legen einen Zeiger auf eben diese Daten fest
            Uri selectedImage = data.getData();

            // prüft noch einmal ob die Berechtigung für den Speicherzugriff vorhanden sind
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                // sorgt dafür, das keine Probleme Auftreten, weil zum Beispiel keine Rechte vergeben wurden
                try {
                    // versucht das Bild aus dem Speicher zu holen
                    Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                    // setzt das Bild in das ImageView ein
                    imageView.setImageBitmap(bitmapImage);
                    // setzt das neue Bild als das Aktuelle Bild des Nutzers ein
                    currentBitmap = bitmapImage;

                    // sorgt dafür, dass alle Informationen die die App produziert in der Konsole ausgegeben werden
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                // ist die Berechtigung nicht gesetzt, wird der Nutzer nocheinmal gefragt, ob er den Zugriff zulassen will
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);

            }

        }
    }

    public void saveUserImage(View view) {

        // speichert das gewählte Bild und verkleinert es gegebenenfalls auf 100x100 Pixel, damit, wenn zu größe Bilder genutzt werden, der Speicher
        // nicht voll läuft oder die App abstürzt
        final Bitmap smallerBitmap = currentBitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

        // hier wird das gewählte Bild in einen Base64String umgewandelt, um es speichern zu können
        final String bitmapString = Util.getBase64StringFromBitmap(smallerBitmap);

        // sorgt dafür, das ein StringRequest, also eine Anfrage an den Server gestellt wird
        String create_user_url = getString(R.string.cliq) + "/insertBitmapIntoDB.php";

        // der Request prüft ob die Daten des Scripts correct sind
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
                        // speichert die Daten der Antwort
                        String message = jsonResponse.getString("message");

                        // holt die success Ausgabe des php skriptes und legt es in die "string-variable" success ab, um sie später leichter aufrufen zu können
                        int success = Integer.parseInt(jsonResponse.get("success").toString());


                        if (success == 1) {
                            // der Toast nimmt die Antwort des Servers und gibt diese für den Nutzer in der App als Popup sichtbar aus
                            Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                            // speichert das neue Profilbild zudem auch direkt in den ChatPreferences, um die spätere Nutzung zu erleichtern
                            PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this).edit().putString("image", Util.getBase64StringFromBitmap(smallerBitmap)).apply();

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
                // Hier werden die Daten
                // und in die entsprechenden Variablen geladen um diese dann an den Server weiter zu geben
                params.put("user_id", String.valueOf(userId));
                params.put("bitmapString", bitmapString);

                return params;
            }
        };


        queue.add(postRequest);
    } */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    // gibt den Toolbar Menüpunkten eine Funktion
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.profileSettings) {

            Intent i = new Intent(getApplicationContext(), ProfileSettingsActivity.class);
            startActivity(i);
        }

        if (id == R.id.logout) {

            SharedPreferences.Editor editor = prf.edit();
            editor.clear();
            editor.commit();
            
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            Toast.makeText(ProfileActivity.this, "Logout successful",  Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
