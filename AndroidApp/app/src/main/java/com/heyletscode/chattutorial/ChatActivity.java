package com.heyletscode.chattutorial;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.databinding.DataBindingUtil;

import com.heyletscode.chattutorial.databinding.VideoCallBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;
//
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import okhttp3.WebSocket;
//import okhttp3.WebSocketListener;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity implements TextWatcher {

    private VideoCallBinding binding;

    private static final String TAG = "ChatActivity";
    private String name;
    private Socket socket;
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Date currentTime;
//    private SimpleDateFormat simpleDateFormat;


    private EditText messageEdit;

    private View sendBtn;
    private ImageView pickImgBtn;
    private FrameLayout cameraPreview;
    private ImageView pickMicBtn;
    private ImageView pickCameraBtn;
    private Button pickVideoBtn;


    private RecyclerView recyclerView;
    private int IMAGE_REQUEST_ID = 1;
    private MessageAdapter messageAdapter;

    //initiate client socket connection
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//
//        name = getIntent().getStringExtra("name");
//        initiateSocketConnection();
//
//    }

    private Socket mSocket;

//    {
//    ruiiying
//        socket = IO.socket(URI.create("http://172.20.10.10:3333"));
//        mSocket = IO.socket(URI.create("http://172.20.10.10:3333/chat"));
//    }

    //    {
//    Glenn
//        socket = IO.socket(URI.create("http://192.168.1.239:3333"));
//        mSocket = IO.socket(URI.create("http://192.168.1.239:3333/chat"));
//    }




    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        name = getIntent().getStringExtra("name");
        {
            Log.d(TAG, "Before socket connection");
//            socket = IO.socket(URI.create("http://192.168.1.239:3333"));
//            mSocket = IO.socket(URI.create("http://192.168.1.239:3333/chat"));
            socket = IO.socket(URI.create("http://172.20.10.10:3333"));
            mSocket = IO.socket(URI.create("http://172.20.10.10:3333/chat"));
            mSocket.connect();

            if (mSocket.connected()){
                Log.d(TAG, "connected");
             } else {
            Log.d(TAG, "Not connected");
        }
            mSocket.emit("join_room","123");
            if (mSocket.connected()) {
                Log.d(TAG, "SOCKET IS CONNECTEd");
                Toast.makeText(ChatActivity.this, "Socket Connected",Toast.LENGTH_SHORT).show();
            }
            mSocket.on("receive_message",onNewMessage);
            mSocket.on("receive_image", onNewImage);
        };

        initializeView();

//        Toast.makeText(ChatActivity.this, "Before Connect Socket",Toast.LENGTH_SHORT).show();

//        System.out.println(socket.io() == mSocket.io());
//        Toast.makeText(ChatActivity.this, "After Connect Socket",Toast.LENGTH_SHORT).show();

//        runOnUiThread(() -> {
//                Toast.makeText(ChatActivity.this,
//                        "Socket Connection Successful!",
//                        Toast.LENGTH_SHORT).show();


//            });

//        mSocket.on("connection", new Emitter.Listener() {
//            @Override
//            public void call(final Object... args) {
//
//
//
//
//            }
//        });

//            mSocket.on("connection", new Emitter.Listener() {
//                @Override
//                public void call(final Object... args) {
//                    Toast.makeText(ChatActivity.this,
//                        "Socket Connection Successful!",
//                        Toast.LENGTH_SHORT).show();
////                    initializeView();
////                    mSocket.on("receive_message",onNewMessage);
//
//
//                }
//            });

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String string = s.toString().trim();

        if (string.isEmpty()) {
            resetMessageEdit();
        } else {

            sendBtn.setVisibility(View.VISIBLE);
            pickImgBtn.setVisibility(View.INVISIBLE);
            pickMicBtn.setVisibility(View.INVISIBLE);
            pickCameraBtn.setVisibility(View.INVISIBLE);
        }

    }

    private void resetMessageEdit() {

        messageEdit.removeTextChangedListener(this);

        messageEdit.setText("");
        sendBtn.setVisibility(View.INVISIBLE);
        pickImgBtn.setVisibility(View.VISIBLE);
        pickMicBtn.setVisibility(View.VISIBLE);
        pickCameraBtn.setVisibility(View.VISIBLE);

        messageEdit.addTextChangedListener(this);

    }

    //socket emitter
//    private class SocketListener extends WebSocketListener {

//        @Override
//        public void onOpen(WebSocket webSocket, Response response) {
//            super.onOpen(webSocket, response);
//
//            runOnUiThread(() -> {
//                Toast.makeText(ChatActivity.this,
//                        "Socket Connection Successful!",
//                        Toast.LENGTH_SHORT).show();
//
//                initializeView();
//            });
//
//        }

