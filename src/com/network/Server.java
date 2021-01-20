package com.network;

import java.io.IOException;
import java.net.ServerSocket;

public class Server extends NetworkEntity {
    private ServerSocket serverSocket;
    private final int listenPort;


    public Server(final int _listenPort){
        super("SERVER");
        listenPort = _listenPort;

    }

    @Override
    public void run() {
        try{
            serverSocket = new ServerSocket(listenPort, 1);
            try{
                waitForConnection();
                getStreams();
                processIncomingData();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitForConnection() throws IOException {
        connectionHandle = serverSocket.accept();
    }

    public void closeConnection(){
        super.closeConnection();
        try{
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
