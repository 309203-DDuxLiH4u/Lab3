package com.example.customadapterdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    private final Context context;
    private final List<User> userList;

    public UserAdapter(Context context, List<User> list) {
        super(context, 0, list);
        this.context = context;
        this.userList = list;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                    R.layout.list_item_layout, parent, false); // Inflate layout
        }

        User currentUser= userList.get(position);

        ImageView userIcon = listItemView.findViewById(R.id.user_icon);
        TextView nameText = listItemView.findViewById(R.id.name_text);
        TextView cityText = listItemView.findViewById(R.id.city_text);

        nameText.setText(currentUser.getName());
        cityText.setText("from " + currentUser.getCity());

        return listItemView;
    }
}