/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

/**
 *
 * @author Menja
 */
public interface IEchoClient {

    public void login(String user);

    public void addObserver(IDataReady observer);

    public void connectToServer(String ipAddress, int port);

    public void sendMessage(String message);

    public void closeConnection();
}
