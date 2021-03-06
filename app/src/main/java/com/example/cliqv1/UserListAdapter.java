package com.example.cliqv1;


import android.content.Intent;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter {

    public ArrayList<User> userListe;
    public UserListActivity activity;

    public UserListAdapter(UserListActivity activity, List<User> list){

        this.activity = activity;
        userListe = (ArrayList) list;

    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout linearLayout;
        public TextView name;
        public ImageView userImage;

        public UserViewHolder (View view){

            super(view);

            linearLayout = view.findViewById(R.id.linearLayout);
            name = view.findViewById(R.id.nameTextView);
            userImage = view.findViewById(R.id.userImageIV);



        }
    }

    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.activity.getLayoutInflater().inflate(R.layout.user_list_item, parent, false);
        return new UserListAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final User user = userListe.get(position);

        UserListAdapter.UserViewHolder userHolder = (UserViewHolder) holder;

        userHolder.name.setText(user.getName());

        if(user.getImage() != null){

            userHolder.userImage.setImageBitmap(Bitmap.createScaledBitmap(user.getImage(), 60, 60, false));

        }else{
            Bitmap standardImageBitmap = Util.getBitmapFromDrawable(activity, R.drawable.standard_user_image);
            user.setImage(standardImageBitmap);

            userHolder.userImage.setImageBitmap(standardImageBitmap);
        }

        userHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(view.getContext(), ChatActivity.class);

                i.putExtra("chatPartnerUsername", userListe.get(position).getName());
                String bitmapString = Util.getBase64StringFromBitmap(user.getImage());
                PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("chatPartnerImageString", bitmapString).apply();

                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userListe.size();
    }

}