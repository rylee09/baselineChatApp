package com.heyletscode.chattutorial;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {

    private static final int TYPE_MESSAGE_SENT = 0;
    private static final int TYPE_MESSAGE_RECEIVED = 1;
    private static final int TYPE_IMAGE_SENT = 2;
    private static final int TYPE_IMAGE_RECEIVED = 3;
    private static final int TYPE_AUDIO_SENT = 4;
    private static final int TYPE_AUDIO_RECEIVED = 5;

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
        ImageView audioView;
        TextView nameTxt, timeTxt;

        public SentAudioHolder(@NonNull View itemView) {
            super(itemView);

            audioView = itemView.findViewById(R.id.playBtn);
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

            imageView = itemView.findViewById(R.id.imageView);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            timeTxt = itemView.findViewById(R.id.dateTxt);

        }
    }

    private class ReceivedAudioHolder extends RecyclerView.ViewHolder {

        ImageView audioView;
        TextView nameTxt, timeTxt;

        public ReceivedAudioHolder(@NonNull View itemView) {
            super(itemView);

            audioView = itemView.findViewById(R.id.playBtn);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            timeTxt = itemView.findViewById(R.id.dateTxt);


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
                    audioHolder.audioView.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    audioHolder.nameTxt.setText(message.getString("name"));
                    audioHolder.timeTxt.setText(message.getString("time"));
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
                    audioHolder.audioView.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    audioHolder.nameTxt.setText(message.getString("name"));
                    audioHolder.timeTxt.setText(message.getString("time"));
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
