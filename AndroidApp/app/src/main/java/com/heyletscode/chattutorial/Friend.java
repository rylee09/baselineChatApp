package com.heyletscode.chattutorial;

public class Friend {
    private final String roomId;
    private final String username;
    private final String port;
    private final String protocol;
    private final String ip;
    private String name;
    private int profilePicture;

    public Friend(String name, String roomId, String username, String port, String protocol, String ip) {
        this.name = name;
        this.roomId = roomId;
        this.username = username;
        this.port = port;
        this.protocol = protocol;
        this.ip =ip;
//        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getUsername() {
        return username;
    }

    public String getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getIp() {
        return ip;
    }

//    public int getProfilePicture() {
//        return profilePicture;
//    }
}