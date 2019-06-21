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

    // stellt den Constructor dar, um im späteren Verlauf der App einen GroupListAdapter verwenden, bzw erzeugen zu können
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
        // hier wird das Layout des group_list_item aktiviert
        View view = this.activity.getLayoutInflater().inflate(R.layout.group_list_item, parent, false);
        return new GroupListAdapter.GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // holt ein Groupobjekt aus der Groupliste
        final Gruppen gruppe = groupListe.get(position);

        // der groupholder ist dafür da , die einzelnen Elemente wie den Groupname abzugreifen
        GroupListAdapter.GroupViewHolder groupHolder = (GroupViewHolder) holder;
        // und sie dann in diesem Abschnitt in die vorgesehenen Bereiche, einzubinden
        groupHolder.groupname.setText(gruppe.getName());
        // prüft ob das Gruppenimage leer ist
        if(gruppe.getImage() != null){
            // wenn es nicht leer ist wird das übergebene Image gespeichert und mit angezeigt
            groupHolder.gruppenImage.setImageBitmap(Bitmap.createScaledBitmap(gruppe.getImage(), 60, 60, false));

        }else{
            // wenn es jedoch leer ist, wird ein Standartbild zugewiesen
            Bitmap standardImageBitmap = Util.getBitmapFromDrawable(activity, R.drawable.teacher);
            // setzt das Standartbild als neues Gruppenbild
            gruppe.setImage(standardImageBitmap);

            groupHolder.gruppenImage.setImageBitmap(standardImageBitmap);
        }

        groupHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startet beim anklicken des Items die nächste Activity, wo dann gechattet werden kann
                Intent i = new Intent(view.getContext(), GroupChatActivity.class);
                // übergibt der nächsten Activity die folgenden, beötigten Daten
//                i.putExtra("groupchatPartnerUsername", groupListe.get(position).getName());
                i.putExtra("groupchatName", groupListe.get(position).getName());
                String bitmapString = Util.getBase64StringFromBitmap(gruppe.getImage());
                PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("chatPartnerImageString", bitmapString).apply();
                // startet die nächste Activity
                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return groupListe.size();
    }

}
