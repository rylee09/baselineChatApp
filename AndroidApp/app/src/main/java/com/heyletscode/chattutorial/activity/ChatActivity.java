package com.heyletscode.chattutorial.activity;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.Toast;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.DataChannel;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;


//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import okhttp3.WebSocket;
//import okhttp3.WebSocketListener;

//import io.socket.client.Socket;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.media.MediaPlayer;
import android.media.MediaRecorder;


import com.heyletscode.chattutorial.R;
import com.heyletscode.chattutorial.adapter.MessageAdapter;
import com.heyletscode.chattutorial.socket.SocketManager;
import com.heyletscode.chattutorial.classes.wavClass;
import com.heyletscode.chattutorial.util.HttpClient;

import java.io.IOException;
import java.util.Objects;
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

    private int size = -1;

    private int offset = 0;

    int CHUNK_SIZE = 64 * 1024;






//    private VideoCallBinding binding;

    private static final String TAG = "ChatActivity";
    private io.socket.client.Socket socket;
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Date currentTime;
    private Date currTime;
//    private SimpleDateFormat simpleDateFormat;


    private EditText messageEdit;

    private View sendBtn;
    //    private FrameLayout cameraPreview;
    private ImageView pickMicBtn, pickCloseMicBtn, pickMoreBtn, playBtn;
    private ImageView pickCameraBtn;
    private Button pickVideoBtn;
    private SeekBar seekBar;

    private Handler handler = new Handler();
    private Boolean isSeekbarTracking = false;


    private RecyclerView recyclerView;
    private int IMAGE_REQUEST_ID = 1;

    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private static final int REQUEST_CODE_VIDEO_CAPTURE = 3;

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

    private String other;
    private String you;

    private String roomId;
    private String baseUrl;
    private Socket mSocket;

    private int notificationId = 0;

    HttpClient httpClient = HttpClient.getInstance("https://example.com/api");

    private PeerConnectionFactory factory;

    private PeerConnection peerConnection;

    private DataChannel dataChannel;

    private SessionDescription remoteSDP;

    private SessionDescription localSDP;

    private String type;

    private String filename;

    private String sender;


    private byte[] imageBytes;

    private  ByteArrayOutputStream outputStream = null;

    private boolean requestSent = false;

    private String videoPath;

    private String id;


    private SurfaceHolder surfaceHolder;

    private Camera camera;

    private SurfaceView surfaceView;

    private ImageButton vidRecBtn;

    private ImageButton vidStopRecBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        other = intent.getStringExtra("other");
        you = intent.getStringExtra("you");
        roomId = intent.getStringExtra("roomId");
        baseUrl = intent.getStringExtra("baseUrl");

        setContentView(R.layout.activity_chat);




        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);

        cameraPermission();

        // get existing socket connection
        mSocket = SocketManager.getInstance().getSocket();
        mSocket.on("receive_message",onNewMessage);
        mSocket.on("image_buffer", onNewImageBuffer);







        // Perform a GET request
        String endpoint = "/getAllChats";
        httpClient.doGet(endpoint, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try{
                    JSONObject result = new JSONObject(response.body().string());
                    JSONArray respMsgArray = result.getJSONArray("resp_msg");
                    Log.d(TAG, String.valueOf(respMsgArray));

                    for (int i=0; i < respMsgArray.length(); i++) {
                        String dbRoomId = respMsgArray.getJSONObject(i).getString("room_id");
                        Log.d(TAG, "dbroomid: " + dbRoomId);

                        String[] dbUsername = respMsgArray.getJSONObject(i).getString("senders").split("!,@");
                        System.out.println("chatactivity:" + dbUsername.length);

                        String[] dbMessage = respMsgArray.getJSONObject(i).getString("messages").split("!,@");
                        String[] dbTimeStamp = respMsgArray.getJSONObject(i).getString("timestamps").split("!,@");


                        if(Objects.equals(roomId, dbRoomId)) {
                            Log.d(TAG, "put messages into this room");

                            for (int j=0; j < dbUsername.length; j++) {


                                Log.d(TAG, "inner: " + dbUsername[j]);
                                JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("name", dbUsername[j]);

                                if (dbMessage[j].contains("image:")){
                                    String fileName = dbMessage[j].replace("image:","");
                                    File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                                    File tempFile = new File(picturesDir,"imgDB");
                                    File imageFile = new File(tempFile,fileName);
                                    String imagePath = imageFile.getAbsolutePath();

                                    byte[] fileData = null;
                                    try {
                                        InputStream inputStream = new FileInputStream(new File(imagePath));
                                        fileData = new byte[inputStream.available()];
                                        inputStream.read(fileData);
                                        inputStream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    String base64String =null;
                                    if (fileData != null) {
                                        base64String = Base64.encodeToString(fileData, Base64.DEFAULT);
                                        // use the base64String as needed
                                    } else {
                                        // handle the case where fileData is null
                                    }

                                    jsonObject.put("image", base64String);
                                } else if (dbMessage[j].contains("audio-")) {
                                    wavClass wavObj = new wavClass(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
                                    String fileName = dbMessage[j].replace("audio-","");
                                    jsonObject.put("audio", wavObj.getPath(fileName));


                                } else if (dbMessage[j].contains("video=")){
                                    String fileName = dbMessage[j].replace("video=","");
                                    String newPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/" + fileName;
                                    jsonObject.put("video", newPath);


                                } else {
                                    jsonObject.put("message", dbMessage[j]);
                                }

                                jsonObject.put("time", dbTimeStamp[j]);

                                if(Objects.equals(dbUsername[j], you)) {
                                    jsonObject.put("isSent", true);
                                } else {
                                    jsonObject.put("isSent", false);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        // Your UI update code here
                                        messageAdapter.addItem(jsonObject);

                                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                                    }
                                });


                            }
//
                            break;
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
//                Log.d(TAG, response.body().string());
            }
        });





//            mSocket.on("receive_image", onNewImage);
//            mSocket.on("receive_web_voice", onNewVoice);




        Log.d(TAG, "current room id is : " + roomId);



        bufferSize = AudioRecord.getMinBufferSize
                (RECORDER_SAMPLERATE,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING)*3;

        audioData = new short [bufferSize]; //short array that pcm data is put into.

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);


        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
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
            pickMoreBtn.setVisibility(View.INVISIBLE);
            pickMicBtn.setVisibility(View.INVISIBLE);
