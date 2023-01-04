package com.heyletscode.chattutorial;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.FileUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;


import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
//
//import static android.Manifest.permission.RECORD_AUDIO;
//import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

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
//    private FrameLayout cameraPreview;
    private ImageView pickMicBtn, pickCloseMicBtn;
    private ImageView pickCameraBtn;
    private Button pickVideoBtn;


    private RecyclerView recyclerView;
    private int IMAGE_REQUEST_ID = 1;
    private MessageAdapter messageAdapter;


    //Intializing all variables..
//    private TextView stopTV, playTV, stopplayTV, statusTV;

//    private ImageView startTV;
//    private Button startTV, stopTV, playBackTV, stopPlayBackTV;

    //creating a variable for medi recorder object class.
    private MediaRecorder mRecorder = new MediaRecorder();

    //below method is used to initialize the media recorder clss
//    mRecorder = new MediaRecorder();
    // creating a variable for mediaplayer class
    private MediaPlayer mPlayer = new MediaPlayer();
    //string variable is created for storing a file name
    private static String mFileName = null;
    // constant for storing audio permission
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

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
            socket = IO.socket(URI.create("http://172.20.10.2:3333"));
            mSocket = IO.socket(URI.create("http://172.20.10.2:3333/chat"));
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
        }

        initializeView();

        if (isMicrophonePresent()) {
            getMicrophonePermission();
        }

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

                    }
                });
            }
        };


    

    @SuppressLint("ClickableViewAccessibility")
    private void initializeView() {

//        Toast.makeText(ChatActivity.this, "Inside InitializeView",Toast.LENGTH_SHORT).show();

        messageEdit = findViewById(R.id.messageEdit);
        sendBtn = findViewById(R.id.sendBtn);
        pickImgBtn = findViewById(R.id.pickImgBtn);

//        cameraPreview = findViewById(R.id.camera_preview);
        pickCameraBtn = findViewById(R.id.cameraBtn);
        pickMicBtn = findViewById(R.id.micBtn);
        pickCloseMicBtn = findViewById(R.id.closeMicBtn);

        pickVideoBtn = findViewById(R.id.videoBtn);

//        playBtn = findViewById(R.id.playBtn);


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



//        startTV = findViewById(R.id.recordBtn);
//        stopTV = findViewById(R.id.stopRecordBtn);
//        playBackTV = findViewById(R.id.playBackBtn);
//        stopPlayBackTV = findViewById(R.id.stopPlayBackBtn);



//        startTV.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                switch (event.getAction() & MotionEvent.ACTION_MASK) {
//                    case MotionEvent.ACTION_DOWN:
//                        v.setPressed(true);
//                        Toast.makeText(getApplicationContext(), "Button Pressed", Toast.LENGTH_SHORT).show();
//                        startRecording();
//
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_OUTSIDE:
//                    case MotionEvent.ACTION_CANCEL:
//                        v.setPressed(false);
//                        Toast.makeText(getApplicationContext(), "Button Released", Toast.LENGTH_SHORT).show();
//                        pauseRecording();
//                        break;
//                    case MotionEvent.ACTION_POINTER_DOWN:
//                    case MotionEvent.ACTION_MOVE:
//                    case MotionEvent.ACTION_POINTER_UP:
//                        break;
//                }
//                return true;
//            }
//        });

        pickMicBtn.setOnClickListener(v -> {

                //pause Recording method will pause the recording of audio.
//                startRecording();
            try{
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setOutputFile(getRecordingFilePath());
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.prepare();
                mRecorder.start();
                pickMicBtn.setVisibility(View.INVISIBLE);
                pickCloseMicBtn.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Started Recording", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }


        });

        pickCloseMicBtn.setOnClickListener(v -> {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            //pause Recording method will pause the recording of audio.
//            pauseRecording();
            Toast.makeText(getApplicationContext(), "Recording Finished", Toast.LENGTH_SHORT).show();
            try {
                sendAudio();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try{
                mPlayer = new MediaPlayer();
                mPlayer.setDataSource(getRecordingFilePath());
                mPlayer.prepare();
                mPlayer.start();
                pickMicBtn.setVisibility(View.VISIBLE);
                pickCloseMicBtn.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Replaying Recording", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

//        playBackTV.setOnClickListener(v -> {
//            try{
//                mPlayer = new MediaPlayer();
//                mPlayer.setDataSource(getRecordingFilePath());
//                mPlayer.prepare();
//                mPlayer.start();
//                Toast.makeText(this, "Replaying Recording", Toast.LENGTH_SHORT).show();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });

//        playBtn.setOnClickListener(v -> {
//            try{
//                mPlayer = new MediaPlayer();
//                mPlayer.setDataSource(getRecordingFilePath());
//                mPlayer.prepare();
//                mPlayer.start();
//                Toast.makeText(this, "Replaying Recording", Toast.LENGTH_SHORT).show();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });

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

    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[0xFFFF];
        for (int len = is.read(buffer); len != -1; len = is.read(buffer)) {
            os.write(buffer, 0, len);
        }

        return os.toByteArray();
    }

    private void sendAudio() throws IOException {
//        File file = new File(getRecordingFilePath());
//        byte[] myByteArray = new byte[];
        try {
            String path = getRecordingFilePath(); // Audio File path
            InputStream inputStream = new FileInputStream(path);
            byte[] myByteArray = getBytesFromInputStream(inputStream);


        currentTime = Calendar.getInstance().getTime();
        String format = "KK:mm";
        DateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String datetime = simpleDateFormat.format(currentTime);

        JSONObject jsonObject = new JSONObject();


            jsonObject.put("name", name);
            jsonObject.put("audio",myByteArray);
            jsonObject.put("time", datetime);
            jsonObject.put("path", getRecordingFilePath());
            mSocket.emit("send_audio",jsonObject.toString());
            jsonObject.put("isSent", true);

            messageAdapter.addItem(jsonObject);

            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);



            // ...
        } catch(IOException e) {
            // Handle error...
        } catch (JSONException e) {
            e.printStackTrace();
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



    private boolean isMicrophonePresent() {
        if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            return true;
        }
        else {
            return false;
        }
    }

    private void getMicrophonePermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, 200);
        }
    }



    private String getRecordingFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File file = new File(musicDirectory, "testRecordingFile" + ".wav");
        System.out.println("filepath: " + file);
        return file.getPath();
    }






}
