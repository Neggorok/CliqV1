package com.example.cliqv1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter implements Filterable
{

    public ArrayList<User> userListe;
    public ArrayList<User> Itemcopy;
    public UserListActivity activity;
    private ArrayList<User> mFilteredList;

    // stellt den Constructor dar, um im späteren Verlauf der App einen UserListAdapter verwenden, bzw erzeugen zu können
    public UserListAdapter(UserListActivity activity, List<User> list){

        this.activity = activity;
        userListe = (ArrayList) list;
        Itemcopy = this.userListe;

        /*
        this.Itemcopy = new ArrayList<>();
        Itemcopy.addAll(this.userListe);
        */
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                userListe = (ArrayList<User>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<User> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = Itemcopy;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected ArrayList<User> getFilteredResults(String constraint) {
        ArrayList<User> results = new ArrayList<>();

        for (User item : Itemcopy) {
            if (item.getName().toLowerCase().contains(constraint)) {
                results.add(item);
            }

        }
        return results;
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
        // hier wird das Layout des user_list_item aktiviert
        View view = this.activity.getLayoutInflater().inflate(R.layout.user_list_item, parent, false);
        return new UserListAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // holt ein Userobjekt aus der Userliste
        final User user = userListe.get(position);

        // der Userholder ist dafür da , die einzelnen Elemente wie username und das Image abzugreifen
        UserListAdapter.UserViewHolder userHolder = (UserViewHolder) holder;
        // und sie dann in diesem Abschnitt in die vorgesehenen Bereiche, einzubinden
        userHolder.name.setText(user.getName());
        userHolder.userImage.setImageBitmap(user.getImage());
        // prüft ob das Userimage leer ist
        if(user.getImage() != null){
            // wenn es nicht leer ist wird das übergebene Userimage gespeichert und mit angezeigt
            userHolder.userImage.setImageBitmap(Bitmap.createScaledBitmap(user.getImage(), 60, 60, false));

        }else{
            // wenn es jedoch leer ist, bekommt das Userlistitem das Standartbild zugewiesen
            Bitmap standardImageBitmap = Util.getBitmapFromDrawable(activity, R.drawable.standard_user_image);
            // setzt das Standartbild als neues Profilbild des Nutzers
            user.setImage(standardImageBitmap);

            userHolder.userImage.setImageBitmap(standardImageBitmap);
        }

        userHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startet beim anklicken des Items die nächste Activity, wo dann gechattet werden kann
                Intent i = new Intent(view.getContext(), ChatActivity.class);
                // übergibt der nächsten Activity die folgenden, beötigten Daten
                i.putExtra("chatPartnerUsername", userListe.get(position).getName());
                String bitmapString = Util.getBase64StringFromBitmap(user.getImage());
                PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("chatPartnerImageString", bitmapString).apply();
                // startet die nächste Activity
                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userListe.size();

    }

}
