package client;

import interfaces.IDataReady;
import interfaces.IEchoClient;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EchoClient extends Thread implements IEchoClient {

    //Variables
    private String user;
    public Socket clientSocket;
    private Scanner input;
    private PrintWriter output;
    private IDataReady observer;
    private volatile boolean running = true;

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
            input = new Scanner(clientSocket.getInputStream());//Makes sure to take the clients input
            output = new PrintWriter(clientSocket.getOutputStream(), true);//Returns an output for this socket
            this.start();

        } catch (IOException ex) {
            Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void sendMessage(String message) {
        output.println(message);
    }

    @Override
    public void login(String user) {
        output.println("" + user);

    }

    @Override
    public void closeConnection() {
        output.println("LOGOUT:");
        running = false;
    }

    //implement the run method from the Runnable class to handle the threads
    @Override
    public void run() {
        while (running) {
            String msg = input.nextLine(); //Læser på socket fra serveren
            observer.messageReady(msg);

        }
        try {
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