//            pickCameraBtn.setVisibility(View.INVISIBLE);
        }

    }

    private void resetMessageEdit() {

        messageEdit.removeTextChangedListener(this);

        messageEdit.setText("");
        sendBtn.setVisibility(View.INVISIBLE);
        pickMoreBtn.setVisibility(View.VISIBLE);
        pickMicBtn.setVisibility(View.VISIBLE);
//        pickCameraBtn.setVisibility(View.VISIBLE);

        messageEdit.addTextChangedListener(this);

    }


    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String s = args[0].toString();
                    String msgtoRoom = args[1].toString();

                    Log.d(TAG, "onReceiveMsg: " + args[1].toString());
                    Log.d(TAG, "msgtoRoom: " + msgtoRoom);
                    Log.d(TAG, "roomID: " + roomId);



                    if(Objects.equals(msgtoRoom, roomId)) {
                        Log.d(TAG, "onReceiveMsg MSG sent to: " + msgtoRoom);
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
                    } else {
                        Log.d(TAG, "onReceiveMsg MSG not sent to: " + msgtoRoom);
                        Log.d(TAG, "msg notification pop up");
                        try {
                            JSONObject json = new JSONObject(s);
                            String username = json.getString("username");
                            String message = json.getString("message");
                            String time = json.getString("time");



                            createNotification(message,username,roomId);






                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
            });
        }
    };



    private Emitter.Listener onNewImageBuffer= new Emitter.Listener() {
        @Override
        public void call(Object... args) {



            String s = args[0].toString();
            String chatRoomID = args[1].toString();

            if (Objects.equals(outputStream, null)) {
                // Create a new ByteArrayOutputStream for each image
                outputStream = new ByteArrayOutputStream();
            }


            if (Objects.equals(size,-1)) {
                try {
                    JSONObject json = new JSONObject(s);

                    Log.d(TAG, "init size and type configuration");
                    size = json.getInt("size");
                    type = json.getString("type");
                    filename = json.getString("name");
                    sender = json.getString("sender");
                    Log.d(TAG, "size: " + size);
                    Log.d(TAG, "type: " + type);
                    Log.d(TAG, "size: " + filename);
                    Log.d(TAG, "type: " + sender);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

                byte[] bytes = (byte[]) args[0];
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
//                Log.d(TAG, "call: bytes: " + Arrays.toString(bytes));
//                Log.d(TAG, "call: receiving buffer: " + buffer);

                Log.d(TAG, "writing new buffer");
                outputStream.write(buffer.array(),buffer.position(),buffer.remaining());
//                Log.d(TAG, "size: " + outputStream.size() );
                offset += CHUNK_SIZE;

                Log.d(TAG, "offset:size " + offset + "-" + size );

                if (offset >= size) {

                    Log.d(TAG, "end of writing of new received image buffer");
                    imageBytes = outputStream.toByteArray();
//                    Log.d(TAG, "final length: " + imageBytes.length);
//                    Log.d(TAG, "bytearraystring" + imageBytes.toString());
//                    Log.d(TAG, "offset" + offset);
//                    Log.d(TAG, "size" + size);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    saveImageToGallery(getApplicationContext(), bitmap, filename);
                    String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);


                    Date date = new Date();
                    DateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    String datetime = simpleDateFormat.format(date);

                    JSONObject jsonObject = new JSONObject();

                    try {
                    jsonObject.put("name", you);
                    jsonObject.put("image", base64Image);
                    jsonObject.put("time", datetime);
                    jsonObject.put("isSent", false);

                    } catch (JSONException e) {
                    e.printStackTrace();
                    }


                    if (!requestSent) {
                        JSONObject json = new JSONObject();
                        try {
                            json.put("roomId",chatRoomID);
                            json.put("msg","image:" + filename);
                            json.put("timestamp",datetime);
                            json.put("sender",sender);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }




                        RequestBody body = RequestBody.create(String.valueOf(json), MediaType.parse("application/json; charset=utf-8"));
                        httpClient.doPost("/checkExistence", body, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d(TAG, "failed to save message");                    }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.d(TAG, "successfully saved message");
                            }
                        });

                        requestSent =true;
                    }


                    runOnUiThread(new Runnable() {
                        public void run() {
                            // Update the UI here
                            messageAdapter.addItem(jsonObject);
                            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }



                        }
                    });

                    outputStream.reset();
                    offset = 0;
                    size = -1;
                }
