package com.heyletscode.chattutorial.classes;

public class Friend {
    private final String friend;
    private final String roomId;
    private final String you;
    private final String baseUrl;


//    private final SocketConnection socketConnection;



    private String name;
    private int profilePicture;

    public Friend(String friend, String roomId, String you, String baseUrl) {
        this.friend = friend;
        this.roomId = roomId;
        this.you = you;
        this.baseUrl = baseUrl;

//        this.socketConnection = socketConnection;
//        this.profilePicture = profilePicture;
    }


    public String getFriend() {
        return friend;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getYou() {
        return you;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

//    public int getProfilePicture() {
//        return profilePicture;
//    }
}