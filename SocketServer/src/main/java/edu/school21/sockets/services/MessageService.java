package edu.school21.sockets.services;


import edu.school21.sockets.models.Message;

import java.util.List;

public interface MessageService {
    void saveMessage(String message, String username, Long chatroomId);

    List<String> loadLastMessagesInRoom(Long roomId);

}
