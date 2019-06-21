package com.example.cliqv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupChatViewActivity extends AppCompatActivity {

    private GroupListAdapter adapter;
    private List<Gruppen> groupList;
    Intent intent;

    RecyclerView groupRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    int loggedAdmin;
    RequestQueue queue;
    SharedPreferences prf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentview liegt das Layout fest, das für diese Seite verwendet werden soll
        setContentView(R.layout.activity_group_chat_view);
        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        loggedAdmin = PreferenceManager.getDefaultSharedPreferences(this).getInt("admin", -1);
        intent = new Intent(GroupChatViewActivity.this,MainActivity.class);
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
                        Toast.makeText(GroupChatViewActivity.this, "You are already on groups",  Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_profile:
                        Intent p = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(p);
                        break;
                }
                return true;
            }
        });

        groupList = new ArrayList<>();
        adapter = new GroupListAdapter(this, groupList);
        // die RecyclerView sorgt dafür, das die App in einem bestimmten Bereich, also dem Bereich der Recyclerview,
        // durch das SwipeRefresh Layout, neu geladen wird, wenn man ´von oben nach unten über den Bildschirm wischt
        // folgend werden ihr noch die nötigen Informationen zugewiesen, wie den Verweiß auf das Layout
        groupRecyclerView = findViewById(R.id.groupchat_recycler_view);
        // die genaue Größe
        groupRecyclerView.setHasFixedSize(true);
        // welches Layout verwendet wird
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // welchen Adapter es verwenden soll
        groupRecyclerView.setAdapter(adapter);

        queue = Volley.newRequestQueue(this);

        // lädt zum create die Gruppen liste neu
        loadGroupList();

        // sorgt, wie oben beschrieben dafür, das ein bestimmter Bereich neu geladen wird, wenn über den Bildschirm gestrichen wird
        swipeRefreshLayout = findViewById(R.id.groupSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // hier wird fest gelegt, was beim Refreshen der Seite ausgeführt wird
                // in diesem Fall also wird die GroupList noch einmal neu geladen
                loadGroupList();
                // sorgt dafür, dass sich das Refresh Rädchen nicht ins unendliche dreht, sondern die Animation nach einem durchlauf beendet wird
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public void loadGroupList() {
        // leert die Gruppenliste, damit sich die Einträge später nicht stapeln, also immer und immer wieder aneinander hängen
        groupList.clear();

        // sorgt dafür, das ein StringRequest, also eine Anfrage an den Server gestellt wird
        // die Anfrage löst das php Skript aus, das hier definiert wird
        String create_group_url = getString(R.string.cliq) + "/getAllGroups.php";

        // erzeugt einen neuen Request an den Server, der das oben angesprochene PhP file ausführt
        StringRequest postRequest = new StringRequest(Request.Method.GET, create_group_url,

                // stellt die Antwort des Servers dar
                response -> {

                    // gibt die Antwort des Servers auf der AS Console aus.
                    // dient nur zur Kontrolle
                    Log.i("response", response);

                    // der Try - catch bereich funktioniert ähnlich wie eine If Abfrage
                    // es wird im Try Bereich versucht auf den Response des Servers zu reagieren
                    // und sollte die Antwort des Servers die Erwartete sein, reagiert der Catch Bereich dementsprechend
                    try {

                        // wandelt die Antwort des Servers in JSON um
                        JSONObject jsonResponse = new JSONObject(response);
                        // erzeugt ein Array das mit den JSONObjekten gefüllt wird
                        JSONArray groupArray = (JSONArray) jsonResponse.get("gruppen");

                        // durchläuft das erzeugte Array
                        for (int i = 0; i < groupArray.length(); i++) {

                            // hier wird für jeden Schleifendurchlauf ein Gruppenobjekt aufgenommen
                            JSONObject groupJson = groupArray.getJSONObject(i);

                                // prüft ob das Gruppenimage NULL ist und (zur sicherheit) ob der Bildstring größer als 0 ist
                                if (!groupJson.getString("groupimage").equals("null") && groupJson.getString("groupimage").length() > 0) {

                                    // wenn dem so ist, wird das vorhandene Image als String aus dem Response geholt
                                    String bitmapString = groupJson.getString("groupimage");
                                    // und durch die Methode in der Util Klasse zu einer Bitmap umgebaut
                                    Bitmap imageBitmap = Util.getBitmapFromBase64String(bitmapString);

                                    // Anschließend wird zur groupliste ein neuer Eintrag hinzugefügt, der den Gruppennamen und das neue Bild enthält
                                    groupList.add(new Gruppen(groupJson.getString("groupname"), imageBitmap));
                                } else {
                                    // andernfalls wird zur groupliste ein neuer Eintrag hinzugefügt, der den Gruppennamen und das Standartbild enthält
                                    groupList.add(new Gruppen(groupJson.getString("groupname"), null));
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
        getMenuInflater().inflate(R.menu.menu_group_chat_view, menu);
        return true;
    }

    @Override
    // gibt den Toolbar Menüpunkten eine Funktion
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if(loggedAdmin == 1)
        {
        if (id == R.id.createNewGroup) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
            builder.setTitle("Enter Group Name:");

            final EditText groupNameField = new EditText(this);
            groupNameField.setHint("Klasse 1A");
            builder.setView(groupNameField);

            builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    String groupName = groupNameField.getText().toString();

                    if (TextUtils.isEmpty(groupName)) {

                        Toast.makeText(GroupChatViewActivity.this, "Please choose a group name",  Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // sorgt dafür, das ein StringRequest, also eine Anfrage an den Server gestellt wird
                        String create_newgroup_url = getString(R.string.cliq) + "/createGroups_cliq.php";

                        // der Request prüft ob die Daten des Scripts "/createGroups_cliq.php" korrect sind
                        StringRequest postRequest = new StringRequest(Request.Method.POST, create_newgroup_url,

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
                                        Toast.makeText(GroupChatViewActivity.this, jsonResponse.get("message").toString(), Toast.LENGTH_SHORT).show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    //adapter.notifyDataSetChanged();

                                }, error -> {

                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                // Hier werden die Eingaben aus den EditTexten userName und userPassword ausgelesen
                                // und in die entsprechenden Variablen geladen
                                params.put("groupname", groupNameField.getText().toString());

                                return params;
                            }

                        };

                        // addet den request zur Request Queue
                        queue.add(postRequest);
                        Intent iGCV = new Intent(GroupChatViewActivity.this, GroupChatViewActivity.class);
                        startActivity(iGCV);
                    }
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.cancel();

                }
            });

            builder.show();

        }}
        else {
            Toast.makeText(GroupChatViewActivity.this, "Only administrators can create groups",  Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.logout) {

            SharedPreferences.Editor editor = prf.edit();
            editor.clear();
            editor.commit();

            PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("id", 0).apply();

            startActivity(intent);
            Toast.makeText(GroupChatViewActivity.this, "Logout successful",  Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}