//
            }
        }



    };



    private void createNotification(String message, String username, String roomId) {
        // Create a notification channel (for devices running Android 8.0 or higher)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(roomId, "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create a RemoteViews object for the custom layout of the notification
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
        contentView.setTextViewText(R.id.notification_title, username);
        contentView.setTextViewText(R.id.notification_text, message);


        // Create a notification builder with the channel ID and set the custom layout
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, roomId)
                .setSmallIcon(R.drawable.ic_baseline_photo_camera_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContent(contentView);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationId = notificationId ++; // unique integer value for the notification

        notificationManager.notify(notificationId, builder.build());
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
                        mobileObject.put("isSent", false);
                        messageAdapter.addItem(mobileObject);

                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                    } catch (JSONException e){
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
                        base64 = json.getString("audio");
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
        pickMoreBtn = findViewById(R.id.pickMoreBtn);

//        cameraPreview = findViewById(R.id.camera_preview);
//        pickCameraBtn = findViewById(R.id.cameraBtn);
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


        pickMoreBtn.setOnClickListener(v -> {
            Log.d(TAG, "clicked on popupmenu");
            PopupMenu popup = new PopupMenu(ChatActivity.this,v );
            popup.getMenuInflater().inflate(R.menu.options_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.takePicture:
                        dispatchTakePictureIntent();
                        Log.d(TAG, "open take picture feature");
                        return true;
                    case R.id.takeVideo:
                        Log.d(TAG, "open take video feature");
                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                        long currentTime = System.currentTimeMillis();
                        SecureRandom secureRandom = new SecureRandom();
                        byte[] randomNumber = new byte[8];
                        secureRandom.nextBytes(randomNumber);
                        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
                        bb.putLong(currentTime);
                        bb.put(randomNumber);
                        UUID uniqueId = UUID.nameUUIDFromBytes(bb.array());
                        id = uniqueId.toString();

                        videoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/" + id + ".mp4";
                        Uri videoUri = Uri.fromFile(new File(videoPath));
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
//                        MediaStore.EXTRA_MEDIA_TITLE
                        startActivityForResult(intent, REQUEST_CODE_VIDEO_CAPTURE);







                        return true;
                    case R.id.gallery:
                        Log.d(TAG, "open gallery feature");

                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("image/*");

                        startActivityForResult(Intent.createChooser(galleryIntent, "Pick image"),
                                IMAGE_REQUEST_ID);

                        return true;
                    case R.id.cancel_option:
                        Log.d(TAG, "close popup");
                        popup.dismiss();
                        return true;
                    default:
                        return false;
                }
            });
            popup.show();

//            dispatchTakePictureIntent();
        });


        // ON CLICK EMITTER
        sendBtn.setOnClickListener(v -> {

            // current date and time
            Date date = new Date();
            DateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String datetime = simpleDateFormat.format(date);


            JSONObject jsonObject = new JSONObject();
            JSONObject mobile = new JSONObject();
            try {

                jsonObject.put("username",you );
                jsonObject.put("message", messageEdit.getText().toString());
                jsonObject.put("time",datetime);
                jsonObject.put("isSent", true);

                mobile.put("name", you);
                mobile.put("message",messageEdit.getText().toString());
                mobile.put("time",datetime);
                mobile.put("isSent", true);

                messageAdapter.addItem(mobile);

                mSocket.emit("send_message",jsonObject.toString(), roomId);


                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                JSONObject json = new JSONObject();
                json.put("roomId",roomId);
                json.put("msg",messageEdit.getText().toString());
                json.put("timestamp",datetime);
                json.put("sender",you);



                RequestBody body = RequestBody.create(String.valueOf(json), MediaType.parse("application/json; charset=utf-8"));
                httpClient.doPost("/checkExistence", body, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "failed to save message");                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d(TAG, "successfully saved message");
                    }
                });
                resetMessageEdit();
                messageEdit.setText("");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        // To reuse for selecting from gallery
