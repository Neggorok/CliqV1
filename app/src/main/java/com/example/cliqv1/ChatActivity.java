package com.example.cliqv1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class ChatActivity extends AppCompatActivity {

    EditText editText;

    private ChatAdapter adapter;
    private List<Message> messageList;

    RecyclerView messageRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    RequestQueue queue;

    int loggedInUserId;
    String loggedInUsername;
    String loggedInUserImage;

    String chatPartnerUsername;
    String chatPartnerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        loggedInUserId = PreferenceManager.getDefaultSharedPreferences(this).getInt("id", -1);
        loggedInUsername = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "-1");
        loggedInUserImage = PreferenceManager.getDefaultSharedPreferences(this).getString("image", "-1");

        chatPartnerUsername = getIntent().getStringExtra("chatPartnerUsername").toString();

        chatPartnerImage = PreferenceManager.getDefaultSharedPreferences(this).getString("chatPartnerImageString", "-1");

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout_chat);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMessages();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        setTitle(chatPartnerUsername);

        editText = (EditText) findViewById(R.id.messageEditText);

        messageList = new ArrayList<>();
        adapter = new ChatAdapter(this, messageList);
        messageRecyclerView = (RecyclerView) findViewById(R.id.message_recycler_view);
        messageRecyclerView.setHasFixedSize(true);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageRecyclerView.setAdapter(adapter);

        queue = Volley.newRequestQueue(this);

        loadMessages();

    }

    public void loadMessages() {

        messageList.clear();

        String create_user_url = getString(R.string.cliq) + "/getMessagesForChat_cliq.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, create_user_url,

                response -> {

                    Log.i("response", response);

                    try {

                        JSONObject jsonResponse = new JSONObject(response);
                        String message = jsonResponse.getString("message");
                        JSONArray messageArray = new JSONArray(message);


                        int success = Integer.parseInt(jsonResponse.get("success").toString());

                        if(success == 1){

                            for (int i = 0; i < messageArray.length(); i++) {

                                JSONObject messageJson = (JSONObject) messageArray.get(i);

                                if(messageJson.getInt("sender_id") == loggedInUserId) {

                                    Bitmap userImage = Util.getBitmapFromBase64String(loggedInUserImage);

                                    messageList.add(new Message(loggedInUsername, messageJson.get("message").toString(), messageJson.get("created_at").toString(), userImage));

                                } else {

                                    Bitmap partnerImage = Util.getBitmapFromBase64String(chatPartnerImage);
                                    messageList.add(new Message(chatPartnerUsername, messageJson.get("message").toString(), messageJson.get("created_at").toString(), partnerImage));

                                }

                            }



                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    adapter.notifyDataSetChanged();
                    messageRecyclerView.scrollToPosition(messageList.size()-1);

                }, error -> {


        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(loggedInUserId));
                params.put("chat_partner_username", chatPartnerUsername);

                return params;
            }
        };


        queue.add(postRequest);

    }

    public void sendMessage(View view) {

        String create_user_url = getString(R.string.cliq) + "/insertMessage_cliq.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, create_user_url,

                response -> {

                    Log.i("response", response);

                    try {

                        JSONObject jsonResponse = new JSONObject(response);


                        int success = Integer.parseInt(jsonResponse.get("success").toString());

                        if(success == 1){

                            Toast.makeText(ChatActivity.this, "Nachricht gesendet!", Toast.LENGTH_SHORT).show();

                            editText.setText(" ");

                            loadMessages();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }, error -> {


        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(loggedInUserId));
                params.put("chat_partner_username", chatPartnerUsername);
                params.put("message", editText.getText().toString());

                return params;
            }
        };


        queue.add(postRequest);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_group_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_settings:
                Toast.makeText(this,"Settings selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.set_logout:
                Toast.makeText(this,"Design selected", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
public void openSettings(View view) {
        Intent intent = new Intent(this, GroupChatSettingsActivity.class);
        setContentView(R.layout.activity_group_chat_settings);
}
}
