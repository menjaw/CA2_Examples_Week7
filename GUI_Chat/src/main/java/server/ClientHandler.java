package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread {

    Scanner reader;
    PrintWriter writer;
    Socket socket;
    EchoServer master;
    String clientName = "Anonym Bruger";

    public ClientHandler(Socket socket, EchoServer master) throws IOException {
        this.socket = socket;
        this.master = master;
        reader = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        String message = reader.nextLine();

        while (!message.equals("LOGOUT:")) {
            String[] MsgParts = message.split(":");

            if (MsgParts.length == 2 & MsgParts[0].toUpperCase().equals("LOGIN")) {
                if (clientName.equals("Anonym Bruger")) {
                    clientName = MsgParts[1];
                    this.master.addClientHandler(this);
                    this.master.PrintUserList();
                }
            }

            if (checkLogin()) {
                if (MsgParts[0].toUpperCase().equals("MSG") & MsgParts.length == 3) {
                    if (MsgParts[1].equals("*")) {
                        master.echoMessageToAll(MsgParts[2], clientName);
                    } else {
                        String[] userParts = MsgParts[1].split(",");
                        master.privateMessage(userParts, MsgParts[2], clientName);
                    }

                }
            }

            message = reader.nextLine();
        }
        try {
            this.master.removeClientHandler(this);
            this.master.PrintUserList();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean checkLogin() {
        if (!clientName.equals("Anonym Bruger")) {
            return true;
        } else {
            sendMessage("You must login first");
            return false;
        }
    }

    void sendMessage(String msg) {
        writer.println(msg);
    }

}
