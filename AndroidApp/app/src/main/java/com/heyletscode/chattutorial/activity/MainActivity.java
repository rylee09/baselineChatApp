package com.heyletscode.chattutorial.activity;

import static org.webrtc.SessionDescription.Type.ANSWER;
import static org.webrtc.SessionDescription.Type.OFFER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ListView;

import com.heyletscode.chattutorial.classes.Friend;
import com.heyletscode.chattutorial.R;
import com.heyletscode.chattutorial.adapter.FriendListAdapter;
import com.heyletscode.chattutorial.socket.SocketManager;
import com.heyletscode.chattutorial.util.HttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.*;


import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    private PeerConnectionFactory factory;

    private PeerConnection peerConnection;

    private DataChannel dataChannel1;

    private SessionDescription remoteSDP;

    private SessionDescription localSDP;



    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.friend_list);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED)
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 10);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
//                != PackageManager.PERMISSION_GRANTED)
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 10);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        ip = intent.getStringExtra("ip");
        port = intent.getStringExtra("port");
        protocol = intent.getStringExtra("protocol");
        friendList = intent.getStringExtra("friendList");
//        Log.d(TAG, friendList);

        String baseUrl = protocol + "://" + ip + ":" + port;

        httpClient = HttpClient.getInstance(baseUrl);

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED)
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);


        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }



        SocketManager.getInstance().connect(protocol,ip,port);
            mSocket = SocketManager.getInstance().getSocket();


