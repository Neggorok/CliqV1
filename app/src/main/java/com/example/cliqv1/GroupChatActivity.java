package com.example.cliqv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {
    private EditText messagesInput;
    private ImageButton sendMessagesbtn;
    private ScrollView scrollView;
    private TextView displayMessages;
    private FirebaseUser currentUser;
    private String currentGroupName, currentUserID, currentUserName, currentDate, currentTime;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference rootRef, userRef, groupNameRef, groupMessageKeyRef;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        currentGroupName = getIntent().getExtras().get("groupname").toString();
        Toast.makeText(this, currentGroupName, Toast.LENGTH_SHORT).show();

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Teachers");
        groupNameRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Teachers").child(currentUserID).child("Groups").child(currentGroupName);

        initializeFields();
        getUserInfo();

        sendMessagesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMessageToDatabase();
                messagesInput.setText("");
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        groupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void DisplayMessages(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();

        while(iterator.hasNext()){
            String chatDate = (String)((DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String)((DataSnapshot)iterator.next()).getValue();
            String chatName = (String)((DataSnapshot)iterator.next()).getValue();
            String chatTime = (String)((DataSnapshot)iterator.next()).getValue();

            displayMessages.append(chatName + " :\n" + chatMessage + " \n" + chatTime + "        " + chatDate + "\n\n\n");

            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }


    private void initializeFields() {

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentGroupName);

        sendMessagesbtn = findViewById(R.id.sendBtn);
        messagesInput = findViewById(R.id.groupmessages);
        displayMessages = findViewById(R.id.groupchat_textdisplay);
        scrollView = findViewById(R.id.scroller);

    }

    private void getUserInfo() {

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    currentUserName = dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void saveMessageToDatabase() {
        String message = messagesInput.getText().toString();
        String messagekey = groupNameRef.push().getKey();

        if (TextUtils.isEmpty(message)){
            Toast.makeText(this, "Du kannst nicht 'nichts' verschicken.", Toast.LENGTH_SHORT).show();
        } else {
            Calendar calDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            currentDate = currentDateFormat.format(calDate.getTime());

            Calendar calTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("HH:mm");
            currentTime = currentTimeFormat.format(calTime.getTime());

            HashMap<String, Object> groupMessageKey = new HashMap<>();
            groupNameRef.updateChildren(groupMessageKey);

            groupMessageKeyRef = groupNameRef.child(messagekey);

            HashMap<String, Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("name", currentUserName);
            messageInfoMap.put("message", message);
            messageInfoMap.put("date", currentDate);
            messageInfoMap.put("time", currentTime);
            groupMessageKeyRef.updateChildren(messageInfoMap);
        }
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_groupchat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.newMember){
            groupNameRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    searchMember();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        if(id == R.id.deleteGroup){

        }

        if (id == R.id.back) {
            finish();
            Intent iMain = new Intent(this, MainPageActivity.class);
            startActivity(iMain);
        }

        return super.onOptionsItemSelected(item);
    }

    private void searchMember() {

    }
}
