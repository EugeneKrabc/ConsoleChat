package edu.school21.sockets.app;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static volatile BufferedWriter out;

    public static void main(String[] args) {
        if (args.length != 2 || !args[0].startsWith("--server-ip=") || !args[1].startsWith("--server-port=")) {
            System.err.println("Need to provide cmd argument: --server-ip=IP --server-port=PORT_NUMBER");
            System.exit(-1);
        }

        int port = Integer.parseInt(args[1].substring(14));
        String IP = args[0].substring(12);

        try {
            Socket socket = new Socket(IP, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Scanner userInputScanner = new Scanner(System.in);

            Thread listener = createListenerThread(in);
            listener.start();
            while (true) {
                String msgToServer = userInputScanner.nextLine();
                if (socket.getInetAddress().isReachable(100) && out != null) {
                    out.write(msgToServer + "\n");
                    out.flush();
                } else {
                    break;
                }
            }
            socket.close();
            userInputScanner.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Thread createListenerThread(BufferedReader in) {
        return new Thread(() -> {
            while (true) {
                try {
                    String serverMsg = in.readLine();
                    if (serverMsg == null) {
                        out = null;
                        break;
                    }
                    System.out.println(serverMsg);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