//        PeerConnectionFactory.InitializationOptions initializationOptions = PeerConnectionFactory.InitializationOptions.builder(getApplicationContext()).createInitializationOptions();
//        PeerConnectionFactory.initialize(initializationOptions);
//        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
//
//        PeerConnectionFactory peerConnectionFactory = PeerConnectionFactory.builder().setOptions(options).createPeerConnectionFactory();
//        List<PeerConnection.IceServer> iceServers = new ArrayList<>();
////        iceServers.add(PeerConnection.IceServer.builder("stun:stun2.1.google.com:19302").createIceServer());
//        PeerConnection.RTCConfiguration rtcConfig =  new PeerConnection.RTCConfiguration(iceServers);
//
//        peerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, new PeerConnection.Observer() {
//            @Override
//            public void onSignalingChange(PeerConnection.SignalingState signalingState) {
//                Log.d(TAG, "onSignalingChange: " + signalingState);
//
//            }
//
//            @Override
//            public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
//                Log.d(TAG, "onIceConnectionChange: " + iceConnectionState);
//            }
//
//            @Override
//            public void onIceConnectionReceivingChange(boolean b) {
//
//            }
//
//            @Override
//            public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
//
//            }
//
//            @Override
//            public void onIceCandidate(IceCandidate iceCandidate) {
//                Log.d(TAG, "onIceCandidate: " + iceCandidate);
//
//            }
//
//            @Override
//            public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
//
//            }
//
//            @Override
//            public void onAddStream(MediaStream mediaStream) {
//
//            }
//
//            @Override
//            public void onRemoveStream(MediaStream mediaStream) {
//
//            }
//
//            @Override
//            public void onDataChannel(DataChannel dataChannel) {
//                Log.d(TAG, "onDataChannel: " + dataChannel.label());
//                DataChannel.Init init = new DataChannel.Init();
//                init.id = 1;
//                init.ordered = true;
//                init.negotiated = false;
//
//
//                DataChannel.Init init1 = new DataChannel.Init();
//                DataChannel dataChannel1 = peerConnection.createDataChannel("myDataChannel", init);
//
//// Send a message through the data channel
//                String message = "Hello, sender!";
//                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
//                DataChannel.Buffer data = new DataChannel.Buffer(buffer, false);
//
//                Timer timer = new Timer();
//                timer.scheduleAtFixedRate(new TimerTask() {
//                    @Override
//                    public void run() {
//                        // This code will be executed every 5 seconds
//                        // Put your code here
//                        dataChannel1.send(data);
//                    }
//                }, 0, 5000);
//
//
//
//                dataChannel.registerObserver(new DataChannel.Observer() {
//
//                    @Override
//                    public void onBufferedAmountChange(long l) {
//
//                    }
//
//                    @Override
//                    public void onStateChange() {
//                        if (dataChannel.state() == DataChannel.State.OPEN) {
//                            // Data channel is open, you can send messages here
//
//                            Log.d(TAG, "onStateChange: channel open");
//
//
//
////
////                            dataChannel.registerObserver(new DataChannel.Observer() {
////                                @Override
////                                public void onBufferedAmountChange(long l) {
////
////                                }
////
////                                @Override
////                                public void onStateChange() {
////
////                                }
////
////                                @Override
////                                public void onMessage(DataChannel.Buffer buffer) {
////                                    Log.d(TAG, "onMessage: new message from data Channel");
////                                }
////                            });
//
//
//                        } else if (dataChannel.state() == DataChannel.State.CLOSED) {
//                            // Data channel is closed, you cannot send messages until it's reopened
//                            Log.d(TAG, "onStateChange: channel closed");
//
//                        } else {
//                            // Data channel is in some other state (e.g. connecting or closing)
//                            // You can handle this case however you like
//                            Log.d(TAG, "onStateChange: channel connecting");
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onMessage(DataChannel.Buffer buffer) {
//                        Log.d(TAG, "onMessage: new message from data Channel");
//                        ByteBuffer data = buffer.data;
//                        byte[] bytes = new byte[data.remaining()];
//                        data.get(bytes);
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
//                        Log.d(TAG, "onMessage: DATACHANNEL");
//
//                    }
//                });
//
//            }
//
//            @Override
//            public void onRenegotiationNeeded() {
//                Log.d(TAG, "onRenegotiationNeeded: ");
//
//            }
//
//            @Override
//            public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
//
//            }
//        });
//
//
//
//            mSocket.on("candidate",args -> {
//                JSONObject message = (JSONObject) args[0];
//
//
//                IceCandidate candidate = null;
//                try {
//                    candidate = new IceCandidate(message.getString("sdpMid"), message.getInt("sdpMLineIndex"), message.getString("candidate"));
//                    Log.d(TAG, "(SUCCESS) adding candidate to android client: " + String.valueOf(candidate));
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
//                peerConnection.addIceCandidate(candidate);
//            } );
//
//
//            mSocket.on("sdp", args -> {
//                JSONObject message = (JSONObject) args[0];
//                Log.d(TAG, String.valueOf(message));
//                try {
//                    String sdp = message.getString("sdp");
//                    String type = message.getString("type");
//
//
//                    if (type.equals("offer")) {
//                        // set sdp as remote description (OFFER)
//                        remoteSDP = new SessionDescription(OFFER,sdp);
//                        Log.d(TAG, String.valueOf(remoteSDP));
//
//                        peerConnection.setRemoteDescription(new SdpObserver() {
//                            @Override
//                            public void onCreateSuccess(SessionDescription sessionDescription) {
//
//                            }
//
//                            @Override
//                            public void onSetSuccess() {
//                                Log.d(TAG, "(SUCCESS)[SET_REMOTE_DESCRIPTION] Answer to Offer from Remote Initiated");
//                                // Get the updated SDP from the local peer connection
//
//
//                                peerConnection.createAnswer(new SdpObserver() {
//                                    @Override
//                                    public void onCreateSuccess(SessionDescription sessionDescription) {
//                                        Log.d(TAG, "(SUCCESS)[CREATEANSWER] Answer Created based on offer");
//
//
//
//                                        localSDP = new SessionDescription(ANSWER, sessionDescription.description);
//
//                                        peerConnection.setLocalDescription(new SdpObserver() {
//                                            @Override
//                                            public void onCreateSuccess(SessionDescription sessionDescription) {
//
//                                            }
//
//                                            @Override
//                                            public void onSetSuccess() {
//                                                Log.d(TAG, "(SUCCESS)[SET_LOCAL_DESCRIPTION] SDP from create answer API set as local's local description");
////
//                                                JSONObject message = new JSONObject();
//                                                try {
//                                                    message.put("type", "answer");
//                                                    message.put("sdp", sessionDescription.description);
//                                                    mSocket.emit("sdp", message);
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCreateFailure(String s) {
//                                                Log.d(TAG, "(FAILURE)[SET_LOCAL_DESCRIPTION]SDP from create answer API set as local's local description");
//
//                                            }
//
//                                            @Override
//                                            public void onSetFailure(String s) {
//                                                Log.d(TAG, "(FAILURE)[SET_LOCAL_DESCRIPTION]SDP from create answer API set as local's local description");
//
//                                            }
//                                        }, localSDP);
//                                    }
//
//                                    @Override
//                                    public void onSetSuccess() {
//
//
//
//                                    }
//
//                                    @Override
//                                    public void onCreateFailure(String s) {
//                                        Log.d(TAG, "(FAILURE)[CREATEANSWER] Answer Created based on offer");
//
//                                    }
//
//                                    @Override
//                                    public void onSetFailure(String s) {
//                                        Log.d(TAG, "(FAILURE)[CREATEANSWER] Answer Created based on offer");
//
//                                    }
//                                }, new MediaConstraints() );
//
//
//
//                            }
//
//                            @Override
//                            public void onCreateFailure(String s) {
//                                Log.d(TAG, "(FAILURE)[SET_REMOTE_DESCRIPTION] Answer to Offer from Remote Initiated");
//
//                            }
//
//                            @Override
//                            public void onSetFailure(String s) {
//                                Log.d(TAG, "(FAILURE)[SET_REMOTE_DESCRIPTION] Answer to Offer from Remote Initiated");
//
//                            }
//                        } , remoteSDP);
//
//
//                    } else {
//                        // set sdp as remote description (ANSWER)
//                        return;
//                    }
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                };
//
//
//            });










////




//            DataChannel.Init init = new DataChannel.Init();
//            init.id = 1;
//            init.ordered = true;
//            init.negotiated = true;
//            dataChannel = peerConnection.createDataChannel("imageChannel", init);


//
//
//            dataChannel.registerObserver(new DataChannel.Observer() {
//
//                @Override
//                public void onBufferedAmountChange(long l) {
//
//                }
//
//                @Override
//                public void onStateChange() {
//
//                }
//
//                @Override
//                public void onMessage(DataChannel.Buffer buffer) {
//                    ByteBuffer data = buffer.data;
//                    byte[] bytes = new byte[data.remaining()];
//                    data.get(bytes);
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
//                    Log.d(TAG, "onMessage: DATACHANNEL");
//
//                }
//            });

//            String test = "android test works";
//            ByteBuffer data = ByteBuffer.wrap(test.getBytes());
//            dataChannel.send(new DataChannel.Buffer(data, false));


//        createOffer();


//        peerConnection.createOffer(new SdpObserver() {
//            @Override
//            public void onCreateSuccess(SessionDescription sessionDescription) {
//                Log.d(TAG, "Created Offer to remote client");
//                peerConnection.setLocalDescription(new SdpObserver() {
//                    @Override
//                    public void onCreateSuccess(SessionDescription sessionDescription) {
//
//                    }
//
//                    @Override
//                    public void onSetSuccess() {
//
//                    }
//
//                    @Override
//                    public void onCreateFailure(String s) {
//
//                    }
//
//                    @Override
//                    public void onSetFailure(String s) {
//
//                    }
//                }, sessionDescription);
//                Log.d(TAG, "sdp from createOffer: " + sessionDescription );
//                Log.d(TAG, "Set sdp as local description");
//                JSONObject message = new JSONObject();
//                try {
//                    message.put("type", "offer");
//                    message.put("sdp", sessionDescription.description);
//                    Log.d(TAG, String.valueOf(message));
//                    mSocket.emit("sdp",message);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onSetSuccess() {
//
//            }
//
//            @Override
//            public void onCreateFailure(String s) {
//
//            }
//
//            @Override
//            public void onSetFailure(String s) {
//
//            }
//        } , new MediaConstraints());

//             for receiving sdp after offer creation, and receiving sdp after answer to offer
//            mSocket.on("sdp", args -> {
//                JSONObject message = (JSONObject) args[0];
//                Log.d(TAG, String.valueOf(message));
//                try {
//                    String sdp = message.getString("sdp");
//                    String type = message.getString("type");
//
//
//
//                    if (type.equals("offer")) {
//                        Log.d(TAG, "offer sent from web client");
//                        Log.d(TAG, String.valueOf(message));
//
//                        peerConnection.setRemoteDescription(new CustomSdpObserver(), new SessionDescription(OFFER,String.valueOf(message)));
//                        Log.d(TAG, "Offer sent from remote client set as remote description");
//                        peerConnection.createAnswer(new CustomSdpObserver() {
//                            @Override
//                            public void onCreateSuccess(SessionDescription sessionDescription) {
//
//                                peerConnection.setLocalDescription(new CustomSdpObserver(), sessionDescription);
//                                Log.d(TAG, "create answer sdp set as local description");
//                                Log.d(TAG, "Peer Connection State: " + peerConnection.signalingState());
//                                JSONObject message = new JSONObject();
//                                try {
//                                    message.put("type", "answer");
//                                    message.put("sdp", sessionDescription.description);
//                                    mSocket.emit("sdp", message);
//                                    Log.d(TAG, "create answer sdp sent to remote peer: " + message);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }, new MediaConstraints());
//                    } else if (type.equals("answer")) {
//                        peerConnection.setRemoteDescription(new CustomSdpObserver(),new SessionDescription(ANSWER,String.valueOf(message)));
//                        Log.d(TAG, "sdp answer from remote peer set as local peer remote");
//                    }
//
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
//            } );






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


//            mSocket.on("receive_message",onNewMessage);
//            mSocket.on("receive_image", onNewImage);
//            mSocket.on("receive_web_voice", onNewVoice);



    }


