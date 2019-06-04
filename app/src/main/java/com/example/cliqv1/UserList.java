package com.example.cliqv1;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserList extends AppCompatActivity {

    private RecyclerView myRecView;
    private DatabaseReference rootRef, userRef;
    private Query query;
    private String userName;
    private FirebaseDatabase database;
    private FirebaseRecyclerAdapter adapter;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        rootRef = FirebaseDatabase.getInstance().getReference();
        database = FirebaseDatabase.getInstance();

        initializeFields();
        getUser();


        if (rootRef != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserList.this, R.style.AlertDialog);
                        builder.setTitle("Benutzer suchen:");

                        final EditText newMemberField = new EditText(UserList.this);
                        newMemberField.setHint("Benutzername");
                        builder.setView(newMemberField);

                        builder.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newMember = newMemberField.getText().toString();

                                if (TextUtils.isEmpty(newMember)) {
                                    Toast.makeText(UserList.this, "Bitte Benutzer angeben", Toast.LENGTH_SHORT).show();
                                } else {
                                    searchUser();
                                }
                            }
                        });
                        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    } else {
                        Toast.makeText(UserList.this, "Du musst einen Admin-Account besitzen, um Gruppen zu erstellen.", Toast.LENGTH_SHORT).show();
                    }

    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView uName;
        public CircleImageView circle;

        public ViewHolder(View itemView) {
            super(itemView);
            TextView uName = findViewById(R.id.name_text);
            CircleImageView circle = findViewById(R.id.profile_image);
        }

        public void uName(String name) {
            uName.setText(name);
        }

        public void circle(String image) {
        }
    }

            public class UsersViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
    }


    private void getUser() {

        userRef = database.getReference().child("Pupils");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userName = dataSnapshot.child("name").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchUser() {

        query = rootRef.child("Users").child("Pupils").orderByChild("name").equalTo(userName);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(query, new SnapshotParser<Users>() {
                        @NonNull
                        @Override
                        public Users parseSnapshot(@NonNull DataSnapshot snapshot) {
                            return new Users(snapshot.child("name").getValue().toString(),
                                    snapshot.child("image").getValue().toString());
                        }
                    }).build();
                    adapter = new FirebaseRecyclerAdapter<Users, ViewHolder>(options) {


                        @NonNull
                        @Override
                        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                            View view = LayoutInflater.from(viewGroup.getContext())
                                    .inflate(R.layout.list_layout, viewGroup, false);

                            return new ViewHolder(view);
                        }

                        @Override
                        protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Users model) {
                            holder.uName(model.getName());
                            holder.circle(model.getImage());
                        }
                    };
                    adapter.startListening();
                    myRecView.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void initializeFields() {
        myRecView = findViewById(R.id.result_list);
    }

}
