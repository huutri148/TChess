package com.network;

import com.chess.engine.gui.Table;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends NetworkEntity{
    private String hostName;
    private int serverPort;


    public Client(final String host, final int port){
        super("CLIENT");
        this.hostName = host;
        this.serverPort = port;
    }

    @Override
    public void run() {
        try{
            connectToServer();
            getStreams();
            processIncomingData();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
    private void connectToServer(){
        try{
            connectionHandle = new Socket(InetAddress.getByName(hostName), serverPort);
            System.out.println("Connecting to server");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
