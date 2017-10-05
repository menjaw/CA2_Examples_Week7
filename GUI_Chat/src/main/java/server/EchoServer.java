package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EchoServer {

    public static int PORT = 8081;
    public static String IP = "127.0.0.1";

    private final List<ClientHandler> clientHandlers = Collections.synchronizedList(new ArrayList());

    public void addClientHandler(ClientHandler h) {
        clientHandlers.add(h);
    }

    public void removeClientHandler(ClientHandler h) {
        clientHandlers.remove(h);
    }

    public void PrintUserList() {

        ArrayList<String> userList = new ArrayList<String>();

        for (int i = 0; i < clientHandlers.size(); i++) {
            if (!clientHandlers.get(i).clientName.equals("Anonym Bruger")) {
                userList.add(clientHandlers.get(i).clientName);
            }
        }

        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendMessage("CLIENTLIST:" + String.join(",", userList));
        }

    }

    ;
    
    public void echoMessageToAll(String msg, String sender) {
        String toSend = "MSGRES:" + sender + ":" + msg;
        clientHandlers.forEach((h)
                -> {
            h.sendMessage(toSend);
        });
    }

    public void privateMessage(String[] userParts, String message, String sender) {

        for (ClientHandler clientHandler : clientHandlers) {
            for (String user : userParts) {
                if (user.equals(clientHandler.clientName)) {
                    clientHandler.sendMessage("MSGRES:" + sender + ":" + message);
                }
            }
        }

    }

    public void listenForClients() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(IP, PORT));
        while (true) {
            Socket socket = serverSocket.accept(); //Important Blocking call
            new ClientHandler(socket, this).start();
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 2) {
            IP = args[0];
            PORT = Integer.parseInt(args[1]);
        }
        System.out.println(String.format("Server Startet, bound to: %s. Listening on: %d", IP, PORT));
        new EchoServer().listenForClients();
    }
}
