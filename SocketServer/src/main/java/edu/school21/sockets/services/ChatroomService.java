package edu.school21.sockets.services;

import edu.school21.sockets.models.Chatroom;

import java.util.List;

public interface ChatroomService {

    Chatroom createChatroom(String chatroomName);

    Chatroom getChatroomByName(String chatroomName);

    List<Chatroom> getAllChatrooms();
}