//        pickMoreBtn.setOnClickListener(v -> {
//
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("image/*");
//
//            startActivityForResult(Intent.createChooser(intent, "Pick image"),
//                    IMAGE_REQUEST_ID);
//
//        });


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

                long currentTime = System.currentTimeMillis();
                SecureRandom secureRandom = new SecureRandom();
                byte[] randomNumber = new byte[8];
                secureRandom.nextBytes(randomNumber);
                ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
                bb.putLong(currentTime);
                bb.put(randomNumber);
                UUID uniqueId = UUID.nameUUIDFromBytes(bb.array());
                String id = uniqueId.toString();

                wavObj.stopRecording(id);
//
                pickMicBtn.setVisibility(View.VISIBLE);
                pickCloseMicBtn.setVisibility(View.INVISIBLE);


                sendAudio(wavObj,id);




                Toast.makeText(this, "Replaying Recording", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);


//            // initiate datachannel for receiver to receive image
//            DataChannel.Init init = new DataChannel.Init();
//            init.id = 1;
//            init.ordered = true;
//            init.negotiated = false;
////            DataChannel.Init init = new DataChannel.Init();
//            dataChannel = peerConnection.createDataChannel("imageChannel", init);
        }
    }


    private void cameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,WRITE_EXTERNAL_STORAGE},1);
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

                displayImage(image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: Captured Image");

            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap bitmap = (Bitmap) extras.get("data");
                if (bitmap != null) {
                    displayImage(bitmap);
                    Log.d(TAG, "before send bitmap");
                    sendImageToWeb(bitmap, mSocket);

                    // To save file name in database using API call for extraction.
                }
            }
        }

        if (requestCode == REQUEST_CODE_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            // Video captured successfully

            Log.d(TAG, "video captured successfully");
            try {
                sendVideo(videoPath, id);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

    private void sendImageToWeb(Bitmap bitmap, Socket socket) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        UUID uuid = UUID.randomUUID();

        // Get the string representation of the UUID
        String randomUUIDString = uuid.toString();

        // send type and size information
        JSONObject fileInfo = new JSONObject();
        try {

            fileInfo.put("type", "image/jpeg");
            fileInfo.put("size", imageBytes.length);
            fileInfo.put("name", "mobile-" + randomUUIDString + ".jpeg");
            fileInfo.put("sender", you);
            socket.emit("send_image_buffer", fileInfo.toString(), roomId);
            saveImageToGallery(this,bitmap,"mobile-" + randomUUIDString + ".jpeg");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // send image data in chunks of 16KB
        int offset = 0;
        int chunkSize = 64 * 1024;
        while (offset < imageBytes.length) {
            int remainingBytes = imageBytes.length - offset;
            int bytesToSend = Math.min(remainingBytes, chunkSize);
            byte[] chunk = Arrays.copyOfRange(imageBytes, offset, offset + bytesToSend);
            socket.emit("send_image_buffer",chunk, roomId) ;
            offset += bytesToSend;
        }
    }


    private void sendVideo(String videoFilePath, String id) throws IOException, JSONException {

        File file = new File(videoFilePath);

        URL url = new URL(baseUrl + "/video/uploads");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "video/mp4");
        connection.setRequestProperty("id", id);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            byte[] audioData = Files.readAllBytes(file.toPath());

            OutputStream os = connection.getOutputStream();
            os.write(audioData);
            os.flush();
            os.close();
        }

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            // current date and time
            Date date = new Date();
            DateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String datetime = simpleDateFormat.format(date);

            JSONObject json = new JSONObject();
            json.put("roomId",roomId);
            json.put("msg","video=" + id + ".mp4" );
            json.put("timestamp",datetime);
            json.put("sender",you);
            mSocket.emit("video_broadcast_to_room", json.toString());




            RequestBody body = RequestBody.create(String.valueOf(json), MediaType.parse("application/json; charset=utf-8"));
            httpClient.doPost("/checkExistence", body, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "failed to save message");                    }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "successfully saved message");
                }
            });
        }

