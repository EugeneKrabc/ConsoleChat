package edu.school21.sockets.models;

public class Chatroom {
    private Long id;
    private String chatRoomName;


    public Chatroom() {
    }

    public Chatroom(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    @Override
    public String toString() {
        return "Chatroom{" +
                "id=" + id +
                ", chatRoomName='" + chatRoomName + '\'' +
                '}';
    }
}
