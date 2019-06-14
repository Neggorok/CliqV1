package com.example.cliqv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class GroupChatActivity extends AppCompatActivity {

    EditText editText;

    private GroupchatAdapter adapter;
    private List<GroupMessage> groupMessageList;

    RecyclerView groupMessageRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;


    RequestQueue queue;

    int loggedInUserId;
    int gruppenID;
    String loggedInUsername;
    String loggedInUserImage;

    String groupchatPartnerUsername;
    String groupchatPartnerImage;
    String groupchatName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        ImageButton btn_attachFile = (ImageButton) findViewById(R.id.btn_attachFile);

        btn_attachFile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PopUpAttachFileActivity.class);
                startActivity(i);
            }
        });

        loggedInUserId = PreferenceManager.getDefaultSharedPreferences(this).getInt("id", -1);


        // FEHLER FEHLER FEHLER FEHLER FEHLER FEHLER FEHLER!!!!!!!!!!!!!!

        gruppenID = PreferenceManager.getDefaultSharedPreferences(this).getInt("gid", -1);

        // FEHLER FEHLER FEHLER FEHLER FEHLER FEHLER FEHLER!!!!!!!!!!!!!!

        loggedInUsername = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "-1");
        loggedInUserImage = PreferenceManager.getDefaultSharedPreferences(this).getString("image", "-1");

//        groupchatPartnerUsername = getIntent().getStringExtra("groupchatPartnerUsername").toString();
        groupchatName = getIntent().getStringExtra("groupchatName").toString();

        groupchatPartnerImage = PreferenceManager.getDefaultSharedPreferences(this).getString("groupchatPartnerImageString", "-1");

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout_groupChat);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadGroupMessages();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        setTitle(groupchatName);

        editText = (EditText) findViewById(R.id.messageEditText);

        groupMessageList = new ArrayList<>();
        adapter = new GroupchatAdapter(this, groupMessageList);
        groupMessageRecyclerView = (RecyclerView) findViewById(R.id.groupMessage_recycler_view);
        groupMessageRecyclerView.setHasFixedSize(true);
        groupMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        groupMessageRecyclerView.setAdapter(adapter);

        queue = Volley.newRequestQueue(this);

        loadGroupMessages();

    }

    public void loadGroupMessages() {

        groupMessageList.clear();

        String create_user_url = getString(R.string.cliq) + "/getMessagesforGroupChat.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, create_user_url,

                response -> {

                    Log.i("response", response);

                    try {

                        JSONObject jsonResponse = new JSONObject(response);
                        String message = jsonResponse.getString("message");
                        JSONArray messageArray = new JSONArray(message);
                        // hier könnte man versuchen den json Response der Gruppe auf zu rufen um dann die Daten unten zu entnehmen
//                        JSONArray groupArray = (JSONArray) jsonResponse.get("gruppen");


                        int success = Integer.parseInt(jsonResponse.get("success").toString());

                        if (success == 1) {

                            for (int i = 0; i < messageArray.length(); i++) {

                                JSONObject messageJson = (JSONObject) messageArray.get(i);

                                // Hier muss zusätzlich die gid abgefragt werden, die von der angewähleten Gruppe mitgegeben wird ----- , && messageJson.getInt("receiver_id") == groupArray.getInt(Integer.parseInt("gid"))
                                if (messageJson.getInt("sender_id") == loggedInUserId  ) {

                                    Bitmap userImage = Util.getBitmapFromBase64String(loggedInUserImage);

                                    groupMessageList.add(new GroupMessage(loggedInUsername, messageJson.get("groupmessage").toString(), messageJson.get("created_at").toString(), userImage));

                                } else {
                                    // FEHLER FEHLER FEHLER FEHLER FEHLER FEHLER FEHLER!!!!!!!!!!!!!!

                                    Bitmap partnerImage = Util.getBitmapFromBase64String(groupchatPartnerImage);
                                    groupMessageList.add(new GroupMessage(loggedInUsername, messageJson.get("groupmessage").toString(), messageJson.get("created_at").toString(), partnerImage));
//                                    groupMessageList.add(new GroupMessage(messageJson.get("sender_id").toString(), messageJson.get("groupmessage").toString(), messageJson.get("created_at").toString(), partnerImage));

                                    // FEHLER FEHLER FEHLER FEHLER FEHLER FEHLER FEHLER!!!!!!!!!!!!!!

                                }

                            }


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    adapter.notifyDataSetChanged();
                    groupMessageRecyclerView.scrollToPosition(groupMessageList.size() - 1);

                }, error -> {


        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(loggedInUserId));
//                params.put("chat_partner_groupname", groupchatName);
                params.put("gruppenID", String.valueOf(gruppenID));


                return params;
            }
        };


        queue.add(postRequest);

    }

    public void sendMessage(View view) {

        String create_user_url = getString(R.string.cliq) + "/insertGroupMessage_cliq.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, create_user_url,

                response -> {

                    Log.i("response", response);

//                    try {
//
//                        JSONObject jsonResponse = new JSONObject(response);
//
//
//                        int success = Integer.parseInt(jsonResponse.get("success").toString());
//
//                        if (success == 1) {
//
//                            Toast.makeText(GroupChatActivity.this, "Nachricht gesendet!", Toast.LENGTH_SHORT).show();
//
//                            editText.setText(" ");
//
//                            loadGroupMessages();
//
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }


                }, error -> {


        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(loggedInUserId));
                params.put("group_message", editText.getText().toString());
                params.put("gruppenID", String.valueOf(gruppenID));


                return params;
            }
        };


        queue.add(postRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.back_to_grouplist) {

            Intent i = new Intent(getApplicationContext(), GroupChatViewActivity.class);
            Toast.makeText(GroupChatActivity.this, "Group overview selected", Toast.LENGTH_SHORT).show();
            startActivity(i);

        }

        if (id == R.id.groupInformation) {

            Intent i = new Intent(getApplicationContext(), GroupSummaryActivity.class);
            startActivity(i);

        }

        if (id == R.id.popupGroupSettings) {

            Intent i = new Intent(getApplicationContext(), PopUpGroupSettingsActivity.class);
            Toast.makeText(GroupChatActivity.this, "Group Settings selected, exclusive authority for the host", Toast.LENGTH_LONG).show();
            startActivity(i);
        }

        if (id == R.id.translator) {

            Intent i = new Intent(getApplicationContext(), PopUpTranslatorActivity.class);
            Toast.makeText(GroupChatActivity.this, "German translation selected", Toast.LENGTH_LONG).show();
            startActivity(i);

        }

//        if (id == R.id.testGruppenInfo) {
//
//            Intent i = new Intent(getApplicationContext(), GroupChatViewActivity.class);
//            Toast.makeText(GroupChatActivity.this, "Group-view selected", Toast.LENGTH_SHORT).show();
//            startActivity(i);
//
//        }

        return super.onOptionsItemSelected(item);
    }

//    public boolean onContextItemSelected(MenuItem item) {
//        switch (item.getItemId())
//        {
//            case R.id.chat_message_delete:
//                deleteMessage();
//                break;
//        }
//        return true;
//    }
//
//    private void deleteMessage() {
//
//    }
}

