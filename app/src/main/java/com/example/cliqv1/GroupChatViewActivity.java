package com.example.cliqv1;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupChatViewActivity extends AppCompatActivity {

    private GroupListAdapter adapter;
    private List<User> userList;

    RecyclerView userRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userList = new ArrayList<>();
        adapter = new GroupListAdapter(GroupChatViewActivity.this, userList);
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

                            if (userJson.getInt("id") != PreferenceManager.getDefaultSharedPreferences(GroupChatViewActivity.this).getInt("id", -1)) {

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
}
