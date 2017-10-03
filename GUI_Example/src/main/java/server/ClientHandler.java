package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientHandler extends Thread{
  Scanner reader;
  PrintWriter writer;
  Socket socket;
  EchoServer master;
  
  public ClientHandler(Socket socket, EchoServer master) throws IOException{
    this.socket = socket;
    this.master = master;
    reader = new Scanner(socket.getInputStream());
    writer = new PrintWriter(socket.getOutputStream(), true);
  }
  
  @Override
  public void run(){
    this.master.addClientHandler(this);
    String message = reader.nextLine(); //IMPORTANT blocking call
    System.out.println("Received " + message);
    while (!message.equals("stop")) {
      master.echoMessageToAll(message);
      message = reader.nextLine();
    }
    try {
      this.master.removeClientHandler(this);
      socket.close();
    } catch (IOException ex) {
      Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  void sendMessage(String msg) {
    writer.println(msg);
  }
  
  
  
}
