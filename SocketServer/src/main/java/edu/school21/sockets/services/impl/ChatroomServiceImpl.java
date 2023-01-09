package edu.school21.sockets.services.impl;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.repositories.ChatroomRepository;
import edu.school21.sockets.services.ChatroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ChatroomServiceImpl implements ChatroomService {

    private final ChatroomRepository chatroomRepository;

    @Autowired
    public ChatroomServiceImpl(ChatroomRepository chatroomRepository) {
        this.chatroomRepository = chatroomRepository;
    }

    @Override
    public Chatroom createChatroom(String chatroomName) {
        if (chatroomRepository.findByChatName(chatroomName).isPresent()) {
            return null;
        } else {
            Chatroom chatroom = new Chatroom(chatroomName);
            chatroomRepository.save(chatroom);

            return chatroom;
        }
    }

    @Override
    public Chatroom getChatroomByName(String chatroomName) {
        return chatroomRepository.findByChatName(chatroomName).orElse(null);
    }

    @Override
    public List<Chatroom> getAllChatrooms() {
        return chatroomRepository.findAll();
    }
}
