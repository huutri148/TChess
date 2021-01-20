package com.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Server extends NetworkEntity {
    private ServerSocket serverSocket;
    private final int listenPort;
    private String ip = "localhost";

    public Server(final int _listenPort){
        super("SERVER");
        listenPort = _listenPort;
    }

    @Override
    public void run() {
        try{
            serverSocket = new ServerSocket(listenPort, 1, InetAddress.getByName(ip));
            System.out.println("Create Successfully");
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
        System.out.println("Accepted connection");
    }

    public void closeConnection(){
        super.closeConnection();
        try{
            serverSocket.close();
            System.out.println("Close serverSocket");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
