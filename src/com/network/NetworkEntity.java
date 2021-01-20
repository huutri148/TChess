package com.network;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class NetworkEntity extends Thread{
    protected ObjectInputStream inputStream;
    protected ObjectOutputStream outputStream;
    protected Socket connectionHandle;
    protected Object receivedMessage;


    NetworkEntity(final String name){
        super(name);
    }

    public abstract void run();


    public void getStreams() throws IOException{
        outputStream= new ObjectOutputStream(connectionHandle.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(connectionHandle.getInputStream());

    }

    public void closeConnection(){
        try{
            if (outputStream != null){
                outputStream.close();
            }
            if (inputStream != null){
                inputStream.close();
            }

            if(connectionHandle != null){
                connectionHandle.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void processIncomingData() throws IOException{
        do{
            try{
                receivedMessage = inputStream.readObject();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (receivedMessage instanceof Move){
                final Move m = (Move) receivedMessage;

            } else if (receivedMessage instanceof Board){
                final Board b = (Board) receivedMessage;
            } else if (receivedMessage instanceof String){

            }
        } while (receivedMessage != null);
    }
    public void sendData(final Object objToSend){
        try {
            outputStream.writeObject(objToSend);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
