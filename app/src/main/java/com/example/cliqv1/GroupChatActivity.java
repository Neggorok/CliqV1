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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
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

    private static final String KEY_EMPTY = "";
    private static final String KEY_BAD_WORD_SH = "scheiße";
    private static final String KEY_BAD_WORD_SHl = "scheiße ";
    private static final String KEY_BAD_WORD_ScH = "scheisse";
    private static final String KEY_BAD_WORD_ScH2 = "scheisse ";
    private static final String KEY_BAD_WORD_MIST = "Schwuchtel";
    private static final String KEY_BAD_WORD_Mist = "Schwuchtel ";
    private static final String KEY_BAD_WORD_HUSO = "Huso";
    private static final String KEY_BAD_WORD_huso = "huso";
    private static final String KEY_BAD_WORD_Hure = "Hure";
    private static final String KEY_BAD_WORD_hure = "hure";
    private static final String KEY_BAD_WORD_idiot = "idiot";
    private static final String KEY_BAD_WORD_Idiot = "Idiot";
    private static final String KEY_BAD_WORD_bastard = "Bastard";
    private static final String KEY_BAD_WORD_Bastard = "Bastard ";
    private static final String KEY_BAD_WORDS_DuHuso = "Du Huso";
    private static final String KEY_BAD_WORDS_duHuso = "Du Huso ";

    RequestQueue queue;

    int loggedInUserId;
    int gruppenID;
    String loggedInUsername;
    String loggedInUserImage;
    int loggedAdmin;
    int loggedModerator;

    private  String Etmessage;
    String groupchatPartnerUsername;
    String groupchatPartnerImage;
    String groupchatName;

    ImageButton sendbutton;
    ImageButton btn_delete;
    Switch deleteSwitch;

    ImageButton chatBackground;
    View view;

    TextView on_off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        view = this.getWindow().getDecorView();
        view.setBackgroundResource(R.color.colorWhite);

        btn_delete = findViewById(R.id.deleteButton);
        sendbutton = findViewById(R.id.sendGroupMessage);
        chatBackground = (ImageButton) findViewById(R.id.chatBackground);
        ImageButton btn_attachFile = (ImageButton) findViewById(R.id.btn_attachFile);
        btn_delete.setEnabled(false);
        on_off = (TextView) findViewById(R.id.on_off);
        deleteSwitch = (Switch) findViewById(R.id.deleteSwitch);

        //Chat-Hintergrund Anderung ermöglichen
        chatBackground.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(GroupChatActivity.this, chatBackground);
                popupMenu.getMenuInflater().inflate(R.menu.menu_chat_background, popupMenu.getMenu());

                // der Toast nimmt die Antwort des Servers und gibt diese für den Nutzer in der App als Popup sichtbar aus
                Toast.makeText(GroupChatActivity.this, "Choose a background color", Toast.LENGTH_SHORT).show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();

                        if (id == R.id.white) {
                            view.setBackgroundResource(R.color.colorWhite);
                        }

                        if (id == R.id.blue) {
                            view.setBackgroundResource(R.drawable.background1);
                        }

                        if (id == R.id.yellow) {
                            view.setBackgroundResource(R.drawable.background2);
                        }

                        if (id == R.id.green) {
                            view.setBackgroundResource(R.drawable.background3);
                        }

                        if (id == R.id.orange) {
                            view.setBackgroundResource(R.drawable.background4);
                        }
                        // der Toast nimmt die Antwort des Servers und gibt diese für den Nutzer in der App als Popup sichtbar aus
                        Toast.makeText(GroupChatActivity.this, "You've chosen " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popupMenu.show();

            }
        });


        loggedAdmin = PreferenceManager.getDefaultSharedPreferences(this).getInt("admin", -1);
        loggedModerator = PreferenceManager.getDefaultSharedPreferences(this).getInt("moderator", -1);


        if (loggedModerator == 0) {
            deleteSwitch.setClickable(false);

        } else {
            deleteSwitch.setClickable(true);
        }

        if (loggedAdmin == 0) {
            deleteSwitch.setClickable(false);

        } else {
            deleteSwitch.setClickable(true);
        }


        deleteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    on_off.setText("ON");
                    btn_delete.setEnabled(true);
                } else {
                    on_off.setText("OFF");
                    btn_delete.setEnabled(false);

                }
            }
        });


        btn_attachFile.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PopUpAttachFileActivity.class);
                startActivity(i);
            }
        });

        loggedInUserId = PreferenceManager.getDefaultSharedPreferences(this).getInt("id", -1);

        gruppenID = PreferenceManager.getDefaultSharedPreferences(this).getInt("gid", -1);

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

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Etmessage = editText.getText().toString();

                if (validateInputs()) {
                    try {
                        sendMessage();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

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

    private boolean validateInputs() {
        if(KEY_EMPTY.equals(Etmessage)){
            editText.setError("Das Chatleiste darf nicht leer sein!");
            editText.requestFocus();
            return false;
        }
        if(KEY_BAD_WORD_SH.equals(Etmessage)){
            editText.setError("unzulässiges Wort enthalten!");
            editText.requestFocus();
            return false;
        }
        if(KEY_BAD_WORD_SHl.equals(Etmessage)){
            editText.setError("unzulässiges Wort enthalten!");
            editText.requestFocus();
            return false;
        }
        if(KEY_BAD_WORD_ScH.equals(Etmessage)){
            editText.setError("unzulässiges Wort enthalten!");
            editText.requestFocus();
            return false;
        }
        if(KEY_BAD_WORD_ScH2.equals(Etmessage)){
            editText.setError("unzulässiges Wort enthalten!");
            editText.requestFocus();
            return false;
        }
        if(KEY_BAD_WORD_Mist.equals(Etmessage)){
            editText.setError("unzulässiges Wort enthalten!");
            editText.requestFocus();
            return false;
        }
        if(KEY_BAD_WORD_MIST.equals(Etmessage)){
            editText.setError("unzulässiges Wort enthalten!");
            editText.requestFocus();
            return false;
        }
        if(KEY_BAD_WORD_Bastard.equals(Etmessage)){
            editText.setError("unzulässiges Wort enthalten!");
            editText.requestFocus();
            return false;
        }
        if(KEY_BAD_WORD_bastard.equals(Etmessage)){
            editText.setError("unzulässiges Wort enthalten!");
            editText.requestFocus();
            return false;
        }
        if(KEY_BAD_WORD_Hure.equals(Etmessage)){
            editText.setError("unzulässiges Wort enthalten!");
            editText.requestFocus();
            return false;
        }
        if(KEY_BAD_WORD_hure.equals(Etmessage)){
            editText.setError("unzulässiges Wort enthalten!");
            editText.requestFocus();
            return false;
        }
        if(KEY_BAD_WORD_idiot.equals(Etmessage)){
            editText.setError("unzulässiges Wort enthalten!");
            editText.requestFocus();
            return false;
        }
        if(KEY_BAD_WORD_Idiot.equals(Etmessage)){
            editText.setError("unzulässiges Wort enthalten!");
            editText.requestFocus();
            return false;
        }
        if(KEY_BAD_WORDS_duHuso.equals(Etmessage)){
            editText.setError("unzulässiges Wort enthalten!");
            editText.requestFocus();
            return false;
        }
        if(KEY_BAD_WORDS_DuHuso.equals(Etmessage)){
            editText.setError("unzulässiges Wort enthalten!");
            editText.requestFocus();
            return false;
        }
        if(KEY_BAD_WORD_HUSO.equals(Etmessage)){
            editText.setError("unzulässiges Wort enthalten!");
            editText.requestFocus();
            return false;
        }
        if(KEY_BAD_WORD_huso.equals(Etmessage)){
            editText.setError("unzulässiges Wort enthalten!");
            editText.requestFocus();
            return false;
        }


        return true;
    }



    public void loadGroupMessages() {

        // leert die Messageliste, damit sich die Einträge später nicht stapeln, also immer und immer wieder aneinander hängen
        groupMessageList.clear();

        // sorgt dafür, das ein StringRequest, also eine Anfrage an den Server gestellt wird
        // die Anfrage löst das php Skript aus, das hier definiert wird
        String create_user_url = getString(R.string.cliq) + "/getMessagesforGroupChat.php";

        // erzeugt einen neuen Request an den Server, der das oben definierte PhP file ausführt
        StringRequest postRequest = new StringRequest(Request.Method.POST, create_user_url,

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
                        // nun werden die Einträge des message Arrays vom Server in Stringform
                        // in der Variable message gespeichert
                        String message = jsonResponse.getString("message");
                        // erzeugt ein Array das mit den JSONObjekten gefüllt wird,
                        // sodass jeder Eintrag zu einem Objekt wird
                        JSONArray messageArray = new JSONArray(message);


                        // holt die success Ausgabe des php skriptes und legt es in die "string-variable" success ab, um sie später leichter aufrufen zu können
                        int success = Integer.parseInt(jsonResponse.get("success").toString());

                        // prüft ob die success Ausgabe in der Antwort des Servers 1 ist
                        if (success == 1) {

                            // durchläuft alle Einträge der Nachrichten im Array
                            for (int i = 0; i < messageArray.length(); i++) {

                                // nimmt sich jeweils die Message an der jeweiligen position i
                                JSONObject messageJson = (JSONObject) messageArray.get(i);

                                // Prüft ob die Senderid der vorliegenden eingeloggten Userid entspricht
                                if (messageJson.getInt("sender_id") == loggedInUserId) {

                                    // wenn dem so ist, wird das Bild des eingeloggten Users geladen
                                    Bitmap userImage = Util.getBitmapFromBase64String(loggedInUserImage);

                                    // und es wird ein neues Messageitem erstellt, das mit dem Namen des eingeloggten Users, seinem Bild, der Message und dem Zeitstempel gefüllt wird
                                    groupMessageList.add(new GroupMessage(loggedInUsername, messageJson.get("groupmessage").toString(), messageJson.get("created_at").toString(), userImage));

                                } else {

                                    // Wenn die Senderid nicht die ID des eingeloggten Users ist, wird das Bild des Chatpartners geladen,
                                    // dieses wird durch die Informationen des php scriptes aus der DB geladen
                                    Bitmap partnerImage = Util.getBitmapFromBase64String(groupchatPartnerImage);
                                    // dann wird ein neues Message Item erstellt, das mit dem namen und dem Bild
                                    // des Chatpartners und wieder mit der Message und dem Zeitstempel gefüllt wird
                                    groupMessageList.add(new GroupMessage(loggedInUsername, messageJson.get("groupmessage").toString(), messageJson.get("created_at").toString(), partnerImage));


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
                // Hier werden die Daten ausgelesen, in die entsprechenden Variablen geladen,
                // die für den Server benötigt werden
                params.put("user_id", String.valueOf(loggedInUserId));
//                params.put("chat_partner_groupname", groupchatName);
                params.put("gruppenID", String.valueOf(gruppenID));


                return params;
            }
        };


        queue.add(postRequest);

    }

    public void sendMessage() throws JSONException {

        // sorgt dafür, das ein StringRequest, also eine Anfrage an den Server gestellt wird
        // die Anfrage löst das php Skript aus, das hier definiert wird
        String create_user_url = getString(R.string.cliq) + "/insertGroupMessage_cliq.php";

        // erzeugt einen neuen Request an den Server, der das oben definierte PhP file ausführt
        StringRequest postRequest = new StringRequest(Request.Method.POST, create_user_url,

                // stellt die Antwort des Servers dar
                response -> {

                    // gibt die Antwort des Servers auf der AS Console aus.
                    // dient nur zur Kontrolle
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


                    // sorgt dafür, dass wenn fehler im Try bereich auftreten, die Fehlermeldung in der Konsole ausgegeben wird
                }, error -> {


        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Hier werden die Daten ausgelesen, in die entsprechenden Variablen geladen,
                // die für den Server benötigt werden
                params.put("user_id", String.valueOf(loggedInUserId));
                params.put("group_message", editText.getText().toString());
                params.put("gruppenID", String.valueOf(gruppenID));


                return params;
            }
        };


        // addet den request zur Request Queue
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

        //zurück zum Group-Overview
        if (id == R.id.back_to_grouplist) {

            Intent i = new Intent(getApplicationContext(), GroupChatViewActivity.class);
            Toast.makeText(GroupChatActivity.this, "Group overview selected", Toast.LENGTH_SHORT).show();
            startActivity(i);

        }

        //Gruppeninformationen Zugriff
//        if (id == R.id.groupInformation) {

//            Intent i = new Intent(getApplicationContext(), GroupSummaryActivity.class);
//            startActivity(i);

//        }

        //Deutsch-Übersetzer Zugriff
        if (id == R.id.translator) {

            Intent i = new Intent(getApplicationContext(), TranslatorActivity.class);
            Toast.makeText(GroupChatActivity.this, "German translation selected", Toast.LENGTH_LONG).show();
            startActivity(i);

        }

        //RequestNewMember() kann nur vom Moderator geöffnet werden
        if(loggedModerator == 1) {
            if (id == R.id.addGroupMember) {

                RequestNewMember();

            }else {
                Toast.makeText(GroupChatActivity.this, "This function is only available for the moderator", Toast.LENGTH_LONG).show();
            }
        }

            return super.onOptionsItemSelected(item);
    }

    //Pop up um Gruppenmitglieder hinzufügen zu können
    private void RequestNewMember() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        builder.setTitle("Enter Username:");

        final EditText newMemberField = new EditText(this);
        newMemberField.setHint("Max Mustermann");
        builder.setView(newMemberField);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String username = newMemberField.getText().toString();

                //Username wird benötigt um Gruppenmitglieder hinzufügen zu können
                if (TextUtils.isEmpty(username)) {

                    Toast.makeText(GroupChatActivity.this, "Please choose a username",  Toast.LENGTH_SHORT).show();
                }
                else {
                    CreateNewMember(username);
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
    }

    private void CreateNewMember(String username) {

        Toast.makeText(GroupChatActivity.this, username + " was successfully added", Toast.LENGTH_SHORT).show();
    }
}

