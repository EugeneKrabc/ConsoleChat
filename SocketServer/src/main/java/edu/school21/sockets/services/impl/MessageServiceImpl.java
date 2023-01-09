package edu.school21.sockets.services.impl;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.repositories.MessagesRepository;
import edu.school21.sockets.repositories.UsersRepository;
import edu.school21.sockets.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class MessageServiceImpl implements MessageService {

    private final MessagesRepository messagesRepository;

    private final UsersRepository usersRepository;

    @Autowired
    public MessageServiceImpl(MessagesRepository messagesRepository, UsersRepository usersRepository) {
        this.messagesRepository = messagesRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public void saveMessage(String message, String username, Long chatroomId) {
        usersRepository.findByUsername(username).ifPresent(
                sender -> messagesRepository.save(new Message(sender.getId(),
                                message, chatroomId, Timestamp.valueOf(LocalDateTime.now())))
        );
    }

    @Override
    public List<String> loadLastMessagesInRoom(Long roomId) {
        List<Message> messages = messagesRepository.loadMessagesFromRoom(roomId);
        return messages.stream()
                .map(msg -> String.format("%s: %s", usersRepository.findById(msg.getSenderId()).get().getUsername(), msg.getText()))
                .collect(Collectors.toList());
    }
}
