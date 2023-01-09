package edu.school21.sockets.models;

import java.sql.Timestamp;
import java.util.Objects;

public class Message {

    private Long id;

    private Long senderId;

    private String text;

    private Long chatroomId;

    private Timestamp sendingTime;

    public Message() {
    }

    public Message(Long senderId, String text, Long chatroomId, Timestamp sendingTime) {
        this.senderId = senderId;
        this.text = text;
        this.chatroomId = chatroomId;
        this.sendingTime = sendingTime;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(Timestamp sendingTime) {
        this.sendingTime = sendingTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(Long chatroomId) {
        this.chatroomId = chatroomId;
    }

}
