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
import com.squareup.picasso.Picasso;

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

        myRecView = findViewById(R.id.result_list);
        myRecView.setLayoutManager(new LinearLayoutManager(this));
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Pupils");
        database = FirebaseDatabase.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(userRef, Users.class)
                .build();

        FirebaseRecyclerAdapter<Users, FindUserViewholder> adapter = new FirebaseRecyclerAdapter<Users, FindUserViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindUserViewholder holder, int position, @NonNull Users model) {
                holder.userName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.profileImage);
            }

            @NonNull
            @Override
            public FindUserViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_layout, viewGroup, false);
                FindUserViewholder viewHolder = new FindUserViewholder(view);
                return viewHolder;
            }
        };

        myRecView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class FindUserViewholder extends RecyclerView.ViewHolder {

        TextView userName;
        CircleImageView profileImage;

        public FindUserViewholder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.name_text);
            profileImage = itemView.findViewById(R.id.profile_image);
        }
    }
}
