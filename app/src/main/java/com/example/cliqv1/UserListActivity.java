package com.example.cliqv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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
import android.widget.ImageButton;
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

public class UserListActivity extends AppCompatActivity {

    private UserListAdapter adapter;
    private List<User> userList;

    RecyclerView userRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton chat_nav = (ImageButton) findViewById(R.id.chat_nav);
        ImageButton profile_nav = (ImageButton) findViewById(R.id.profile_nav);

        chat_nav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent j = new Intent(getApplicationContext(), GroupChatViewActivity.class);
                startActivity(j);
//                Toast.makeText(UserListActivity.this, "You are already on Groupchat", Toast.LENGTH_SHORT).show();
            }
        });

        profile_nav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserListActivity.this, ProfileActivity.class));
            }
        });

        userList = new ArrayList<>();
        adapter = new UserListAdapter(this, userList);
        userRecyclerView = findViewById(R.id.userList_recycler_view);
        userRecyclerView.setHasFixedSize(true);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userRecyclerView.setAdapter(adapter);

        queue = Volley.newRequestQueue(this);

        loadUserList();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUserList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public void loadUserList() {

        userList.clear();

        String create_user_url = getString(R.string.cliq) + "/getAllUsers_cliq.php";

        StringRequest postRequest = new StringRequest(Request.Method.GET, create_user_url,
                response -> {


                    Log.i("response", response);

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray userArray = (JSONArray) jsonResponse.get("user");

                        for (int i = 0; i < userArray.length(); i++) {

                            JSONObject userJson = userArray.getJSONObject(i);

                            if (userJson.getInt("id") != PreferenceManager.getDefaultSharedPreferences(UserListActivity.this).getInt("id", -1)) {

                                if (!userJson.getString("image").equals("null") && userJson.getString("image").length() > 0) {

                                    String bitmapString = userJson.getString("image");
                                    Bitmap imageBitmap = Util.getBitmapFromBase64String(bitmapString);

                                    userList.add(new User(userJson.getString("username"), imageBitmap));
                                } else {
                                    userList.add(new User(userJson.getString("username"), null));
                                }

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
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
        return true;
    }

    @Override
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





