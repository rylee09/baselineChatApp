package com.heyletscode.chattutorial;

public class Friend {
    private final String friend;
    private final String roomId;
    private final String you;


//    private final SocketConnection socketConnection;



    private String name;
    private int profilePicture;

    public Friend(String friend, String roomId, String you) {
        this.friend = friend;
        this.roomId = roomId;
        this.you = you;

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



//    public int getProfilePicture() {
//        return profilePicture;
//    }
}