//        System.out.println("Response Code: " + responseCode);


        try {
            Date date = new Date();
            DateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String datetime = simpleDateFormat.format(date);

            String newPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/" + id + ".mp4";

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", you);
            jsonObject.put("id",id);
            jsonObject.put("video", newPath );
            jsonObject.put("time", datetime);
//            jsonObject.put("path",wavObj.getPath(idWav +".wav"));
            jsonObject.put("isSent", true);
//            mSocket.emit("send_audio", jsonObject.toString());

            messageAdapter.addItem(jsonObject);

            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




    private void sendAudio(wavClass wavObj, String idWav) throws IOException, JSONException {

        File file = new File(wavObj.getPath(idWav + ".wav"));

        long currentTime = System.currentTimeMillis();
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomNumber = new byte[8];
        secureRandom.nextBytes(randomNumber);
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(currentTime);
        bb.put(randomNumber);
        UUID uniqueId = UUID.nameUUIDFromBytes(bb.array());
        String id = uniqueId.toString();

//        "http://172.20.10.2:3333/echo/uploads"
        URL url = new URL(baseUrl + "/echo/uploads");

//        URL url = new URL(protocol + "://" + ip + ":" + port + "/echo/uploads");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "audio/wav");
        connection.setRequestProperty("id", idWav);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            byte[] audioData = Files.readAllBytes(file.toPath());

            OutputStream os = connection.getOutputStream();
            os.write(audioData);
            os.flush();
            os.close();
        }

        int responseCode = connection.getResponseCode();


        if (responseCode == 200) {
            // current date and time
            Date date = new Date();
            DateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String datetime = simpleDateFormat.format(date);

            JSONObject json = new JSONObject();
            json.put("roomId",roomId);
            json.put("msg","audio-" + idWav + ".wav" );
            json.put("timestamp",datetime);
            json.put("sender",you);
            mSocket.emit("audio_broadcast_to_room", json.toString());



            RequestBody body = RequestBody.create(String.valueOf(json), MediaType.parse("application/json; charset=utf-8"));
            httpClient.doPost("/checkExistence", body, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "failed to save message");                    }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "successfully saved audio");
                }
            });
        }


        System.out.println("Response Code: " + responseCode);

        try {
            Date date = new Date();
            DateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String datetime = simpleDateFormat.format(date);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", you);
            jsonObject.put("id",id);
            jsonObject.put("audio", wavObj.getPath(idWav + ".wav"));
            jsonObject.put("time", datetime);
//            jsonObject.put("path",wavObj.getPath(idWav +".wav"));
            jsonObject.put("isSent", true);
//            mSocket.emit("send_audio", jsonObject.toString());

            messageAdapter.addItem(jsonObject);

            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }



    
    // NOT
    private void displayImage(Bitmap image) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

        String base64String = Base64.encodeToString(outputStream.toByteArray(),
                Base64.DEFAULT);


        // current date and time
        Date date = new Date();
        DateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String datetime = simpleDateFormat.format(date);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", you);
            jsonObject.put("image", base64String);
            jsonObject.put("time", datetime);
            jsonObject.put("isSent", true);

            messageAdapter.addItem(jsonObject);

            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void saveImageToGallery(Context context, Bitmap bitmap, String fileName) {
        // Get the directory for the user's public pictures directory.
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        // Create a sub-directory with the specified name
        File imageDir = new File(dir, "imgDB");
        if (!imageDir.exists()) {
            imageDir.mkdir();
        }

        // Create a file for the image in the sub-directory
        File imageFile = new File(imageDir, fileName);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Add the image to the gallery
        MediaScannerConnection.scanFile(context, new String[] { imageFile.getAbsolutePath() }, null, null);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void stopRecordingVideo() {
        mRecorder.stop();
        mRecorder.reset();
        mRecorder.release();

//        byte[] videoBytes = convertVideoToBytes(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/video.mp4");
//        Log.d(TAG, "bytes: " + videoBytes);
    }





    }






