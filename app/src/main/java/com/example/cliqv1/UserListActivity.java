package com.example.cliqv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserListActivity extends AppCompatActivity {



    private UserListAdapter adapter;
    private List<User> userList;



    SearchView sv;
    RecyclerView userRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentview liegt das Layout fest, das für diese Seite verwendet werden soll
        setContentView(R.layout.activity_user_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.NavBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.nav_contacts:
                        Toast.makeText(UserListActivity.this, "You are already on contacts", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_groups:
                        Intent g = new Intent(getApplicationContext(), GroupChatViewActivity.class);
                        startActivity(g);
                        break;

                    case R.id.nav_profile:
                        Intent p = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(p);
                        break;
                }
                return true;
            }
        });


        userList = new ArrayList<>();

        adapter = new UserListAdapter(this, userList);
        // die RecyclerView sorgt dafür, das die App in einem bestimmten Bereich, also dem Bereich der Recyclerview,
        // durch das SwipeRefresh Layout, neu geladen wird, wenn man ´von oben nach unten über den Bildschirm wischt
        // folgend werden ihr noch die nötigen Informationen zugewiesen, wie den Verweiß auf das Layout
        userRecyclerView = findViewById(R.id.userList_recycler_view);
        // die genaue Größe
        userRecyclerView.setHasFixedSize(true);
        // welches Layout verwendet wird
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // welchen Adapter es verwenden soll
        userRecyclerView.setAdapter(adapter);

        queue = Volley.newRequestQueue(this);
        // lädt zum create die Userliste neu
        loadUserList();
        // sorgt, wie oben beschrieben dafür, das ein bestimmter Bereich neu geladen wird, wenn über den Bildschirm gestrichen wird
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // hier wird fest gelegt, was beim Refreshen der Seite ausgeführt wird
                // in diesem Fall also wird die Userliste noch einmal neu geladen
                loadUserList();
                // sorgt dafür, dass sich das Refresh Rädchen nicht ins unendliche dreht, sondern die Animation nach einem durchlauf beendet wird
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    public void loadUserList() {
        // leert die Userliste, damit sich die Einträge später nicht stapeln, also immer und immer wieder aneinander hängen
        userList.clear();

        // sorgt dafür, das ein StringRequest, also eine Anfrage an den Server gestellt wird
        // die Anfrage löst das php Skript aus, das hier definiert wird
        String create_user_url = "https://cliqstudent.000webhostapp.com/cliq/getAllUsers_cliq.php/";

        // erzeugt einen neuen Request an den Server, der das oben definierte PhP file ausführt
        StringRequest postRequest = new StringRequest(Request.Method.GET, create_user_url,
                // stellt die Antwort des Servers dar
                response -> {

                    // gibt die Antwort des Servers auf der AS Console aus.
                    // dient nur zur Kontrolle
                    Log.i("response", response);

                    // der Try - catch bereich funktioniert ähnlich wie eine If Abfrage
                    // es wird im Try Bereich versucht auf den Response des Servers zu reagieren
                    // und sollte die Antwort des Servers die Erwartete sein, reagiert der Catch Bereich dementsprechend
                    try {

                        // wandelt die Antwort des Servers in ein JSONObjekt um
                        JSONObject jsonResponse = new JSONObject(response);
                        // erzeugt ein Array das mit den JSONObjekten gefüllt wird
                        JSONArray userArray = (JSONArray) jsonResponse.get("user");

                        // durchläuft das erzeugte Array
                        for (int i = 0; i < userArray.length(); i++) {
                            // hier wird für jeden Schleifendurchlauf ein Userobjekt aufgenommen
                            JSONObject userJson = userArray.getJSONObject(i);

                            // um dann zu testen ob das Objekt die Id des eingeloggten Nutzers hat und wenn das so ist, wird er nicht in der Liste aufgeführt
                            // man will ja nicht mit sich selbst chatten
                            if (userJson.getInt("id") != PreferenceManager.getDefaultSharedPreferences(UserListActivity.this).getInt("id", -1)) {

                                // prüft ob das Userimage NULL ist und (zur sicherheit) ob der Bildstring größer als 0 ist
                                if (!userJson.getString("image").equals("null") && userJson.getString("image").length() > 0) {

                                    // wenn dem so ist, wird das vorhandene Image als String aus dem Response geholt
                                    String bitmapString = userJson.getString("image");
                                    // und durch die Methode in der Util Klasse zu einer Bitmap umgebaut
                                    Bitmap imageBitmap = Util.getBitmapFromBase64String(bitmapString);

                                    // Anschließend wird zur Userliste ein neuer Eintrag hinzugefügt, der den Usernamen und das neue Bild enthält
                                    userList.add(new User(userJson.getString("username"), imageBitmap));
                                } else {
                                    // andernfalls wird zur Userliste ein neuer Eintrag hinzugefügt, der den Usernamen und das Standartbild enthält
                                    userList.add(new User(userJson.getString("username"), null));
                                }

                            }

                        }
                        // sorgt dafür, dass alle Informationen die die App produziert in der Konsole ausgegeben werden
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // sorgt dafür, dass der Adapter über die Änderungen des Datenbestandes informiert wird
                    // sonst bleibt die Liste in der Ansicht leer
                    adapter.notifyDataSetChanged();
                    // sorgt dafür, dass wenn fehler im Try bereich auftreten, die Fehlermeldung in der Konsole ausgegeben wird
                }, error -> {

        });

        // addet den request zur Request Queue
        queue.add(postRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    // gibt den Toolbar Menüpunkten eine Funktion
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search) {

            Toast.makeText(UserListActivity.this, "Nach Gruppenname suchen", Toast.LENGTH_SHORT).show();

        }

        if (id == R.id.logout) {

            PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("id", 0).apply();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            Toast.makeText(UserListActivity.this, "Logout successful", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


}




