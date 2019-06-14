package com.example.cliqv1;

import android.content.DialogInterface;
import android.content.Intent;
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

    RecyclerView groupRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    int loggedAdmin;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_view);
        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);


        loggedAdmin = PreferenceManager.getDefaultSharedPreferences(this).getInt("admin", -1);

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
        groupRecyclerView = findViewById(R.id.groupchat_recycler_view);
        groupRecyclerView.setHasFixedSize(true);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        groupRecyclerView.setAdapter(adapter);

        queue = Volley.newRequestQueue(this);

        loadGroupList();

        swipeRefreshLayout = findViewById(R.id.groupSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadGroupList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public void loadGroupList() {

        groupList.clear();

        String create_group_url = getString(R.string.cliq) + "/getAllGroups.php";

        StringRequest postRequest = new StringRequest(Request.Method.GET, create_group_url,
                response -> {


                    Log.i("response", response);

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray groupArray = (JSONArray) jsonResponse.get("gruppen");

                        for (int i = 0; i < groupArray.length(); i++) {

                            JSONObject groupJson = groupArray.getJSONObject(i);

                                if (!groupJson.getString("groupimage").equals("null") && groupJson.getString("groupimage").length() > 0) {

                                    String bitmapString = groupJson.getString("groupimage");
                                    Bitmap imageBitmap = Util.getBitmapFromBase64String(bitmapString);

                                    groupList.add(new Gruppen(groupJson.getString("groupname"), imageBitmap));
                                } else {
                                    groupList.add(new Gruppen(groupJson.getString("groupname"), null));
                                }



                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    adapter.notifyDataSetChanged();

                }, error -> {

        });

// Add the request to the RequestQueue.
        queue.add(postRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_chat_view, menu);
        return true;
    }

    @Override
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

// Add the request to the RequestQueue.
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

            PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("id", 0).apply();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            Toast.makeText(GroupChatViewActivity.this, "Logout successful",  Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}