//    PeerConnectionFactory.InitializationOptions initializationOptions = PeerConnectionFactory.InitializationOptions.builder(getApplicationContext()).createInitializationOptions();
//        PeerConnectionFactory.initialize(initializationOptions);
//                PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
//
//                PeerConnectionFactory peerConnectionFactory = PeerConnectionFactory.builder().setOptions(options).createPeerConnectionFactory();
//                List<PeerConnection.IceServer> iceServers = new ArrayList<>();
////        iceServers.add(PeerConnection.IceServer.builder("stun:stun2.1.google.com:19302").createIceServer());
//        PeerConnection.RTCConfiguration rtcConfig =  new PeerConnection.RTCConfiguration(iceServers);
//
//        peerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, new PeerConnection.Observer() {
//@Override
//public void onSignalingChange(PeerConnection.SignalingState signalingState) {
//        Log.d(TAG, "onSignalingChange: " + signalingState);
//
//        }
//
//@Override
//public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
//        Log.d(TAG, "onIceConnectionChange: " + iceConnectionState);
//        }
//
//@Override
//public void onIceConnectionReceivingChange(boolean b) {
//
//        }
//
//@Override
//public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
//
//        }
//
//@Override
//public void onIceCandidate(IceCandidate iceCandidate) {
//        Log.d(TAG, "onIceCandidate: " + iceCandidate);
//
//        }
//
//@Override
//public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
//
//        }
//
//@Override
//public void onAddStream(MediaStream mediaStream) {
//
//        }
//
//@Override
//public void onRemoveStream(MediaStream mediaStream) {
//
//        }
//
//@Override
//public void onDataChannel(DataChannel dataChannel) {
//        Log.d(TAG, "onDataChannel: " + dataChannel.label());
//
//
//        dataChannel.registerObserver(new DataChannel.Observer() {
//
//@Override
//public void onBufferedAmountChange(long l) {
//
//        }
//
//@Override
//public void onStateChange() {
//        if (dataChannel.state() == DataChannel.State.OPEN) {
//        // Data channel is open, you can send messages here
//
//        Log.d(TAG, "onStateChange: channel open");
//
//
//
//        } else if (dataChannel.state() == DataChannel.State.CLOSED) {
//        // Data channel is closed, you cannot send messages until it's reopened
//        Log.d(TAG, "onStateChange: channel closed");
//
//        } else {
//        // Data channel is in some other state (e.g. connecting or closing)
//        // You can handle this case however you like
//        Log.d(TAG, "onStateChange: channel connecting");
//
//        }
//
//        }
//
//@Override
//public void onMessage(DataChannel.Buffer buffer) {
//
//
//        Log.d(TAG, "onMessage: new message from data Channel");
//
//
//        ByteBuffer data = buffer.data;
//        byte[] bytes = new byte[data.remaining()];
//        data.get(bytes);
//
//        if (Objects.equals(outputStream,null)) {
//        // Create a new ByteArrayOutputStream for each image
//        outputStream = new ByteArrayOutputStream();
//        }
//
//
//
//        String message = new String(bytes, Charset.forName("UTF-8"));
//
//        if (Objects.equals(size,-1)) {
//        try {
//        JSONObject json = new JSONObject(message);
//
//        Log.d(TAG, "init size and type configuration");
//        size = json.getInt("size");
//        type = json.getString("type");
//        Log.d(TAG, "size: " + size);
//        Log.d(TAG, "type: " + type);
//        } catch (JSONException e) {
//        e.printStackTrace();
//        }
//        } else {
//        try {
//        Log.d(TAG, "writing new buffer");
//        outputStream.write(bytes);
//        offset += CHUNK_SIZE;
//
//        } catch (IOException e) {
//        throw new RuntimeException(e);
//        }
//
//        if (offset >= size) {
//
//        Log.d(TAG, "end of writing of new received image buffer");
//        imageBytes = outputStream.toByteArray();
//        Log.d(TAG, "final length: " + imageBytes.length);
//        Log.d(TAG, "bytearraystring" + imageBytes.toString());
//        Log.d(TAG, "offset" + offset);
//        Log.d(TAG, "size" + size);
//
//        String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
////                                Log.d(TAG, "base64String: " + base64Image);
//
//
//        Date date = new Date();
//        DateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
//        String datetime = simpleDateFormat.format(date);
//
//        JSONObject jsonObject = new JSONObject();
//
//        try {
//        jsonObject.put("name", you);
//        jsonObject.put("image", base64Image);
//        jsonObject.put("time", datetime);
//        jsonObject.put("isSent", false);
////                                    mSocket.emit("send_image",jsonObject.toString());
//
//        } catch (JSONException e) {
//        e.printStackTrace();
//        }
//
//        runOnUiThread(new Runnable() {
//public void run() {
//        // Update the UI here
//        messageAdapter.addItem(jsonObject);
//        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
//        try {
//        outputStream.close();
//        } catch (IOException e) {
//        throw new RuntimeException(e);
//        }
//        outputStream.reset();
//        offset = 0;
//        size = -1;
//        }
//        });
//
//
//        }
//
//
//        }
//
//
//
//
//
//
////                        ByteBuffer data = buffer.data;
////                        byte[] bytes = new byte[data.remaining()];
////                        data.get(bytes);
////                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
////                        Log.d(TAG, "onMessage: DATACHANNEL");
//
//        }
//        });
//
//        }
//
//@Override
//public void onRenegotiationNeeded() {
//        Log.d(TAG, "onRenegotiationNeeded: ");
//
//        }
//
//@Override
//public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
//
//        }
//        });


