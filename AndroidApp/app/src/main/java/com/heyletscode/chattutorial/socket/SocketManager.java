package com.heyletscode.chattutorial.socket;

import android.net.TrafficStats;

import java.net.URI;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketManager {
    private static SocketManager instance = null;
    private Socket socket;

    private SocketManager() {
        // Private constructor to prevent instantiation from outside the class
    }

    public static SocketManager getInstance() {
        if (instance == null) {
            instance = new SocketManager();
        }
        return instance;
    }

    public void connect(String protocol, String ip, String port) {
        TrafficStats.setThreadStatsTag(0xF00D); // set the tag to a unique value

        socket = IO.socket(URI.create(protocol + "://" + ip + ":" + port + "/chat"));
        socket.connect();
    }

    public void disconnect() {
        if (socket != null) {
            socket.disconnect();
            socket.close();
            TrafficStats.clearThreadStatsTag();
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
