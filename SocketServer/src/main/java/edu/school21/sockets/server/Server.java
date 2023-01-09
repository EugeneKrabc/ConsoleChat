package edu.school21.sockets.server;

import edu.school21.sockets.services.ChatroomService;
import edu.school21.sockets.services.MessageService;
import edu.school21.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class Server {

    final UsersService usersService;
    final MessageService messageService;

    final ChatroomService chatroomService;

    @Autowired
    public Server(UsersService usersService, MessageService messageService, ChatroomService chatroomService) {
        this.usersService = usersService;
        this.messageService = messageService;
        this.chatroomService = chatroomService;
    }

    public void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Session(this, socket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