// listen for new candidates from remote client
//        mSocket.on("candidate",args -> {
//                JSONObject message = (JSONObject) args[0];
//
//
//                IceCandidate candidate = null;
//                try {
//                candidate = new IceCandidate(message.getString("sdpMid"), message.getInt("sdpMLineIndex"), message.getString("candidate"));
//                Log.d(TAG, "(SUCCESS) adding candidate to android client: " + String.valueOf(candidate));
//                } catch (JSONException e) {
//                throw new RuntimeException(e);
//                }
//                peerConnection.addIceCandidate(candidate);
//                } );
//
//
//
//                // listen for offer and answer sdp
//                mSocket.on("sdp", args -> {
//                JSONObject message = (JSONObject) args[0];
//                Log.d(TAG, String.valueOf(message));
//                try {
//                String sdp = message.getString("sdp");
//                String type = message.getString("type");
//
//
//                if (type.equals("offer")) {
//                // set sdp as remote description (OFFER)
//                remoteSDP = new SessionDescription(OFFER,sdp);
//                Log.d(TAG, String.valueOf(remoteSDP));
//
//                peerConnection.setRemoteDescription(new SdpObserver() {
//@Override
//public void onCreateSuccess(SessionDescription sessionDescription) {
//
//        }
//
//@Override
//public void onSetSuccess() {
//        Log.d(TAG, "(SUCCESS)[SET_REMOTE_DESCRIPTION] Answer to Offer from Remote Initiated");
//        // Get the updated SDP from the local peer connection
//
//
//        peerConnection.createAnswer(new SdpObserver() {
//@Override
//public void onCreateSuccess(SessionDescription sessionDescription) {
//        Log.d(TAG, "(SUCCESS)[CREATEANSWER] Answer Created based on offer");
//
//
//
//        localSDP = new SessionDescription(ANSWER, sessionDescription.description);
//
//        peerConnection.setLocalDescription(new SdpObserver() {
//@Override
//public void onCreateSuccess(SessionDescription sessionDescription) {
//
//        }
//
//@Override
//public void onSetSuccess() {
//        Log.d(TAG, "(SUCCESS)[SET_LOCAL_DESCRIPTION] SDP from create answer API set as local's local description");
////
//        JSONObject message = new JSONObject();
//        try {
//        message.put("type", "answer");
//        message.put("sdp", sessionDescription.description);
//        mSocket.emit("sdp", message);
//        } catch (JSONException e) {
//        e.printStackTrace();
//        }
//        }
//
//@Override
//public void onCreateFailure(String s) {
//        Log.d(TAG, "(FAILURE)[SET_LOCAL_DESCRIPTION]SDP from create answer API set as local's local description");
//
//        }
//
//@Override
//public void onSetFailure(String s) {
//        Log.d(TAG, "(FAILURE)[SET_LOCAL_DESCRIPTION]SDP from create answer API set as local's local description");
//
//        }
//        }, localSDP);
//        }
//
//@Override
//public void onSetSuccess() {
//
//
//
//        }
//
//@Override
//public void onCreateFailure(String s) {
//        Log.d(TAG, "(FAILURE)[CREATEANSWER] Answer Created based on offer");
//
//        }
//
//@Override
//public void onSetFailure(String s) {
//        Log.d(TAG, "(FAILURE)[CREATEANSWER] Answer Created based on offer");
//
//        }
//        }, new MediaConstraints() );
//
//
//
//        }
//
//@Override
//public void onCreateFailure(String s) {
//        Log.d(TAG, "(FAILURE)[SET_REMOTE_DESCRIPTION] Answer to Offer from Remote Initiated");
//
//        }
//
//@Override
//public void onSetFailure(String s) {
//        Log.d(TAG, "(FAILURE)[SET_REMOTE_DESCRIPTION] Answer to Offer from Remote Initiated");
//
//        }
//        } , remoteSDP);
//
//        } else {
//        // set sdp as remote description (ANSWER), not implemented yet
//        return;
//        }
//
//
//
//        } catch (JSONException e) {
//        e.printStackTrace();
//        };
//
//
//        });
