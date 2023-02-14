package com.heyletscode.chattutorial;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MainActivity extends AppCompatActivity {



    private static final String TAG = "MainActivity";
    private io.socket.client.Socket socket;

    private String username;

    private String ip;
    private String port;

    private String friendList;

    private String protocol;
    private Socket mSocket;



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



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);


        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        {
            Log.d(TAG, "Before socket connection");
//            socket = IO.socket(URI.create("http://192.168.1.239:3333"));
//            mSocket = IO.socket(URI.create("http://192.168.1.239:3333/chat"));
//            mSocket = IO.socket(URI.create("http://192.168.6.138:3333/chat"));
//            mSocket = IO.socket(URI.create("http://192.168.6.127:3333/chat"));
            mSocket = IO.socket(URI.create(protocol + "://" + ip + ":" + port + "/chat"));
//            mSocket = IO.socket(URI.create("http://172.20.10.2:3333/chat"));

            mSocket.connect();



            if (mSocket.connected()){
                Log.d(TAG, "connected");
            } else {
                Log.d(TAG, "Not connected");
            }
            mSocket.emit("join_room","123");
            if (mSocket.connected()) {
                Log.d(TAG, "SOCKET IS CONNECTEd");
                Toast.makeText(MainActivity.this, "Socket Connected",Toast.LENGTH_SHORT).show();
            }
//            mSocket.on("receive_message",onNewMessage);
//            mSocket.on("receive_image", onNewImage);
//            mSocket.on("receive_web_voice", onNewVoice);

        }

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
                System.out.println(jsonList.get(i).get("room_id"));
                System.out.println(jsonList.get(i).get("username"));
                String roomId = (String) jsonList.get(i).get("room_id");
                String name = (String) jsonList.get(i).get("username");
                friends.add(new Friend(name, roomId, username, port, protocol , ip));


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



    }





}
