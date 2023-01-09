package edu.school21.sockets.server;

import edu.school21.sockets.exception.AuthorizationException;
import edu.school21.sockets.models.Chatroom;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Session extends Thread {

    private final Server server;

    private String username;

    private final Socket socket;

    private static final List<Session> sessions = new ArrayList<>();

    private BufferedWriter out;

    private BufferedReader in;

    private boolean isAuthorized = false;

    private Chatroom selectedChatroom = null;

    private final String AUTHENTICATION_MENU =
            "1. signIn\n" +
            "2. signUp\n" +
            "3. Exit\n";

    private final String ROOM_MENU =
                "1. Create Room\n" +
                "2. Choose Room\n" +
                "3. Exit\n";

    public Session(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        Thread thread = new Thread(this);
        thread.start();
    }



    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write("Hello from Server!\n");
            out.flush();

            while (!isAuthorized) {
                out.write(AUTHENTICATION_MENU);
                out.flush();
                String userMsg = in.readLine();
                if (userMsg == null) {
                    continue;
                }
                switch (userMsg) {
                    case "1":
                        signIn();
                        break;
                    case "2":
                        signUp();
                        break;
                    case "3":
                        exit();
                        return;
                    default:
                        out.write("Invalid menu option\n");
                        out.flush();
                        break;
                }
            }
            while (selectedChatroom == null) {
                out.write(ROOM_MENU);
                out.flush();
                String userMsg = in.readLine();
                switch (userMsg) {
                    case "1":
                        createRoom();
                        break;
                    case "2":
                        joinRoom();
                        break;
                    case "3":
                        exit();
                        return;
                    default:
                        out.write("Invalid menu option\n");
                        out.flush();
                        break;
                }
            }
            startMessaging();
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }

    private void signIn() throws IOException {
        out.write("Enter username:\n");
        out.flush();
        String login = in.readLine();
        out.write("Enter password:\n");
        out.flush();
        String password = in.readLine();
        try {
            server.usersService.signIn(login, password);
            this.username = login;
            isAuthorized = true;
            sessions.add(this);
        } catch (AuthorizationException e) {
            out.write(e.getMessage() + "\n");
            out.flush();
        }
    }

    private void signUp() throws IOException {
        out.write("Enter username:\n");
        out.flush();
        String login = in.readLine();
        out.write("Enter password:\n");
        out.flush();
        String password = in.readLine();
        try {
            server.usersService.signUp(login, password);
            this.username = login;
            isAuthorized = true;
            sessions.add(this);
        } catch (AuthorizationException e) {
            out.write(e.getMessage() + "\n");
            out.flush();
        }
    }

    private void exit() throws IOException {
        out.write("You have left the chat\n");
        out.flush();
        out.close();
        in.close();
        socket.close();
        sessions.remove(this);
    }

    private void createRoom() throws IOException {
        out.write("Enter chatroom name:\n");
        out.flush();
        String chatroomName = in.readLine();
        Chatroom createdChatroom = server.chatroomService.createChatroom(chatroomName);
        if (createdChatroom != null) {
            selectedChatroom = createdChatroom;
        } else {
            out.write("Can not create chatroom. Seems like chatroom with this name is already exists\n");
            out.flush();
        }
    }

    private void joinRoom() throws IOException {
        List<Chatroom> chatrooms = server.chatroomService.getAllChatrooms();
        if (chatrooms.isEmpty()) {
            out.write("There is no created chats\n");
            out.flush();
            return;
        }
        out.write("Rooms:\n");
        out.flush();
        for (int i = 0; i < chatrooms.size(); i++) {
            out.write(String.format("%d: %s\n", i+1, chatrooms.get(i).getChatRoomName()));
            out.flush();
        }
        String chatroomNmb = in.readLine();
        try {
            int nmb = Integer.parseInt(chatroomNmb);
            Chatroom chatroom = null;
            for (int i = 0; i < chatrooms.size(); i++) {
                if (i == nmb - 1) {
                    chatroom = chatrooms.get(i);
                    break;
                }
            }
            if (chatroom != null) {
                selectedChatroom = chatroom;
            } else {
                out.write("There is no such room\n");
                out.flush();
            }
        } catch (RuntimeException e) {
            out.write("Invalid input, can't parse your number\n");
            out.flush();
        }

    }

    private void startMessaging() throws IOException {
        out.write(selectedChatroom.getChatRoomName() + " ---\n");
        out.flush();
        loadLastMessages();
        while (true) {
            String userMsg = in.readLine();
            if (userMsg.equals("Exit")) {
                exit();
                return;
            } else {
                new Thread(() -> sendMessageToAll(userMsg, selectedChatroom)).start();
            }
        }
    }

    private void sendMessageToAll(String msg, Chatroom currentChatroom) {
        server.messageService.saveMessage(msg, username, currentChatroom.getId());
        for (Session session : sessions) {
            try {
                if (session.isAuthorized && session.selectedChatroom != null
                        && session.selectedChatroom.getChatRoomName().equals(currentChatroom.getChatRoomName())) {
                    session.out.write(username + ": " + msg + "\n");
                    session.out.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadLastMessages() throws IOException {
        List<String> messages = server.messageService.loadLastMessagesInRoom(selectedChatroom.getId());
        for (int i = messages.size() - 1; i >= 0; i--) {
            out.write(messages.get(i) + "\n");
            out.flush();
        }
    }

}
