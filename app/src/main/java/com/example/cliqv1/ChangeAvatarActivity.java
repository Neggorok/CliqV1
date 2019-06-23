package com.example.cliqv1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChangeAvatarActivity extends AppCompatActivity {

    ImageView imageView;
    ImageButton imageButton1;
    ImageButton imageButton2;
    ImageButton imageButton3;
    ImageButton imageButton4;
    ImageButton imageButton5;
    ImageButton imageButton6;
    ImageButton imageButton7;
    ImageButton imageButton8;
    int userId;
    String userImage;
    Bitmap currentBitmap;

    RequestQueue queue;

    Button button;

    //private static final int IMAGE_PICK_CODE = 1000;
    //private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avatar);
        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        imageView = findViewById(R.id.profileUnknown);
        imageButton1 = findViewById(R.id.imageButton1);
        imageButton2 = findViewById(R.id.imageButton2);
        imageButton3 = findViewById(R.id.imageButton3);
        imageButton4 = findViewById(R.id.imageButton4);
        imageButton5 = findViewById(R.id.imageButton5);
        imageButton6 = findViewById(R.id.imageButton6);
        imageButton7 = findViewById(R.id.imageButton7);
        imageButton8 = findViewById(R.id.imageButton8);
        button = findViewById(R.id.safe);
        queue = Volley.newRequestQueue(this);

        userId = PreferenceManager.getDefaultSharedPreferences(this).getInt("id", -1);
        userImage = PreferenceManager.getDefaultSharedPreferences(this).getString("image", "-1");



            imageButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = findViewById(R.id.profileUnknown);
                    imageView.setImageResource(R.drawable.cliq_avatar1);

                }
            });

            imageButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = findViewById(R.id.profileUnknown);
                    imageView.setImageResource(R.drawable.cliq_avatar2);

                }
            });
            imageButton3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = findViewById(R.id.profileUnknown);
                    imageView.setImageResource(R.drawable.cliq_avatar3);

                }
            });
            imageButton4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = findViewById(R.id.profileUnknown);
                    imageView.setImageResource(R.drawable.cliq_avatar4);

                }
            });
            imageButton5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = findViewById(R.id.profileUnknown);
                    imageView.setImageResource(R.drawable.cliq_avatar7);

                }
            });
            imageButton6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = findViewById(R.id.profileUnknown);
                    imageView.setImageResource(R.drawable.cliq_avatar6);

                }
            });
            imageButton7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = findViewById(R.id.profileUnknown);
                    imageView.setImageResource(R.drawable.cliq_avatar5);

                }
            });
            imageButton8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = findViewById(R.id.profileUnknown);
                    imageView.setImageResource(R.drawable.cliq_avatar8);

                }
            });
        }

    public void chooseImage(View view) {
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
                            Toast.makeText(ChangeAvatarActivity.this, message, Toast.LENGTH_SHORT).show();
                            // speichert das neue Profilbild zudem auch direkt in den ChatPreferences, um die spätere Nutzung zu erleichtern
                            PreferenceManager.getDefaultSharedPreferences(ChangeAvatarActivity.this).edit().putString("image", Util.getBase64StringFromBitmap(smallerBitmap)).apply();

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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.back) {

            finish();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
// -