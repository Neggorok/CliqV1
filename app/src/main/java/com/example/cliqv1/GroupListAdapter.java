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

import java.util.ArrayList;
import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter {

    public ArrayList<Gruppen> groupListe;
    public GroupChatViewActivity activity;

    public GroupListAdapter(GroupChatViewActivity activity, List<Gruppen> list){

        this.activity = activity;
        groupListe = (ArrayList) list;

    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout linearLayout;
        public TextView groupname;
        public ImageView gruppenImage;

        public GroupViewHolder (View view){

            super(view);

            linearLayout = view.findViewById(R.id.groupLinearLayout);
            groupname = view.findViewById(R.id.groupNameTextView);
            gruppenImage = view.findViewById(R.id.groupImageIV);



        }
    }

    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.activity.getLayoutInflater().inflate(R.layout.group_list_item, parent, false);
        return new GroupListAdapter.GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Gruppen gruppe = groupListe.get(position);

        GroupListAdapter.GroupViewHolder groupHolder = (GroupViewHolder) holder;

        groupHolder.groupname.setText(gruppe.getName());

        if(gruppe.getImage() != null){

            groupHolder.gruppenImage.setImageBitmap(Bitmap.createScaledBitmap(gruppe.getImage(), 60, 60, false));

        }else{
            Bitmap standardImageBitmap = Util.getBitmapFromDrawable(activity, R.drawable.teacher);
            gruppe.setImage(standardImageBitmap);

            groupHolder.gruppenImage.setImageBitmap(standardImageBitmap);
        }

        groupHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(view.getContext(), GroupChatActivity.class);
                // hier muss später zusätzlich der name des Gesprächspartners sondern der "raum" der Gesprächsgruppe übergeben werden
                i.putExtra("chatPartnerUsername", groupListe.get(position).getName());
                String bitmapString = Util.getBase64StringFromBitmap(gruppe.getImage());
                PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("chatPartnerImageString", bitmapString).apply();

                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return groupListe.size();
    }

}
