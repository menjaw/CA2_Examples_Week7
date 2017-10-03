/*
 * This class contains the logic for the client
 */
package client;

import interfaces.IDataReady;
import interfaces.IEchoClient;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Menja
 */
public class EchoClient extends Thread implements IEchoClient {

    //Variables
    public Socket clientSocket;
    private Scanner input;
    private PrintWriter output;
    private IDataReady observer;
    private boolean running = true;

    //Add observer to check if the observed object is changing (in this case the message)
    @Override
    public void addObserver(IDataReady observer) {
        this.observer = observer;
    }

    //Connects to the server and listen for incomming messages
    @Override
    public void connectToServer(String ipAddress, int port) {
        try {
            clientSocket = new Socket(ipAddress, port);
            input = new Scanner(clientSocket.getInputStream());//Sørger for at kunne tage klientens input
            output = new PrintWriter(clientSocket.getOutputStream(), true);//Returnerer et output for denne socket
        } catch (IOException ex) {
            Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void sendMessage(String message) {
        output.println(message);
    }

    @Override
    public void closeConnection() {
        sendMessage("Stop");
        running = false;
    }

    //implement the run method from the Runnable class to handle the threads
    @Override
    public void run() {
        while (running) {
            String msg = input.nextLine();
            observer.messageReady(msg);
        }
        try {
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
