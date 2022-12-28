package com.heyletscode.chattutorial;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
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

    private static final String TAG = "ChatActivity";
    private String name;
    private Socket socket;

//    private WebSocket webSocket;
//    private String SERVER_PATH = "ws://echo.websocket.org";
    private EditText messageEdit;
    private View sendBtn;
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
//        socket = IO.socket(URI.create("http://172.20.10.10:3333"));
//        mSocket = IO.socket(URI.create("http://172.20.10.10:3333/chat"));
//    }




    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        name = getIntent().getStringExtra("name");
        {

            socket = IO.socket(URI.create("https://192.168.1.2:3333"));
            mSocket = IO.socket(URI.create("https://192.168.1.2:3333/chat"));
            mSocket.connect();
            if (mSocket.connected()) {
                Toast.makeText(ChatActivity.this, "Socket Connected",Toast.LENGTH_SHORT).show();
            }
            mSocket.emit("join_room","123");
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

            mSocket.on(mSocket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    Toast.makeText(ChatActivity.this,
                        "Socket Connection Successful!",
                        Toast.LENGTH_SHORT).show();
//                    initializeView();
                    mSocket.on("receive_message",onNewMessage);


                }
            });








    }

//
//
//    // NOT USING
//    private void initiateSocketConnection() {
//
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url(SERVER_PATH).build();
//        webSocket = client.newWebSocket(request, new SocketListener());
//
//    }

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
//            pickImgBtn.setVisibility(View.INVISIBLE);
        }

    }

    private void resetMessageEdit() {

        messageEdit.removeTextChangedListener(this);

        messageEdit.setText("");
        sendBtn.setVisibility(View.INVISIBLE);
//        pickImgBtn.setVisibility(View.VISIBLE);

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





        // ADD MESSAGE TO RECYCLER
//        @Override
//        public void onMessage(WebSocket webSocket, String text) {
//            super.onMessage(webSocket, text);
//
//            runOnUiThread(() -> {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(text);
//                    jsonObject.put("isSent", false);
//
//                    messageAdapter.addItem(jsonObject);
//
//                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            });
//
//        }
//    }
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject data = (JSONObject) args[0];
//                        Log.v(TAG,ge);
                        String username;
                        String message;
                        String time;


                        try{
                            username = data.getString("username");
                            message = data.getString("message");
                            time = data.getString("time");

                        }catch(JSONException e){
                            return;
                        }

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("name", "Roy");
                            jsonObject.put("message", message);
                            jsonObject.put("isSent", false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        messageAdapter.addItem(data);
//
                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
//
                    }
                });
            }
        };


    private void initializeView() {

//        Toast.makeText(ChatActivity.this, "Inside InitializeView",Toast.LENGTH_SHORT).show();

        messageEdit = findViewById(R.id.messageEdit);
        sendBtn = findViewById(R.id.sendBtn);
//        pickImgBtn = findViewById(R.id.pickImgBtn);

        recyclerView = findViewById(R.id.recyclerView);

        messageAdapter = new MessageAdapter(getLayoutInflater());
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        messageEdit.addTextChangedListener(this);

        // ON CLICK EMITTER
        sendBtn.setOnClickListener(v -> {

            JSONObject jsonObject = new JSONObject();
            try {
//                String message = messageEdit.getText().toString().trim();

//                if (TextUtils.isEmpty(message)) {
//                    return;
//                }
//                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", name);
                jsonObject.put("message", messageEdit.getText().toString());
//                jsonObject.put("time", "10:00");
                jsonObject.put("isSent", true);
                messageAdapter.addItem(jsonObject);
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
//        pickImgBtn.setOnClickListener(v -> {
//
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("image/*");
//
//            startActivityForResult(Intent.createChooser(intent, "Pick image"),
//                    IMAGE_REQUEST_ID);
//
//        });

    }


    // NOT
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == IMAGE_REQUEST_ID && resultCode == RESULT_OK) {
//
//            try {
//                InputStream is = getContentResolver().openInputStream(data.getData());
//                Bitmap image = BitmapFactory.decodeStream(is);
//
//                sendImage(image);
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }

    // NOT
//    private void sendImage(Bitmap image) {
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
//
//        String base64String = Base64.encodeToString(outputStream.toByteArray(),
//                Base64.DEFAULT);
//
//        JSONObject jsonObject = new JSONObject();
//
//        try {
//            jsonObject.put("name", name);
//            jsonObject.put("image", base64String);
//
//            webSocket.send(jsonObject.toString());
//
//            jsonObject.put("isSent", true);
//
//            messageAdapter.addItem(jsonObject);
//
//            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
}
