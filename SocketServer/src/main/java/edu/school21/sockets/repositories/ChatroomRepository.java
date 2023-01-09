package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Chatroom;

import java.util.List;
import java.util.Optional;

public interface ChatroomRepository extends CrudRepository<Chatroom> {
    Optional<Chatroom> findByChatName(String chatName);

    void save(Chatroom chatroom);

    List<Chatroom> findAll();
}
