package com.example.cliqv1;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {

    public ArrayList<Message> messageListe;
    public ChatActivity activity;

    public ChatAdapter(ChatActivity activity, List<Message> list){

        this.activity = activity;
        messageListe = (ArrayList) list;

    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout linearLayout;

        public TextView username;
        public TextView message;
        public TextView timeStamp;

        public ImageView userImage;

        public MessageViewHolder (View view){

            super(view);

            linearLayout = view.findViewById(R.id.linearLayout);

            username = view.findViewById(R.id.message_usernameTV);
            message = view.findViewById(R.id.message_messageTV);
            timeStamp = view.findViewById(R.id.message_timestampTV);

            userImage = view.findViewById(R.id.message_userImageIV);

        }

    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.activity.getLayoutInflater().inflate(R.layout.chat_message_item, parent, false);
        return new ChatAdapter.MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Message message = messageListe.get(position);

        ChatAdapter.MessageViewHolder messageHolder = (ChatAdapter.MessageViewHolder) holder;

        messageHolder.username.setText(message.getUsername());
        messageHolder.message.setText(message.getMessage());
        messageHolder.timeStamp.setText(message.getTimestamp());

        if(message.getUserImage() != null){

            messageHolder.userImage.setImageBitmap(Bitmap.createScaledBitmap(message.getUserImage(), 60, 60, false));

        }else{
            messageHolder.userImage.setImageResource(R.drawable.standard_user_image);
        }



    }

    @Override
    public int getItemCount() {
        return messageListe.size();
    }
}
