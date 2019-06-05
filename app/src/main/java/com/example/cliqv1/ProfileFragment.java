package com.example.cliqv1;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private View profileFragmentView;
    private TextView userName;
    private CircleImageView userProfile, editProfile;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseReference profileRef, rootRef;
    private String currentUserName, currentUserID;
    private FirebaseAuth firebaseAuth;
    private static final int galleryPick = 1;
    private StorageReference userProfileImageRef;
    private ProgressDialog progressDialog;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        editProfile = getView().findViewById(R.id.editPic);
        userName = getView().findViewById(R.id.name);
        userProfile = getView().findViewById(R.id.profile_image);
        progressDialog = new ProgressDialog(getActivity());

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         /*       Intent iGallery = new Intent();
                iGallery.setAction(Intent.ACTION_GET_CONTENT);
                iGallery.setType("image/*");
                startActivityForResult(iGallery, galleryPick );
         */
            }
        });

        
        retrieveUserInfo();
    }

    private void retrieveUserInfo() {
        rootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image")))){
                    String getName = dataSnapshot.child("name").getValue().toString();
                    String getProfileImage = dataSnapshot.child("image").getValue().toString();

                    userName.setText(getName);
                    Picasso.get().load(getProfileImage).resize(250, 250).into(userProfile);

                } else if ((dataSnapshot.exists())&&(dataSnapshot.hasChild("name"))){
                    String getName = dataSnapshot.child("name").getValue().toString();
                    userName.setText(getName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == galleryPick && data != null){

            progressDialog.setTitle("Avatar ändern");
            progressDialog.setMessage("Bitte warten Sie, bis das Profilbild geändert wurde.");
            progressDialog.show();

            Uri imageUri = data.getData();

            StorageReference filePath = userProfileImageRef.child(currentUserID + ".jpg");
            filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getActivity(), "Avatar geändert", Toast.LENGTH_SHORT).show();
                        final String downloadUrl = filePath.getDownloadUrl().toString();
                        rootRef.child("Users").child(currentUserID).child("image").setValue(downloadUrl)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            progressDialog.dismiss();

                                        } else {
                                            String message = task.getException().toString();
                                            Toast.makeText(getActivity(), "Error: "+message, Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    }
                                });

                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(getActivity(), "Error: "+ message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }

    }
}


