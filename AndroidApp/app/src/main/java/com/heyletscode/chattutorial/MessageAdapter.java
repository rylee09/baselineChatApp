package com.heyletscode.chattutorial;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

public class MessageAdapter extends RecyclerView.Adapter {

    private static final int TYPE_MESSAGE_SENT = 0;
    private static final int TYPE_MESSAGE_RECEIVED = 1;
    private static final int TYPE_IMAGE_SENT = 2;
    private static final int TYPE_IMAGE_RECEIVED = 3;
    private static final int TYPE_AUDIO_SENT = 4;
    private static final int TYPE_AUDIO_RECEIVED = 5;

    private MediaPlayer mPlayer = new MediaPlayer();

    private LayoutInflater inflater;
    private List<JSONObject> messages = new ArrayList<>();

    public MessageAdapter (LayoutInflater inflater) {
        this.inflater = inflater;
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {

        TextView messageTxt,nameTxt,timeTxt;

        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.sentNameTxt);
            messageTxt = itemView.findViewById(R.id.sentTxt);
            timeTxt = itemView.findViewById(R.id.sentDateTxt);

        }
    }

    private class SentImageHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameTxt, timeTxt;

        public SentImageHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            nameTxt = itemView.findViewById(R.id.sentNameTxt);
            timeTxt = itemView.findViewById(R.id.sentDateTxt);
        }
    }

    private class SentAudioHolder extends RecyclerView.ViewHolder {
        ImageView playBtn, pauseBtn;
        SeekBar seekBar;
        TextView nameTxt, timeTxt;

        public SentAudioHolder(@NonNull View itemView) {
            super(itemView);

            playBtn = itemView.findViewById(R.id.playBtn);
            pauseBtn = itemView.findViewById(R.id.pauseBtn);
            seekBar = itemView.findViewById(R.id.seekbar);

            nameTxt = itemView.findViewById(R.id.sentNameTxt);
            timeTxt = itemView.findViewById(R.id.sentDateTxt);


        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {

        TextView nameTxt, timeTxt, messageTxt;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.nameTxt);
            messageTxt = itemView.findViewById(R.id.receivedTxt);
            timeTxt = itemView.findViewById(R.id.dateTxt);
        }
    }

    private class ReceivedImageHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameTxt, timeTxt;

        public ReceivedImageHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.receiveImageView);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            timeTxt = itemView.findViewById(R.id.dateTxt);

        }
    }

    private class ReceivedAudioHolder extends RecyclerView.ViewHolder {

        ImageView playBtn, pauseBtn;
        SeekBar seekBar;
        TextView nameTxt, timeTxt;

        public ReceivedAudioHolder(@NonNull View itemView) {
            super(itemView);

            playBtn = itemView.findViewById(R.id.playBtn);
            pauseBtn = itemView.findViewById(R.id.pauseBtn);
            seekBar = itemView.findViewById(R.id.seekbar);

            nameTxt = itemView.findViewById(R.id.nameTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);


        }
    }

    @Override
    public int getItemViewType(int position) {

        JSONObject message = messages.get(position);

        try {
            if (message.getBoolean("isSent")) {

                if (message.has("message"))
                    return TYPE_MESSAGE_SENT;
                else if (message.has("image"))
                    return TYPE_IMAGE_SENT;
                else
                    return TYPE_AUDIO_SENT;

            } else {

                if (message.has("message"))
                    return TYPE_MESSAGE_RECEIVED;
                else if (message.has("image"))
                    return TYPE_IMAGE_RECEIVED;
                else
                    return TYPE_AUDIO_RECEIVED;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        switch (viewType) {
            case TYPE_MESSAGE_SENT:
                view = inflater.inflate(R.layout.item_sent_message, parent, false);
                return new SentMessageHolder(view);
            case TYPE_MESSAGE_RECEIVED:

                view = inflater.inflate(R.layout.item_received_message, parent, false);
                return new ReceivedMessageHolder(view);

            case TYPE_IMAGE_SENT:

                view = inflater.inflate(R.layout.item_sent_image, parent, false);
                return new SentImageHolder(view);

            case TYPE_IMAGE_RECEIVED:

                view = inflater.inflate(R.layout.item_received_photo, parent, false);
                return new ReceivedImageHolder(view);

            case TYPE_AUDIO_SENT:

                view = inflater.inflate(R.layout.item_sent_audio, parent, false);
                return new SentAudioHolder(view);

            case TYPE_AUDIO_RECEIVED:

                view = inflater.inflate(R.layout.item_received_audio, parent, false);
                return new ReceivedAudioHolder(view);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        JSONObject message = messages.get(position);

        try {
            if (message.getBoolean("isSent")) {

                if (message.has("message")) {

                    SentMessageHolder messageHolder = (SentMessageHolder) holder;
                    messageHolder.messageTxt.setText(message.getString("message"));
                    messageHolder.nameTxt.setText(message.getString("name"));
                    messageHolder.timeTxt.setText(message.getString("time"));


                } else if (message.has("image")) {

                    SentImageHolder imageHolder = (SentImageHolder) holder;
                    Bitmap bitmap = getBitmapFromString(message.getString("image"));

                    imageHolder.imageView.setImageBitmap(bitmap);
                    imageHolder.nameTxt.setText(message.getString("name"));
                    imageHolder.timeTxt.setText(message.getString("time"));

                } else {
                    SentAudioHolder audioHolder = (SentAudioHolder) holder;
                    audioHolder.playBtn.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                    audioHolder.nameTxt.setText(message.getString("name"));
                    audioHolder.timeTxt.setText(message.getString("time"));

                    wavClass wavObj = new wavClass(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());

                    audioHolder.playBtn.setOnClickListener(v -> {
                        try {
//                        System.out.println((String) message.getString("id"));
                            mPlayer = new MediaPlayer();
                            // testing
//                        mPlayer.setDataSource(wavObj.getPath("final_record.wav"));

                            mPlayer.setDataSource((String) message.get("audioPath"));
                            mPlayer.prepare();
                            mPlayer.start();
                            System.out.println("max duration of recording is: " + mPlayer.getDuration());

                            audioHolder.playBtn.setVisibility(View.INVISIBLE);
                            audioHolder.pauseBtn.setVisibility(View.VISIBLE);
                            audioHolder.seekBar.setVisibility(View.VISIBLE);

                            mPlayer.setOnCompletionListener(z -> {
                                audioHolder.playBtn.setVisibility(View.VISIBLE);
                                audioHolder.pauseBtn.setVisibility(View.INVISIBLE);
                                audioHolder.seekBar.setVisibility(View.INVISIBLE);
//                                audioHolder.seekBar.setProgress(0);
                                mPlayer.stop();
                                mPlayer.reset();



                            } );




                            audioHolder.pauseBtn.setOnClickListener(w -> {
                                mPlayer.pause();
                                audioHolder.playBtn.setVisibility(View.VISIBLE);
                                audioHolder.pauseBtn.setVisibility(View.INVISIBLE);
                            });


                            audioHolder.seekBar.setMax(mPlayer.getDuration());
                            audioHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                    if(b) {
                                        mPlayer.seekTo(i);
                                    }
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {

                                }
                            });



                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    int currentPosition = mPlayer.getCurrentPosition();
                                    audioHolder.seekBar.setProgress(currentPosition);
                                    handler.postDelayed(this,50);
                                }
                            }, 50);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });




                }

            } else {

                if (message.has("message")) {

                    ReceivedMessageHolder messageHolder = (ReceivedMessageHolder) holder;
                    messageHolder.nameTxt.setText(message.getString("name"));
                    messageHolder.messageTxt.setText(message.getString("message"));
                    messageHolder.timeTxt.setText(message.getString("time"));

                } else if (message.has("image")){

                    ReceivedImageHolder imageHolder = (ReceivedImageHolder) holder;

                    Bitmap bitmap = getBitmapFromString(message.getString("image"));
                    imageHolder.imageView.setImageBitmap(bitmap);
                    imageHolder.nameTxt.setText(message.getString("name"));
                    imageHolder.timeTxt.setText(message.getString("time"));

                } else {
                    ReceivedAudioHolder audioHolder = (ReceivedAudioHolder) holder;
                    audioHolder.playBtn.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                    audioHolder.nameTxt.setText(message.getString("name"));
                    audioHolder.timeTxt.setText(message.getString("time"));

//                    wavClass wavObj = new wavClass(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());

//                    System.out.println(wavObj.getPath("hello.wav"));


                    try {
                        System.out.println(message.getString("id"));
                        mPlayer = new MediaPlayer();
                        // testing
//                        mPlayer.setDataSource(wavObj.getPath("final_record.wav"));

                        mPlayer.setDataSource((String) message.get("audioPath"));
                        mPlayer.prepare();


                        mPlayer.setOnCompletionListener(v -> {
                            audioHolder.playBtn.setVisibility(View.VISIBLE);
                            audioHolder.pauseBtn.setVisibility(View.INVISIBLE);
                            audioHolder.seekBar.setProgress(View.VISIBLE);
                        } );



                        audioHolder.playBtn.setOnClickListener(v -> {
                            mPlayer.start();
                            System.out.println("max duration of recording is: " + mPlayer.getDuration());

                            audioHolder.playBtn.setVisibility(View.INVISIBLE);
                            audioHolder.pauseBtn.setVisibility(View.VISIBLE);
                        });

                        audioHolder.pauseBtn.setOnClickListener(v -> {
                            mPlayer.pause();
                            audioHolder.playBtn.setVisibility(View.VISIBLE);
                            audioHolder.pauseBtn.setVisibility(View.INVISIBLE);
                        });


                        audioHolder.seekBar.setMax(mPlayer.getDuration());
                        audioHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                if(b) {
                                    mPlayer.seekTo(i);
                                }
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int currentPosition = mPlayer.getCurrentPosition();
                                audioHolder.seekBar.setProgress(currentPosition);
                                handler.postDelayed(this,50);
                            }
                        }, 50);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }




                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addItem (JSONObject jsonObject) {
        messages.add(jsonObject);
        notifyDataSetChanged();
    }

}
