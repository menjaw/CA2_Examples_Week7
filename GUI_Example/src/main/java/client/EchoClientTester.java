package client;

import java.io.IOException;


public class EchoClientTester {
  
  //Start the Server on the PORT used below, before running
  public static void main(String[] args) throws IOException, InterruptedException {
    EchoClient client = new EchoClient();
    
    client.addObserver((msg) -> {
      System.out.println("Received a message: "+msg);
    });
    client.connect("localhost",1234);
    
    client.send("Hello");
    client.send("Hello World");
    client.send("Hello Wonderfull World");
    Thread.sleep(100);
    client.closeConnection();
    
    System.out.println("DONE");
   
    
  }

  
}
