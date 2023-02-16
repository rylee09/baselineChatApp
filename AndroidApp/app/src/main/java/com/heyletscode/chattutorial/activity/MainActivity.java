package com.heyletscode.chattutorial.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ListView;

import com.heyletscode.chattutorial.classes.Friend;
import com.heyletscode.chattutorial.R;
import com.heyletscode.chattutorial.adapter.FriendListAdapter;
import com.heyletscode.chattutorial.socket.SocketManager;
import com.heyletscode.chattutorial.util.HttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;


public class MainActivity extends AppCompatActivity {



    private static final String TAG = "MainActivity";
    private io.socket.client.Socket socket;

    private String username;

    private String ip;
    private String port;

    private String friendList;

    private String protocol;
    private Socket mSocket;

    private HttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.friend_list);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        ip = intent.getStringExtra("ip");
        port = intent.getStringExtra("port");
        protocol = intent.getStringExtra("protocol");
        friendList = intent.getStringExtra("friendList");
//        Log.d(TAG, friendList);

        String baseUrl = protocol + "://" + ip + ":" + port;

        httpClient = HttpClient.getInstance(baseUrl);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);


        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


            SocketManager.getInstance().connect(protocol,ip,port);
            mSocket = SocketManager.getInstance().getSocket();
        System.out.println("hi");



        List<JSONObject> jsonList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(friendList);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonList.add(jsonArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ArrayList<Friend> friends = new ArrayList<>();

        for (int i=0; i < jsonList.size(); i++) {
            try {
//                System.out.println(jsonList.get(i).get("room_id"));
//                System.out.println(jsonList.get(i).get("username"));
                String roomId = (String) jsonList.get(i).get("room_id");
                String friendName = (String) jsonList.get(i).get("username");
                mSocket.emit("join_room",roomId);
                friends.add(new Friend(friendName, roomId, username));


            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

//
//        friends.add(new Friend("Friend 1", "room 1", "control1", "3333", "http" , "192.168.13.23"));
//        friends.add(new Friend("Friend 2", "room 2", "control1", "3333", "http" , "192.168.13.23"));
//        friends.add(new Friend("Friend 3", "room 3", "control1", "3333", "http" , "192.168.13.23"));

        FriendListAdapter adapter = new FriendListAdapter(this, friends);

        ListView friendList = findViewById(R.id.friendList);
        friendList.setAdapter(adapter);




//            mSocket.emit("join_room","123");
//            if (mSocket.connected()) {
//                Log.d(TAG, "SOCKET IS CONNECTEd");
//                Toast.makeText(MainActivity.this, "Socket Connected",Toast.LENGTH_SHORT).show();
//            }
//            mSocket.on("receive_message",onNewMessage);
//            mSocket.on("receive_image", onNewImage);
//            mSocket.on("receive_web_voice", onNewVoice);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SocketManager.getInstance().disconnect();
    }
}