//
//    private void createOffer() {
//        MediaConstraints sdpMediaConstraints = new MediaConstraints();
//
////        sdpMediaConstraints.mandatory.add(
////                new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "false"));
////        sdpMediaConstraints.mandatory.add(
////                new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
//        peerConnection.createOffer(new CustomSdpObserver() {
//            @Override
//            public void onCreateSuccess(SessionDescription sessionDescription) {
//                Log.d(TAG, "Created Offer to remote client");
//                peerConnection.setLocalDescription(new CustomSdpObserver(), sessionDescription);
//                Log.d(TAG, "sdp from createOffer: " + sessionDescription );
//                Log.d(TAG, "Set sdp as local description");
//                JSONObject message = new JSONObject();
//                try {
//                    message.put("type", "offer");
//                    message.put("sdp", sessionDescription.description);
//                    Log.d(TAG, String.valueOf(message));
//                    mSocket.emit("sdp",message);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, sdpMediaConstraints);
//    }

//    private void initializePeerConnectionFactory() {
//        PeerConnectionFactory.initializeAndroidGlobals(this, true, true, true);
//        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
//        factory = new PeerConnectionFactory(options);
////        factory.setVideoHwAccelerationOptions(rootEglBase.getEglBaseContext(), rootEglBase.getEglBaseContext());
//    }
//
//    private void initializePeerConnections() {
//        peerConnection = createPeerConnection(factory);
//    }
//
//
//    private PeerConnection createPeerConnection(PeerConnectionFactory factory) {
//        ArrayList<PeerConnection.IceServer> iceServers = new ArrayList<>();
////        String URL = "stun:stun.l.google.com:19302";
////        iceServers.add(new PeerConnection.IceServer(URL));
//
//        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(iceServers);
//        MediaConstraints pcConstraints = new MediaConstraints();
//
//        PeerConnection.Observer pcObserver = new PeerConnection.Observer() {
//            @Override
//            public void onSignalingChange(PeerConnection.SignalingState signalingState) {
//                Log.d(TAG, "State: " + signalingState);
//            }
//
//
//
//            @Override
//            public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
//                Log.d(TAG, "onIceConnectionChange: " + iceConnectionState);
//            }
//
//            @Override
//            public void onIceConnectionReceivingChange(boolean b) {
//                Log.d(TAG, "onIceConnectionReceivingChange: ");
//            }
//
//            @Override
//            public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
//                Log.d(TAG, "onIceGatheringChange: " + iceGatheringState);
//            }
//
//            @Override
//            public void onIceCandidate(IceCandidate iceCandidate) {
//                Log.d(TAG, "onIceCandidate: " + iceCandidate);
//                JSONObject message = new JSONObject();
//
//
//                try {
//                    message.put("type", "candidate");
//                    message.put("sdpMLineIndex", iceCandidate.sdpMLineIndex);
//
//                    message.put("sdpMid", iceCandidate.sdpMid);
//                    message.put("candidate", iceCandidate.sdp);
//
//                    Log.d(TAG, "onIceCandidate: sending candidate " + message);
//                    mSocket.emit("candidate", message);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
//                Log.d(TAG, "onIceCandidatesRemoved: ");
//            }
//
//            @Override
//            public void onAddStream(MediaStream mediaStream) {
////                Log.d(TAG, "onAddStream: " + mediaStream.videoTracks.size());
////                VideoTrack remoteVideoTrack = mediaStream.videoTracks.get(0);
////                AudioTrack remoteAudioTrack = mediaStream.audioTracks.get(0);
////                remoteAudioTrack.setEnabled(true);
////                remoteVideoTrack.setEnabled(true);
////                remoteVideoTrack.addRenderer(new VideoRenderer(binding.surfaceView2));
//
//            }
//
//            @Override
//            public void onRemoveStream(MediaStream mediaStream) {
//                Log.d(TAG, "onRemoveStream: ");
//            }
//            @Override
//            public void onDataChannel(DataChannel dataChannel) {
//                Log.d(TAG, "onDataChannel: " + dataChannel);
//            }
//
//            @Override
//            public void onRenegotiationNeeded() {
//                Log.d(TAG, "onRenegotiationNeeded: ");
//            }
//
//
//        };
//
//
//        return factory.createPeerConnection(rtcConfig, pcConstraints, pcObserver);
//    }






    @Override
    protected void onDestroy() {
        super.onDestroy();

        SocketManager.getInstance().disconnect();
    }






}
