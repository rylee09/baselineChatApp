package com.heyletscode.chattutorial;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;



import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Button;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.sql.SQLOutput;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;


//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import okhttp3.WebSocket;
//import okhttp3.WebSocketListener;

import de.tavendo.autobahn.ByteBufferOutputStream;
import io.socket.client.IO;
//import io.socket.client.Socket;
import io.socket.client.Socket;
import io.socket.client.Url;
import io.socket.emitter.Emitter;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;


import android.os.Environment;


import java.io.IOException;
import java.util.UUID;
//
//import static android.Manifest.permission.RECORD_AUDIO;
//import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ChatActivity extends AppCompatActivity implements TextWatcher {


    private static final int RECORDER_BPP = 16;
    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    short[] audioData;

    private AudioRecord recorder = null;
    private int bufferSize = 0;
    private Thread recordingThread = null;
    private boolean isRecording = false;
    int[] bufferData;
    int bytesRecorded;





//    private VideoCallBinding binding;

    private static final String TAG = "ChatActivity";
    private String name;
    private io.socket.client.Socket socket;
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Date currentTime;
    private Date currTime;
//    private SimpleDateFormat simpleDateFormat;


    private EditText messageEdit;

    private View sendBtn;
    private ImageView pickImgBtn;
//    private FrameLayout cameraPreview;
    private ImageView pickMicBtn, pickCloseMicBtn, playBtn;
    private ImageView pickCameraBtn;
    private Button pickVideoBtn;
    private SeekBar seekBar;

    private Handler handler = new Handler();
    private Boolean isSeekbarTracking = false;


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

        bufferSize = AudioRecord.getMinBufferSize
                (RECORDER_SAMPLERATE,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING)*3;

        audioData = new short [bufferSize]; //short array that pcm data is put into.

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        {
            Log.d(TAG, "Before socket connection");
//            socket = IO.socket(URI.create("http://192.168.1.239:3333"));
//            mSocket = IO.socket(URI.create("http://192.168.1.239:3333/chat"));
            mSocket = IO.socket(URI.create("http://172.20.10.4:3333/chat"));
//            mSocket = IO.socket(URI.create("http://192.168.6.127:3333/chat"));
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
            mSocket.on("receive_web_voice", onNewVoice);

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


    private Emitter.Listener onNewVoice = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    
                    
                    String s = args[0].toString();
                    Log.d(TAG, "Buffer data: " + s);

                    JSONObject json = null;
                    String base64 = null;
                    String name = null;
                    String time = null;
                    String id = null;
//                    String path = null;
                    try {
                        json = new JSONObject(s);
                        base64 = json.getString("audioB64Str");
                        name = json.getString("senderName");
                        time = json.getString("time");
                        id = json.getString("id");

                        System.out.println(base64);
                        System.out.println(name);
                        System.out.println(time);
                        System.out.println(id);
//                        String message = json.getString("message");
//                        String time = json.getString("time");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    
                    
                    wavClass wavObj = new wavClass(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
                    Log.d(TAG, wavObj.getPath(id + ".mp3"));

                    String[] parts = base64.split(",");
                    String base64Data = parts[1];

                    byte[] decodedAudioBuffer = Base64.decode(base64Data, Base64.DEFAULT);
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(wavObj.getPath(id + ".mp3"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.write(decodedAudioBuffer);
                        fos.close();

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("name", name);
                            jsonObject.put("audioPath",wavObj.getPath(id + ".mp3"));
                            jsonObject.put("time", time);
                            jsonObject.put("id",id);
                            jsonObject.put("isSent", false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        messageAdapter.addItem(jsonObject);

                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                    } catch (IOException e) {
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

        seekBar = findViewById(R.id.seekbar);
        playBtn = findViewById(R.id.playBtn);
        pickVideoBtn = findViewById(R.id.videoBtn);

//        playBtn = findViewById(R.id.playBtn);


        recyclerView = findViewById(R.id.recyclerView);

        messageAdapter = new MessageAdapter(getLayoutInflater());
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        messageEdit.addTextChangedListener(this);



        pickVideoBtn.setOnClickListener(v -> {
                    Log.d(TAG, "VIDEO SHOULD OPEN");
                    Intent intent = new Intent(this, VideoActivity.class);
                    startActivity(intent);

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

                jsonObject.put("username", name);
                jsonObject.put("message", messageEdit.getText().toString());
                jsonObject.put("time",datetime);
                jsonObject.put("isSent", true);

                mobile.put("name", name);
                mobile.put("message",messageEdit.getText().toString());
                mobile.put("time",datetime);
                mobile.put("isSent", true);

                messageAdapter.addItem(mobile);
                messageEdit.setText("");
                mSocket.emit("send_message",jsonObject.toString());


                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                resetMessageEdit();


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


        wavClass wavObj = new wavClass(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
        pickMicBtn.setOnClickListener(v -> {


            try{
                if(checkWritePermission()) {
                    wavObj.startRecording();
                }
                if(!checkWritePermission()){
                    requestWritePermission();
                }
                pickMicBtn.setVisibility(View.INVISIBLE);
                pickCloseMicBtn.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Started Recording", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }


        });

        pickCloseMicBtn.setOnClickListener(v -> {


            Toast.makeText(getApplicationContext(), "Recording Finished", Toast.LENGTH_SHORT).show();

            try{

                wavObj.stopRecording();
                mPlayer = new MediaPlayer();
                mPlayer.setDataSource(wavObj.getPath("final_record.wav"));
                mPlayer.prepare();
                mPlayer.start();
                pickMicBtn.setVisibility(View.VISIBLE);
                pickCloseMicBtn.setVisibility(View.INVISIBLE);
                sendAudio(wavObj);




                Toast.makeText(this, "Replaying Recording", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }



    private boolean checkWritePermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED ;
    }
    private void requestWritePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.MODIFY_AUDIO_SETTINGS,WRITE_EXTERNAL_STORAGE},1);
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

    private void sendAudio(wavClass wavObj) throws IOException {

        File file = new File(wavObj.getPath("final_record.wav"));

        long currentTime = System.currentTimeMillis();
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomNumber = new byte[8];
        secureRandom.nextBytes(randomNumber);
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(currentTime);
        bb.put(randomNumber);
        UUID uniqueId = UUID.nameUUIDFromBytes(bb.array());
        String id = uniqueId.toString();

        URL url = new URL("http://172.20.10.4:3333/echo/uploads");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "audio/wav");
        connection.setRequestProperty("id", id);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            byte[] audioData = Files.readAllBytes(file.toPath());

            OutputStream os = connection.getOutputStream();
            os.write(audioData);
            os.flush();
            os.close();
        }

        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        try {
            currTime = Calendar.getInstance().getTime();
            String format = "KK:mm";
            DateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            String datetime = simpleDateFormat.format(currTime);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("id",id);
            jsonObject.put("time", datetime);
            jsonObject.put("isSent", true);
            mSocket.emit("send_audio", jsonObject.toString());

            messageAdapter.addItem(jsonObject);

            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

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


}
