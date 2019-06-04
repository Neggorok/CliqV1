package com.example.cliqv1;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserList extends AppCompatActivity {

    private RecyclerView myRecView;
    private DatabaseReference userRef;
    private EditText editText;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        initializeFields();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        userRef = FirebaseDatabase.getInstance().getReference("Pupils");
    }

  /*  private void searchUser() {
        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>() {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users model) {
                Users.class,
                R.layout.activity_user_list,
                UsersViewHolder.class,
                userRef
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return null;
            }
        };
    } */

    private void initializeFields() {
        myRecView = findViewById(R.id.result_list);
        editText = findViewById(R.id.editUser);
        imageView = findViewById(R.id.searchUser);
    }


    public class UsersViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setDetails(String userName, String userImage) {
            TextView user_name = findViewById(R.id.name_text);
            ImageView user_profile = findViewById(R.id.profile_image);
        }
    }
}