//    private void attemptSend() throws JSONException {
//
//        String message = messageEdit.getText().toString().trim();
//
//        if (TextUtils.isEmpty(message)) {
//            return;
//        }
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("username", "user");
//        jsonObject.put("message", message);
//        jsonObject.put("time", "10:00");
//        jsonObject.put("isSent", true);
//        messageAdapter.addItem(jsonObject);
//        messageEdit.setText("");
//        mSocket.emit("send_message",message);
//
//        messageAdapter.addItem(jsonObject);
//
//        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
//
//        resetMessageEdit();
//    }

    private Emitter.Listener onNewImage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: ONNEWIMAGELISTENER");
                    String s = args[0].toString();


                    JSONObject mobileObject = new JSONObject();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        String name  = jsonObject.getString("name");
                        String image = jsonObject.getString("image");
                        String time = jsonObject.getString("time");

                        Log.d(TAG, name);
                        Log.d(TAG, image);
                        Log.d(TAG, time);

                        mobileObject.put("name",name);
                        mobileObject.put("image", image);
                        mobileObject.put("time",time);
                        mobileObject.put("isSent", true);
                        messageAdapter.addItem(mobileObject);

                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    }

            });
        }
    };


    private Emitter.Listener onNewMessage = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String s = args[0].toString();

                        try {
                            JSONObject json = new JSONObject(s);
                            String username = json.getString("username");
                            String message = json.getString("message");
                            String time = json.getString("time");
                            Log.v(TAG,username);
                            Log.v(TAG,time);
                            Log.v(TAG,message);

                            JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("name", username);
                            jsonObject.put("message", message);
                            jsonObject.put("time", time);
                            jsonObject.put("isSent", false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                            messageAdapter.addItem(jsonObject);

                            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



//                        try{
//                            username = data.getString("username");
//                            message = data.getString("message");
//                            time = data.getString("time");
//
//                        }catch(JSONException e){
//                            return;
//                        }

//                        JSONObject jsonObject = new JSONObject();
//                        try {
//                            jsonObject.put("name", "Roy");
//                            jsonObject.put("message", message);
//                            jsonObject.put("isSent", false);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

//
                    }
                });
            }
        };


    

    private void initializeView() {

//        Toast.makeText(ChatActivity.this, "Inside InitializeView",Toast.LENGTH_SHORT).show();

        messageEdit = findViewById(R.id.messageEdit);
        sendBtn = findViewById(R.id.sendBtn);
        pickImgBtn = findViewById(R.id.pickImgBtn);

        cameraPreview = findViewById(R.id.camera_preview);
        pickCameraBtn = findViewById(R.id.cameraBtn);
        pickMicBtn = findViewById(R.id.micBtn);

        pickVideoBtn = findViewById(R.id.videoBtn);


        recyclerView = findViewById(R.id.recyclerView);

        messageAdapter = new MessageAdapter(getLayoutInflater());
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        messageEdit.addTextChangedListener(this);

        pickVideoBtn.setOnClickListener(v -> {
                    Log.d(TAG, "VIDEO SHOULD OPEN");
//                    Intent i = new Intent(getApplicationContext(),ChatActivity.class);
//                    startActivity(i);
                    Intent intent = new Intent(this, VideoActivity.class);
////                    intent.putExtra("name", editText.getText().toString());
                    startActivity(intent);
//
//                    binding = DataBindingUtil.setContentView(this, R.layout.video_call);
//                    setSupportActionBar(binding.toolbar);
//                    startActivity(new Intent(this, VideoActivity.class));

                }
                    );


        // ON CLICK EMITTER
        sendBtn.setOnClickListener(v -> {
            currentTime = Calendar.getInstance().getTime();
            String format = "KK:mm";
            DateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            String datetime = simpleDateFormat.format(currentTime);



            JSONObject jsonObject = new JSONObject();
            JSONObject mobile = new JSONObject();
            try {


//                String message = messageEdit.getText().toString().trim();

//                if (TextUtils.isEmpty(message)) {
//                    return;
//                }
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("time",currentTime );
                jsonObject.put("username", name);
                jsonObject.put("message", messageEdit.getText().toString());
                jsonObject.put("time",datetime);
                jsonObject.put("isSent", true);

                mobile.put("name", name);
                mobile.put("message",messageEdit.getText().toString());
                mobile.put("time",datetime);
//                mobile.put("time",currentTime );
                mobile.put("isSent", true);

                messageAdapter.addItem(mobile);
                messageEdit.setText("");
                mSocket.emit("send_message",jsonObject.toString());

//                messageAdapter.addItem(jsonObject);

                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                resetMessageEdit();


//                jsonObject.put("name", name);
//                jsonObject.put("message", messageEdit.getText().toString());
//
//                webSocket.send(jsonObject.toString());
//
//                jsonObject.put("isSent", true);
//                messageAdapter.addItem(jsonObject);
//
//                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
//
//                resetMessageEdit();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        // NOT
        pickImgBtn.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

            startActivityForResult(Intent.createChooser(intent, "Pick image"),
                    IMAGE_REQUEST_ID);

        });

    }


    // NOT
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_ID && resultCode == RESULT_OK) {

            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                Bitmap image = BitmapFactory.decodeStream(is);

                sendImage(image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    // NOT
    private void sendImage(Bitmap image) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

        String base64String = Base64.encodeToString(outputStream.toByteArray(),
                Base64.DEFAULT);

        currentTime = Calendar.getInstance().getTime();
        String format = "KK:mm";
        DateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String datetime = simpleDateFormat.format(currentTime);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", name);
            jsonObject.put("image", base64String);
            jsonObject.put("time", datetime);

            mSocket.emit("send_image",jsonObject.toString());
            jsonObject.put("isSent", true);

            messageAdapter.addItem(jsonObject);

            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
