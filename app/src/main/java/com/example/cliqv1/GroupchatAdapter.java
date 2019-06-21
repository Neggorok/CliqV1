package com.example.cliqv1;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GroupchatAdapter extends RecyclerView.Adapter {

    public ArrayList<GroupMessage> groupMessageListe;
    public GroupChatActivity activity;

    // stellt den Constructor dar, um im späteren Verlauf der App einen GroupchatAdapter verwenden, bzw erzeugen zu können
    public GroupchatAdapter(GroupChatActivity activity, List<GroupMessage> list){

        this.activity = activity;
        groupMessageListe = (ArrayList) list;

    }

    public static class GroupMessageViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout linearLayout;

        public TextView username;
        public TextView message;
        public TextView timeStamp;

        public ImageView userImage;

        public GroupMessageViewHolder (View view){

            super(view);

            linearLayout = view.findViewById(R.id.linearLayout);

            username = view.findViewById(R.id.message_groupUsernameTV);
            message = view.findViewById(R.id.message_groupMessageTV);
            timeStamp = view.findViewById(R.id.message_groupTimestampTV);

            userImage = view.findViewById(R.id.message_groupUserImageIV);



        }
    }

    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // hier wird das Layout des groupchat_message_item aktiviert
        View view = this.activity.getLayoutInflater().inflate(R.layout.groupchat_message_item, parent, false);
        return new GroupchatAdapter.GroupMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // holt ein Messageobjekt aus der groupMessageListe
        final GroupMessage groupMessage = groupMessageListe.get(position);

        // der GroupMessageHolder ist dafür da , die einzelnen Elemente wie den username abzugreifen
        GroupchatAdapter.GroupMessageViewHolder groupMessageHolder = (GroupchatAdapter.GroupMessageViewHolder) holder;
        // und sie dann in diesem Abschnitt in die vorgesehenen Bereiche, einzubinden
        groupMessageHolder.username.setText(groupMessage.getUsername());
        groupMessageHolder.message.setText(groupMessage.getMessage());
        groupMessageHolder.timeStamp.setText(groupMessage.getTimestamp());
        // prüft ob das Userimage leer ist
        if(groupMessage.getUserImage() != null){
            // wenn es nicht leer ist wird das übergebene Userimage gespeichert und mit angezeigt
            groupMessageHolder.userImage.setImageBitmap(Bitmap.createScaledBitmap(groupMessage.getUserImage(), 60, 60, false));

        }else{
            // wenn es jedoch leer ist, wird ein Standartbild zugewiesen
            groupMessageHolder.userImage.setImageResource(R.drawable.standard_user_image);
        }



    }

    @Override
    public int getItemCount() {
        return groupMessageListe.size();
    }
}
