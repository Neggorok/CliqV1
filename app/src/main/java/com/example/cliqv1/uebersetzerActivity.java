package com.example.cliqv1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class uebersetzerActivity extends AppCompatActivity {

    EditText eingabe;
    TextView translationTextView;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uebersetzer);

        eingabe = findViewById(R.id.eingabeEditText);
        translationTextView = findViewById(R.id.translation);

        queue = Volley.newRequestQueue(this);

    }

    public void getTranslation(View view) {

        String key = "trnsl.1.1.20190506T140410Z.fe5b6005f2c69090.71f3437bd28b97ecdfebb9a52d40e7c40d878eb7";
        String url = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + key +
                "&text=" + eingabe.getText().toString() + "&lang=de";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {

                    Log.i("ausgabe", response);

                    try {

                        JSONObject translation = new JSONObject(response);
                        JSONArray text = (JSONArray) translation.get("text");

                        translationTextView.setText(text.get(0).toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {

        });

        queue.add(stringRequest);


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