package com.heyletscode.chattutorial.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.heyletscode.chattutorial.activity.ChatActivity;
import com.heyletscode.chattutorial.classes.Friend;
import com.heyletscode.chattutorial.R;

import java.util.ArrayList;

public class FriendListAdapter extends ArrayAdapter<Friend> {

    public FriendListAdapter(Context context, ArrayList<Friend> friends) {
        super(context, 0, friends);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_item, parent, false);
        }

        Friend friend = getItem(position);

        ImageView profilePicture = convertView.findViewById(R.id.profilePicture);
//        profilePicture.setImageResource(friend.getProfilePicture());

        String other = friend.getFriend();
        String currentRoomId = friend.getRoomId();
        String you = friend.getYou();
//        SocketConnection socketConnection = friend.getSocket();



        TextView name = convertView.findViewById(R.id.name);
        name.setText(friend.getFriend());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChatActivity.class);

                intent.putExtra("other", other);
                intent.putExtra("roomId", currentRoomId);
                intent.putExtra("you", you);

//                intent.putExtra("socketConnection", socketConnection);
//                intent.putExtra("friendName", friend.getName());
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}